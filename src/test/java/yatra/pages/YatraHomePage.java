package yatra.pages;

import java.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import manager.Const;
import utils.DateUtils;
import utils.Element;
import utils.Reporting;

public class YatraHomePage {
    private final WebDriver driver;
    By fromLabel = By.xpath("//div[contains(@aria-label,'Departure From')]");
    By fromInput = By.xpath("//label[text()='Departure From']/..//input");
    By toLabel = By.xpath("//div[contains(@aria-label,'Going To')]");
    By toInput = By.xpath("//label[text()='Going To']/..//input");
    By departureDate = By
            .xpath("//span[text()='Departure Date']/ancestor::div[contains(@aria-label,'Departure Date')]");
    By returnDate = By.xpath("//span[text()='Return Date']/ancestor::div[contains(@aria-label,'Return Date')]");
    By searchButton = By.xpath("//button[text()='Search']");
    By nextMonth = By.xpath("//button[@aria-label='Next Month']");
    By previousMonth = By.xpath("//button[@aria-label='Previous Month']");
    By travellersAndClassLabel = By
            .xpath("//span[text()='Travellers & Class']/ancestor::div[contains(@aria-label,'Travellers class')]");
    By travellersAndClassDoneButton = By.xpath("//button[text()='Done']");
    By yatraCalendarModel = By.xpath("//div[contains(@class,'yatra-calendar')]");
    By multiCityfromLabel = By.xpath("//div[contains(@aria-label,'From')]");
    By multiCityFromInput = By.xpath("//label[text()='From']/..//input");
    By multiCityToLabel = By.xpath("//div[contains(@aria-label,'To')]");
    By multiCityToInput = By.xpath("//label[text()='To']/..//input");
    By addAnotherCityButton = By.xpath("//button[text()='+ Add Another City']");

    // Dynamic locators listed below
    By navigation(String nav) {
        return By.xpath("//nav[@id='PrimaryNav']//span[text()='" + nav + "']/ancestor::a");
    }

    By city(String city) {
        return By.xpath("//li//span[text()='" + city + "']");
    }

    By monthTitle(String month, String year) {
        return By.xpath("//div[@class='header-content-wrapper']//span[text()='" + month + " " + year + "']");
    }

    By dateOfTheMonth(String day, String month, String year) {
        return By.xpath("//div[@class='react-datepicker__month-container'][.//span[contains(.,'"
                + month
                + "') and contains(.,'" + year +
                "')]]//div[contains(@aria-label,'Choose')]/span[contains(text(),'"
                + day + "')]");
    }

    By dateOfTheMonth(String day, String month, String year, String date) {
        String xpath = "//div[@aria-label='Choose " + date + "']";
        return By.xpath("//div[@class='react-datepicker__month-container'][.//span[contains(.,'"
                + month
                + "') and contains(.,'" + year + "')]]//div[@aria-label='Choose " + date + "']");

    }

    By adultTraveller(int adults) {
        return By.xpath("//p[text()='Adult']/following-sibling::div//li[text()='" + adults + "']");
    }

    By childTraveller(int children) {
        return By.xpath("//p[text()='Child']/following-sibling::div//li[text()='" + children + "']");
    }

    By infantTraveller(int infants) {
        return By.xpath("//p[text()='Infant']/following-sibling::div//li[text()='" + infants + "']");
    }

    By travellerClass(String travelClass) {
        return By.xpath("//label[@aria-label='" + travelClass + "']");
    }

    By tripTypeRadio(String tripType) {
        // String xpath = "//h4[text()='" + tripType + "']/ancestor::label//input";
        return By.xpath("//h4[normalize-space()='Multi City']/ancestor::label//input");
    }

    // Only actions definations below, no locators
    public YatraHomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo(String url) {
        driver.get(url);
        Reporting.step("Navigating to : " + url);
    }

    protected Element element(By locator) {
        return new Element(locator, driver);
    }

    public void clickNavigation(String nav) {
        new Element(navigation(nav), driver).click();
    }

    public void setFromCity(String city) {
        new Element(fromLabel, driver).click();
        new Element(fromInput, driver).enterText(city);
        new Element(city(city), driver).click();
        Reporting.step("Entering flight details — From: " + city);
    }

    public void setToCity(String city) {
        new Element(toLabel, driver).click();
        new Element(toInput, driver).enterText(city);
        new Element(city(city), driver).click();
        Reporting.step("Entering flight details — Destination: " + city);
    }

    public void clickSearch() {
        new Element(searchButton, driver).click();
        Reporting.step("Clicking Search");
    }

    public void selectDepartureDate(String departDate) {
        new Element(departureDate, driver).click();
        if (!new Element(yatraCalendarModel, driver).isDisplayed(Const.SHORT_WAIT))
            new Element(departureDate, driver).click();
        selectDate(departDate);
        Reporting.step("Entering flight details — Departure Date : " + departDate);
    }

    public void selectReturnDate(String returnDt) {
        new Element(returnDate, driver).click();
        if (!new Element(yatraCalendarModel, driver).isDisplayed(Const.SHORT_WAIT))
            new Element(returnDate, driver).click();
        selectDate(returnDt);
        Reporting.step("Entering flight details — Return Date : " + returnDt);
    }

    private void selectDate(String departDate) {
        departDate = new DateUtils().resolveDate(departDate);
        LocalDate date = new DateUtils().parseDate(departDate);
        String formattedDate = new DateUtils().getFormattedDateWithSuffix(date);
        // String day = new DateUtils().getDay(date) + "";
        String day = new DateUtils().getDay(date) + "";
        String year = date.getYear() + "";
        String month = new DateUtils().getMonth(date);
        // Navigate to Month by clicking Next button
        navigateToMonth(month, year);
        // Select Date for the Month
        clickDate(day, month, year, formattedDate);
    }

    private void navigateToMonth(String month, String year) {
        while (!isMonthVisible(month, year)) {
            new Element(nextMonth, driver).click(1);
        }
    }

    private boolean isMonthVisible(String month, String year) {
        if (new Element(monthTitle(month, year), driver).getElementsList()
                .size() > 0)
            return true;
        return false;
    }

    // private void clickDate(String day, String month, String year) {
    //     new Element(dateOfTheMonth(day, month, year), driver).click();
    // }

    private void clickDate(String day, String month, String year, String date) {
        new Element(dateOfTheMonth(day, month, year, date), driver).click();
    }

    public void selectTravellersAndCabinClass(String travellers, String travelClass) {
        selectTravellersInTravellersModel(travellers);
        selectTravelClass(travelClass);
        clickTravellerAndCabinClassDoneButton();
    }

    public void selectTravellersInTravellersModel(String travellers) {
        String[] travellerList = travellers.split(",");

        int adults = travellerList.length > 0 ? Math.min(Integer.parseInt(travellerList[0]), 10) : 0;
        int children = travellerList.length > 1 ? Math.min(Integer.parseInt(travellerList[1]), 6) : 0;
        int infants = travellerList.length > 2 ? Math.min(Integer.parseInt(travellerList[2]), 6) : 0;
        new Element(travellersAndClassLabel, driver).click();
        if (!new Element(travellersAndClassDoneButton, driver).isDisplayed(Const.MEDIUM_WAIT)) {
            new Element(travellersAndClassLabel, driver).click();
        }
        new Element(adultTraveller(adults), driver).click();
        new Element(childTraveller(children), driver).click();
        new Element(infantTraveller(infants), driver).click();
        Reporting.step("Selected traveller details : {Adult: " + adults + ", Childrens: " + children + ", Infants: "
                + infants);
    }

    public void selectTravelClass(String travelClass) {
        new Element(travellerClass(travelClass), driver).click();
        Reporting.step("Selected traveller cabin class: " + travelClass);
    }

    public void clickTravellerAndCabinClassDoneButton() {
        new Element(travellersAndClassDoneButton, driver).click();
    }

    // Multi City Scenario

    public void setMultiCityFrom(String city, int leg) {
        new Element(multiCityfromLabel, driver).clickElementFromList(leg);
        new Element(multiCityFromInput, driver).enterText(city);
        new Element(city(city), driver).click();
        Reporting.step("Entering Multi City flight details — From City : " + leg + " with city : " + city);
    }

    public void setMultiCityTo(String city, int leg) {
        new Element(multiCityToLabel, driver).clickElementFromList(leg);
        new Element(multiCityToInput, driver).enterText(city);
        new Element(city(city), driver).click();
        Reporting.step("Entering Multi City flight details — Destination City : " + leg + " with city : " + city);
    }

    public void selectMultiCityDepartureDate(String departDate, int leg) {
        new Element(departureDate, driver).clickElementFromList(leg);
        if (!new Element(yatraCalendarModel, driver).isDisplayed(Const.SHORT_WAIT))
            new Element(departureDate, driver).click();
        selectDate(departDate);
        Reporting
                .step("Entering Multi City flight details — Departure Date : " + leg + " with date as : " + departDate);
    }

    public void clickTripTypeRadio(String tripType) {
        // new Element(tripTypeRadio(tripType), driver).click();
        new Element(By.xpath("//label[@aria-label='Multi City']"), driver).click();
    }

    public void clickAddAnotherCityButton() {
        new Element(addAnotherCityButton, driver).click();
    }

    public void disableWebPopups() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript(
                "document.getElementById('webpush-onsite')?.remove();");
    }

    public void disableNotificationPopup() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "document.getElementById('webklipper-publisher-widget-container-notification-frame')?.remove();");
    }
}
