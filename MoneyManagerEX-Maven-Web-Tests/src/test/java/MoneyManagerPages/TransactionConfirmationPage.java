package MoneyManagerPages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TransactionConfirmationPage {
    protected WebDriver driver;

    @FindBy(xpath = "/html/body/div[2]/h3")
    protected WebElement confirmationHeading;
    
    public TransactionConfirmationPage(WebDriver driver){
        this.driver = driver;
        // This page can be either the edit or create transaction page, this if statement will account for both.
        if (!driver.getTitle().equals("New Transaction | Money Manager EX")) {
            throw new IllegalStateException("This is not the transaction confirmation Page," +
                  " current page is: " + driver.getCurrentUrl());
          }
        PageFactory.initElements(driver, this);
    }

    public String getConfirmationText() {
        return confirmationHeading.getText();
    }
}
