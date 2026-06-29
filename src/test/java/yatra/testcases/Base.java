package yatra.testcases;

import java.io.IOException;
import java.util.Arrays;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import manager.DriverManager;
import utils.Excels;
import utils.Reporting;
import manager.ExtentReportManager;

public class Base {

    public WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    @Parameters({ "browser"})
    public void setup(@Optional("edge") String browser, ITestResult result) {
        String[] groups = result.getMethod().getGroups();
        String testName = result.getMethod().getMethodName();
        ExtentReportManager.createTest(testName, browser, groups);
        driver = DriverManager.initializeWebdriver(browser, testName);
        driver.manage().window().maximize();

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        String methodName = Reporting.getTestName();

        if (ITestResult.FAILURE == result.getStatus()) {
            Throwable error = result.getThrowable();
            Reporting.error("TEST FAILED : " + methodName);
            Reporting.error("REASON : " + error.getMessage());
            Reporting.error(Arrays.toString(error.getStackTrace()));
            Reporting.captureScreenshot(driver, methodName);

        } else if (ITestResult.SUCCESS == result.getStatus()) {
            Reporting.pass("TEST PASSED : " + methodName);
        } else if (ITestResult.SKIP == result.getStatus()) {
            Reporting.warn("TEST SKIPPED : " + methodName);
        }

        DriverManager.releaseDriverThread();
        ExtentReportManager.removeTest();
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() throws IOException {

        Excels.loadWorkbook("src\\test\\java\\yatra\\testdata\\DemoTestData.xlsx");
        ExtentReportManager.getInstance();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        Excels.clearWorkbookCache();
        ExtentReportManager.flushReport();
        Reporting.info("ExtentReports HTML report written to target/extent-reports/");
    }
}
