package de.sirvierl0ffel.viswiz.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import de.sirvierl0ffel.viswiz.R;
import de.sirvierl0ffel.viswiz.databinding.ActivityMainBinding;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.util.FileUtil;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        load();

        MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);
        model._currentFragment.observe(this, clazz -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, clazz, null)
                .addToBackStack(null)
                .commit());

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, AlgorithmListFragment.newInstance(1))
                .commit();
        setContentView(binding.getRoot());
    }

    private void load() {
        MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);
        String data = FileUtil.readFromFile(this, "inputs.json", "{}");
        model.managerInputs.loadJSON(data, Algorithm::dummyGetAlgorithm);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);
        String data = model.managerInputs.saveJSON();
        FileUtil.writeToFile(this, "inputs.json", data);
    }
}