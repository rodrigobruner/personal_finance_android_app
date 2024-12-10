package app.personalfinance.helpper;

public class CurrencyFormatter {
    public static String format(double value) {
        return "$"+String.format("%.2f", value);
    }
}
