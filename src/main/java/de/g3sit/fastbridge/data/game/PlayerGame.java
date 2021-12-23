package de.g3sit.fastbridge.data.game;

import java.util.Arrays;

public class PlayerGame {

    private PlayerGameState status;
    private long startTimestamp;
    private long endTimestamp;

    public PlayerGame() {
        this.status = PlayerGameState.PRE_GAME;
        this.resetGame();
    }

    public PlayerGameState getState() {
        return status;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    private void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    private void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    private void setStatus(PlayerGameState status) {
        this.status = status;
    }

    public void resetGame() {
        this.status = PlayerGameState.PRE_GAME;
        this.startTimestamp = 0;
        this.endTimestamp = 0;
    }

    public void startTimer() throws IllegalStateException {

        this.throwIfNotState(PlayerGameState.PRE_GAME);


        this.setStatus(PlayerGameState.BUILDING);
        this.setStartTimestamp(System.currentTimeMillis());
    }

    public void stopTimer() {
        this.throwIfNotState(PlayerGameState.BUILDING);

        this.setStatus(PlayerGameState.POST_GAME);
        this.setEndTimestamp(System.currentTimeMillis());
    }

    private void throwIfNotState(PlayerGameState... statuses) throws IllegalStateException {
        boolean any = false;
        for (PlayerGameState status :
                statuses) {
            if (status == this.getState()) {
                any = true;
                break;
            }
        }
        if (!any) {
            throw new IllegalStateException(this.getClass().getName() + " is in wrong state. Expected it to be in one of: " + Arrays.deepToString(statuses) + " but was: " + this.getState());
        }
    }

    /**
     * Returns whether this game has one of the given statuses
     *
     * @param statuses one or more status which are checked
     * @return whether this game has one of the given statuses
     */
    public boolean hasStatus(PlayerGameState... statuses) {
        for (PlayerGameState s :
                statuses) {
            if (s == this.getState())
                return true;
        }
        return false;
    }

    /**
     * Returns a string which contains the time difference in minutes, seconds with one decimal point
     *
     * @param otherTime Miliseconds timestamp
     * @return a formatted time string
     */
    public String getTimeDifferenceString(long otherTime) {
        long startTimestamp = System.currentTimeMillis();

        if (this.hasStatus(PlayerGameState.BUILDING, PlayerGameState.POST_GAME)) {
            startTimestamp = this.getStartTimestamp();
        }

        long diffTime = otherTime - startTimestamp;
        long milliseconds = (diffTime / 100) % 9;
        long seconds = (diffTime / 1000) % 60;
        long minutes = diffTime / 1000 / 60;

        return String.format("%dm %02d.%ds", minutes, seconds, milliseconds);
    }

    /**
     * Returns a string which contains the time difference of the start and end timestamp in minutes, seconds with one decimal point
     *
     * @return a formatted time string
     */
    public String getTimeDifferenceString() {
        return this.getTimeDifferenceString(this.getEndTimestamp());
    }
}
