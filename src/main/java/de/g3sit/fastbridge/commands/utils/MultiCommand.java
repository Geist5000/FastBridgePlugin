package de.g3sit.fastbridge.commands.utils;

import de.g3sit.fastbridge.FastBridgePlugin;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.util.*;

public class MultiCommand implements CommandExecutor, TabCompleter {
    private final HashMap<String,CommandExecutor> commands;
    private CommandExecutor fallback;

    public MultiCommand() {
        this.commands = new HashMap<>();
        this.fallback = new FallbackCommand();
    }

    public void register(PluginCommand command) {
        command.setExecutor(this);
    }

    /**
     * Sets the command executor for the given Command
     *
     * TODO accept TabCompleter
     * @param command name of the command
     * @param executor if this is null, the given command is removed
     */
    public void setCommandExecutor(String command, CommandExecutor executor) {
        this.setCommandExecutor(command, null, executor);
    }

    public void setCommandExecutor(String command, String[] aliases, CommandExecutor executor) {
        if (Objects.isNull(executor)) {
            this.commands.remove(command);
        } else {
            this.commands.put(command.toLowerCase(Locale.ROOT), executor);
        }
    }

    public void setFallbackExecutor(CommandExecutor executor) {
        if (Objects.isNull(executor)) {
            this.fallback = new FallbackCommand();
        } else {
            this.fallback = executor;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            String cmd = args[0].toLowerCase(Locale.ROOT);
            String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

            if (this.commands.containsKey(cmd)) {
                return this.commands.get(cmd).onCommand(sender, command, cmd, newArgs);
            }
        }
        return this.fallback.onCommand(sender, command, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Bukkit.getLogger().info("Command alias: " + alias);
        Bukkit.getLogger().info(Arrays.toString(args));
        if(args.length != 1){
            return null;
        }
        return this.commands.keySet().stream().filter((c)->c.startsWith(args[0])).toList();
    }

    public static class FallbackCommand implements CommandExecutor {

        private BaseComponent[] fallbackMessage;

        public FallbackCommand() {

        }

        public FallbackCommand(String message) {
            this.fallbackMessage = new BaseComponent[]{new TextComponent(message)};
        }

        public FallbackCommand(BaseComponent[] fallbackMessage) {
            this.fallbackMessage = fallbackMessage;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!Objects.isNull(this.fallbackMessage)) {
                sender.spigot().sendMessage(this.fallbackMessage);
                return true;
            }
            return false;
        }
    }
}
