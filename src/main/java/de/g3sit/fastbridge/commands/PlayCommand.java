package de.g3sit.fastbridge.commands;

import de.g3sit.fastbridge.GameManager;
import de.g3sit.fastbridge.commands.utils.PlayerCommand;
import de.g3sit.fastbridge.data.game.PlayerGameState;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class PlayCommand extends PlayerCommand {

    private GameManager gameManager;

    public PlayCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onPlayerCommand(Player player, Command command, String label, String[] args) {
        if (gameManager.isPlaying(player)) {
            if(gameManager.getGame(player).getState() == PlayerGameState.POST_GAME){
                gameManager.resetGame(player);
            }else{
                gameManager.stopTimer(player);
            }

        }else{
            gameManager.createGame(player);
            gameManager.startTimer(player);
            player.sendMessage("Game started");
        }
        return true;
    }
}
