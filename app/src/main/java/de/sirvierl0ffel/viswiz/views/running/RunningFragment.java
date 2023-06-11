package de.sirvierl0ffel.viswiz.views.running;

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

import de.sirvierl0ffel.viswiz.databinding.FragmentRunningBinding;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.viewmodels.AlgorithmViewModel;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;

public class RunningFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainViewModel model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        AlgorithmViewModel algorithmModel = new ViewModelProvider(requireActivity()).get(AlgorithmViewModel.class);
        Algorithm algorithm = model.selectedAlgorithm.getValue();
        if (algorithm == null) throw new IllegalStateException();

        FragmentRunningBinding binding = FragmentRunningBinding.inflate(inflater, container, false);

        binding.textRunningTesting.setVisibility(algorithm.id == -1 ? View.VISIBLE : View.GONE);

        binding.textAlgorithmName.setText(algorithm.name);
        binding.viewPager.setAdapter(new FragmentStateAdapter(requireActivity()) {
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
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(binding.tabLayout.getTabAt(position)).select();
            }
        });
        binding.viewPager.setOffscreenPageLimit(AlgorithmViewModel.TAB_FRAGMENT_TYPES.length);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
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
            TabLayout.Tab tab = Objects.requireNonNull(binding.tabLayout.getTabAt(tabIdx));
            tab.select();
        });

        algorithmModel.selectTab(DescriptionFragment.class);

        return binding.getRoot();
    }

}