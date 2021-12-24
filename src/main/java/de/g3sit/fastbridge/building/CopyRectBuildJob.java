package de.g3sit.fastbridge.building;

import de.g3sit.fastbridge.utils.Locations;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * BuildJob which copies one are to another location
 * <p>
 * ATTENTION: BuildInstructions are created when needed,
 * which means: if the build job is mid execution and changes are made to the source region, these changes may be copied
 */
public class CopyRectBuildJob implements BuildJob {

    private final Location newMax;
    private final Location newMin;
    private final Vector sizeVector;
    private Location oldMin;
    private Location oldMax;

    /**
     * Creates a new CopyRectBuildJob, sourceStart and sourceEnd needs to be the same size
     *
     * @param sourceStart      start location of the source area
     * @param sourceEnd        end location of the source area
     * @param destinationStart start of the destination area
     * @param destinationEnd   end of the destination area
     */
    public CopyRectBuildJob(Location sourceStart, Location sourceEnd, Location destinationStart, Location destinationEnd) {

        this.oldMin = Locations.minLocation(sourceStart, sourceEnd);
        this.oldMax = Locations.maxLocation(sourceStart, sourceEnd);

        this.newMin = Locations.minLocation(destinationStart, destinationEnd);
        this.newMax = Locations.maxLocation(destinationStart, destinationEnd);

        this.sizeVector = newMax.subtract(newMin).toVector();
        Vector oldVector = oldMax.subtract(oldMin).toVector();

        if (!this.sizeVector.equals(oldVector)) {
            throw new IllegalArgumentException("source and destination region doesn't have same size");
        }

    }

    @Override
    public Iterator<BuildInstruction> iterator() {
        return new InstructionCopyRectIterator();
    }

    @Override
    public BuildJobIterator buildJobIterator() {
        return new InstructionCopyRectIterator();
    }

    @Override
    public int size() {
        return this.sizeVector.getBlockX() * this.sizeVector.getBlockY() * this.sizeVector.getBlockZ();
    }

    private class InstructionCopyRectIterator implements BuildJobIterator {

        private int xDiff, yDiff, zDiff;
        private int remaining = size();

        @Override
        public boolean hasNext() {
            return remaining > 0;
        }

        @Override
        public BuildInstruction next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no more instructions left");
            }
            BlockData data = oldMin.clone().add(xDiff, yDiff, zDiff).getBlock().getBlockData();
            Location l = newMin.clone().add(xDiff, yDiff, zDiff);


            // change offset to new block
            if (zDiff == sizeVector.getBlockZ()) {
                zDiff = 0;
                if (xDiff == sizeVector.getBlockX()) {
                    xDiff = 0;
                    yDiff += 1;
                } else {
                    xDiff += 1;
                }
            } else {
                zDiff += 1;
            }

            remaining -= 1;

            return new BuildInstruction(l, data);

        }

        @Override
        public int remaining() {

            return remaining;
        }
    }
}
