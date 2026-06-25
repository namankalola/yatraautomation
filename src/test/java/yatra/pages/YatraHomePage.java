package yatra.pages;

import java.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import manager.Const;
import utils.DateUtils;
import utils.Element;

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
        return By.xpath("//div[@class='react-datepicker__month-container'][.//span[contains(.,'" + month
                + "') and contains(.,'" + year + "')]]//div[contains(@aria-label,'Choose')]/span[contains(text(),'"
                + day + "')]");
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

    // Only actions definations below, no locators
    public YatraHomePage(WebDriver driver) {
        this.driver = driver;
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
    }

    public void setToCity(String city) {
        new Element(toLabel, driver).click();
        new Element(toInput, driver).enterText(city);
        new Element(city(city), driver).click();
    }

    public void clickSearch() {
        new Element(searchButton, driver).click();
    }

    public void selectDepartureDate(String departDate) {
        new Element(departureDate, driver).click();
        if (!new Element(yatraCalendarModel, driver).isDisplayed(Const.SHORT_WAIT))
            new Element(departureDate, driver).click();
        selectDate(departDate);
    }

    public void selectReturnDate(String returnDt) {
        new Element(returnDate, driver).click();
        if (!new Element(yatraCalendarModel, driver).isDisplayed(Const.SHORT_WAIT))
            new Element(returnDate, driver).click();
        selectDate(returnDt);
    }

    private void selectDate(String departDate) {
        departDate = new DateUtils().resolveDate(departDate);
        LocalDate date = new DateUtils().parseDate(departDate);
        String day = date.getDayOfMonth() + "";
        String year = date.getYear() + "";
        String month = new DateUtils().getMonth(date);
        // Navigate to Month by clicking Next button
        navigateToMonth(month, year);
        // Select Date for the Month
        clickDate(day, month, year);
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

    private void clickDate(String day, String month, String year) {
        new Element(dateOfTheMonth(day, month, year), driver).click();
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
    }

    public void selectTravelClass(String travelClass) {
        new Element(travellerClass(travelClass), driver).click();
    }

    public void clickTravellerAndCabinClassDoneButton() {
        new Element(travellersAndClassDoneButton, driver).click();
    }

}
