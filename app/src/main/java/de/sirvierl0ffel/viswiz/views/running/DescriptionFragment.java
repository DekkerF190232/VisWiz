package de.sirvierl0ffel.viswiz.views.running;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import de.sirvierl0ffel.viswiz.databinding.FragmentRunningDescriptionBinding;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.util.TextViewUtil;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;

public class DescriptionFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRunningDescriptionBinding binding = FragmentRunningDescriptionBinding.inflate(inflater, container, false);

        MainViewModel model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        Algorithm algorithm = model.selectedAlgorithm.getValue();

        if (algorithm == null) throw new IllegalStateException();

        Picasso.get().load(algorithm.imageLocation)
                //.error(R.drawable.error)
                //.placeholder(R.drawable.placeholder)
                .into(binding.mainImage);

        binding.txtMainDescription.setText(TextViewUtil.makeSpannableString(algorithm.description, true));

        return binding.getRoot();
    }
}