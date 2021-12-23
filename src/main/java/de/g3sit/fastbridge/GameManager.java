package de.g3sit.fastbridge;

import de.g3sit.fastbridge.data.game.PlayerGame;
import de.g3sit.fastbridge.data.game.PlayerGameState;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

public class GameManager {

    private FastBridgePlugin plugin;

    public GameManager(FastBridgePlugin plugin) {
        this.plugin = plugin;
        this.playerGames = new HashMap<>();
    }

    private HashMap<Player, PlayerGame> playerGames;

    public boolean isPlaying(Player player) {
        return playerGames.containsKey(player);
    }

    public PlayerGame getGame(Player player) {
        if (this.playerGames.containsKey(player)) {
            return this.playerGames.get(player);
        }
        return null;
    }

    private void addGame(Player player,PlayerGame playerGame){
        this.playerGames.put(player,playerGame);
    }

    public HashMap<Player, PlayerGame> getPlayerGames() {
        return this.playerGames;
    }

    public void createGame(Player player) {
        if(!this.playerGames.containsKey(player)){
            this.addGame(player,new PlayerGame());
        }
        // TODO copy blocks, port player, send chat messages
    }

    public void startTimer(Player player){
        if(this.isPlaying(player)){
            PlayerGame game = this.getGame(player);
            if(game.getState() != PlayerGameState.PRE_GAME){
                this.resetGame(player);
            }
            game.startTimer();
        }
    }

    public void stopTimer(Player player) {
        PlayerGame game = getGame(player);
        if (!Objects.isNull(game)) {
            game.stopTimer();
            // TODO port player , remove placed blocks, send chat messages
        }
    }

    /**
     * brings the game in Pre_Game state if it isn't already
     * @param player the player, which game is manipulated
     */
    public void resetGame(Player player){
        if(this.isPlaying(player)) {
            PlayerGame game = this.getGame(player);
            if(game.getState() != PlayerGameState.PRE_GAME){
                game.resetGame();
            }
        }
    }

    public void removeGame(Player player) {
        PlayerGame game = getGame(player);
        if (!Objects.isNull(game)) {
            this.playerGames.remove(player);
        }
    }
}
