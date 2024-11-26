package MoneyManagerPages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    protected WebDriver driver;

    @FindBy(id = "Username")
    protected WebElement usernameElem;
    @FindBy(id = "Password")
    protected WebElement passwordElem;
    @FindBy(id = "Login")
    protected WebElement loginButtonElem;

    public LoginPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterUsername(String username){
        this.usernameElem.sendKeys(username);
    }
    public void enterPassword(String password){
        this.passwordElem.sendKeys(password);
    }
    public LandingPage submitLogin(){
        loginButtonElem.submit();
        return new LandingPage(driver);
    }

    public LandingPage login(String username, String password){
        enterUsername(username);
        enterPassword(password);
        return submitLogin();
    }

}
