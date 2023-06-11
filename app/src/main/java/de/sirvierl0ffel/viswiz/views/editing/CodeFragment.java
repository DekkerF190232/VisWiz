package de.sirvierl0ffel.viswiz.views.editing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.stream.Collectors;

import de.sirvierl0ffel.viswiz.databinding.FragmentEditingCodeBinding;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.viewmodels.EditViewModel;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;

public class CodeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentEditingCodeBinding binding = FragmentEditingCodeBinding.inflate(inflater, container, false);


        MainViewModel model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        Algorithm algorithm = model.selectedAlgorithm.getValue();
        if (algorithm != null) {
            binding.txtEditPseudoCode.setText(Arrays.stream(algorithm.pseudoCode).collect(Collectors.joining(System.lineSeparator())));
            binding.txtEditCode.setText(algorithm.code);
            binding.txtEditInputExample.setText(algorithm.defaultInput.input);
        }

        EditViewModel em = new ViewModelProvider(requireActivity()).get(EditViewModel.class);

        binding.txtEditPseudoCode.setOnEditorActionListener((v, action, evt) -> {
            em.pseudoCode = binding.txtEditPseudoCode.getText().toString();
            return false;
        });
        em.pseudoCode = binding.txtEditPseudoCode.getText().toString();

        binding.txtEditCode.setOnEditorActionListener((v, action, evt) -> {
            em.code = binding.txtEditCode.getText().toString();
            return false;
        });
        em.code = binding.txtEditCode.getText().toString();

        binding.txtEditInputExample.setOnEditorActionListener((v, action, evt) -> {
            em.input = binding.txtEditInputExample.getText().toString();
            return false;
        });
        em.input = binding.txtEditInputExample.getText().toString();

        return binding.getRoot();
    }
}