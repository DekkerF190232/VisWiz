package de.sirvierl0ffel.viswiz.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import de.sirvierl0ffel.viswiz.databinding.FragmentAlgorithmListBinding;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;
import de.sirvierl0ffel.viswiz.views.editing.EditingFragment;

public class AlgorithmListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    public AlgorithmListFragment() {
    }

    @SuppressWarnings("unused")
    public static AlgorithmListFragment newInstance(int columnCount) {
        AlgorithmListFragment fragment = new AlgorithmListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAlgorithmListBinding binding = FragmentAlgorithmListBinding.inflate(inflater, container, false);

        RecyclerView view = binding.algorithmList;
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            view.setLayoutManager(new LinearLayoutManager(context));
        } else {
            view.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        MainViewModel model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        AlgorithmListItemAdapter adapter;
        view.setAdapter(adapter = new AlgorithmListItemAdapter(requireActivity(), model, new ArrayList<>(model.managerAlgorithms.getData().values())));

        binding.buttonNewAlgorithm.setOnClickListener(v -> {
            model.selectedAlgorithm.setValue(null);
            model.open(EditingFragment.class);
        });

        model.algorithmUpdate.observe(getViewLifecycleOwner(), b -> {
            Objects.requireNonNull(view.getAdapter()).notifyDataSetChanged();
            adapter.algorithms.clear();
            adapter.algorithms.addAll(model.managerAlgorithms.getData().values());
        });

        return binding.getRoot();
    }
}