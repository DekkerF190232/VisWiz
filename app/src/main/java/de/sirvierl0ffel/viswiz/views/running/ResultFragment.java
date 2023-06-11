package de.sirvierl0ffel.viswiz.views.running;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import de.sirvierl0ffel.viswiz.algorithm.Evaluater;
import de.sirvierl0ffel.viswiz.algorithm.Result;
import de.sirvierl0ffel.viswiz.algorithm.ResultStep;
import de.sirvierl0ffel.viswiz.databinding.FragmentRunningResultsBinding;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.models.InputSave;
import de.sirvierl0ffel.viswiz.util.TextViewUtil;
import de.sirvierl0ffel.viswiz.viewmodels.AlgorithmViewModel;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;

public class ResultFragment extends Fragment {

    private Algorithm algorithm;
    private Result result;
    private int stepIndex;
    private FragmentRunningResultsBinding binding;

    private Timer timer;
    private long lastDownMs;
    private long nextDownMs;
    private boolean isLastDown;
    private boolean isNextDown;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainViewModel model = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        algorithm = model.selectedAlgorithm.getValue();

        AlgorithmViewModel algorithmModel = new ViewModelProvider(requireActivity()).get(AlgorithmViewModel.class);

        binding = FragmentRunningResultsBinding.inflate(inflater);

        initPseudoCode();

        algorithmModel.selectedInput.observe(getViewLifecycleOwner(), this::runAlgorithm);

        initNavigationButtons();

        if (algorithmModel.selectedInput.getValue() == null) {
            algorithmModel.selectedInput.setValue(algorithm.defaultInput);
        }

        return binding.getRoot();
    }

    private void runAlgorithm(InputSave inputSave) {
        binding.layoutDone.setVisibility(View.GONE);

        // Reinitialize views
        binding.layoutLoading.setVisibility(View.VISIBLE);
        binding.textRunningInput.setText(inputSave.input);
        binding.progressBar.setProgress(0);
        binding.textLoading.setTextColor(0xff000000);

        Evaluater.instance.evaluateAsync(algorithm, inputSave,
                new Handler(requireActivity().getMainLooper())::post,
                this::setProgress,
                this::setResult,
                this::setError,
                () -> {
                });
    }

    public void setResult(Result result) {
        binding.layoutDone.setVisibility(View.VISIBLE);
        binding.layoutLoading.setVisibility(View.GONE);

        if (this.result != null && stepIndex < this.result.stepList.size()) {
            View v = binding.layoutCode.getChildAt(this.result.stepList.get(stepIndex).pseudoCodeIndex);
            if (v != null) v.setBackground(null);
        }
        stepIndex = 0;

        if (result == null) {
            binding.textLineTotal.setText("-");
            isLastDown = false;
            binding.buttonStepLast.setEnabled(false);
            isNextDown = false;
            binding.buttonStepNext.setEnabled(false);
            binding.textLineCurrent.setText("-");
            binding.layoutVariables.removeAllViews();
            return;
        }

        this.result = result;

        binding.textLineTotal.setText(String.valueOf(result.stepList.size()));

        step(0);
    }

    private void setProgress(float progress, Evaluater.Stage stage) {
        String stageString;
        switch (stage) {
            case WAITING:
                stageString = "Waiting for previous run to finish";
                break;
            case PARSING:
                stageString = "Parsing results";
                break;
            case RUNNING:
                stageString = "Running";
                break;
            default:
                throw new UnsupportedOperationException(stage.toString());
        }
        binding.textLoading.setText(stageString);
        binding.progressBar.setProgress((int) (progress * 100));
    }

    private void setError(Throwable error) {
        binding.progressBar.setProgress(0);
        binding.textLoading.setText("ERROR! " + error.getClass().getName() + ": " + error.getMessage());
        binding.textLoading.setTextColor(0xffa01040);
        error.printStackTrace();
    }

    private void onTick() {
        long lastPast = System.currentTimeMillis() - lastDownMs;
        if (isLastDown && lastPast > 250) {
            binding.buttonStepLast.playSoundEffect(SoundEffectConstants.CLICK);
            step(stepIndex - 1);
        }
        long nextPast = System.currentTimeMillis() - nextDownMs;
        if (isNextDown && nextPast > 250) {
            binding.buttonStepNext.playSoundEffect(SoundEffectConstants.CLICK);
            step(stepIndex + 1);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initNavigationButtons() {
        binding.buttonStepLast.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                lastDownMs = System.currentTimeMillis();
                isLastDown = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                isLastDown = false;
                v.playSoundEffect(SoundEffectConstants.CLICK);
                step(stepIndex - 1);
            }
            return true;
        });

        binding.buttonStepNext.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                nextDownMs = System.currentTimeMillis();
                isNextDown = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                isNextDown = false;
                v.playSoundEffect(SoundEffectConstants.CLICK);
                step(stepIndex + 1);
            }
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                requireView().post(ResultFragment.this::onTick);
            }
        }, 0, 1000 / 20);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        Evaluater.instance.cancel();
    }

    private void step(int index) {
        if (index < 0 || index >= result.stepList.size()) return;
        binding.buttonStepLast.setEnabled(index > 0);

        isLastDown &= index > 0;
        binding.buttonStepNext.setEnabled(index < result.stepList.size() - 1);

        isNextDown &= index < result.stepList.size() - 1;
        binding.textLineCurrent.setText(String.valueOf(index + 1));

        updatePseudoCode(index);
        updateVariables(index);

        stepIndex = index;
    }

    private void updatePseudoCode(int index) {
        ResultStep lastStep = result.stepList.get(stepIndex);
        ResultStep nextStep = result.stepList.get(index);
        if (lastStep.pseudoCodeIndex >= 0 && lastStep.pseudoCodeIndex < algorithm.pseudoCode.length) {
            binding.layoutCode.getChildAt(lastStep.pseudoCodeIndex).setBackground(null);
        }
        if (nextStep.pseudoCodeIndex >= 0 && nextStep.pseudoCodeIndex < algorithm.pseudoCode.length) {
            binding.layoutCode.getChildAt(nextStep.pseudoCodeIndex).setBackgroundColor(0xffffff80);
        }
    }

    private void updateVariables(int index) {
        ResultStep stepBefore = index == 0 ? null : result.stepList.get(index - 1);
        ResultStep nextStep = result.stepList.get(index);

        Set<String> changed = stepBefore == null ? nextStep.variables.keySet() :
                nextStep.variables.entrySet().stream()
                        .filter(e -> !e.getValue().equals(stepBefore.variables.get(e.getKey())))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());

        binding.layoutVariables.removeAllViews();
        for (Map.Entry<String, String> entry : nextStep.variables.entrySet()) {
            LinearLayout.LayoutParams params;

            LinearLayout layout = new LinearLayout(binding.getRoot().getContext());

            if (changed.contains(entry.getKey())) {
                layout.setBackgroundColor(0xffc0e8ff);
            }

            TextView label = new TextView(binding.getRoot().getContext());
            layout.addView(label);
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.35f
            );
            label.setTypeface(Typeface.MONOSPACE);
            label.setLayoutParams(params);
            label.setGravity(Gravity.END);
            label.setTextColor(0xff006080);
            label.setText(entry.getKey());
            label.setTextSize(14);

            TextView value = new TextView(binding.getRoot().getContext());
            value.setText(entry.getValue());
            value.setTypeface(Typeface.MONOSPACE);
            params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            params.setMargins(8, 0, 0, 0);
            value.setLayoutParams(params);
            value.setTextSize(14);
            layout.addView(value);
            binding.layoutVariables.addView(layout);
        }
    }

    private void initPseudoCode() {
        for (int i = 0; i < algorithm.pseudoCode.length; i++) {
            String line = algorithm.pseudoCode[i];
            TextView textView = new TextView(binding.getRoot().getContext());
            textView.setText(line);
            textView.setTypeface(Typeface.MONOSPACE);
            textView.setTextSize(12);
            textView.setPadding(5, 0, 5, 0);
            textView.setText(TextViewUtil.makeSpannableString(line));
            binding.layoutCode.addView(textView);
        }
    }

}