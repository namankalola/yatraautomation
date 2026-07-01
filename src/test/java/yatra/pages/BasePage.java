package yatra.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import utils.Element;

public class BasePage {
    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected Element element(By locator) {
        return new Element(locator, driver);
    }

    public void navigateTo(String url) {
        driver.get(url);
    }
}
