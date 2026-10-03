package org.hzmsg.utils;

import dev.diona.pluginhooker.PluginHooker;
import dev.diona.pluginhooker.player.DionaPlayer;
import me.tjsh.luckpermsutils.LuckPermsDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.hzmsg.Anticheat;
import org.hzmsg.HezhongMessage;
import org.hzmsg.PermissionType;
import org.hzmsg.data.PlayerData;
import org.hzmsg.data.PlayerDataManager;
import org.hzmsg.utils.type.AnticheatInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectAnticheat {
    public static void select(Player player, String[] anticheats) {
        if (anticheats.length < 1) {
            messageAllAnticheats(player);
            return;
        } else {
            ArrayList<Anticheat> anticheatsList = new ArrayList<>(); // 存储Anticheat实例，方便获取插件列表。
            PlayerData data = PlayerDataManager.get(player);
            ArrayList<Anticheat> oldAnticheats = new ArrayList<>(data.getSelectedAnticheats());
            for (String anticheat : anticheats) {
                try {
                    String upped = anticheat.toUpperCase();
                    Anticheat ac = Anticheat.valueOf(upped);
                    if (!PermissionType.hasAnticheat(player, ac)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c你没有权限选择 &b" + ac.name() + " &c为当前反作弊！"));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have no permission to select &b" + ac.name() + " &cas your current anticheat!"));
                        return;
                    }
                    anticheatsList.add(ac);
                } catch (Exception e) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c/selac <Anticheats>"));
                    return;
                }
            }
            DionaPlayer dionaP = PluginHooker.getPlayerManager().getDionaPlayer(player);
            if (dionaP == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&cPluginHooker 失败！PluginHooker failed!"));
                return;
            }
            if (anticheatsList.contains(Anticheat.VANILLA)) { // 选择无反作弊，禁用反作弊。
                for (Anticheat anticheat : oldAnticheats) {
                    if (anticheat != Anticheat.VANILLA) {
                        Plugin plugin = Bukkit.getPluginManager().getPlugin(HezhongMessage.anticheatsVsPluginName.get(anticheat));
                        if (plugin != null && dionaP.isPluginEnabled(plugin)) {
                            dionaP.disablePlugin(plugin);
                        }
                    }
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&b你已选择 &a Vanilla &b 反作弊！"));
                ArrayList<Anticheat> newAnticheats = new ArrayList<>();
                newAnticheats.add(Anticheat.VANILLA);
                data.setSelectedAnticheats(newAnticheats);
                LuckPermsDataUtil.setPlayerSuffix(player.getUniqueId(), " &aVanilla", 250);
                HezhongMessage.instance.getLogger().info("Player " + player.getName() + " selected VANILLA, set suffix to " + " &aVanilla");
                return;
            }
            if (!PermissionType.INFINITE_ANTICHEAT.has(player) && anticheatsList.size() > HezhongMessage.MAX_ANTICHEATS) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c选择反作弊过多！最大数量：" + HezhongMessage.MAX_ANTICHEATS));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSelect anticheats too many! Maximum number: " + HezhongMessage.MAX_ANTICHEATS));
                return;
            }
            for (Anticheat anticheat : oldAnticheats) {
                if (anticheat == Anticheat.VANILLA) continue;
                Plugin plugin = Bukkit.getPluginManager().getPlugin(HezhongMessage.anticheatsVsPluginName.get(anticheat));
                if (plugin != null && dionaP.isPluginEnabled(plugin)) {
                    dionaP.disablePlugin(plugin);
                }
            }
            for (Anticheat anticheat : anticheatsList) { // 遍历，获取。
                String pluginName = HezhongMessage.anticheatsVsPluginName.get(anticheat);
                Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
                if (plugin == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c插件 &b" + pluginName + " &c未加载！"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlugin &b" + pluginName + " &cnot loaded!"));
                    return;
                }
                dionaP.enablePlugin(plugin);
            }
            player.playSound(player.getLocation(), Sound.ANVIL_USE, 1, 1);
            data.setSelectedAnticheats(anticheatsList);
            List<Anticheat> ac = data.getSelectedAnticheats();
            String suffix = "";
            if (ac.size() > 1) {
                suffix = " &bMulti";
            } else {
                suffix = " &b" + HezhongMessage.allAnticheats.get(ac.get(0)).name;
            }
            LuckPermsDataUtil.setPlayerSuffix(player.getUniqueId(), suffix, 250);
            HezhongMessage.instance.getLogger().info("Player " + player.getName() + " selected anticheats, set suffix to " + suffix);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&b你已选择 &a" + anticheatsList + "&b 反作弊！"));

            // 重踢出
            if (anticheatsList.contains(Anticheat.KARHU)) {
                player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&c&l你选择了 &b&lKarhu &c&l反作弊，你需要重新进入服务器 \n" +
                        "&c&lYou Selected &b&lKarhu &c&lAnticheat，you should rejoin the server."));
            }
            else if (anticheatsList.contains(Anticheat.TATAKO)) {
                player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&c&l你选择了 &b&lTatako &c&l反作弊，你需要重新进入服务器 \n" +
                        "&c&lYou Selected &b&lTatako &c&lAnticheat，you should rejoin the server."));
            }
        }
    }


    public static void messageAllAnticheats(Player player) {
        for (AnticheatInfo anticheatInfo : HezhongMessage.allAnticheats.values()) {
            if (anticheatInfo.anticheat == Anticheat.VANILLA) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + anticheatInfo.name + " &a版本 " + "1.8.8" + " " +
                                        "&b 简介 " + "Spigot 1.8.8" + " &9 作者 " + "Mojang &a√"));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + anticheatInfo.name + " &a版本 " + anticheatInfo.plugin.getDescription().getVersion() + " " +
                        "&b 简介 " + anticheatInfo.plugin.getDescription().getDescription() + " &9 作者 " + anticheatInfo.plugin.getDescription().getAuthors())
                        + (PermissionType.hasAnticheat(player, anticheatInfo.anticheat)
                        ? ChatColor.translateAlternateColorCodes('&', " &a√") : ChatColor.translateAlternateColorCodes('&', " &c×")));
            }
        }
    }
}
