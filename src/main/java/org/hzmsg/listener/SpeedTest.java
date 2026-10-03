package org.hzmsg.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.hzmsg.HezhongMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpeedTest implements Listener {

    public static final Map<UUID, org.hzmsg.utils.type.SpeedTest> speedTests = new HashMap<>();
    @EventHandler
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        if (speedTests.getOrDefault(event.getPlayer().getUniqueId(), null) != null) {
            Player pp = event.getPlayer();
            org.hzmsg.utils.type.SpeedTest speedTest = speedTests.get(pp.getUniqueId());
            if (!speedTest.isAllowedToMove()) {
                pp.teleport(event.getFrom());
            } else {
                if (speedTest.finishedTest()) {
                    speedTests.remove(pp.getUniqueId());
                    pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&lSpeedTest Finished!"));
                    pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aMovement Speed: &b" + String.format("%.2f", speedTest.getSpeedInSeconds())+ "&a blocks/s"));
                    pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aMovement &b" + String.format("%.2f", speedTest.getSpeedPercentage().getKey() - 100) + "%&a Faster than Vanilla!"));
                    pp.teleport(speedTest.getStart().add(-5, 0, 0));
                }
            }
        }
    }
    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        if (speedTests.getOrDefault(event.getPlayer().getUniqueId(), null) == null) return;
        Player pp = event.getPlayer();
        org.hzmsg.utils.type.SpeedTest speedTest = speedTests.get(pp.getUniqueId());
        pp.teleport(speedTest.getStart());
        speedTests.remove(pp.getUniqueId());
    }
    @EventHandler
    public void onPlayerDied(org.bukkit.event.entity.PlayerDeathEvent event) {
        if (speedTests.getOrDefault(event.getEntity().getUniqueId(), null) == null) return;
        Player pp = event.getEntity();
        speedTests.remove(pp.getUniqueId());
    }
    @EventHandler
    public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        Player pp = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                String[] lines = sign.getLines();
                if (lines[0].replace(" ", "").contains("SpeedTest")) {
                    Location loc = pp.getLocation();
                    Location signLoc = sign.getLocation();
                    if (loc.getX() >= 235 && loc.getX() <= 292 && loc.getY() >= 0 && loc.getZ() >= 165 && loc.getZ() <= 207) {
                        pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&lDon't try to start speedtest in scaffold area!"));
                        pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l不要在搭路区域开始速度测试！"));
                        return;
                    }
                    if (signLoc.getX() >= 235 && signLoc.getX() <= 292 && signLoc.getY() >= 0 && signLoc.getZ() >= 165 && signLoc.getZ() <= 207) {
                        pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&lDon't try to start speedtest in scaffold area!"));
                        pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l不要在搭路区域开始速度测试！"));
                        return;
                    }
                    if (speedTests.getOrDefault(pp.getUniqueId(), null) != null) {
                        pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&lDon't try to start speedtest in speedtest!"));
                        pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l不要二次开始测试！"));
                        return;
                    }
                    String[] startLocationString = lines[1].split(" ");
                    String[] endLocationString = lines[2].split(" ");
                    Location startLocation = new Location(pp.getWorld(), Double.parseDouble(startLocationString[0]), Double.parseDouble(startLocationString[1]), Double.parseDouble(startLocationString[2]));
                    Location endLocation = new Location(pp.getWorld(), Double.parseDouble(endLocationString[0]), Double.parseDouble(endLocationString[1]), Double.parseDouble(endLocationString[2]));
                    speedTests.put(event.getPlayer().getUniqueId(), new org.hzmsg.utils.type.SpeedTest(
                            event.getPlayer(),
                            startLocation,
                            endLocation
                    ));
                    pp.sendMessage(ChatColor.GREEN + "Beginning SpeedTest in 3 seconds...");
                    HezhongMessage.scheduler.runTaskLater(HezhongMessage.instance, () -> {event.getPlayer().sendMessage(ChatColor.AQUA + "3...");}, 0L);
                    HezhongMessage.scheduler.runTaskLater(HezhongMessage.instance, () -> {event.getPlayer().sendMessage(ChatColor.AQUA + "2...");}, 20L);
                    HezhongMessage.scheduler.runTaskLater(HezhongMessage.instance, () -> {event.getPlayer().sendMessage(ChatColor.AQUA + "1...");}, 40L);
                    HezhongMessage.scheduler.runTaskLater(HezhongMessage.instance, () -> {
                        event.getPlayer().sendMessage(ChatColor.GREEN + "Go!");
                        speedTests.get(event.getPlayer().getUniqueId()).allowToMove();
                    }, 60L);
                }
            }
        }
    }
}
