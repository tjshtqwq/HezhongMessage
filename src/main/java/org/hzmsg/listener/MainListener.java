package org.hzmsg.listener;

import dev.diona.pluginhooker.PluginHooker;
import dev.diona.pluginhooker.player.DionaPlayer;
import me.tjsh.luckpermsutils.LuckPermsDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.hzmsg.Anticheat;
import org.hzmsg.HezhongMessage;
import org.hzmsg.PermissionType;
import org.hzmsg.data.PlayerData;
import org.hzmsg.data.PlayerDataManager;
import org.hzmsg.task.ScoreBoard;
import org.hzmsg.utils.PositionTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.bukkit.event.EventPriority.LOWEST;


public class MainListener implements Listener {

    private static final Pattern PATTERN_IDE_PLAYER = Pattern.compile("^Player\\d{3}$");

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player pp = event.getPlayer();
        PositionTracker.addPlayer(pp);
        PlayerDataManager.get(pp);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player pp = event.getPlayer();
        String ppName = pp.getName();
        // 验证玩家用户名，是不是Player+三位数字
        // 那种IDE内启动客户端的 会这样
        if (PATTERN_IDE_PLAYER.matcher(ppName).matches()) {
            HezhongMessage.instance.getLogger().info("Kicked " + ppName + " because of name");
            Bukkit.getScheduler().runTaskLater(HezhongMessage.instance, () -> {
                pp.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&c&l如果你直接在Java IDE中启动Minecraft客户端，请使用&b&lAltManager&7/&b&lAccountManager&c&l功能切换一个用户名！"));
            }, 2);
        }
        PositionTracker.addPlayer(pp);
        PlayerData data = PlayerDataManager.get(pp);
        for (Anticheat ac : data.getSelectedAnticheats()) {
            if (ac == Anticheat.VANILLA) break;
            String name = HezhongMessage.anticheatsVsPluginName.get(ac);
            DionaPlayer dp = PluginHooker.getPlayerManager().getDionaPlayer(pp);
            if (dp != null) {
                dp.enablePlugin(Bukkit.getServer().getPluginManager().getPlugin(name));
            }
        }
        String suffix = "";
        if (data.getSelectedAnticheats().contains(Anticheat.VANILLA)) {
            suffix = " &aVanilla";
        } else {
            if (data.getSelectedAnticheats().size() > 1) {
                suffix = " &bMulti";
            } else {
                suffix = " &b" + HezhongMessage.allAnticheats.get(data.getSelectedAnticheats().get(0)).name;
            }
        }
        LuckPermsDataUtil.setPlayerSuffix(pp.getUniqueId(), suffix, 250);
        HezhongMessage.instance.getLogger().info("Player " + pp.getName() + " joined the server, set suffix to " + suffix);

    }

    @EventHandler
    public void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        ScoreBoard.boardMap.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = LOWEST)
    public void onPreCommand(PlayerCommandPreprocessEvent event) {
        if (PermissionType.ADMIN.has(event.getPlayer())) {
            return;
        } else {
            if (event.getMessage().startsWith("/ver") || event.getMessage().startsWith("/version")) {
                event.setCancelled(true);
                Player pp = event.getPlayer();
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&bHezhong test server &aModified from Spigot 1.8.8"));
            } else if (event.getMessage().startsWith("/plugins") || event.getMessage().startsWith("/pl")) {
                event.setCancelled(true);
                Player pp = event.getPlayer();

                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&bHezhong test server &aModified from Spigot 1.8.8"));
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&aThird-party plugins: &bAnticheats, PluginHooker, TitleAPI, Citizens, Viaversion, TabTPS, TAB, ViaLegacyAPI, SkinsRestorer, AuthMe, LPC, PlaceholderAPI, AntiCrash, Spark"));

            } else if (event.getMessage().startsWith("/crashfix")) {

                event.setCancelled(true);
                Player pp = event.getPlayer();
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lHezhong GUARD &a Developed by Hezhong Development"));

            }
        }
    }

}
