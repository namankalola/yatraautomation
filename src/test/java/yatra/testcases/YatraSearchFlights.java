package yatra.testcases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

import yatra.dataproviders.FlightSearchDataProvider;
import yatra.pages.MMTHomePage;
import yatra.pages.YatraHomePage;
import yatra.pages.YatraSearchResultsPage;

/**
 * Test Class: YatraSearchFlights
 *
 * Purpose:
 * Validates the end-to-end flight search functionality on Yatra and
 * identifies the cheapest available fare within a specified date range.
 *
 * Test Flow:
 * 1. Navigate to Yatra Flight Search page.
 * 2. Enter source and destination cities.
 * 3. Select departure and return dates.
 * 4. Select passengers and cabin class.
 * 5. Perform flight search.
 * 6. Verify search results are displayed successfully.
 * 7. Capture the current trip fare.
 * 8. Retrieve fares for the next N days.
 * 9. Sort fares and identify the cheapest available option.
 *
 * Test Data:
 * Source City : Ahmedabad
 * Destination City : Sydney
 * Departure Date : Today
 * Return Date : Today + 7 Days
 * Travellers : 2 Adults, 2 Children, 1 Infant
 * Cabin Class : Premium Economy
 *
 * Expected Result:
 * - Flight search results should be displayed.
 * - Fare details should be retrieved successfully.
 * - Lowest fare among the specified date range should be identified.
 *
 * Author: Namanumar Kalola
 * Framework: Selenium + TestNG + Page Object Model (POM)
 * Created On: 22-Jun-2026
 * Version: 1.0
 */

public class YatraSearchFlights extends Base {
    MMTHomePage homePage;
    YatraHomePage yatraHomePage;
    YatraSearchResultsPage yatraSearchResultsPage;

    @Test(description = "Yatra - Search flight and get cheapest flight for given n days", dataProvider = "flightSearchData", dataProviderClass = FlightSearchDataProvider.class)
    public void yatra_search_flight(Map<String, String> data) throws InterruptedException {
        driver.get("https://flight.yatra.com/");
        yatraHomePage = new YatraHomePage(driver);
        yatraSearchResultsPage = new YatraSearchResultsPage(driver);
        yatraHomePage.setFromCity(data.get("from"));
        yatraHomePage.setToCity(data.get("to"));
        yatraHomePage.selectDepartureDate(data.get("depart"));
        yatraHomePage.selectReturnDate(data.get("return"));
        yatraHomePage.selectTravellersAndCabinClass(data.get("travellers"), data.get("cabinClass"));

        yatraHomePage.clickSearch();
        Assert.assertTrue(yatraSearchResultsPage.verifySearchResultDisplayed());

        System.out.println("Total Fare is :" + yatraSearchResultsPage.getTotalFare());
        Map<String, Double> fares = yatraSearchResultsPage.getFareForNextNDays(data.get("depart"),
                Integer.parseInt(data.get("daysToCheck")));
        List<Map.Entry<String, Double>> list = new ArrayList<>(fares.entrySet());
        list.sort(Map.Entry.comparingByValue());
        System.out.println("Lowest fare for the trip will be : " + list.get(0));
    }
}
