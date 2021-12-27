package de.g3sit.fastbridge.building;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Stack;
import java.util.function.Consumer;

public class BuilderWorker extends BukkitRunnable {

    private final Consumer<BuilderWorker> onStopCallback;
    private final Consumer<BuildJob> onBuildFinishedCallback;
    private final Stack<BuildJob> buildJobs;
    private BuildJob currentBuildJob;
    private BuildJobIterator currentInstructionIterator;


    /**
     * how many {@link BuildInstruction}s are processed within each call of the run function
     */
    private int speed;

    public BuilderWorker(Consumer<BuilderWorker> onStopCallback, Consumer<BuildJob> onBuildFinishedCallback){
        this(5,onStopCallback,onBuildFinishedCallback);
    }

    public BuilderWorker(int speed, Consumer<BuilderWorker> onStopCallback, Consumer<BuildJob> onBuildFinishedCallback) {
        this.buildJobs = new Stack<>();
        this.onStopCallback = onStopCallback;
        this.onBuildFinishedCallback = onBuildFinishedCallback;
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    private void setCurrentBuildJob(BuildJob job){
        this.currentBuildJob = job;
        this.currentInstructionIterator = job.buildJobIterator();
    }

    public void addBuildJob(BuildJob buildJob){
        if(Objects.isNull(this.currentBuildJob)){
            this.setCurrentBuildJob(buildJob);
        }else{
            this.buildJobs.add(buildJob);
        }
    }

    public int getInstructionCount(){
        int result = this.buildJobs.stream().map(BuildJob::size).reduce(Integer::sum).orElse(0);
        if(Objects.nonNull(this.currentInstructionIterator))
            result+= this.currentInstructionIterator.remaining();
        return  result;
    }

    public boolean hasWork(){
        return hasCurrentWork() || !this.buildJobs.empty();
    }

    public boolean hasCurrentWork(){
        return Objects.nonNull(this.currentInstructionIterator) && currentInstructionIterator.hasNext();
    }

    @Override
    public void run() {

        long processed = 0;
        while(hasCurrentWork() && processed < this.getSpeed()){

            if(Objects.nonNull(currentInstructionIterator) && currentInstructionIterator.hasNext()){
                BuildInstruction currentInstruction = currentInstructionIterator.next();
                currentInstruction.perform();
                processed++;
            }
        }

        if(!hasCurrentWork()){
            this.finishCurrentBuildJob();
        }

        if(hasWork()){
            this.nextBuildJob();
        }else{
            this.stopSelf();
        }
        Bukkit.getLogger().info("one build cycle finished run " + processed + " build instructions while having speed of " + getSpeed());
    }

    /**
     * call build job finished callback and changes the currentBuildJob and currentInstructionIterator to new values.
     *
     * If no BuildJob is left, this worker is stopped
     *
     */
    private void nextBuildJob(){
        if(!this.buildJobs.empty()){
            this.setCurrentBuildJob(this.buildJobs.pop());
        }
    }

    private void finishCurrentBuildJob(){
        if(Objects.nonNull(currentBuildJob))
            this.onBuildFinishedCallback.accept(currentBuildJob);
        this.currentBuildJob = null;
        this.currentInstructionIterator = null;
    }


    /**
     * Stops this BukkitRunnable
     */
    private void stopSelf(){
        try{
            this.cancel();
        }catch (IllegalStateException exception){
            Bukkit.getLogger().info("Tried stopping BukkitRunnable without starting it first!");
        }finally {
            this.onStopCallback.accept(this);
        }
    }


}
