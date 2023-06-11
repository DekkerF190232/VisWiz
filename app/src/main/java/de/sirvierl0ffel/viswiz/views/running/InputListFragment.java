package de.sirvierl0ffel.viswiz.views.running;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.sirvierl0ffel.viswiz.databinding.FragmentRunningInputListBinding;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.models.InputSave;
import de.sirvierl0ffel.viswiz.viewmodels.AlgorithmViewModel;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;

public class InputListFragment extends Fragment {

    public InputListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AlgorithmViewModel algorithmModel = new ViewModelProvider(requireActivity()).get(AlgorithmViewModel.class);

        FragmentRunningInputListBinding binding = FragmentRunningInputListBinding.inflate(inflater, container, false);

        MainViewModel model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        Algorithm algorithm = model.selectedAlgorithm.getValue();

        if (algorithm == null) throw new IllegalStateException();

        List<InputSave> inputList = algorithm.id == -1 ?
                new ArrayList<InputSave>() {{
                    add(algorithm.defaultInput);
                }} : model.managerInputs.getInputs(algorithm);

        binding.list.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.list.setAdapter(new InputListItemAdapter(inputList, (AppCompatActivity) requireActivity()));

        binding.btnRun.setOnClickListener(v -> {
            String input = binding.textAlgorithmInput.getText().toString();

            algorithmModel.selectedInput.setValue(new InputSave(input));
            algorithmModel.selectTab(ResultFragment.class);
        });

        binding.btnSave.setOnClickListener(v -> {
            String input = binding.textAlgorithmInput.getText().toString();

            inputList.add(new InputSave(input));
            Objects.requireNonNull(binding.list.getAdapter())
                    .notifyItemInserted(inputList.size() - 1);
            binding.list.scrollToPosition(inputList.size() - 1);
        });

        return binding.getRoot();
    }
}