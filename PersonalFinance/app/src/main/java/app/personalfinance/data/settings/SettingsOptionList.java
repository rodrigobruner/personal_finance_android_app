package app.personalfinance.data.settings;

import android.content.Context;
import java.util.ArrayList;
import app.personalfinance.R;

public class SettingsOptionList {

    // Get settings options to generate the settings listView
    public static ArrayList<SettingsOption> getSettingsOptions(Context context) {
        ArrayList<SettingsOption> settingsOptions = new ArrayList<>();
        // Add accounts option
        settingsOptions.add(
                new SettingsOption(
                        context.getString(R.string.menu_accounts),
                        context.getString(R.string.description_account),
                        R.drawable.icon_accounts,
                        R.id.nav_accounts));
        // Add categories option
        settingsOptions.add(
                new SettingsOption(
                        context.getString(R.string.menu_categories),
                        context.getString(R.string.description_categories),
                        R.drawable.icon_categories,
                        R.id.nav_categories));

        return settingsOptions;
    }
}