package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excels {
    private static final Map<String, List<Map<String, String>>> workbookData = new ConcurrentHashMap<>();

    public static void loadWorkbook(String path) throws IOException {
        FileInputStream fis = null;
        Workbook workbook = null;
        DataFormatter formatter = new DataFormatter();
        try {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);

            for (Sheet sheet : workbook) {
                List<Map<String, String>> rows = new ArrayList<>();
                Row headerRow = sheet.getRow(0);
                List<String> headers = new ArrayList<>();
                for (Cell cell : headerRow) {
                    headers.add(formatter.formatCellValue(cell).trim());
                }

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    Map<String, String> rowData = new LinkedHashMap<>();
                    for (int j = 0; j < headers.size(); j++) {
                        Cell cell = row.getCell(j);
                        rowData.put(headers.get(j), formatter.formatCellValue(cell).trim());
                    }
                    rows.add(rowData);
                }
                workbookData.put(sheet.getSheetName(), rows);

            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to load Excel : " + path, e);
        } finally {
            workbook.close();
            fis.close();
        }
    }

    public static List<Map<String, String>> getSheetData(String sheet) {
        return workbookData.getOrDefault(sheet, Collections.emptyList());
    }

    public static List<Map<String, String>> getRowsForTest(String sheet, String testID) {
        List<Map<String, String>> testData = new ArrayList<>();
        for (Map<String, String> row : getSheetData(sheet)) {
            if (row.get("TC_ID").equalsIgnoreCase(testID))
                testData.add(row);
        }
        return testData;
    }

    public static Map<String, String> getRow(String sheetName, String testID) {

        return getSheetData(sheetName)
                .stream()
                .filter(row -> testID.equalsIgnoreCase(row.get("TC_ID")))
                .findFirst()
                .orElse(Collections.emptyMap());

    }

    public static String getValue(String sheet, String testID, String columnName) {
        return getRow(sheet, testID).get(columnName);
    }

    public static boolean isWorkbookLoaded(){
        return !workbookData.isEmpty();
    }

    public static void clearWorkbookCache() {

        workbookData.clear();

    }
    public static void main(String args[]) {
        try {
            loadWorkbook("src\\test\\java\\yatra\\testdata\\DemoTestData.xlsx");
            // System.out.println(getSheetData("Flights").toString());
            // System.out.println(getValue("Flights", "TC001", "From"));

            System.out.println(getRowsForTest("Journey", "FL003"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
