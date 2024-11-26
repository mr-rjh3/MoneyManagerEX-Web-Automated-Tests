import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import MoneyManagerPages.LandingPage;
import MoneyManagerPages.LoginPage;
import MoneyManagerPages.TransactionConfirmationPage;
import MoneyManagerPages.TransactionPage;

import org.openqa.selenium.*;

import java.security.InvalidParameterException;
import java.time.*;
import java.util.List;

@ExtendWith(FailureLogger.class)
@TestMethodOrder(OrderAnnotation.class)
public class TransactionsTest {

    static final String URL = "http://localhost:8080/";// Address Book URL

    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.get(URL);
    }

    @AfterEach
    void cleanUp() {
        // See FailureLogger.java to see AfterEach logic
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ LOGIN ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Test
    void validLogin(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Riley", "password");
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ADD TRANSACTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Order(1)
    @ParameterizedTest(name = "testValidAddTransaction [{0} - {1}]")
    @CsvFileSource(resources = "/ValidData.csv", numLinesToSkip = 1)
    void testValidAddTransaction(String testID, String testName, String date, String status, String type, String account, String amount, String payee, String category, String subcategory, String notes) throws NoSuchElementException {

        // Test that we are on the login page
        String expectedUrl = URL;
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);
        
        // Login
        LoginPage loginPage = new LoginPage(driver);
        assertDoesNotThrow(() -> {
            LandingPage landingPage = loginPage.login("Riley", "password");
            assertDoesNotThrow(()->{
                TransactionPage transactionPage = landingPage.clickNewTransaction();
                transactionPage.createTransaction(date, status, type, account, account, payee, amount, category, subcategory, notes);
            });
        });
        
        // Ensure the entry was added properly by initializing the confirmation page object and the success element is present and holds the correct message.
        assertDoesNotThrow(() ->{
            TransactionConfirmationPage confirmationPage = new TransactionConfirmationPage(driver);
            assertEquals(confirmationPage.getConfirmationText(), "Added successfully");
        });
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ LIST TRANSACTIONS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ EDIT TRANSACTIONS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ VIEW TRANSACTIONS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ DUPLICATE TRANSACTIONS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ==================================== HELPER METHODS ====================================

    /**
     * Takes in test data and inputs the data into the addressbook form.
     * 
     * @param testID       - The test ID for the current test. Usually in the form:
     *                     TC-XX
     * @param testData     - The data for all the text inputs for the form. This
     *                     will be retrieved from a CSV file
     * @param dropdownData - The data for all the dropdown forms. This will be
     *                     retrieved from a CSV file
     * @throws NoSuchElementException - If the driver cannot find an element this
     *                                will be thrown, likely cause is an issue in
     *                                the test data.
     */
    void enterTestDataIntoTransactionForm(String date, String status, String type, String account, String amount, String payee, String category, String subcategory, String notes) {
        
        driver.findElement(By.id("Date")).sendKeys(date);
        driver.findElement(By.id("Status")); //TODO handle the select

        if(type == "Withdrawl"){
            driver.findElement(By.id("Type_Withdrawal")).click();
            driver.findElement(By.id("Payee")).sendKeys(payee);
        }
        else if(type == "Deposit"){
            driver.findElement(By.id("Type_Deposit")).click();
            driver.findElement(By.id("Payee")).sendKeys(payee);
        }
        else if(type == "Transfer"){
            driver.findElement(By.id("Type_Transfer")).click();
            // Only this transaction type removes payee and adds a new dropdown "To Account"
            driver.findElement(By.id("ToAccount")); //TODO handle the select

        }
        else {
            throw new InvalidParameterException("Trasaction Type is invalid!!");
        }

        driver.findElement(By.id("Account")); // TODO: handle the select

        driver.findElement(By.id("Amount")).sendKeys(amount);
        driver.findElement(By.id("Category")).sendKeys(category);
        driver.findElement(By.id("SubCategory")).sendKeys(subcategory);
        driver.findElement(By.id("Notes")).sendKeys(notes);

        // Submit the entry
        driver.findElement(By.id("SubmitButton")).submit();
    }

}
