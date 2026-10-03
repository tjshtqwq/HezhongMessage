package org.hzmsg.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hzmsg.HezhongMessage;
import org.hzmsg.data.PlayerData;
import org.hzmsg.data.PlayerDataManager;

public class ToggleCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c此命令只能由玩家执行！"));
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("d")) {
            try {
                Player pp = (Player) sender;
                PlayerData data = PlayerDataManager.get(pp);
                if (data.isEnableDamageShow()) {
                    data.setEnableDamageShow(false);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c已关闭伤害显示！"));
                } else {
                    data.setEnableDamageShow(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&a已开启伤害显示！"));
                }

            } catch (Exception e) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c内部错误！"));
            }
        } else if (command.getName().equalsIgnoreCase("nofall")) {
            try {
                Player pp = (Player) sender;
                PlayerData data = PlayerDataManager.get(pp);
                if (data.isNoFall()) {
                    data.setNoFall(false);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c已开启摔落伤害！"));
                } else {
                    data.setNoFall(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&a已关闭摔落伤害！"));
                }
            } catch (Exception e) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c内部错误！"));
            }
        } else if (command.getName().equalsIgnoreCase("nodamage")) {
            try {
                Player pp = (Player) sender;
                PlayerData data = PlayerDataManager.get(pp);
                if (data.isNoDamage()) {
                    data.setNoDamage(false);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c已开启伤害！"));
                } else {
                    data.setNoDamage(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&a已关闭伤害！"));
                }
            } catch (Exception e) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c内部错误！"));
            }
        } else if (command.getName().equalsIgnoreCase("alert")) {
            try {
                Player pp = (Player) sender;
                PlayerData data = PlayerDataManager.get(pp);
                if (data.isAlert()) {
                    data.setAlert(false);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c已关闭警报！"));
                } else {
                    data.setAlert(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&a已开启警报！"));
                }
            } catch (Exception e) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c内部错误！"));
                e.printStackTrace();
            }
        }

        return true;
    }
}
