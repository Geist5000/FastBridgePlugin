package de.g3sit.fastbridge;

import de.g3sit.fastbridge.listeners.BlockListeners;
import de.g3sit.fastbridge.scheduler.PlayerTimeScheduler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class FastBridgePlugin extends JavaPlugin {

    private GameManager gameManager;
    private PlayerTimeScheduler playerTimeScheduler;

    @Override
    public void onEnable() {
        super.onEnable();
        this.gameManager = new GameManager(this);
        this.registerEvents();
        this.registerCommands();
        this.startScheduler();
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockListeners(this), this);
    }

    private void registerCommands() {
        PluginCommand fbCommand = getCommand("fb");
        if (!Objects.isNull(fbCommand)) {
            fbCommand.setExecutor(new CommandExecutor() {
                @Override
                public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                    GameManager gameManager = FastBridgePlugin.this.getGameManager();
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (gameManager.isPlaying(player)) {
                            gameManager.stopTimer(player);
                        }else{
                            gameManager.createGame(player);
                            player.sendMessage("Game started");
                        }
                    }
                    return true;
                }
            });
        } else {
            getLogger().warning("fb command isn't registered. No FastBridge commands work");
        }
    }


    private void startScheduler() {
        this.playerTimeScheduler = new PlayerTimeScheduler(this.getGameManager());
        this.playerTimeScheduler.runTaskTimer(this,0,3);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }


}
