package de.sirvierl0ffel.viswiz.views.editing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import de.sirvierl0ffel.viswiz.databinding.FragmentEditingBinding;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.viewmodels.AlgorithmViewModel;
import de.sirvierl0ffel.viswiz.viewmodels.EditViewModel;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;
import de.sirvierl0ffel.viswiz.views.running.DescriptionFragment;

public class EditingFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentEditingBinding binding = FragmentEditingBinding.inflate(inflater, container, false);

        MainViewModel model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        EditViewModel algorithmModel = new ViewModelProvider(requireActivity()).get(EditViewModel.class);
        Algorithm algorithm = model.selectedAlgorithm.getValue();
        if (algorithm == null) throw new IllegalStateException();

        binding.pagerEdit.setAdapter(new FragmentStateAdapter(requireActivity()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                try {
                    return (Fragment) AlgorithmViewModel.TAB_FRAGMENT_TYPES[position].newInstance();
                } catch (Exception e) {
                    return new DescriptionFragment();
                }
            }

            @Override
            public int getItemCount() {
                return AlgorithmViewModel.TAB_FRAGMENT_TYPES.length;
            }
        });
        binding.pagerEdit.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(binding.tabsEdit.getTabAt(position)).select();
            }
        });
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

        algorithmModel.tab.observe(getViewLifecycleOwner(), tabIdx ->
        {
            TabLayout.Tab tab = Objects.requireNonNull(binding.tabsEdit.getTabAt(tabIdx));
            tab.select();
        });

        //noinspection unchecked
        algorithmModel.selectTab((Class<? extends Fragment>) EditViewModel.TAB_FRAGMENT_TYPES[0]);

        return binding.getRoot();
    }
}