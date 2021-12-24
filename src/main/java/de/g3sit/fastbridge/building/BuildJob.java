package de.g3sit.fastbridge.building;

import java.util.Iterator;

public interface BuildJob extends Iterable<BuildInstruction> {

    /**
     * returns the size of the BuildJob
     * @return amount of needed BuildInstructions
     */
    int size();

    @Override
    Iterator<BuildInstruction> iterator();

    BuildJobIterator buildJobIterator();
}
