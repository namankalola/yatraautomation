package yatra.testcases;

import org.testng.annotations.Test;
import yatra.pages.MMTHomePage;
import yatra.pages.YatraHomePage;

public class Archieved_MMTSearchFlights extends Base {
    MMTHomePage homePage;
    YatraHomePage yatraHomePage;

    @Test(description = "MakeMyTrip - Search flight and get cheapest flight for given n days")
    public void mmt_search_flight() throws InterruptedException {
        driver.get("https://www.makemytrip.com/flights/");
        Thread.sleep(5000);
        System.out.println("Sample test started : ");
        homePage = new MMTHomePage(driver);
        homePage.closeLoginPopup();
        homePage.setFromCity("Ahmedabad");
        homePage.setToCity("New York");
        homePage.selectDepartureDate("Wed Jul 01 2026");
        homePage.selectReturnDate("Sep 05 2026");
        homePage.selectTravellersAndCabinClass("5,2,3", "Economy/ Premium Economy");
        homePage.clickSearch();
        System.out.println("Search clicked");
    }
}
