package de.g3sit.fastbridge;

import de.g3sit.fastbridge.building.BuildJob;
import de.g3sit.fastbridge.building.BuilderManager;
import de.g3sit.fastbridge.building.CopyRectBuildJob;
import de.g3sit.fastbridge.commands.PlayCommand;
import de.g3sit.fastbridge.commands.utils.MultiCommand;
import de.g3sit.fastbridge.commands.utils.PlayerCommand;
import de.g3sit.fastbridge.data.game.PlayerGameState;
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
import java.util.function.Consumer;

public class FastBridgePlugin extends JavaPlugin {

    private GameManager gameManager;
    private BuilderManager builderManager;
    private PlayerTimeScheduler playerTimeScheduler;

    @Override
    public void onEnable() {
        super.onEnable();
        this.gameManager = new GameManager(this);
        this.builderManager = new BuilderManager(this);
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
            MultiCommand multiCommand = new MultiCommand();

            multiCommand.setCommandExecutor("play",new PlayCommand(this.getGameManager()));

            multiCommand.setCommandExecutor("debug",new PlayerCommand() {
                @Override
                public boolean onPlayerCommand(Player player, Command command, String label, String[] args) {
                    int size = Integer.parseInt(args[0]);
                    builderManager.queueBuildJob(new CopyRectBuildJob(player.getLocation(), player.getLocation().add(size, size, size), player.getLocation().add(size, 0, size), player.getLocation().add(size*2, size, size*2)), new Consumer<BuildJob>() {
                        @Override
                        public void accept(BuildJob buildInstructions) {
                            player.sendMessage("build finished");
                        }
                    });
                    return true;
                }
            });

            multiCommand.register(fbCommand);

        } else {
            getLogger().warning("fb command isn't registered. No FastBridge commands work");
        }
    }


    private void startScheduler() {
        this.playerTimeScheduler = new PlayerTimeScheduler(this.getGameManager());
        // TODO change if to synchronous timer if issues occur
        this.playerTimeScheduler.runTaskTimerAsynchronously(this,0,3);
    }

    @Override
    public void onDisable() {
        this.playerTimeScheduler.cancel();
        super.onDisable();
    }


}
