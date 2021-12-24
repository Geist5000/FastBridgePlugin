package de.g3sit.fastbridge.building;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

/**
 * Describes a block change in the world.
 */
public class BuildInstruction {

    private Location location;
    private BlockData blockData;

    public BuildInstruction(Location location, BlockData blockData) {
        this.location = location;
        this.blockData = blockData;
    }

    public Location getLocation() {
        return location;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    /**
     * Changes the block to the data which is described in this instruction
     */
    public void perform(){
        this.getLocation().getBlock().setBlockData(this.getBlockData());
    }
}
