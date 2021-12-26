package de.g3sit.fastbridge.data.db.models;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Score {

    public Score(long id, String player, long map_id, long time, Date runAt){
        this(id,UUID.fromString(player),map_id,time,runAt);
    }

    public Score(long id, UUID player, long map_id, long time, Date runAt) {
        this.id = id;
        this.player = player;
        this.map_id = map_id;
        this.time = time;
        this.runAt = runAt;
    }

    private final long id;
    private UUID player;
    private long map_id;
    private long time;
    private Date runAt;

    public long getId() {
        return id;
    }

    public Date getRunAt() {
        return runAt;
    }

    /**
     * sets the run at date if it is not already set
     *
     * @throws IllegalStateException If the runAt date is already set
     * @param runAt at which time this score was achieved
     */
    public void setRunAt(Date runAt) throws IllegalStateException {
        if(Objects.nonNull(this.runAt))
            throw new IllegalStateException("runAt is already set. can not set again");
        this.runAt = runAt;
    }

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public void setPlayer(String player){
        this.player = UUID.fromString(player);
    }

    public long getMap_id() {
        return map_id;
    }

    public void setMap_id(long map_id) {
        this.map_id = map_id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
