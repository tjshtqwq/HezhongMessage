package org.hzmsg.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import static org.hzmsg.HezhongMessage.prefix;

public class SpammerTaskA implements Runnable{

    public SpammerTaskA() {}
    @Override
    public void run() {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&bCommand Help: /help"));
    }
}
