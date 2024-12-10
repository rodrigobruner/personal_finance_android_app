package app.personalfinance.ui.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.personalfinance.R;
import app.personalfinance.data.settings.SettingsOption;

public class SettingsOptionAdapter extends ArrayAdapter<SettingsOption> {
    public SettingsOptionAdapter(Context context, ArrayList<SettingsOption> settingsOptions) {
        super(context, 0, settingsOptions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_settings, parent, false);
        }

        SettingsOption option = getItem(position);

        TextView title = convertView.findViewById(R.id.textViewTitle);
        TextView description = convertView.findViewById(R.id.textViewDescription);
        ImageView icon = convertView.findViewById(R.id.imageView2);

        title.setText(option.getTitle());
        description.setText(option.getDescription());
        icon.setImageResource(option.getIcon());

        return convertView;
    }
}