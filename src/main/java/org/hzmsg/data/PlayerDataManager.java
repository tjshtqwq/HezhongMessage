package org.hzmsg.data;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerDataManager {

    private static final Map<UUID, PlayerData> PLAYERS = new HashMap<>();

    private PlayerDataManager() {
    }

    public static PlayerData get(UUID uuid) {
        return PLAYERS.computeIfAbsent(uuid, k -> new PlayerData());
    }

    public static PlayerData get(Player player) {
        return get(player.getUniqueId());
    }
}
