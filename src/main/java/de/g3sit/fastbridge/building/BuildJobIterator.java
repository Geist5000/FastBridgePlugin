package de.g3sit.fastbridge.building;

import java.util.Iterator;

public interface BuildJobIterator extends Iterator<BuildInstruction> {

    /**
     * returns how many instructions are left
     * @return amount of left instructions
     */
    int remaining();
}
