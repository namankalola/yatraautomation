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

public class Reporting {
    public static void captureScreenshot(WebDriver driver, String testName) {

        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filePath = "target/screenshots/" + testName + "_" + timestamp + ".png";
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.createDirectories(new File("target/screenshots").toPath());
            Files.copy(source.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot is saved in this location : "+filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
