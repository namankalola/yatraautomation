package yatra.pages;

import java.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.DateUtils;
import utils.Element;

public class MMTHomePage {
    private final WebDriver driver;
    By fromLabel = By.id("fromCity");
    By fromInput = By.xpath("//input[@placeholder='From']");
    By toLabel = By.id("toCity");
    By toInput = By.xpath("//input[@placeholder='To']");
    By departureDate = By.xpath("//*[@data-cy='departureDate']");
    By returnDate = By.xpath("//*[@data-cy='returnArea']");
    By searchButton = By.xpath("//a[text()='Search']");
    By nextMonth = By.xpath("//span[@aria-label='Next Month']");
    By previousMonth = By.xpath("//span[@aria-label='Previous Month']");
    By closeButtonLoginPopup = By.xpath("//span[@data-cy='closeModal']");
    // Locators when traveller label = Travellers and cabin class label = Cabin
    // Class
    By travellersLabel = By.xpath("//span[text()='Travellers']/ancestor::div[@data-cy='flightTravellersOnly']");
    By travellerModel = By.xpath("//div[@class='fltTravellers gbTravellers']");
    By travellerNextButton = By.xpath("//button[text()='NEXT']");
    By cabinClassLabel = By.xpath("//span[text()='Cabin Class']/ancestor::div[@data-cy='flightCabinClass']");
    By cabinClassModel = By.xpath("//div[@class='fltTravellers cabinClassOnly']");
    By cabinClassDoneButton = By.xpath("//button[@data-cy='cabinClassDoneBtn']");

    // Locators when traveller label = Travellers & Class
    By travellerAndCabinClassLabel = By
            .xpath("//span[text()='Travellers & Class']/ancestor::div[@data-cy='flightTraveller']");
    By travellerAndCabinClassApplyButton = By.xpath("//button[text()='APPLY']");

    public MMTHomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickNavigation(String nav) {
        By navigation = By.xpath("//nav[@id='PrimaryNav']//span[text()='" + nav + "']/ancestor::a");
        new Element(navigation, driver).click();
        System.out.println("Navigation Link clicked :" + nav);
    }

    public void setFromCity(String city) {
        new Element(fromLabel, driver).click();
        new Element(fromInput, driver).enterText(city);
        new Element(By.xpath("//div[@class='revampedSearchSuggestionMain']//span[contains(text(),'" + city + "')]"),
                driver).click();
        System.out.println("From City is set to :" + city);
    }

    public void setToCity(String city) {
        new Element(toLabel, driver).click();
        new Element(toInput, driver).enterText(city);
        new Element(By.xpath("//div[@class='revampedSearchSuggestionMain']//span[contains(text(),'" + city + "')]"),
                driver).click();
        System.out.println("From City is set to :" + city);
    }

    public void clickSearch() {
        new Element(searchButton, driver).click();
        System.out.println("Search button clicked");
    }

    public void closeLoginPopup() {
        if (new Element(closeButtonLoginPopup, driver).isDisplayed(10))
            new Element(closeButtonLoginPopup, driver).click();
    }

    public void selectDepartureDate(String departDate) {

        new Element(departureDate, driver).click();
        selectDate(departDate);

    }

    public void selectReturnDate(String returnDt) {

        new Element(returnDate, driver).click();
        selectDate(returnDt);

    }

    private void selectDate(String departDate) {
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
            new Element(nextMonth, driver).click();
        }
    }

    private boolean isMonthVisible(String month, String year) {
        String xpath = "//div[@class='DayPicker-Month'][.//div[@role='heading' and contains(.,'" + month
                + "') and contains(.,'" + year + "')]]";
        if (new Element(By.xpath(xpath), driver).getElementsList()
                .size() > 0)
            return true;
        return false;
    }

    private void clickDate(String day, String month, String year) {
        String xpath = "//div[@class='DayPicker-Month'] [.//div[@role='heading' and contains(.,'" + month
                + "') and contains(.,'" + year + "')]]//div[contains(@class,'DayPicker-Day')]//p[text()='" + day + "']";
        new Element(By.xpath(xpath), driver).click();
    }

    public void selectTravellersInTravellersModel(String travellers) {
        String[] travellerList = travellers.split(",");

        int adults = travellerList.length > 0 ? Math.min(Integer.parseInt(travellerList[0]), 10) : 0;
        int children = travellerList.length > 1 ? Math.min(Integer.parseInt(travellerList[1]), 6) : 0;
        int infants = travellerList.length > 2 ? Math.min(Integer.parseInt(travellerList[2]), 6) : 0;
        new Element(travellersLabel, driver).click();
        if (new Element(travellerModel, driver).isDisplayed(20)) {
            new Element(By.xpath("//li[@data-cy='adults-" + adults + "']"), driver).click();
            new Element(By.xpath("//li[@data-cy='children-" + children + "']"), driver).click();
            new Element(By.xpath("//li[@data-cy='infants-" + infants + "']"), driver).click();
        }

    }

    public void selectTravellersInTravellersAndCabinClassModel(String travellers) {
        String[] travellerList = travellers.split(",");

        int adults = travellerList.length > 0 ? Math.min(Integer.parseInt(travellerList[0]), 10) : 0;
        int children = travellerList.length > 1 ? Math.min(Integer.parseInt(travellerList[1]), 6) : 0;
        int infants = travellerList.length > 2 ? Math.min(Integer.parseInt(travellerList[2]), 6) : 0;
        new Element(travellerAndCabinClassLabel, driver).click();
        if (new Element(travellerModel, driver).isDisplayed(20)) {
            new Element(By.xpath("//li[@data-cy='adults-" + adults + "']"), driver).click();
            new Element(By.xpath("//li[@data-cy='children-" + children + "']"), driver).click();
            new Element(By.xpath("//li[@data-cy='infants-" + infants + "']"), driver).click();
        }

    }

    public void selectCabinClass(String cabinCls) {
        if (!new Element(cabinClassModel, driver).isDisplayed(20)) {
            new Element(cabinClassLabel, driver).click();
        }
        new Element(By.xpath("//p/font[text()='" + cabinCls + "']"), driver).click();

    }

    public void selectTravellersAndCabinClass(String travellers, String travelClass) {
        if (new Element(travellerAndCabinClassLabel, driver).isDisplayed(5)) {
            if (travelClass.equalsIgnoreCase("Economy/ Premium Economy"))
                travelClass = "Economy/Premium Economy";
            selectTravellersInTravellersAndCabinClassModel(travellers);
            selectTravelClass(travelClass);
            clickTravellerAndCabinClassApplyButton();
        } else if (new Element(travellersLabel, driver).isDisplayed(5)) {
            if (travelClass.equalsIgnoreCase("Economy/Premium Economy"))
                travelClass = "Economy/ Premium Economy";
            selectTravellersInTravellersModel(travellers);
            clickTravellerNextButton();
            selectCabinClass(travelClass);
            clickCabinClassDoneButton();
        }
    }

    public void selectTravelClass(String travelClass) {
        new Element(By.xpath("//li[text()='" + travelClass + "']"), driver).click();
    }

    public void clickTravellerAndCabinClassApplyButton() {
        new Element(travellerAndCabinClassApplyButton, driver).click();
    }

    public void clickTravellerNextButton() {
        new Element(travellerNextButton, driver).click();
    }

    public void clickCabinClassDoneButton() {
        new Element(cabinClassDoneButton, driver).click();
    }

   
}
