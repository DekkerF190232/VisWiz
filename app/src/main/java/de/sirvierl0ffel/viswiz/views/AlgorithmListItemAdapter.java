package de.sirvierl0ffel.viswiz.views;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.sirvierl0ffel.viswiz.databinding.FragmentAlgorithmListItemBinding;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.viewmodels.AlgorithmViewModel;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;
import de.sirvierl0ffel.viswiz.views.running.RunningFragment;

public class AlgorithmListItemAdapter extends RecyclerView.Adapter<AlgorithmListItemAdapter.ViewHolder> {

    private final ViewModelStoreOwner activity;
    private final List<Algorithm> algorithms;
    private final MainViewModel model;

    public AlgorithmListItemAdapter(ViewModelStoreOwner activity, MainViewModel model, List<Algorithm> items) {
        this.activity = activity;
        this.model = model;
        algorithms = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentAlgorithmListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.algorithm = algorithms.get(position);
        holder.binding.algorithmItemTitle.setText(holder.algorithm.name);

        Picasso.get().load(holder.algorithm.imageLocation)
                //.error(R.drawable.error)
                //.placeholder(R.drawable.placeholder)
                .into(holder.binding.algorithmItemBanner);
    }

    @Override
    public int getItemCount() {
        return algorithms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Algorithm algorithm;
        public FragmentAlgorithmListItemBinding binding;

        public ViewHolder(FragmentAlgorithmListItemBinding binding) {
            super(binding.getRoot());
            binding.algorithmItemBanner.setClipToOutline(true);
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if (algorithm == null) return;
                AlgorithmViewModel algorithmModel = new ViewModelProvider(activity).get(AlgorithmViewModel.class);
                algorithmModel.selectedInput.setValue(algorithm.defaultInput);
                model.selectedAlgorithm.setValue(algorithm);
                model.open(RunningFragment.class);
            });
        }

    }
}