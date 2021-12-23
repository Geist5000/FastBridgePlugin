package de.g3sit.fastbridge.commands;

import de.g3sit.fastbridge.GameManager;
import de.g3sit.fastbridge.commands.utils.PlayerCommand;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class PlayCommand extends PlayerCommand {

    private GameManager gameManager;

    public PlayCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onPlayerCommand(Player player, Command command, String label, String[] args) {
        this.gameManager.createGame(player);
        return false;
    }
}
