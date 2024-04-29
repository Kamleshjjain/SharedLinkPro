import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.awt.datatransfer.StringSelection;

public class OpenLinkInNewTabTest {
    private WebDriver driver;

    String UserName = getPropertyVal("credentials", "UName");
    String Password = getPropertyVal("credentials", "Pwd");
    boolean isSaveForLaterB2C = true;
    @BeforeClass
    public void setUp() {
        // Set up WebDriver
        WebDriverManager.chromedriver().clearDriverCache().setup();
        WebDriverManager.chromedriver().clearResolutionCache().setup();
        WebDriverManager.chromedriver().setup();
        //WebDriver driver = new WebDriver() ;
        driver = new ChromeDriver();
    }

    public static String getPropertyVal(String propFN, String variable) {
        FileReader fr;
        Properties property = new Properties();

        String fileName = System.getProperty("user.dir") + "/src/test/resources/" + propFN + ".properties";

        try {
            fr = new FileReader(fileName);
            property.load(fr);
            return property.getProperty(variable);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return "";
        }
    }


    @Test
    public void openLinkInNewTab() throws InterruptedException, UnsupportedFlavorException, IOException {
        // Open the website and login
        driver.manage().window().maximize();
        String clipboardData1=null;
        String clipboardData2=null;
        driver.get("https://book.bestpricecruises.com/swift-test/cruise?advancedsearch=true&siid=45293&lang=1&showtrace=true");
        //Thread.sleep(10000);

         driver.findElement(By.xpath("(//button[contains(@ng-reflect-tooltip,'Share')])[1]")).click();
         //Thread.sleep(20);

        try {
            clipboardData1 =  Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor).toString();
            //LoggerUtils.logInfo("clipbord URL : "+clipboardData1);
        } catch (UnsupportedFlavorException | IOException ex) {
            //actions.sleep(20);
            clipboardData1 = Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor).toString();
        }


        openNewTabAndSwitch();
        driver.get(clipboardData1);
        //Thread.sleep(10000);

        // Assert the content of the new tab
        String actualUrl = driver.getCurrentUrl();
        String expectedUrl = "https://book.bestpricecruises.com/swift-test/cruise?siid=45293&lang=1&showtrace=true&sortColumn=departureDate&sortOrder=asc";
        Assert.assertEquals(actualUrl.toLowerCase(), expectedUrl.toLowerCase(), "URLs do not match");
        Thread.sleep(5000);

        //driver.quit();
        System.out.println("Test1 Completed");
        System.out.println("Value of Clipboard before clearing its content :"+Toolkit.getDefaultToolkit().getSystemClipboard()
                .getData(DataFlavor.stringFlavor).toString());
        clearClipBoard(Toolkit.getDefaultToolkit().getSystemClipboard());
        System.out.println("Value of Clipboard after clearing its value :"+Toolkit.getDefaultToolkit().getSystemClipboard()
                .getData(DataFlavor.stringFlavor).toString());
        driver.get("https://uat.odysol.com/swift/cruise?siid=130386&lang=1&destinationtype=All&transportid=29&sortColumn=cruiselinePriority&sortOrder=asc");

        //Thread.sleep(10000);

        driver.findElement(By.xpath("(//button[contains(@ng-reflect-tooltip,'Share')])[1]")).click();
       //Thread.sleep(20);

        try {
            clipboardData2 =  Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor).toString();
            //LoggerUtils.logInfo("clipbord URL : "+clipboardData1);
        } catch (UnsupportedFlavorException | IOException ex) {
            //actions.sleep(20);
            clipboardData2 = Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor).toString();
        }


        openNewTabAndSwitch();
        System.out.println("Value of ClipboardData2 before clearing its content " +clipboardData2);
        driver.get(clipboardData2);
        Thread.sleep(10000);
        clearClipBoard(Toolkit.getDefaultToolkit().getSystemClipboard());

        try {
            clipboardData2 =  Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor).toString();
            //LoggerUtils.logInfo("clipbord URL : "+clipboardData1);
        } catch (UnsupportedFlavorException | IOException ex) {
            //actions.sleep(20);
            clipboardData2 = Toolkit.getDefaultToolkit().getSystemClipboard()
                    .getData(DataFlavor.stringFlavor).toString();
        }

        System.out.println("Value of ClipboardData2 after clearing its content " +clipboardData2);



        String actualUrl1 = driver.getCurrentUrl();
        String expectedUrl1 = "https://uat.odysol.com/swift/cruise?siid=130386&lang=1&destinationtype=All&transportid=29&sortColumn=cruiselinePriority&sortOrder=asc";
        Assert.assertEquals(actualUrl1.toLowerCase(), expectedUrl1.toLowerCase(), "URLs do not match");
        Thread.sleep(5000);

        driver.quit();

    }

    public void clearClipBoard(Clipboard systemClipboard) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection("");
        clipboard.setContents(stringSelection, null);
    }

    public String openNewTab() {
        Set<String> oldWinHandles = driver.getWindowHandles();
        ((JavascriptExecutor) driver).executeScript("window.open('about:blank','_blank');");
        Set<String> newWinHandles = driver.getWindowHandles();
        newWinHandles.removeAll(oldWinHandles);
        return (String) newWinHandles.toArray()[0];
    }

    public String openNewTabAndSwitch() {
        Set<String> oldWinHandles = driver.getWindowHandles();
        ((JavascriptExecutor) driver).executeScript("window.open('about:blank','_blank');");
        Set<String> newWinHandles = driver.getWindowHandles();
        newWinHandles.removeAll(oldWinHandles);
        String newWinHandle = (String) newWinHandles.toArray()[0];
        driver.switchTo().window(newWinHandle);
        return newWinHandle;
    }

    @AfterClass
    public void tearDown() {
        // Close the WebDriver
        if (driver != null) {
            driver.quit();
        }
    }
}
