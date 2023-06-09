package de.sirvierl0ffel.viswiz.algorithm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import app.cash.zipline.QuickJs;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.models.InputSave;

public class EvaluatingTask implements Runnable {

    private final Algorithm algorithm;
    private final InputSave input;
    private final Consumer<Runnable> post;
    private final BiConsumer<Float, Evaluater.Stage> progress;
    private final Consumer<Result> success;
    private final Consumer<Exception> error;
    private final Runnable over;

    private volatile boolean canceled;

    public EvaluatingTask(Algorithm algorithm,
                          InputSave input,
                          Consumer<Runnable> post,
                          BiConsumer<Float, Evaluater.Stage> progress,
                          Consumer<Result> success,
                          Consumer<Exception> error,
                          Runnable over) {
        this.algorithm = algorithm;
        this.input = input;
        this.post = post;
        this.progress = progress;
        this.success = success;
        this.error = error;
        this.over = over;
    }

    @Override
    public void run() {
        post.accept(() -> progress.accept(0f, Evaluater.Stage.RUNNING));

        try (QuickJs engine = QuickJs.create()) {
            engine.setMemoryLimit(500_000_000L);
            engine.setInterruptHandler(this::isCanceled);
            engineRegisterFunctions(engine, post, progress);
            engine.evaluate(input.input, "input.js");
            Object resultObject = engine.evaluate(algorithm.code, "algorithm.js");
            Result result = parseResult(String.valueOf(resultObject), post, progress);
            if (!isCanceled()) post.accept(() -> success.accept(result));
        } catch (Exception e) {
            if (!isCanceled()) post.accept(() -> error.accept(e));
        }

        if (!isCanceled()) post.accept(over);
    }

    private void engineRegisterFunctions(QuickJs engine, Consumer<Runnable> mainRunner, BiConsumer<Float, Evaluater.Stage> progress) {
        //noinspection KotlinInternalInJava
        engine.initOutboundChannel$zipline_release(new app.cash.zipline.internal.bridge.CallChannel() {
            @NonNull
            @Override
            public String[] serviceNamesArray() {
                return new String[]{};
            }

            @NonNull
            @Override
            public String call(@NonNull String s) {
                JsonObject callObj = new Gson().fromJson(s, JsonObject.class);
                JsonObject argsObj = callObj.get("args").getAsJsonObject();
                String method = callObj.get("method").getAsString();
                if (method.equals("updateProgress")) {
                    float progressValue = argsObj.get("progress").getAsFloat();
                    mainRunner.accept(() -> progress.accept(Math.min(0.99f, Math.max(0, progressValue)), Evaluater.Stage.RUNNING));
                } else if (method.equals("logi")) {
                    Log.i(Evaluater.class.getSimpleName(), String.valueOf(argsObj.get("message")));
                }
                return "";
            }

            @Override
            public boolean disconnect(@NonNull String s) {
                return false;
            }
        });
        engine.evaluate("" +
                        "function updateProgress(progress) {\n" +
                        "  globalThis.app_cash_zipline_outboundChannel.call(JSON.stringify({ method: \"updateProgress\", args: { progress } }));\n" +
                        "}\nupdateProgress(0);\n" +
                        "function logi(message) {\n" +
                        "  globalThis.app_cash_zipline_outboundChannel.call(JSON.stringify({ method: \"logi\", args: { message } }));\n" +
                        "}\n",
                "updateProgress.js");
    }

    private Result parseResult(String stringResult, Consumer<Runnable> mainRunner, BiConsumer<Float, Evaluater.Stage> progress) {
        mainRunner.accept(() -> progress.accept(0f, Evaluater.Stage.PARSING));
        JsonObject jsonResult = new Gson().fromJson(stringResult, JsonObject.class);
        JsonArray jsonSteps = jsonResult.getAsJsonArray("steps");
        List<ResultStep> list = new ArrayList<>(jsonSteps.size());
        for (int i = 0; i < jsonSteps.size(); i++) {
            JsonObject jsonStep = jsonSteps.get(i).getAsJsonObject();
            JsonObject jsonVariables = jsonStep.getAsJsonObject("v");
            Map<String, String> variables = new HashMap<>(jsonVariables.size());
            for (Map.Entry<String, JsonElement> entry : jsonVariables.entrySet()) {
                variables.put(entry.getKey(), entry.getValue().toString());
            }
            int pseudoCodeIndex = jsonStep.get("i").getAsInt();
            String message = jsonStep.has("m") ? jsonStep.get("m").getAsString() : null;
            list.add(new ResultStep(variables, pseudoCodeIndex, message));
            mainRunner.accept(() -> progress.accept((float) list.size() / jsonSteps.size(), Evaluater.Stage.PARSING));
            if (isCanceled()) throw new Evaluater.ParseInterruptedException("cancelled while parsing");
        }
        return new Result(list);
    }

    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

}
