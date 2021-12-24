package de.g3sit.fastbridge.building;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Stack;
import java.util.function.Consumer;

public class BuilderRunnable extends BukkitRunnable {

    private final Consumer<BuilderRunnable> onStopCallback;
    private Stack<BuildInstruction> buildInstructions;


    /**
     * how many {@link BuildInstruction}s are processed within each call of the run function
     */
    private int speed = 5;

    public BuilderRunnable(Consumer<BuilderRunnable> onStopCallback) {
        this.buildInstructions = new Stack<>();
        this.onStopCallback = onStopCallback;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void addInstructions(Collection<BuildInstruction> instructions){
        this.buildInstructions.addAll(instructions);
    }

    public int getInstructionCount(){
        return this.buildInstructions.size();
    }

    @Override
    public void run() {
        int processed = 0;
        while(!this.buildInstructions.empty() && processed < this.getSpeed()){
            processed++;
            BuildInstruction currentInstruction = this.buildInstructions.pop();
            currentInstruction.perform();
        }

        if(this.buildInstructions.empty()){
            this.stopSelf();
        }
    }

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
