package de.g3sit.fastbridge.building;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public class CopyRectBuildJob implements BuildJob{

    private Location old_min;
    private Location old_max;

    public CopyRectBuildJob(Location start, Location end) {
        World world = start.getWorld();
        if(start.getWorld() != end.getWorld()){
            throw new IllegalArgumentException("Locations aren't in same world");
        }
        this.old_min = new Location (world,Math.min(start.getBlockX(),end.getBlockX()),Math.min(start.getBlockY(),end.getBlockY()),Math.min(start.getBlockZ(),end.getBlockZ()));
        this.old_max = new Location (world,Math.max(start.getBlockX(),end.getBlockX()),Math.max(start.getBlockY(),end.getBlockY()),Math.max(start.getBlockZ(),end.getBlockZ()));
    }

    @Override
    public List<BuildInstruction> getInstructions() {
        return null;
    }
}
