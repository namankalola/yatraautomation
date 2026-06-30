package yatra.testcases;

import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

import manager.Const;
import utils.Excels;
import yatra.dataproviders.FlightSearchDataProvider;
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
        YatraHomePage yatraHomePage;
        YatraSearchResultsPage yatraSearchResultsPage;

        @Test(description = "FL005_Yatra - Search flight and get cheapest flight for given n days", dataProvider = "flightSearchData", dataProviderClass = FlightSearchDataProvider.class, groups = "Smoke")
        public void search_international_round_trip_flight(Map<String, String> data) throws InterruptedException {

                yatraHomePage = new YatraHomePage(driver);
                yatraHomePage.navigateTo(Const.YATRA_BASE_URL);
                yatraSearchResultsPage = new YatraSearchResultsPage(driver);
                yatraHomePage.setFromCity(data.get("from"));
                yatraHomePage.setToCity(data.get("to"));
                yatraHomePage.selectDepartureDate(data.get("depart"));
                yatraHomePage.selectReturnDate(data.get("return"));
                yatraHomePage.selectTravellersAndCabinClass(data.get("travellers"), data.get("cabinClass"));
                yatraHomePage.clickSearch();
                Assert.assertTrue(yatraSearchResultsPage.verifySearchResultDisplayed(),
                                "Search results not displayed for route: " + data.get("from") + " → " + data.get("to"));
                yatraSearchResultsPage.getTotalFare("International");
                yatraSearchResultsPage.lowestFare(yatraSearchResultsPage.getFareForNextNDays(data.get("depart"),
                                Integer.parseInt(data.get("daysToCheck")), "International"));

        }

        @Test(description = "DataDriven_Yatra - Search flight and get cheapest flight for given n days", dataProvider = "internationalFlightSearch", dataProviderClass = FlightSearchDataProvider.class, groups = "Smoke")
        public void search_international_round_trip_flight_datadriven(String testID) throws InterruptedException {

                String sheet = "Journey";
                String daysToCheck = Excels.getValue(sheet, testID, "daysToCheck");
                String from = Excels.getValue(sheet, testID, "From");
                String to = Excels.getValue(sheet, testID, "To");
                String departure = Excels.getValue(sheet, testID, "Departure");
                String returnDate = Excels.getValue(sheet, testID, "Return");
                String tripType = Excels.getValue(sheet, testID, "TripType");
                sheet = "Travellers";
                String travellers = Excels.getValue(sheet, testID, "Adults") + ","
                                + Excels.getValue(sheet, testID, "Children")
                                + "," + Excels.getValue(sheet, testID, "Infants");
                String cabinClass = Excels.getValue(sheet, testID, "cabinClass");

                yatraHomePage = new YatraHomePage(driver);
                yatraHomePage.navigateTo(Const.YATRA_BASE_URL);
                yatraSearchResultsPage = new YatraSearchResultsPage(driver);
                yatraHomePage.setFromCity(from);
                yatraHomePage.setToCity(to);
                yatraHomePage.selectDepartureDate(departure);
                yatraHomePage.selectReturnDate(returnDate);
                yatraHomePage.selectTravellersAndCabinClass(travellers, cabinClass);
                yatraHomePage.clickSearch();
                Assert.assertTrue(yatraSearchResultsPage.verifySearchResultDisplayed(),
                                "Search results not displayed for route: " + from + " → " + to);
                yatraSearchResultsPage.getTotalFare("International");
                yatraSearchResultsPage.lowestFare(yatraSearchResultsPage.getFareForNextNDays(departure,
                                Integer.parseInt(daysToCheck), tripType));

        }

        @Test(description = "FL001_Yatra - Search One WayFlight and get cheapest flight for given n days", groups = {
                        "Regression" })
        public void search_one_way_flight() throws InterruptedException {
                String testID = "FL001";
                String sheet = "Journey";
                String daysToCheck = Excels.getValue(sheet, testID, "daysToCheck");
                String from = Excels.getValue(sheet, testID, "From");
                String to = Excels.getValue(sheet, testID, "To");
                String departure = Excels.getValue(sheet, testID, "Departure");
                String tripType = Excels.getValue(sheet, testID, "TripType");
                sheet = "Travellers";
                String travellers = Excels.getValue(sheet, testID, "Adults") + ","
                                + Excels.getValue(sheet, testID, "Children")
                                + "," + Excels.getValue(sheet, testID, "Infants");
                String cabinClass = Excels.getValue(sheet, testID, "cabinClass");
                yatraHomePage = new YatraHomePage(driver);
                yatraHomePage.navigateTo(Const.YATRA_BASE_URL);
                yatraSearchResultsPage = new YatraSearchResultsPage(driver);
                yatraHomePage.setFromCity(from);
                yatraHomePage.setToCity(to);
                yatraHomePage.selectDepartureDate(departure);
                yatraHomePage.selectTravellersAndCabinClass(travellers, cabinClass);
                yatraHomePage.clickSearch();
                Assert.assertTrue(yatraSearchResultsPage.verifySearchResultDisplayed(),
                                "Search results not displayed for route: " + from + " → " + to);
                yatraSearchResultsPage.getTotalFare(tripType);
                yatraSearchResultsPage.lowestFare(yatraSearchResultsPage.getFareForNextNDays(departure,
                                Integer.parseInt(daysToCheck), tripType));

        }

        @Test(description = "FL002_Yatra - Search_Round_Trip_Flight and get cheapest flight for given n days", groups = {
                        "Regression" })
        public void search_round_trip_flight() throws InterruptedException {
                String testID = "FL002";
                String sheet = "Journey";
                String daysToCheck = Excels.getValue(sheet, testID, "daysToCheck");
                String from = Excels.getValue(sheet, testID, "From");
                String to = Excels.getValue(sheet, testID, "To");
                String departure = Excels.getValue(sheet, testID, "Departure");
                String tripType = Excels.getValue(sheet, testID, "TripType");
                sheet = "Travellers";
                String travellers = Excels.getValue(sheet, testID, "Adults") + ","
                                + Excels.getValue(sheet, testID, "Children")
                                + "," + Excels.getValue(sheet, testID, "Infants");
                yatraHomePage = new YatraHomePage(driver);
                yatraHomePage.navigateTo(Const.YATRA_BASE_URL);
                yatraSearchResultsPage = new YatraSearchResultsPage(driver);
                yatraHomePage.setFromCity(from);
                yatraHomePage.setToCity(to);
                yatraHomePage.selectDepartureDate(departure);
                yatraHomePage.selectTravellersAndCabinClass(travellers, Excels.getValue(sheet, testID, "cabinClass"));
                yatraHomePage.clickSearch();
                Assert.assertTrue(yatraSearchResultsPage.verifySearchResultDisplayed(),
                                "Search results not displayed for route: " + from + " → " + to);
                yatraSearchResultsPage.getTotalFare(tripType);
                yatraSearchResultsPage.lowestFare(yatraSearchResultsPage.getFareForNextNDays(departure,
                                Integer.parseInt(daysToCheck), tripType));
        }

        @Test(description = "FL003_Search_Multi_City_Domestic_Flight", groups = { "Release1.1" }, enabled = false)
        public void search_multi_city_domestic_flight() throws InterruptedException {
                String testID = "FL003";
                // String sheet = "Journey";
                // String from = Excels.getValue(sheet, testID, "From");
                // String to = Excels.getValue(sheet, testID, "To");
                // String departure = Excels.getValue(sheet, testID, "Departure");
                // String tripType = Excels.getValue(sheet, testID, "TripType");
                String sheet = "Travellers";
                String travellers = Excels.getValue(sheet, testID, "Adults") + ","
                                + Excels.getValue(sheet, testID, "Children")
                                + "," + Excels.getValue(sheet, testID, "Infants");

                yatraHomePage = new YatraHomePage(driver);
                yatraHomePage.navigateTo(Const.YATRA_BASE_URL);
                yatraSearchResultsPage = new YatraSearchResultsPage(driver);

                List<Map<String, String>> testData = Excels.getRowsForTest("Journey", testID);
                if (testData.size() > 1) {
                        // yatraHomePage.clickTripTypeRadio("Multi City");
                }
                for (Map<String, String> map : testData) {
                        int leg = Integer.parseInt(map.get("Leg")) - 1;
                        yatraHomePage.setMultiCityFrom(map.get("From"), leg);
                        yatraHomePage.setMultiCityTo(map.get("To"), leg);
                        yatraHomePage.selectMultiCityDepartureDate(map.get("Departure"), leg);
                }

                yatraHomePage.selectTravellersAndCabinClass(travellers,
                                Excels.getValue(sheet, testID, "cabinClass"));
                yatraHomePage.clickSearch();
                Assert.assertTrue(yatraSearchResultsPage.verifyResultDisplayedForMultiCitySearch(),
                "Search results not displayed for selected route");
                // yatraSearchResultsPage.getTotalFare(tripType);

        }
}
