package de.sirvierl0ffel.viswiz.views;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import de.sirvierl0ffel.viswiz.databinding.FragmentAlgorithmListItemBinding;
import de.sirvierl0ffel.viswiz.views.running.RunningFragment;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;

import java.util.List;

public class AlgorithmListItemAdapter extends RecyclerView.Adapter<AlgorithmListItemAdapter.ViewHolder> {

    private final List<Algorithm> algorithms;
    private final MainViewModel model;

    public AlgorithmListItemAdapter(MainViewModel model, List<Algorithm> items) {
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
                model.selectedAlgorithm.setValue(algorithm);
                model.open(RunningFragment.class);
            });
        }

    }
}