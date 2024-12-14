package app.personalfinance.helpper;

// Currency formatter
public class CurrencyFormatter {
    // Format a double value to a currency string
    public static String format(double value) {
        return "$"+String.format("%.2f", value);
    }

    // Convert a currency string to a double value
    public static double convert(String value) {
        return Double.parseDouble(value.replace("$", ""));
    }

    // Format a string to a currency string
    public static String formatString(String text) {
        if (text.isEmpty()) {
            return "$";
        }
        try {
            double value = Double.parseDouble(text.replace("$", ""));
            return String.format("$%.2f", value);
        } catch (NumberFormatException e) {
            return text;
        }
    }
}
