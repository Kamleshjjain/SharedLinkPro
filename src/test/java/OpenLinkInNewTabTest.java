import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

public class OpenLinkInNewTabTest {
    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        // Set up WebDriver
        WebDriverManager.chromedriver().clearDriverCache().setup();
        WebDriverManager.chromedriver().clearResolutionCache().setup();
        WebDriverManager.chromedriver().setup();
        //WebDriver driver = new WebDriver() ;
        driver = new ChromeDriver();
    }

    @Test
    public void openLinkInNewTab() throws InterruptedException, UnsupportedFlavorException, IOException {
        // Open the website and login
        driver.manage().window().maximize();
        driver.get("https://uat.odysol.com/swift/cruise?siid=130386&lang=1&destinationtype=All&transportid=29&sortColumn=cruiselinePriority&sortOrder=asc");
        Thread.sleep(25000);
        String urlToOpen = null;

        WebElement sharedLinkIcon = driver.findElement(By.xpath("(//button[contains(@ng-reflect-tooltip,'Share')])[1]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(sharedLinkIcon).keyDown(Keys.CONTROL).click().keyUp(Keys.CONTROL).build().perform();

        ((JavascriptExecutor) driver).executeScript("window.open();");
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        urlToOpen =  Toolkit.getDefaultToolkit().getSystemClipboard()
                .getData(DataFlavor.stringFlavor).toString();

        actions.keyDown(Keys.CONTROL).sendKeys("v").keyUp(Keys.CONTROL).perform();


        driver.get(urlToOpen);

        // Assert the content of the new tab
        String actualUrl = driver.getCurrentUrl();
        String expectedUrl = "https://uat.odysol.com/swift/cruise?siid=130386&lang=1&destinationtype=All&transportid=29&sortColumn=cruiselinePriority&sortOrder=asc";
        Assert.assertEquals(actualUrl.toLowerCase(), expectedUrl.toLowerCase(), "URLs do not match");
        Thread.sleep(25000);

    }

    @AfterClass
    public void tearDown() {
        // Close the WebDriver
        if (driver != null) {
            driver.quit();
        }
    }
}
