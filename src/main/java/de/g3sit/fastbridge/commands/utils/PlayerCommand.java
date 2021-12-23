package de.g3sit.fastbridge.commands.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * Command Executor, which only allows Players to perform the command
 */
public abstract class PlayerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            return this.onPlayerCommand((Player) sender,command,label,args);
        }
        BaseComponent[] message = this.getNoPlayerMessage();

        if(!Objects.isNull(message)){
            sender.spigot().sendMessage(this.getNoPlayerMessage());
        }
        return true;
    }

    public abstract boolean onPlayerCommand(Player player, Command command, String label, String[] args);

    /**
     * Returns the message which is displayed to non Player sender.
     * @return The Message which the sender sees
     */
    protected BaseComponent[] getNoPlayerMessage(){
        return new ComponentBuilder("This command is only available for Players!").color(ChatColor.RED).create();
    }
}
