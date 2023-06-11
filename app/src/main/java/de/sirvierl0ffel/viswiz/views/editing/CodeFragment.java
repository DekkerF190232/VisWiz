package de.sirvierl0ffel.viswiz.views.editing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import de.sirvierl0ffel.viswiz.databinding.FragmentEditingCodeBinding;

public class CodeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentEditingCodeBinding binding = FragmentEditingCodeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}