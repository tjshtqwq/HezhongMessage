package org.hzmsg;

import org.bukkit.entity.Player;

public enum PermissionType {
    COLOR_CHAT("hezhong.colorchat"),
    ANTICHEAT("hezhong.anticheats.<ac>"),
    INFINITE_ANTICHEAT("hezhong.infac"),
    SHOUT("hezhong.shout"),
    ADMIN("hezhong.admin"),
    RESTART("hezhong.restart");

    private final String node;

    PermissionType(String node) {
        this.node = node;
    }
    public String getNode() {
        return node;
    }
    public boolean has(Player player) {
        return player.hasPermission(getNode());
    }
    public static boolean hasAnticheat(Player player, Anticheat anticheat) {
        return player.hasPermission("hezhong.anticheats." + anticheat.name().toLowerCase());
    }
}