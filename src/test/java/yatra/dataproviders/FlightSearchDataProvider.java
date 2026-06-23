package yatra.dataproviders;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.DataProvider;

public class FlightSearchDataProvider {

    @DataProvider(name = "flightSearchData")
    public Object[][] flightSearchData() {

        Map<String, String> test1 = new HashMap<>();
        test1.put("from", "Boston");
        test1.put("to", "Amsterdam");
        test1.put("depart", "today+1");
        test1.put("return", "today+9");
        test1.put("travellers", "1,1,1");
        test1.put("cabinClass", "Economy");
        test1.put("daysToCheck", "5");

        Map<String, String> test2 = new HashMap<>();
        test2.put("from", "Boston");
        test2.put("to", "Amsterdam");
        test2.put("depart", "today+10");
        test2.put("return", "today+month");
        test2.put("travellers", "2,2,1");
        test2.put("cabinClass", "Premium Economy");
        test2.put("daysToCheck", "6");

        return new Object[][] {
                { test1 },
                { test2 }
        };
    }
}
