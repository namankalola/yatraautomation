package yatra.pages;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import manager.Const;
import utils.DateUtils;
import utils.Reporting;

public class YatraSearchResultsPage extends BasePage {

    // private final WebDriver driver;
    private By upFare = By.xpath(
            "//div[contains(@class,'selected') and contains(@class,'flightItem') and contains(@class,'left')]//span[@class='rt-segment-price']");
    private By downFare = By.xpath(
            "//div[contains(@class,'selected') and contains(@class,'flightItem') and contains(@class,'right')]//span[@class='rt-segment-price']");
    private By tatalFareInternational = By.xpath("//*[contains(@ng-show,'flt.totalFare')]");
    private By tatalFareDomesticOneway = By.xpath("//p[@class='ow-price-above-btn']");
    private By totalFareRoundMultiCity = By.xpath("//p[contains(@class,'total-fare')]/following-sibling::p");
    private By flightCards = By.xpath(
            "(//input[@type='radio']|//button[contains(.,'View Fares')]|//button[contains(.,'Selected')])/ancestor::div[contains(@class,'flightItem border-shadow pr')]");
    private By modifySearchButton = By.xpath("//button/span[text()='Modify Search']");
    private By departDateInput = By
            .xpath("//input[@name='flight_depart_date_0']/ancestor::div[contains(@class,'depart')]");
    // private By returnDateInput =
    // By.xpath("//span[text()='Return']/following-sibling::input[@name='arrivalDate_0']");
    private By nextMonth = By
            .xpath("//div[contains(@class,'datepicker-inner full')]/i[@class='ytfi-arrow-right cursor-pointer']");
    private By searchAgainButton = By.xpath("//button/span[text()='Search Again']");
    private By yatraCalendarModel = By.xpath(
            "//div[contains(@class,'depart')]//div[contains(@class,'datepicker-wrapper months-2') and not(contains(@class,'ng-hide'))]//div[@class='datepicker-inner full']");
    // By multiCityFlightCards = By
    // .xpath("//button[contains(text(),'Selected')]/ancestor::div[contains(@class,'flight-det')]");

    // Dynamic locators listed below
    private By monthTitle(String month, String year) {
        return By.xpath("//div[@class='month-name full' and contains(text(),'" + month + " " + year + "')]");
    }

    private By dateOfTheMonth(String day, String month, String year) {
        return By.xpath("//div[@class='month-name full' and contains(text(),'" + month + " " + year
                + "')]/..//span[@class='full date-val' and text()='" + day + "']");
    }

    private By domesticDate(String date) {
        return By.xpath("//div[contains(@class,'daymatrix')]//p[text()='" + date + "']");
    }

    // Only actions definations below, no locators

    public YatraSearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public boolean verifySearchResultDisplayed() {
        boolean displayed = element(flightCards).isDisplayed(Const.LONG_WAIT);
        Reporting.step("Flight search result is displayed: " + displayed);
        return displayed;
    }

    public Double getUpJouneyFare() {
        return Double.parseDouble(element(upFare).getText().replaceAll("[^\\d.]", ""));
    }

    public Double getReturnJouneyFare() {
        return Double.parseDouble(element(downFare).getText().replaceAll("[^\\d.]", ""));
    }

    public Double getTotalFare(String tripType) {
        Double totalFare = 0.0;
        if (tripType.equalsIgnoreCase("OneWay_Domestic"))
            totalFare = Double.parseDouble(element(tatalFareDomesticOneway).getText().replaceAll("[^\\d.]", ""));
        else if (tripType.equalsIgnoreCase("OneWay_International")
                || tripType.equalsIgnoreCase("RoundTrip_International")
                || tripType.equalsIgnoreCase("MultiCity_International"))
            totalFare = Double.parseDouble(element(tatalFareInternational).getText().replaceAll("[^\\d.]", ""));
        else if (tripType.equalsIgnoreCase("RoundTrip_Domestic") || tripType.equalsIgnoreCase("MultiCity_Domestic"))
            totalFare = Double.parseDouble(element(totalFareRoundMultiCity).getText().replaceAll("[^\\d.]", ""));

        Reporting.step("Current fare for trip :" + tripType + " is :" + totalFare);
        return totalFare;
    }

    public void clickModifySearchButton() {
        element(modifySearchButton).click();
    }

    public void clickDepartDate() {
        element(departDateInput).click();
        if (!element(yatraCalendarModel).isDisplayed(Const.SHORT_WAIT))
            element(departDateInput).click();
    }

    public void clickSearchAgainButton() {
        element(searchAgainButton).click();
    }

    public Map<String, Double> getFareForNextNDays(String dateStr, int days, String tripType) {
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
            Double fare = getTotalFare(tripType);
            fares.put(departDate, fare);
        }
        Reporting.step("Fetching fares for next " + days + " days and fares are :" + fares.toString());
        return fares;
    }

    private void navigateToMonth(String month, String year) {
        while (!isMonthVisible(month, year)) {
            List<WebElement> elements = element(nextMonth).getElementsList();
            for (WebElement we : elements) {
                if (we.isDisplayed())
                    we.click();
            }
        }
    }

    private boolean isMonthVisible(String month, String year) {
        if (element(monthTitle(month, year)).getElementsList()
                .size() > 0)
            return true;
        return false;
    }

    private void clickDate(String day, String month, String year) {
        element(dateOfTheMonth(day, month, year)).click();
    }

    public Map<String, Double> getDomesticFareForNextNDays(String dateStr, int days) {
        Map<String, Double> fares = new HashMap<String, Double>();
        String dates = new DateUtils().resolveDate(dateStr);
        for (int i = 1; i <= days; i++) {
            String departDate = new DateUtils().addDaysToDate(dates, i);
            LocalDate date = new DateUtils().parseDate(departDate);
            String dd = date.getDayOfMonth() + "";
            String mmm = new DateUtils().getMonthMMM(date);
            String ddd = new DateUtils().getDayOfTheWeekDDD(date);
            String day = ddd + ", " + dd + " " + mmm;
            clickDate(day);
            verifySearchResultDisplayed();
            Double fare = getTotalFare("Domestic");
            fares.put(departDate, fare);

        }
        Reporting.step("Fetching fares for next " + days + " days and fares are :" + fares.toString());
        return fares;
    }

    public void clickDate(String date) {
        element(domesticDate(date)).click();
    }

    public Double lowestFare(Map<String, Double> fares) {
        List<Map.Entry<String, Double>> list = new ArrayList<>(fares.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Reporting
                .step("Lowest fare for the departure date: " + list.get(0).getKey() + "is : " + list.get(0).getValue());
        return list.get(0).getValue();
    }

    // public boolean verifyResultDisplayedForMultiCitySearch() {
    // boolean displayed = element(flightCards,
    // driver).isDisplayed(Const.LONG_WAIT);
    // Reporting.step("Flight search result is displayed: " + displayed);
    // return displayed;
    // }
}
