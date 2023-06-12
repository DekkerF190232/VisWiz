package de.sirvierl0ffel.viswiz.views.editing;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.function.Consumer;

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

        registerOnTextChange(binding.txtEditName, string -> em.name = string);
        registerOnTextChange(binding.txtEditImgUrl, string -> em.thumbnailURL = string);
        registerOnTextChange(binding.txtEditDescription, string -> em.description = string);

        return binding.getRoot();
    }

    private void registerOnTextChange(EditText editText, Consumer<String> action) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                action.accept(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        action.accept(editText.getText().toString());
    }

}