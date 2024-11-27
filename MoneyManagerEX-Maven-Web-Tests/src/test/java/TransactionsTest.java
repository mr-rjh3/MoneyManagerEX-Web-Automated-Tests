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
import MoneyManagerPages.ShowTransactionsPage;
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

    // TODO: Change to actual values from test script
    static final String VALID_USERNAME = "Riley";
    static final String VALID_PASSWORD = "password";


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

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ LOGIN TESTS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Order(0)
    @Test
    void validLogin(){
        performValidLogin();
    }
    @Order(1)
    @ParameterizedTest
    @CsvSource({"nonexistantuser, password", "Riley, WrongPass123"}) // TODO change to actual values from test scripts
    void invalidLogin(String username, String password){
        LoginPage loginPage = new LoginPage(driver);
        assertThrows(IllegalStateException.class, ()->{
            loginPage.login(username, password);
        });
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ADD TRANSACTION ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Order(2)
    @ParameterizedTest(name = "testValidAddTransaction [{0} - {1}]")
    @CsvFileSource(resources = "/ValidData.csv", numLinesToSkip = 1)
    void testValidAddTransaction(String testID, String testName, String date, String status, String type, String account, String amount, String payee, String category, String subcategory, String notes) throws NoSuchElementException {

        // Test that we are on the login page
        String expectedUrl = URL;
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);
        
        LandingPage landingPage = performValidLogin();

        TransactionPage transactionPage = validGoToNewTransactionPage(landingPage);

        transactionPage.createTransaction(date, status, type, account, account, payee, amount, category, subcategory, notes);
        
        // Ensure the entry was added properly by initializing the confirmation page object and confirming the success element is present and holds the correct message.
        assertDoesNotThrow(() ->{
            TransactionConfirmationPage confirmationPage = new TransactionConfirmationPage(driver);
            assertEquals(confirmationPage.getConfirmationText(), "Added successfully");
        });
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ EDIT TRANSACTIONS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Order(3)
    @ParameterizedTest(name = "testValidAddTransaction [{0} - {1}]")
    @CsvFileSource(resources = "/ValidData.csv", numLinesToSkip = 1)
    void testValidEditTransaction(String testID, String testName, String date, String status, String type, String account, String amount, String payee, String category, String subcategory, String notes) throws NoSuchElementException {

        // Test that we are on the login page
        String expectedUrl = URL;
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);
        
        LandingPage landingPage = performValidLogin();

        ShowTransactionsPage showTransactionsPage = validGoToShowTransactionsPage(landingPage);

        TransactionPage transactionPage = validGoToEditTransactionPage(showTransactionsPage);

        transactionPage.createTransaction(date, status, type, account, account, payee, amount, category, subcategory, notes);
        
        // Ensure the entry was added by initializing a ShowTransactionsPage object without an exception being thrown to confirm we are on the correct page 
        assertDoesNotThrow(() ->{
            new ShowTransactionsPage(driver);
        });
    }

    // ==================================== HELPER METHODS ====================================

        LandingPage performValidLogin(){
            LoginPage loginPage = new LoginPage(driver);
            return assertDoesNotThrow(()->{
                return loginPage.login(VALID_USERNAME, VALID_PASSWORD);
            });
        } 
        TransactionPage validGoToNewTransactionPage(LandingPage landingPage){
            return assertDoesNotThrow(() -> {
                return landingPage.clickNewTransaction();
            });
        }
        ShowTransactionsPage validGoToShowTransactionsPage(LandingPage landingPage){
            return assertDoesNotThrow(() -> {
                return landingPage.clickShowTransactions();
            });
        }
        TransactionPage validGoToEditTransactionPage(ShowTransactionsPage showTransactionsPage){
            return assertDoesNotThrow(() -> {
                return showTransactionsPage.clickFirstEditTransactionButton();
            });
        }


}
