<?php

$b_restricted_auth  = true;

include_once '_common.php';

if ($_SERVER["REQUEST_METHOD"] == "POST")
    {   
        if (isset ($_POST["Language"]))
            {$language = $_POST["Language"];}
            else
            {$language = "en";}

        if (isset ($_POST["Set_Disable_authentication"]))
            {$disable_authentication = $_POST["Set_Disable_authentication"];}
            else
            {$disable_authentication = "False";}
        
        if (isset ($_POST["Set_Username"]))
            {$username = $_POST["Set_Username"];}
            else
            {$username = "";}
        
        $default_account = $_POST["Default_Account"];
        
        if (isset ($_POST["Set_Disable_payee"]))
            {$disable_payee = $_POST["Set_Disable_payee"];}
            else
            {$disable_payee = "False";}
        
        if (isset ($_POST["Set_Disable_category"]))
            {$disable_category = $_POST["Set_Disable_category"];}
            else
            {$disable_category = "False";}
        
        $guid = $_POST["Set_Guid"];
        
        if (isset($_POST["Set_Password"]) && $_POST["Set_Password"] !== "" && $_POST["Set_Password"] !== Null)
            {$password = hash("sha512", $_POST["Set_Password"]);}
        else
            {
                if (isset ($_POST["Set_Disable_authentication"]))
                    {$password = "";}
                    else
                    {$password = costant::login_password();} 
            }
        
        $parameterarray = array
            (
                "language"              => $language,
                "disable_authentication"=> $disable_authentication,
                "user_username"         => $username,
                "user_password"         => $password,
                "disable_payee"         => $disable_payee,
                "disable_category"      => $disable_category,
                "defaultaccountname"    => $default_account,
                "desktop_guid"          => $guid
            );
              
        if (file_exists("configuration_user.php"))
            {
                various::update_configuration_file($parameterarray);
                header("Location: landing.php");
            }
        else
            {
                various::update_configuration_file($parameterarray);
                header("Location: guide.php");
            }
    }

$is_edit                = (isset($const_username) AND isset($const_password));

$s_page_title = $lang["page.settings"];

if (!$is_edit)
{
    $s_page_title           = $lang["page.settings.new"];
    $b_page_logo            = true;

}

include_once '_header.php';

?>
    <div class="container">
        <?php
            if (!$is_edit) :
        ?>
        <h3 class="text_align_center"><?php echo $lang["settings.new"] ?></h3>
        <?php
            endif;
        ?>
        <br />
        <form id="login" method="post" action="settings.php">
            <?php
                $const_uiLanguage = costant::uiLanguage();
                $const_disable_authentication = costant::disable_authentication();
                $const_username = costant::login_username();
                $const_password = costant::login_password();
                $const_desktop_guid = costant::desktop_guid();
                $const_disable_payee = costant::disable_payee();
                $const_disable_category = costant::disable_category();
                $const_defaultaccountname = costant::transaction_default_account();
                
                //SECTION LANGUAGE
                if  ($const_uiLanguage)
                    {design::settings_language($const_uiLanguage);}
                    else
                    {design::settings_language("en");}
                echo "<br />";

                //SECTION AUTHENTICATION
                design::section_legened($lang["settings.authentication.section"]);
                    if ($const_disable_authentication == True)
                        {design::settings_checkbox("Set_Disable_authentication",True,$lang["settings.authentication.disable"]);}
                        else
                        {design::settings_checkbox("Set_Disable_authentication",False,$lang["settings.authentication.disable"]);}

                    if (isset($const_username) && $const_disable_authentication == False)
                        {design::settings("Username",$const_username,"","Text",True);}
                        else
                        {design::settings("Username","",$lang["sec.username.placeholder"] ,"Text",True);}
                    
                    if (isset($const_password) && $const_disable_authentication == False)
                        {design::settings_password("Password",$lang["settings.authentication.password.update.placeholder"],False,"Password");}
                        else
                        {design::settings_password("Password",$lang["settings.authentication.password.new.placeholder"],True,"Password");}
            
                    design::settings_password("Confirm_Password",$lang["settings.authentication.password.confirm.placeholder"],False,"Conferma Password");
                echo "<br />";
                
                //SECTION NEW TRANSACTIONS
                design::section_legened($lang["settings.new-trans.section"]);
                    if ($const_disable_payee == True)
                        {design::settings_checkbox("Set_Disable_payee",True,$lang["settings.new-trans.disable-payee.placeholder"]);}
                        else
                        {design::settings_checkbox("Set_Disable_payee",False,$lang["settings.new-trans.disable-payee.placeholder"]);}
                        
                    if ($const_disable_category == True)
                        {design::settings_checkbox("Set_Disable_category",True,$lang["settings.new-trans.disable-category.placeholder"]);}
                        else
                        {design::settings_checkbox("Set_Disable_category",False,$lang["settings.new-trans.disable-category.placeholder"]);}
                    
                    if (isset($const_defaultaccountname))
                        {design::settings_default_account($const_defaultaccountname);}
                        else
                        {design::settings_default_account("None");}
                echo "<br />";
                
                //SECTION DESKTOP INTEGRATION
                design::section_legened($lang["settings.new-trans.section"]);
                    if (isset($const_desktop_guid))
                        {design::settings("Guid",$const_desktop_guid,"","Text",True);}
                        else
                        {design::settings("Guid",security::generate_guid(),"","Text",True);}
                
            ?>
            <script type="text/javascript">
                function Disable_Authentication()
                    {
                        if (document.getElementById("Set_Disable_authentication").checked)
                            {
                                document.getElementById("Set_Username").disabled = true;
                                document.getElementById("Set_Username").value = "";
                                document.getElementById("Set_Password").disabled = true;
                                document.getElementById("Set_Password").value = "";
                                document.getElementById("Set_Confirm_Password").disabled = true;
                                document.getElementById("Set_Confirm_Password").value = "";
                                disable_confirm_password_if_empty ();
                            }
                        else
                            {
                                document.getElementById("Set_Username").disabled = false;
                                document.getElementById("Set_Password").disabled = false;
                                document.getElementById("Set_Confirm_Password").disabled = false;
                                disable_confirm_password_if_empty ();
                            }
                    }
                $("#Set_Disable_authentication").bind("change", Disable_Authentication);
                $("#Set_Password").bind("input", disable_confirm_password_if_empty);
                disable_confirm_password_if_empty();
                Disable_Authentication();
            </script>  
            <br />
            <?php
                if (isset($const_username) AND isset($const_password))
                    {
                        echo ('<button type="button" id="EditSettings" name="EditSettings" class="btn btn-lg btn-success btn-block" onclick="check_password_match_and_submit(\'Set_Password\',\'Set_Confirm_Password\',\'login\')">'.$lang["settings.save"].'</button>');
                        echo '<br />';
                        echo ('<a href="landing.php" class="btn btn-lg btn-success btn-block">'.$lang['return_to_menu'].'</a>');
                    }
                else
                    {
                        echo ('<button type="button" id="EditSettings" name="EditSettings" class="btn btn-lg btn-success btn-block" onclick="check_password_match_and_submit(\'Set_Password\',\'Set_Confirm_Password\',\'login\')">'.$lang["settings.apply"].'</button>');
                    }
                echo '<br />';
                echo '<br />';
            ?>
            <br />
        </form>
    </div>
	
	<script src="res/app/base-1.0.4.js" type="text/javascript"></script>
<?php

include_once '_footer.php';
