package de.g3sit.fastbridge.data.db;

import de.g3sit.fastbridge.data.db.tables.FastBridgeMapsTable;
import de.g3sit.fastbridge.data.db.tables.ScoresTable;

public class FastBridgeDatabase extends Database {

    private ScoresTable scoresTable;
    private FastBridgeMapsTable fastBridgeMapsTable;

    public ScoresTable getScoresTable() {
        return scoresTable;
    }

    public FastBridgeMapsTable getFastBridgeMapsTable() {
        return fastBridgeMapsTable;
    }
}
