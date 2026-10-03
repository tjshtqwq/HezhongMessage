package org.hzmsg.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FoodRestoreTask implements Runnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setFoodLevel(20);
        }
    }
}
