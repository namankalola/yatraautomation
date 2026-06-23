package utils;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import manager.Const;

public class Element {
    private final WebDriver driver;
    private final By by;

    public Element(By by, WebDriver driver) {
        this.by = by;
        this.driver = driver;
    }

    public void click() {
        WebElement element = null;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Const.MEDIUM_WAIT));
        try {
            element = wait.until(ExpectedConditions.elementToBeClickable(by));
            element.click();
        } catch (Exception e) {
            element = getElement(5);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
        }
        System.out.println("Element clicked : " + by);
    }

    public WebElement getElement(int seconds) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            throw new RuntimeException("Element not found or not visible: " + by, e);
        }
    }

    public void enterText(String text) {
        getElement(30).sendKeys(text);
    }

    public boolean isDisplayed(int seconds) {
        try {
            return getElement(seconds).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement getElementByWaitPolling(int seconds) {
        try {
            Wait<WebDriver> wait = new FluentWait<>(driver)

                    .withTimeout(Duration.ofSeconds(seconds))

                    .pollingEvery(Duration.ofSeconds(Const.SHORT_WAIT))

                    .ignoring(NoSuchElementException.class);

            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("example")));
        } catch (Exception e) {
            throw new RuntimeException("Element not found or not visible: " + by, e);
        }
    }

    public List<WebElement> getElementsList() {
        return driver.findElements(by);
    }

    public boolean isClickable(){
        return getElement(Const.MEDIUM_WAIT).isEnabled();
    }

    public String getText(){
        return getElement(Const.MEDIUM_WAIT).getText();
    }
}
