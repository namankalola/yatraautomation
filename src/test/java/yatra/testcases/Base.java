package yatra.testcases;

import java.lang.reflect.Method;
import java.util.Arrays;
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
    public void setup(@Optional("edge") String browser, Method method) {
        String testName = method.getName();
        driver = DriverManager.initializeWebdriver(browser, testName);
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            Throwable error = result.getThrowable();
            Reporting.error("TEST FAILED : " + result.getMethod().getMethodName());
            Reporting.error("REASON : " + error.getMessage());
            Reporting.error(Arrays.toString(error.getStackTrace()));
            Reporting.captureScreenshot(driver, result.getMethod().getMethodName());
        }
        DriverManager.releaseDriverThread();
    }
}
