package org.hzmsg.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("此命令仅限玩家使用！");
            return true;
        }
        int ping;
        Player player = (Player) sender;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (Exception e) {return false;}
        String color;
        if (ping <= 50) {
            color = "&a";
        } else if (ping <= 100) {
            color = "&9";
        } else if (ping <= 200) {
            color = "&6";
        } else if (ping <= 500) {
            color = "&c";
        } else {
            color = "&4";
        }
        player.sendMessage(ChatColor.GREEN + "你的延迟为: " + ChatColor.translateAlternateColorCodes('&', color) + ping + ChatColor.WHITE + "ms");
        return true;
    }
}
