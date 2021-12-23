package de.g3sit.fastbridge.building;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Stack;

public class BuilderRunnable extends BukkitRunnable {

    private Stack<BuildInstruction> buildInstructions;
    /**
     * how many {@link BuildInstruction}s are processed within each run function call
     */
    private int speed = 5;

    public BuilderRunnable() {
        this.buildInstructions = new Stack<>();
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

    @Override
    public void run() {
        int processed = 0;
        while(!this.buildInstructions.empty() && processed < this.speed){
            BuildInstruction currentInstruction = this.buildInstructions.pop();
        }
    }
}
