package utils;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
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
    private final WebDriverWait wait;

    public Element(By by, WebDriver driver) {
        this.by = by;
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Const.LONG_WAIT));
    }

    public void click() {
        WebElement element = null;
        try {
            element = wait.until(ExpectedConditions.elementToBeClickable(by));
            element.click();
            Reporting.info("Element clicked : " + by.toString());
        } catch (Exception e) {
            element = getElement(5);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            Reporting.info("Element clicked using Javascript executor: " + by.toString());
        }

    }

    public void click(int index) {
        List<WebElement> elements = driver.findElements(by);
        if (elements.size() <= index) {
            throw new RuntimeException("Element index " + index + " not found for locator : " + by);
        }
        elements.get(index).click();
        Reporting.info("Clicked element index " + index + " for locator : " + by);
    }

    public WebElement getElement(int seconds) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            Reporting.error(e.getClass().getName());
            throw new RuntimeException("Element not found or not visible: " + by, e);
        }
    }

    public void enterText(String text) {
        getElement(30).sendKeys(text);
        Reporting.info(text + ": Text entered in element : " + by);
    }

    public boolean isDisplayed(int seconds) {
        try {
            boolean displayed = getElementByWaitPolling(seconds).isDisplayed();
            Reporting.info("Element : " + by + "is set to displayed : " + displayed);
            return displayed;
        } catch (Exception e) {
            Reporting.warn("Element : " + by + "is NOT displayed : ");
            return false;
        }
    }

    public WebElement getElementByWaitPolling(int seconds) {
        try {
            Wait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(seconds))
                    .pollingEvery(Duration.ofSeconds(Const.SHORT_WAIT))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class);

            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            throw new RuntimeException("Element not found or not visible: " + by, e);
        }
    }

    public List<WebElement> getElementsList() {
        List<WebElement> list = driver.findElements(by);
        Reporting.info(list.size() + " : Element found for the locator : " + by);
        return list;
    }

    public boolean isClickable() {
        boolean clickable = getElement(Const.MEDIUM_WAIT).isEnabled();
        Reporting.info(by + " : Element is set to clickable : " + clickable);
        return clickable;
    }

    public String getText() {
        return getElementByWaitPolling(Const.LONG_WAIT).getText();
    }

    public void clickElementFromList(int index) {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)).get(index).click();
            Reporting.info("Element Clicked from the list : " + by);
        } catch (ElementClickInterceptedException e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "document.getElementById('webpush-onsite')?.remove();");
            js.executeScript(
                    "document.getElementById('webklipper-publisher-widget-container-notification-frame')?.remove();");

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)).get(index).click();
            Reporting.info("ElementClickInterceptedException - Element Clicked from the list : " + by);
        }
    }
}
