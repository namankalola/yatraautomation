package yatra.pages;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import manager.Const;
import utils.DateUtils;
import utils.Element;

public class YatraSearchResultsPage {

    private final WebDriver driver;
    By upFare = By.xpath(
            "//div[contains(@class,'selected') and contains(@class,'flightItem') and contains(@class,'left')]//span[@class='rt-segment-price']");
    By downFare = By.xpath(
            "//div[contains(@class,'selected') and contains(@class,'flightItem') and contains(@class,'right')]//span[@class='rt-segment-price']");
    By tatalFare = By.xpath("//*[contains(@ng-show,'flt.totalFare')]");
    By modifySearchButton = By.xpath("//button/span[text()='Modify Search']");
    By departDateInput = By.xpath("//span[text()='Date']/following-sibling::input[@name='flight_depart_date_0']");
    By returnDateInput = By.xpath("//span[text()='Return']/following-sibling::input[@name='arrivalDate_0']");
    By nextMonth = By
            .xpath("//div[contains(@class,'datepicker-inner full')]/i[@class='ytfi-arrow-right cursor-pointer']");
    By searchAgainButton = By.xpath("//button/span[text()='Search Again']");
    By yatraCalendarModel = By.xpath("//div[@class='datepicker-inner full']");

    // Dynamic locators listed below
    By monthTitle(String month, String year) {
        return By.xpath("//div[@class='month-name full' and contains(text(),'" + month + " " + year + "')]");
    }

    By dateOfTheMonth(String day, String month, String year) {
        return By.xpath("//div[@class='month-name full' and contains(text(),'" + month + " " + year
                + "')]/..//span[@class='full date-val' and text()='" + day + "']");
    }

    // Only actions definations below, no locators

    public YatraSearchResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean verifySearchResultDisplayed() {
        return new Element(tatalFare, driver).isDisplayed(Const.LONG_WAIT);
    }

    public String getUpJouneyFare() {
        return new Element(upFare, driver).getText();
    }

    public String getReturnJouneyFare() {
        return new Element(downFare, driver).getText();
    }

    public String getTotalFare() {
        return new Element(tatalFare, driver).getText();
    }

    public void clickModifySearchButton() {
        new Element(modifySearchButton, driver).click();
    }

    public void clickDepartDate() {
        new Element(departDateInput, driver).click();
        if (!new Element(yatraCalendarModel, driver).isDisplayed(Const.SHORT_WAIT))
            new Element(departDateInput, driver).click();
    }

    public void clickSearchAgainButton() {
        new Element(searchAgainButton, driver).click();
    }

    public Map<String, Double> getFareForNextNDays(String dateStr, int days) {
        Map<String, Double> fares = new HashMap<String, Double>();
        String dates = new DateUtils().resolveDate(dateStr);
        for (int i = 1; i <= days; i++) {
            clickDepartDate();
            String departDate = new DateUtils().addDaysToDate(dates, i);
            LocalDate date = new DateUtils().parseDate(departDate);
            String day = date.getDayOfMonth() + "";
            String year = date.getYear() + "";
            String month = new DateUtils().getMonthMMM(date);
            // Navigate to Month by clicking Next button
            navigateToMonth(month, year);
            // Select Date for the Month
            clickDate(day, month, year);
            clickSearchAgainButton();
            verifySearchResultDisplayed();
            String fare = getTotalFare();
            fares.put(departDate, Double.parseDouble(fare.replaceAll(",", "")));

        }
        System.out.println(fares.toString());
        return fares;
    }

    private void navigateToMonth(String month, String year) {
        while (!isMonthVisible(month, year)) {
            List<WebElement> elements = new Element(nextMonth, driver).getElementsList();
            for (WebElement we : elements) {
                if (we.isDisplayed())
                    we.click();
            }
        }
    }

    private boolean isMonthVisible(String month, String year) {
        if (new Element(monthTitle(month, year), driver).getElementsList()
                .size() > 0)
            return true;
        return false;
    }

    private void clickDate(String day, String month, String year) {
        new Element(dateOfTheMonth(day, month, year), driver).click();
    }
}
