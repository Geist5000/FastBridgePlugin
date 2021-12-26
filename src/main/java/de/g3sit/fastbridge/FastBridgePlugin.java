package de.g3sit.fastbridge;

import de.g3sit.fastbridge.commands.PlayCommand;
import de.g3sit.fastbridge.commands.utils.MultiCommand;
import de.g3sit.fastbridge.commands.utils.PlayerCommand;
import de.g3sit.fastbridge.data.db.FastBridgeDatabase;
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

public class FastBridgePlugin extends JavaPlugin {

    private GameManager gameManager;
    private PlayerTimeScheduler playerTimeScheduler;
    private FastBridgeDatabase database;

    @Override
    public void onEnable() {
        super.onEnable();
        this.gameManager = new GameManager(this);

        this.openDatabase();
        this.registerEvents();
        this.registerCommands();
        this.startScheduler();
    }

    public void openDatabase(){
        this.database = new FastBridgeDatabase();
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
                    return false;
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
