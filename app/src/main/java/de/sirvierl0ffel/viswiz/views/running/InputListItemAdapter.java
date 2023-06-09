package de.sirvierl0ffel.viswiz.views.running;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.sirvierl0ffel.viswiz.databinding.FragmentRunningInputListItemBinding;
import de.sirvierl0ffel.viswiz.models.InputSave;
import de.sirvierl0ffel.viswiz.viewmodels.AlgorithmViewModel;

public class InputListItemAdapter extends RecyclerView.Adapter<InputListItemAdapter.ViewHolder> {

    private final List<InputSave> saves;
    private final AppCompatActivity activity;

    public InputListItemAdapter(List<InputSave> saves, AppCompatActivity activity) {
        this.saves = saves;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentRunningInputListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.save = saves.get(position);

        holder.binding.textInputDisplay.setText(holder.save.input);
        holder.binding.btnInputRun.setOnClickListener(v -> {
            AlgorithmViewModel algorithmModel = new ViewModelProvider(activity).get(AlgorithmViewModel.class);
            algorithmModel.selectedInput.setValue(holder.save);
            algorithmModel.selectTab(ResultFragment.class);
        });
        if (position != 0) {
            holder.binding.btnInputDelete.setVisibility(View.VISIBLE);
            holder.binding.btnInputDelete.setOnClickListener(v -> {
                // 😔
                if (position >= saves.size()) return;
                saves.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, saves.size());
            });
        } else {
            holder.binding.btnInputDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return saves.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public FragmentRunningInputListItemBinding binding;
        public InputSave save;

        public ViewHolder(FragmentRunningInputListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}