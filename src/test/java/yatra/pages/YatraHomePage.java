package yatra.pages;

import java.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import manager.Const;
import utils.DateUtils;
import utils.Reporting;

public class YatraHomePage extends BasePage {
    private By fromLabel = By.xpath("//div[contains(@aria-label,'Departure From')]");
    private By fromInput = By.xpath("//label[text()='Departure From']/..//input");
    private By toLabel = By.xpath("//div[contains(@aria-label,'Going To')]");
    private By toInput = By.xpath("//label[text()='Going To']/..//input");
    private By departureDate = By
            .xpath("//span[text()='Departure Date']/ancestor::div[contains(@aria-label,'Departure Date')]");
    private By returnDate = By.xpath("//span[text()='Return Date']/ancestor::div[contains(@aria-label,'Return Date')]");
    private By searchButton = By.xpath("//button[text()='Search']");
    private By nextMonth = By.xpath("//button[@aria-label='Next Month']");
    // private By previousMonth = By.xpath("//button[@aria-label='Previous
    // Month']");
    private By travellersAndClassLabel = By
            .xpath("//span[text()='Travellers & Class']/ancestor::div[contains(@aria-label,'Travellers class')]");
    private By travellersAndClassDoneButton = By.xpath("//button[text()='Done']");
    private By yatraCalendarModel = By.xpath("//div[contains(@class,'yatra-calendar')]");
    private By multiCityfromLabel = By.xpath("//div[contains(@aria-label,'From')]");
    private By multiCityFromInput = By.xpath("//label[text()='From']/..//input");
    private By multiCityToLabel = By.xpath("//div[contains(@aria-label,'To')]");
    private By multiCityToInput = By.xpath("//label[text()='To']/..//input");
    private By addAnotherCityButton = By.xpath("//button[text()='+ Add Another City']");

    // Dynamic locators listed below
    private By navigation(String nav) {
        return By.xpath("//nav[@id='PrimaryNav']//span[text()='" + nav + "']/ancestor::a");
    }

    private By city(String city) {
        return By.xpath("//li//span[text()='" + city + "']");
    }

    private By monthTitle(String month, String year) {
        return By.xpath("//div[@class='header-content-wrapper']//span[text()='" + month + " " + year + "']");
    }

    // private By dateOfTheMonth(String day, String month, String year) {
    // return
    // By.xpath("//div[@class='react-datepicker__month-container'][.//span[contains(.,'"
    // + month
    // + "') and contains(.,'" + year +
    // "')]]//div[contains(@aria-label,'Choose')]/span[contains(text(),'"
    // + day + "')]");
    // }

    private By dateOfTheMonth(String day, String month, String year, String date) {
        String xpath = "//div[@aria-label='Choose " + date + "']";
        return By.xpath("//div[@class='react-datepicker__month-container'][.//span[contains(.,'"
                + month
                + "') and contains(.,'" + year + "')]]//div[@aria-label='Choose " + date + "']");

    }

    private By adultTraveller(int adults) {
        return By.xpath("//p[text()='Adult']/following-sibling::div//li[text()='" + adults + "']");
    }

    private By childTraveller(int children) {
        return By.xpath("//p[text()='Child']/following-sibling::div//li[text()='" + children + "']");
    }

    private By infantTraveller(int infants) {
        return By.xpath("//p[text()='Infant']/following-sibling::div//li[text()='" + infants + "']");
    }

    private By travellerClass(String travelClass) {
        return By.xpath("//label[@aria-label='" + travelClass + "']");
    }

    // private By tripTypeRadio(String tripType) {
    // // String xpath = "//h4[text()='" + tripType + "']/ancestor::label//input";
    // return By.xpath("//h4[normalize-space()='Multi
    // City']/ancestor::label//input");
    // }

    // Only actions definations below, no locators
    public YatraHomePage(WebDriver driver) {
        super(driver);
    }

    public void clickNavigation(String nav) {
        element(navigation(nav)).click();
    }

    public void setFromCity(String city) {
        element(fromLabel).click();
        element(fromInput).enterText(city);
        element(city(city)).click();
        Reporting.step("Entering flight details — From: " + city);
    }

    public void setToCity(String city) {
        element(toLabel).click();
        element(toInput).enterText(city);
        element(city(city)).click();
        Reporting.step("Entering flight details — Destination: " + city);
    }

    public void clickSearch() {
        element(searchButton).click();
        Reporting.step("Clicking Search");
    }

    public void selectDepartureDate(String departDate) {
        element(departureDate).click();
        if (!element(yatraCalendarModel).isDisplayed(Const.SHORT_WAIT))
            element(departureDate).click();
        selectDate(departDate);
        Reporting.step("Entering flight details — Departure Date : " + departDate);
    }

    public void selectReturnDate(String returnDt) {
        element(returnDate).click();
        if (!element(yatraCalendarModel).isDisplayed(Const.SHORT_WAIT))
            element(returnDate).click();
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
            element(nextMonth).click(1);
        }
    }

    private boolean isMonthVisible(String month, String year) {
        if (element(monthTitle(month, year)).getElementsList()
                .size() > 0)
            return true;
        return false;
    }

    // private void clickDate(String day, String month, String year) {
    // new Element(dateOfTheMonth(day, month, year)).click();
    // }

    private void clickDate(String day, String month, String year, String date) {
        element(dateOfTheMonth(day, month, year, date)).click();
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
        element(travellersAndClassLabel).click();
        if (!element(travellersAndClassDoneButton).isDisplayed(Const.MEDIUM_WAIT)) {
            element(travellersAndClassLabel).click();
        }
        element(adultTraveller(adults)).click();
        element(childTraveller(children)).click();
        element(infantTraveller(infants)).click();
        Reporting.step("Selected traveller details : {Adult: " + adults + ", Childrens: " + children + ", Infants: "
                + infants);
    }

    public void selectTravelClass(String travelClass) {
        element(travellerClass(travelClass)).click();
        Reporting.step("Selected traveller cabin class: " + travelClass);
    }

    public void clickTravellerAndCabinClassDoneButton() {
        element(travellersAndClassDoneButton).click();
    }

    // Multi City Scenario

    public void setMultiCityFrom(String city, int leg) {
        element(multiCityfromLabel).clickElementFromList(leg);
        element(multiCityFromInput).enterText(city);
        element(city(city)).click();
        Reporting.step("Entering Multi City flight details — From City : " + leg + " with city : " + city);
    }

    public void setMultiCityTo(String city, int leg) {
        element(multiCityToLabel).clickElementFromList(leg);
        element(multiCityToInput).enterText(city);
        element(city(city)).click();
        Reporting.step("Entering Multi City flight details — Destination City : " + leg + " with city : " + city);
    }

    public void selectMultiCityDepartureDate(String departDate, int leg) {
        element(departureDate).clickElementFromList(leg);
        if (!element(yatraCalendarModel).isDisplayed(Const.SHORT_WAIT))
            element(departureDate).click();
        selectDate(departDate);
        Reporting
                .step("Entering Multi City flight details — Departure Date : " + leg + " with date as : " + departDate);
    }

    public void clickTripTypeRadio(String tripType) {
        // new Element(tripTypeRadio(tripType)).click();
        element(By.xpath("//label[@aria-label='" + tripType + "']")).click();
    }

    public void clickAddAnotherCityButton() {
        element(addAnotherCityButton).click();
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
