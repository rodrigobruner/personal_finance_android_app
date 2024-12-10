package app.personalfinance.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;

import app.personalfinance.data.settings.SettingsOption;
import app.personalfinance.data.settings.SettingsOptionList;
import app.personalfinance.databinding.FragmentSettingsBinding;
import app.personalfinance.viewModel.settings.SettingsViewModel;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

    ArrayList<SettingsOption> settingsOptions = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        settingsOptions = SettingsOptionList.getSettingsOptions(getContext());

        ListView listView = binding.listViewSettings;
        SettingsOptionAdapter adapter = new SettingsOptionAdapter(getContext(), settingsOptions);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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