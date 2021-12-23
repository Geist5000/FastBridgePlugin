package de.g3sit.fastbridge.listeners;

import de.g3sit.fastbridge.FastBridgePlugin;
import de.g3sit.fastbridge.GameManager;
import de.g3sit.fastbridge.data.game.PlayerGame;
import de.g3sit.fastbridge.data.game.PlayerGameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListeners implements Listener {


    private FastBridgePlugin plugin;

    public BlockListeners(FastBridgePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent){
        GameManager gameManager = this.plugin.getGameManager();
        Player player = blockPlaceEvent.getPlayer();
        if(gameManager.isPlaying(player)){
            PlayerGame game = gameManager.getGame(player);
            if(game.getState() == PlayerGameState.PRE_GAME){
                game.resetGame();
            }
        }
    }
}
