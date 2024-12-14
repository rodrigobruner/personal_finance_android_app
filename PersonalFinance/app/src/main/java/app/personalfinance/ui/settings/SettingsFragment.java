package app.personalfinance.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import java.util.ArrayList;
import app.personalfinance.data.settings.SettingsOption;
import app.personalfinance.data.settings.SettingsOptionList;
import app.personalfinance.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment {
    // private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    // list of settings options
    ArrayList<SettingsOption> settingsOptions = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // get list of settings options
        settingsOptions = SettingsOptionList.getSettingsOptions(getContext());

        // set up list view
        ListView listView = binding.listViewSettings;
        // create adapter
        SettingsOptionAdapter adapter = new SettingsOptionAdapter(getContext(), settingsOptions);
        // set adapter
        listView.setAdapter(adapter);
        // set up on click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // navigate to the selected settings option
                NavController navController = Navigation.findNavController(view);
                SettingsOption option = settingsOptions.get(position);
                navController.navigate(option.getMenuId());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}