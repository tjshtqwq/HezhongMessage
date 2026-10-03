package org.hzmsg.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class WarpLocations {
    public static final Map<String, Location> locations = new HashMap<>();
    public static final World world = Bukkit.getWorlds().get(0);
    public static final List<String> names = new ArrayList<>();
    static {
        locations.put("spawn", new Location(world, 211, 61, 162));
        locations.put("scaffold", new Location(world, 239, 62, 163));
        locations.put("killaura", new Location(world, 168, 62, 165));
        locations.put("fly", new Location(world, 211, 62, 139));
        locations.put("speed", new Location(world, 213, 62, 105));
        locations.put("step", new Location(world, 210, 62, 140));
        locations.put("velocity", new Location(world, 141, 62, 174));
        locations.put("tnt", new Location(world, 154, 62, 182));
        locations.put("killaurabot", new Location(world, 139, 62, 195));
        locations.put("longjump", new Location(world, 218, 62, 123));
        locations.put("jesus", new Location(world, 214, 62, 191));
        locations.put("spider", new Location(world, 207, 62, 183));
        locations.put("nofall", new Location(world, 206, 62, 213));
        locations.put("void", new Location(world, 154, 62, 192));
        locations.put("slimejump", new Location(world, 207, 62, 100));
        names.addAll(locations.keySet());
    }
}
