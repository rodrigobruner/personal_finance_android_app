package app.personalfinance.helpper;

// Currency formatter
public class CurrencyFormatter {
    // Format a double value to a currency string
    public static String format(double value) {
        return "$"+String.format("%.2f", value);
    }

    public static double convert(String value) {
        return Double.parseDouble(value.replace("$", ""));
    }
}
