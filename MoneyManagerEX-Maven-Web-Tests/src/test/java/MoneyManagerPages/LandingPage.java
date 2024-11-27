package MoneyManagerPages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LandingPage {
    protected WebDriver driver;

    @FindBy(xpath = "/html/body/div[2]/div[1]/a")
    protected WebElement newTrasactionButton;
    @FindBy(xpath = "/html/body/div[2]/div[2]/a")
    protected WebElement showTrasactionButton;
    @FindBy(xpath = "/html/body/div[2]/div[3]/a")
    protected WebElement editSettingsButton;
    @FindBy(xpath = "/html/body/div[2]/div[4]/a")
    protected WebElement guideButton;
    @FindBy(xpath = "/html/body/div[2]/div[5]/a")
    protected WebElement aboutButton;
    @FindBy(xpath = "/html/body/div[2]/div[6]/a")
    protected WebElement logoutButton;

    public LandingPage(WebDriver driver){
        this.driver = driver;
        if (!driver.getCurrentUrl().equals("http://localhost:8080/landing.php")) {
            throw new IllegalStateException("This is not the landing Page," +
                  " current page is: " + driver.getCurrentUrl());
          }
        PageFactory.initElements(driver, this);
    }

    public TransactionPage clickNewTransaction(){
        newTrasactionButton.click();
        return new TransactionPage(driver);
    }
    public ShowTransactionsPage clickShowTransactions(){
        showTrasactionButton.click();
        return new ShowTransactionsPage(driver);
    }


}
