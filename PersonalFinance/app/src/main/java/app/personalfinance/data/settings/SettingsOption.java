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

    public SettingsOption(String title, String description, int icon, int menuId) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.menuId = menuId;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getIcon() {
        return icon;
    }

    public int getMenuId() {
        return menuId;
    }
}
