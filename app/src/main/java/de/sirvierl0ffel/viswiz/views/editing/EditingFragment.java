package de.sirvierl0ffel.viswiz.views.editing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import de.sirvierl0ffel.viswiz.databinding.FragmentEditingBinding;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.models.InputSave;
import de.sirvierl0ffel.viswiz.util.FileUtil;
import de.sirvierl0ffel.viswiz.util.GsonRequest;
import de.sirvierl0ffel.viswiz.viewmodels.AlgorithmViewModel;
import de.sirvierl0ffel.viswiz.viewmodels.EditViewModel;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;
import de.sirvierl0ffel.viswiz.views.running.DescriptionFragment;
import de.sirvierl0ffel.viswiz.views.running.RunningFragment;

public class EditingFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentEditingBinding binding = FragmentEditingBinding.inflate(inflater, container, false);

        EditViewModel editModel = new ViewModelProvider(requireActivity()).get(EditViewModel.class);

        binding.pagerEdit.setAdapter(new FragmentStateAdapter(requireActivity()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                try {
                    return (Fragment) EditViewModel.TAB_FRAGMENT_TYPES[position].newInstance();
                } catch (Exception e) {
                    return new DescriptionFragment();
                }
            }

            @Override
            public int getItemCount() {
                return EditViewModel.TAB_FRAGMENT_TYPES.length;
            }
        });
        binding.pagerEdit.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(binding.tabsEdit.getTabAt(position)).select();
            }
        });
        binding.pagerEdit.setOffscreenPageLimit(EditViewModel.TAB_FRAGMENT_TYPES.length);

        binding.tabsEdit.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.pagerEdit.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        editModel.tab.observe(getViewLifecycleOwner(), tabIdx ->
        {
            TabLayout.Tab tab = Objects.requireNonNull(binding.tabsEdit.getTabAt(tabIdx));
            tab.select();
        });

        //noinspection unchecked
        editModel.selectTab((Class<? extends Fragment>) EditViewModel.TAB_FRAGMENT_TYPES[0]);

        binding.buttonEditSave.setOnClickListener(v -> {
            Algorithm algorithm = buildAlgorithm();
            if (algorithm == null) return;
            RequestQueue volley = Volley.newRequestQueue(requireContext());
            volley.add(new GsonRequest<>(Request.Method.POST, "http://10.0.2.2:8080/algorithm/new",
                    new Gson().toJson(algorithm),
                    new TypeToken<Long>() {
                    },
                    Collections.emptyMap(),
                    success -> {
                        // TODO: Test post method
                        MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);
                        algorithm.id = success;
                        model.managerAlgorithms.getData().put(algorithm.id, algorithm);
                        model.algorithmUpdate.setValue(Boolean.FALSE.equals(model.algorithmUpdate.getValue()));
                        requireActivity().getSupportFragmentManager().popBackStack();
                    },
                    error -> {
                        onRequestError();
                        System.err.println("Error requesting algorithms! :(");
                        error.printStackTrace();
                    }));
        });

        binding.buttonEditTest.setOnClickListener(v -> {
            Algorithm algorithm = buildAlgorithm();
            if (algorithm == null) return;
            MainViewModel m = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
            AlgorithmViewModel am = new ViewModelProvider(requireActivity()).get(AlgorithmViewModel.class);
            m.selectedAlgorithm.setValue(algorithm);
            am.selectedInput.setValue(algorithm.defaultInput);
            m.open(RunningFragment.class);
        });

        return binding.getRoot();
    }

    private void onRequestError() {
        MainViewModel model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        Algorithm algorithm = buildAlgorithm();
        if (algorithm == null) return;
        algorithm.id = model.managerAlgorithms.getData().size() + new Random().nextInt(1_000_000);
        model.managerAlgorithms.getData().put(algorithm.id, algorithm);
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private Algorithm buildAlgorithm() {
        EditViewModel em = new ViewModelProvider(requireActivity()).get(EditViewModel.class);
        em.name = em.name.trim();
        if (em.name.isEmpty()) {
            Toast.makeText(requireActivity(), "Name cannot be empty!", Toast.LENGTH_LONG)
                    .show();
            return null;
        }

        em.description = em.description.trim();
        if (em.description.isEmpty()) {
            Toast.makeText(requireActivity(), "Description cannot be empty!", Toast.LENGTH_LONG)
                    .show();
            return null;
        }

        em.thumbnailURL = em.thumbnailURL.trim();
        if (!em.thumbnailURL.contains(".")) {
            Toast.makeText(requireActivity(), "Thumbnail URL is invalid!", Toast.LENGTH_LONG)
                    .show();
            return null;
        }

        em.pseudoCode = em.pseudoCode.trim();
        if (em.pseudoCode.isEmpty()) {
            Toast.makeText(requireActivity(), "Pseudo code cannot be empty!", Toast.LENGTH_LONG)
                    .show();
            return null;
        }

        em.code = em.code.trim();
        if (em.code.isEmpty()) {
            Toast.makeText(requireActivity(), "JavaScript code cannot be empty!", Toast.LENGTH_LONG)
                    .show();
            return null;
        }

        em.input = em.input.trim();
        if (em.input.isEmpty()) {
            Toast.makeText(requireActivity(), "Example input cannot be empty!", Toast.LENGTH_LONG)
                    .show();
            return null;
        }

        return new Algorithm(-1, em.name, em.description, em.thumbnailURL, em.code, em.pseudoCode.split(System.lineSeparator()), new InputSave(em.input));
    }

}