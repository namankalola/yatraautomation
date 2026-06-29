package manager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportPath = "target/extent-reports/TestReport_" + timestamp + ".html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Test Execution Report");
            spark.config().setReportName("Yatra Flight Search – Automation Suite");
            spark.config().setTimeStampFormat("dd MMM yyyy HH:mm:ss");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Framework", "Selenium + TestNG + POM");
            extent.setSystemInfo("Author", "Namankumar Kalola");
            extent.setSystemInfo("Auther Email Address", "namankalola@yahoo.com");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("URL", "https://www.yatra.com/");
        }
        return extent;
    }

    public static void createTest(String testName, String browser, String[] groups) {
        ExtentTest test = getInstance().createTest(testName).assignCategory(browser.toUpperCase());
        for (String group : groups) {
            test.assignCategory(group);
        }
        testThread.set(test);
    }

    public static ExtentTest getTest() {
        return testThread.get();
    }

    public static synchronized void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static void removeTest() {
        testThread.remove();
    }
}
