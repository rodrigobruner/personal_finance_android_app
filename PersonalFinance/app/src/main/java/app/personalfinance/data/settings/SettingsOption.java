package app.personalfinance.data.settings;

// A option in the settings menu
public class SettingsOption {
    // Title of the option
    private String title;
    // Description of the option
    private String description;
    // Icon of the option
    private int icon;
    // Menu id of the option
    private int menuId;

    // Constructor
    public SettingsOption(String title, String description, int icon, int menuId) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.menuId = menuId;
    }

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
}
