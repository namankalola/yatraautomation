package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {

    public LocalDate parseDate(String date) {

        List<DateTimeFormatter> formats = List.of(
                DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("EEEE MMM dd yyyy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH));

        for (DateTimeFormatter formatter : formats) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (Exception ignored) {
            }
        }

        throw new RuntimeException("Unsupported Date Format : " + date);
    }

    public LocalDate getFormattedDate(String date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM YYYY", Locale.ENGLISH);
        return LocalDate.parse(date, format);
    }

    public String getMonth(LocalDate date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
        return date.format(format);
    }

    public String getMonthMMM(LocalDate date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);
        return date.format(format);
    }

    public String getDay(LocalDate date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("DD", Locale.ENGLISH);
        return date.format(format);
    }

    public String getYear(LocalDate date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("YYYY", Locale.ENGLISH);
        return date.format(format);
    }

    // This is picked up from chatGPT
    public String resolveDate(String input) {
        LocalDate date = null;
        if (input.contains("today")) {
            date = LocalDate.now();
        } else {
            date = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        if ("today".equalsIgnoreCase(input)) {
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        Pattern pattern = Pattern.compile("today([+])(\\d+)?(month|year)?",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {

            String operator = matcher.group(1);
            String numberStr = matcher.group(2);
            String unit = matcher.group(3);

            int value = numberStr == null ? 1 : Integer.parseInt(numberStr);

            if ("+".equals(operator)) {

                if ("month".equalsIgnoreCase(unit))
                    date = date.plusMonths(value);
                else if ("year".equalsIgnoreCase(unit))
                    date = date.plusYears(value);
                else
                    date = date.plusDays(value);

            }
        }

        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String addDaysToDate(String date, int days) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.plusDays(days).format(formatter);
    }
}
