package app.personalfinance.data.helpper;

public class DataChartLabelValue {
    private String label;
    private float value;

    public DataChartLabelValue(String label, float value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public float getValue() {
        return value;
    }
}
