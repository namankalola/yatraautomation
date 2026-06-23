package yatra.testcases;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import manager.DriverManager;
import utils.Reporting;

public class Base {

    public WebDriver driver;

    @BeforeMethod
    @Parameters("browser")
    public void setup(@Optional("chrome") String browser) {
        driver = DriverManager.initializeWebdriver(browser);
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            Reporting.captureScreenshot(driver, result.getMethod().getMethodName());
        }
        DriverManager.releaseDriverThread();
    }
}
