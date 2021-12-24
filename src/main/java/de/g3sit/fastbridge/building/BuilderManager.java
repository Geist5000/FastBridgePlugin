package de.g3sit.fastbridge.building;

import java.util.LinkedList;
import java.util.List;

public class BuilderManager {

    private static final int WORKER_COUNT = 5;

    List<BuilderRunnable> worker;

    public BuilderManager() {
        this.worker = new LinkedList<>();
    }


    /**
     * creates a new Worker if the maximum worker count isn't reached and returns the least busy worker
     * @return a BuilderRunnable instance, which is still running
     */
    private BuilderRunnable getOrCreateRunnable() {
        if (worker.size() < WORKER_COUNT) {
            BuilderRunnable runnable = new BuilderRunnable(this.worker::remove); // maybe create an own callback and don't use remove function of List
            this.worker.add(runnable);
            return runnable;
        }
        BuilderRunnable minRunnable = null;
        for (BuilderRunnable builder :
                this.worker) {
            if (minRunnable == null || minRunnable.getInstructionCount() > builder.getInstructionCount()) {
                minRunnable = builder;
            }
        }
        return minRunnable;
    }
}
