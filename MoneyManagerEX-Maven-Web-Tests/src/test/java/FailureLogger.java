import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class FailureLogger implements AfterEachCallback {

    static final String SCREENSHOT_DIRECTORY_PATH = "src/test/Screenshots/";

    @Override
    public void afterEach(ExtensionContext context) throws Exception {

        // Get the current instance of the testObject
        TransactionsTest testObject = (TransactionsTest) context.getRequiredTestInstance(); 

        if (context.getExecutionException().isPresent()) { // Check if an exception was thrown during test excecution
            // Code to run when a test fails
            System.out.println("Test failed: " + context.getDisplayName());

            // Take Screenshot of current page
            System.out.println("Taking Screenshot of current page...");
            // Get current date 
            Date currentDate = new Date();
            String fileName = context.getDisplayName() + " " + currentDate.getTime() +".png";
            System.out.println(fileName);
            takeScreenshot(testObject.driver, SCREENSHOT_DIRECTORY_PATH + fileName);
        }
        // Close the driver page
        testObject.driver.close();
    }

    /**
     * Takes a screenshot of the current page the WebDriver is on and saves it to the given file path.
     * Parameters:
     * @param driver - The driver used during the test.
     * @param filePath - The file path which the screenshot will be saved to. Ensure that this includes the name and file extension for the image as well.
     */
    void takeScreenshot(WebDriver driver, String filePath) {
        System.out.println(driver.getCurrentUrl() + " " + filePath);
        try {
            TakesScreenshot screenshoter = ((TakesScreenshot) driver);
            File screenshot = screenshoter.getScreenshotAs(OutputType.FILE);
            
            // Create Screenshot directory if it does not exist
            File destFile = new File(filePath);
            if(!destFile.getParentFile().exists()){
                destFile.getParentFile().mkdirs();
            }

            FileUtils.copyFile(screenshot, destFile);
        } catch (IOException ex) {
            System.err.println("ERROR: Unable to save screenshot... | " + ex.getMessage());
        }
    }
}
