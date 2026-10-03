package org.hzmsg.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtil {
    public static void serverChat(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b&lHezhong " + message));
    }

    public static void serverShout(String message, Player player) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b&lHezhong &e&lSHOUT &7from &f" + player.getName() + ": " + message));
    }

    public static void serverLog(String message) {
        System.out.println(ChatColor.translateAlternateColorCodes('&', "&b&lHezhong " + message));
    }
}
