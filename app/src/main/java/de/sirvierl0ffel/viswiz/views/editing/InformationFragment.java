package de.sirvierl0ffel.viswiz.views.editing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.sirvierl0ffel.viswiz.databinding.FragmentEditingInformationBinding;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.viewmodels.EditViewModel;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;

public class InformationFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentEditingInformationBinding binding = FragmentEditingInformationBinding.inflate(inflater, container, false);

        MainViewModel model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        Algorithm algorithm = model.selectedAlgorithm.getValue();
        if (algorithm != null) {
            binding.txtEditName.setText(algorithm.name);
            binding.txtEditImgUrl.setText(algorithm.imageLocation);
            binding.txtEditDescription.setText(algorithm.description);
        }

        EditViewModel em = new ViewModelProvider(requireActivity()).get(EditViewModel.class);

        binding.txtEditName.setOnEditorActionListener((v, action, evt) -> {
            em.name = binding.txtEditName.getText().toString();
            return false;
        });
        em.name = binding.txtEditName.getText().toString();

        binding.txtEditImgUrl.setOnEditorActionListener((v, action, evt) -> {
            em.thumbnailURL = binding.txtEditImgUrl.getText().toString();
            return false;
        });
        em.thumbnailURL = binding.txtEditImgUrl.getText().toString();

        binding.txtEditDescription.setOnEditorActionListener((v, action, evt) -> {
            em.description = binding.txtEditDescription.getText().toString();
            return false;
        });
        em.description = binding.txtEditDescription.getText().toString();

        return binding.getRoot();
    }

}