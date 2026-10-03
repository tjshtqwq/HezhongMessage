package org.hzmsg.utils.type;

import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.hzmsg.Anticheat;

public class AnticheatInfo {
    public String name;
    public Anticheat anticheat;
    public Plugin plugin;
    public boolean hook;

    public AnticheatInfo(String name, Anticheat anticheat, Plugin plugin, boolean hook) {
        this.name = name;
        this.anticheat = anticheat;
        this.plugin = plugin;
        this.hook = hook;
    }

    public AnticheatInfo(String name, Anticheat anticheat, Plugin plugin) {
        this.name = name;
        this.anticheat = anticheat;
        this.plugin = plugin;
        this.hook = true;
    }

    @Override
    public String toString() {
        return "[Hezhong-Registered-Anticheat name=" + name + ", anticheat-enum=" + anticheat + ", plugin=" + plugin + "]";
    }
}
