package de.sirvierl0ffel.viswiz.views;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.sirvierl0ffel.viswiz.databinding.FragmentAlgorithmListItemBinding;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.util.GsonRequest;
import de.sirvierl0ffel.viswiz.viewmodels.AlgorithmViewModel;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;
import de.sirvierl0ffel.viswiz.views.editing.EditingFragment;
import de.sirvierl0ffel.viswiz.views.running.RunningFragment;

public class AlgorithmListItemAdapter extends RecyclerView.Adapter<AlgorithmListItemAdapter.ViewHolder> {

    private final ViewModelStoreOwner activity;
    public final List<Algorithm> algorithms;
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

            binding.btnAlgorithmDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(binding.getRoot().getContext())
                        .setTitle("Delete Algorithm")
                        .setMessage("Are you sure you want to delete this algorithm?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            RequestQueue volley = Volley.newRequestQueue((AppCompatActivity) activity);
                            volley.add(new GsonRequest<>(Request.Method.POST, "http://10.0.2.2:8080/algorithm/new",
                                    new Gson().toJson(algorithm),
                                    new TypeToken<Long>() {
                                    },
                                    Collections.emptyMap(),
                                    success -> {
                                        // TODO: Test delete request to server
                                        int position = algorithms.indexOf(algorithm);
                                        algorithms.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, algorithms.size());
                                        model.managerAlgorithms.getData().remove(algorithm.id);
                                    },
                                    error -> {
                                        onDeleteError();
                                        System.err.println("Error requesting algorithms! :(");
                                        error.printStackTrace();
                                    }));
                        })
                        .setNegativeButton("No", null)
                        .show();
            });

            binding.btnAlgorithmFork.setOnClickListener(v -> {
                model.selectedAlgorithm.setValue(algorithm);
                model.open(EditingFragment.class);
            });
        }

        private void onDeleteError() {
            int position = algorithms.indexOf(algorithm);
            algorithms.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, algorithms.size());
            model.managerAlgorithms.getData().remove(algorithm.id);
        }

    }
}