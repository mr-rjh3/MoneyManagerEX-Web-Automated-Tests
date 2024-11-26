package MoneyManagerPages;

import java.security.InvalidParameterException;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class TransactionPage {
    protected WebDriver driver;

    @FindBy(id = "Date")
    protected WebElement dateElement;
    @FindBy(id = "Status")
    protected WebElement statusElement;
    protected Select statusSelect;
    @FindBy(id = "Type_Withdrawal")
    protected WebElement withdrawalRadioElement;
    @FindBy(id = "Type_Deposit")
    protected WebElement depositRadioElement;
    @FindBy(id = "Type_Transfer")
    protected WebElement transferRadioElement;
    @FindBy(id = "ToAccount")
    protected WebElement toAccountElement;
    protected Select toAccountSelect;
    @FindBy(id = "Payee")
    protected WebElement payeeElement;
    @FindBy(id = "Account")
    protected WebElement accountElement;
    protected Select accountSelect;
    @FindBy(id = "Amount")
    protected WebElement amountElement;
    @FindBy(id = "Category")
    protected WebElement categoryElement;
    @FindBy(id = "SubCategory")
    protected WebElement subCategoryElement;
    @FindBy(id = "Notes")
    protected WebElement notesElement;
    @FindBy(id = "SubmitButton")
    protected WebElement submitButton;
    
    public TransactionPage(WebDriver driver){
        this.driver = driver;
        // This page can be either the edit or create transaction page, this if statement will account for both.
        if (!driver.getTitle().equals("Creating new transaction | Money Manager EX") && !driver.getTitle().equals("Editing transaction | Money Manager EX")) {
            throw new IllegalStateException("This is not the transaction Page," +
                  " current page is: " + driver.getCurrentUrl());
          }
        PageFactory.initElements(driver, this);
        statusSelect = new Select(statusElement);
        accountSelect = new Select(accountElement);
        toAccountSelect = new Select(toAccountElement);
    }

    public void setDate(String dateString) {
        if(dateString != null){ // defaults to current date, null input is allowed
            dateElement.clear();
            dateElement.sendKeys(dateString);
        }
    }
    public void selectStatus(String statusString) {
        statusSelect.selectByVisibleText(statusString);
    }
    public void selectType(String type) {
        if(type.toLowerCase().equals("withdrawal")){
            withdrawalRadioElement.click();
        }
        else if(type.toLowerCase().equals("deposit")){
            depositRadioElement.click();
        }
        else if(type.toLowerCase().equals("transfer")){
            transferRadioElement.click();
        }
        else{
            throw new InvalidParameterException("Type must be one of: withdrawal, deposit, or transfer. Was actually: " + type);
        }
    }
    public void selectToAccount(String accountString) {
        if(!transferRadioElement.isSelected()){
            throw new IllegalStateException("To Account is only accessible when the transaction type is: Transfer.");
        }
        toAccountSelect.selectByVisibleText(accountString);
    }
    public void setPayee(String payeeString){
        if(transferRadioElement.isSelected()){
            throw new IllegalStateException("Payee is only accessible when the transaction type is: Withdrawal or Deposit.");
        }
        payeeElement.clear();
        payeeElement.sendKeys(payeeString);
    }
    public void selectAccount(String accountString){
        accountSelect.selectByVisibleText(accountString);
    }
    public void setAmount(String amountString){
        amountElement.clear();
        amountElement.sendKeys(amountString);
    }
    public void setCategory(String categoryString){
        categoryElement.clear();
        if(categoryString != null){ // optional field, allow null input
            categoryElement.sendKeys(categoryString);
        }
    }
    public void setSubCategory(String subCategoryString){
        subCategoryElement.clear();
        if(subCategoryString != null){ // optional field, allow null input
            subCategoryElement.sendKeys(subCategoryString);
        }
    }
    public void setNotes(String notesString){
        notesElement.clear();
        if(notesString != null){ // optional field, allow null input
            notesElement.sendKeys(notesString);
        }
    }
    public void submitTransaction(){
        submitButton.submit();
        try{
            driver.switchTo().alert().accept(); // there may be an alert to create a new payee, category, etc. Accept it.
        } catch (NoAlertPresentException ex) {
            // there was no alert, continue
        }
    }

    public void createTransaction(String date, String status, String type, String account, String toAccount, String payee, String amount, String category, String subcategory, String notes){
        setDate(date);
        selectStatus(status);
        selectType(type);
        selectAccount(account);
        setAmount(amount);
        if(type.toLowerCase() == "transfer"){
            selectToAccount(toAccount);
        }
        else{
            setPayee(payee);
        }
        setCategory(category);
        setSubCategory(subcategory);
        setNotes(notes);
        submitTransaction();
    }
}
