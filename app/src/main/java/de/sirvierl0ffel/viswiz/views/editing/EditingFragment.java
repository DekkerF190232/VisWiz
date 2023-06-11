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
import de.sirvierl0ffel.viswiz.viewmodels.EditViewModel;
import de.sirvierl0ffel.viswiz.views.running.DescriptionFragment;

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

        return binding.getRoot();
    }
}