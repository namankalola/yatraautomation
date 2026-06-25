package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reporting {
    private static final Logger logger = LogManager.getLogger(Reporting.class);
    private static final ThreadLocal<String> testName = new ThreadLocal<>();

    public static void setTestName(String name) {
        testName.set(name);
    }

    public static String getTestName() {
        return testName.get();
    }

    public static void captureScreenshot(WebDriver driver, String testName) {

        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filePath = "target/screenshots/" + testName + "_" + timestamp + ".png";
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.createDirectories(new File("target/screenshots").toPath());
            Files.copy(source.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
            fail("Screenshot is saved in this location : " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void info(String message) {
        logger.info(message);
        System.out.println(message);
    }

    public static void warn(String message) {
        logger.warn(message);
        System.out.println(message);
    }

    public static void error(String message) {
        logger.error(message);
        System.out.println(message);
    }

    public static void pass(String message) {
        logger.info("PASS : " + message);
        System.out.println("PASS : " + message);
    }

    public static void fail(String message) {
        logger.error("FAIL : " + message);
        System.out.println("FAIL : " + message);
    }
}
