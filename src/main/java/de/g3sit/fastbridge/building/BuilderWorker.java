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
        if(!Objects.isNull(this.currentInstructionIterator))
            result+= this.currentInstructionIterator.remaining();
        return  result;
    }

    @Override
    public void run() {
        int processed = 0;
        while((!Objects.isNull(currentInstructionIterator) && currentInstructionIterator.hasNext()) && processed < this.getSpeed()){


            if(currentInstructionIterator.hasNext()){
                BuildInstruction currentInstruction = currentInstructionIterator.next();
                currentInstruction.perform();
                processed++;
            }else{
                if(!this.nextBuildJob()){
                    return;
                }
            }
        }


        if(this.buildJobs.empty()){
            this.nextBuildJob();
        }
    }

    /**
     * call build job finished callback and changes the currentBuildJob and currentInstructionIterator to new values.
     *
     * If no BuildJob is left, this worker is stopped
     *
     */
    private boolean nextBuildJob(){
        this.onBuildFinishedCallback.accept(currentBuildJob);
        if(!this.buildJobs.empty()){
            this.setCurrentBuildJob(this.buildJobs.pop());
            return true;
        }else{
            this.stopSelf();
            return false;
        }
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
