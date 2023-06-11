package de.sirvierl0ffel.viswiz.algorithm;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.models.InputSave;


public class Evaluater {

    public static final Evaluater instance = new Evaluater();

    private final ScheduledExecutorService exec = new ScheduledThreadPoolExecutor(1);

    private EvaluatingTask lastTask;


    /**
     * Automatically cancels the last task, which means that the last task's callbacks don't get called.
     *
     * @param algorithm the algorithm to run
     * @param input     the input of the algorithm
     * @param post      every callback is passed to this consumer, so the its implementation can prevent race conditions
     * @param progress  progress callback
     * @param success   success callback
     * @param error     error callback
     * @param over      over callback
     */
    public void evaluateAsync(Algorithm algorithm,
                              InputSave input,
                              Consumer<Runnable> post,
                              BiConsumer<Float, Stage> progress,
                              Consumer<Result> success,
                              Consumer<Exception> error,
                              Runnable over) {
        if (lastTask != null) lastTask.cancel();

        post.accept(() -> progress.accept(0f, Stage.WAITING));

        lastTask = new EvaluatingTask(algorithm, input, post, progress, success, error, over);
        exec.execute(lastTask);
    }

    public void cancel() {
        if (lastTask != null) lastTask.cancel();
    }

    public enum Stage {
        WAITING, // Waiting for canceled tasks to complete execution
        RUNNING, // Running the javascript code
        PARSING // Parsing the results of the javascript code
    }

    public static class ParseInterruptedException extends RuntimeException {
        public ParseInterruptedException(String message) {
            super(message);
        }
    }

}
