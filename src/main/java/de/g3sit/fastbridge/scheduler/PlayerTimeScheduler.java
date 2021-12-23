package de.g3sit.fastbridge.scheduler;

import de.g3sit.fastbridge.GameManager;
import de.g3sit.fastbridge.data.game.PlayerGame;
import de.g3sit.fastbridge.data.game.PlayerGameState;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

/**
 * Scheduler which updates the current time of each player which is currently playing
 */
public class PlayerTimeScheduler extends BukkitRunnable {

    private final GameManager gameManager;

    public PlayerTimeScheduler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        for (Map.Entry<Player, PlayerGame> playerGameEntry : this.gameManager.getPlayerGames().entrySet()) {
            PlayerGame game = playerGameEntry.getValue();
            Player player = playerGameEntry.getKey();
            if (game.getState() == PlayerGameState.BUILDING) {


                BaseComponent[] message = new ComponentBuilder("Your current Time: ").color(ChatColor.GRAY)
                        .append(game.getTimeDifferenceString(System.currentTimeMillis())).color(ChatColor.GREEN).create();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
            }else if(game.getState() == PlayerGameState.POST_GAME){


                BaseComponent[] message = new ComponentBuilder("Your last Time: ").color(ChatColor.GRAY)
                        .append(game.getTimeDifferenceString()).color(ChatColor.RED).create();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,message);
            }
        }
    }
}
