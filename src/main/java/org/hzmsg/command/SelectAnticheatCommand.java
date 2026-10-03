package org.hzmsg.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hzmsg.HezhongMessage;
import org.hzmsg.utils.SelectAnticheat;

public class SelectAnticheatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c此命令只能由玩家执行！"));
            return true;
        }
        Player pp = (Player) sender;
        SelectAnticheat.select(pp, args);
        return true;
    }
}
