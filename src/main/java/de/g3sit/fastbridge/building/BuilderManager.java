package de.g3sit.fastbridge.building;

import org.bukkit.Bukkit;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class BuilderManager {

    private static final int WORKER_COUNT = 5;

    List<BuilderWorker> worker;

    public BuilderManager() {
        this.worker = new LinkedList<>();
    }


    /**
     * creates a new Worker if the maximum worker count isn't reached and returns the least busy worker
     * @return a BuilderRunnable instance, which is still running
     */
    private BuilderWorker getOrCreateRunnable() {
        if (worker.size() < WORKER_COUNT) {
            BuilderWorker runnable = new BuilderWorker(this.worker::remove, this::buildJobFinished); // maybe create an own callback and don't use remove function of List
            this.worker.add(runnable);
            return runnable;
        }
        BuilderWorker minRunnable = null;
        for (BuilderWorker builder :
                this.worker) {
            if (minRunnable == null || minRunnable.getInstructionCount() > builder.getInstructionCount()) {
                minRunnable = builder;
            }
        }
        return minRunnable;
    }

    private void buildJobFinished(BuildJob job){
        Bukkit.getLogger().info("finished BuildJob");
    }
}
