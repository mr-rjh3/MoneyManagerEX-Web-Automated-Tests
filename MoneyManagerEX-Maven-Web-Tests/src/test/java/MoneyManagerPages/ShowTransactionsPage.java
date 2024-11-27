package MoneyManagerPages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ShowTransactionsPage {
    protected WebDriver driver;

    @FindBy(xpath = "//*[@id=\"Show_Function\"]/table/tbody/tr[2]/td[1]/span[1]")
    protected WebElement firstEditTransactionButton;
    
    public ShowTransactionsPage(WebDriver driver){
        this.driver = driver;
        // This page can be either the edit or create transaction page, this if statement will account for both.
        if (!driver.getTitle().equals("Show transactions | Money Manager EX")) {
            throw new IllegalStateException("This is not the show transactions Page," +
                  " current page is: " + driver.getCurrentUrl());
          }
        PageFactory.initElements(driver, this);
    }

    public TransactionPage clickFirstEditTransactionButton() {
        if(firstEditTransactionButton == null){
            throw new IllegalStateException("There are currently no transactions to edit, ensure you add transactions first.");
        }
        firstEditTransactionButton.click();
        return new TransactionPage(driver);
    }
}
