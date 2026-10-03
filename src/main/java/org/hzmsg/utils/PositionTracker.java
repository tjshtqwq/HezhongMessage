package org.hzmsg.utils;
import org.bukkit.entity.Player;
import org.hzmsg.utils.PlayerPosition;

import java.util.HashMap;
import java.util.Map;

public class PositionTracker {
    private static Map<Player, PlayerPosition> playerPositions = new HashMap<>();

    public static void addPlayer(Player player) {
        double x = player.getLocation().getX();
        double z = player.getLocation().getZ();
        if (playerPositions.containsKey(player)) {
            updatePlayerPosition(player);
        } else {
            playerPositions.put(player, new PlayerPosition(x, z));
        }
    }

    public static void updatePlayerPosition(Player player) {
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();
        PlayerPosition position = playerPositions.get(player);
        if (position != null) {
            position.updatePosition(x, y, z);
        }
    }

    public static PlayerPosition getPlayerPosition(Player player) {
        return playerPositions.get(player);
    }
}
