package manager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverManager {
    private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static WebDriver initializeWebdriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome":
                driverThread.set(new ChromeDriver());
                break;
            case "firefox":
                driverThread.set(new FirefoxDriver());
                break;
            case "edge":
                driverThread.set(new EdgeDriver());
                break;
            default:
                break;
        }

        return getDriver();
    }

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static void releaseDriverThread(){
         WebDriver driver = driverThread.get();
        if(driver != null){
            driver.quit();
            driverThread.remove();
        }
    }
}
