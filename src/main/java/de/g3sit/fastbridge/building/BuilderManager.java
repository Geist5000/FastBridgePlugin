package de.g3sit.fastbridge.building;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class BuilderManager {

    private static final int WORKER_COUNT = 5;
    private static final int WORKER_DELAY = 1;
    private static final int WORKER_SPEED = 100;
    private final Plugin plugin;

    private final List<BuilderWorker> worker;
    private final HashMap<BuildJob,Consumer<BuildJob>> buildJobFinishedCallbacks;

    public BuilderManager(Plugin plugin) {
        this.worker = new LinkedList<>();
        this.buildJobFinishedCallbacks = new HashMap<>();
        this.plugin = plugin;
    }


    /**
     * creates a new Worker if the maximum worker count isn't reached and returns the least busy worker.
     *
     * Returned worker is always running
     * @return a BuilderRunnable instance, which is still running
     */
    private BuilderWorker getOrCreateRunnable() {
        if (worker.size() < WORKER_COUNT) {
            BuilderWorker runnable = new BuilderWorker(WORKER_SPEED,this.worker::remove, this::buildJobFinished); // maybe create an own callback and don't use remove function of List
            this.worker.add(runnable);
            runnable.runTaskTimer(plugin,5, WORKER_DELAY);
            return runnable;
        }


        BuilderWorker minRunnable = null;
        int minInstructionCount = 0;
        for (BuilderWorker builder :
                this.worker) {
            int instructionCount = builder.getInstructionCount();
            if (minRunnable == null || minInstructionCount > instructionCount) {
                minRunnable = builder;
                minInstructionCount = instructionCount;
            }
        }
        return minRunnable;
    }

    public void queueBuildJob(BuildJob job, Consumer<BuildJob> onBuildJobFinishedCallback){
        this.getOrCreateRunnable().addBuildJob(job);
        this.buildJobFinishedCallbacks.put(job,onBuildJobFinishedCallback);
    }

    private void buildJobFinished(BuildJob job){
        Bukkit.getLogger().info("finished BuildJob");
        // call build job finished callback
        this.buildJobFinishedCallbacks.remove(job).accept(job);
    }
}
