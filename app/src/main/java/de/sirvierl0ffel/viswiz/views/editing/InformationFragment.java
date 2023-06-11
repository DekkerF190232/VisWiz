package de.sirvierl0ffel.viswiz.views.editing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.sirvierl0ffel.viswiz.databinding.FragmentEditingInformationBinding;

public class InformationFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentEditingInformationBinding binding = FragmentEditingInformationBinding.inflate(inflater, container, false);



        return binding.getRoot();
    }

}