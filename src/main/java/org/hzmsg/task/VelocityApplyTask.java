package org.hzmsg.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.hzmsg.HezhongMessage;
import org.hzmsg.data.PlayerData;
import org.hzmsg.data.PlayerDataManager;
import org.hzmsg.utils.WarpLocations;

public class VelocityApplyTask implements Runnable {
    @Override
    public void run() {
        for (Player pp : Bukkit.getOnlinePlayers()) {
            Location loc = pp.getLocation();
            if (loc.getWorld() != WarpLocations.world) continue;
            PlayerData data = PlayerDataManager.get(pp);
            if (loc.distance(WarpLocations.locations.get("velocity")) <= 4 && (data.getLastVelocityApply() == 0 || System.currentTimeMillis() - data.getLastVelocityApply() >= 1000)) {
                float yaw = loc.getYaw();
                double yawRadians = Math.toRadians(yaw);
                double x = -Math.sin(yawRadians);
                double z = Math.cos(yawRadians);
                Vector direction = new Vector(x, 0, z).normalize();
                direction.multiply(-1);
                Vector knockBack = direction.multiply(0.85);
                knockBack.setY(0.4);
                pp.setVelocity(knockBack);
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&aGive you a velocity."));
                data.setLastVelocityApply(System.currentTimeMillis());
            }
        }
    }
}
