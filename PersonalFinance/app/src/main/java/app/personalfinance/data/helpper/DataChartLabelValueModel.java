package app.personalfinance.data.helpper;

// This class is used to stor the label and value of the data that will be displayes in the chart.
// The label is the name of the data and the value is the value of the data.
public class DataChartLabelValueModel {
    // The name of the data.
    private String label;
    // The value of the data.
    private float value;

    // Constructor
    public DataChartLabelValueModel(String label, float value) {
        this.label = label;
        this.value = value;
    }

    // Getter method for the label.
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
