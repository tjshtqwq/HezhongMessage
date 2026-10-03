package org.hzmsg.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.hzmsg.HezhongMessage;
import org.hzmsg.PermissionType;
import org.hzmsg.data.PlayerData;
import org.hzmsg.data.PlayerDataManager;
import org.hzmsg.utils.ChatUtil;
import org.hzmsg.utils.WarpLocations;

public class MiscCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            if (command.getName().equalsIgnoreCase("shout")) {
                String message = "";
                for (String arg : args) {
                    message += arg + " ";
                }
                ChatUtil.serverChat(message);
                return true;
            }
            else if (command.getName().equalsIgnoreCase("rest")) {
                ChatUtil.serverChat(HezhongMessage.RESTART_MESSAGE);
                for (Player pp : Bukkit.getOnlinePlayers()) {
                    pp.playSound(pp.getLocation(), Sound.ANVIL_LAND, 1, 1);
                }
                return true;
            }
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c此命令只能由玩家执行！"));
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("suicide")) {
            Player pp = (Player) sender;
            pp.setHealth(0);
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c再见，残酷的世界......"));
        } else if (command.getName().equalsIgnoreCase("shout")) {
            Player pp = (Player) sender;
            if (PermissionType.SHOUT.has(pp)) {
                String message = "";
                for (String arg : args) {
                    message += arg + " ";
                }
                ChatUtil.serverShout(message, pp);
            } else {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c你没有权限执行此命令！"));
            }
        } else if (command.getName().equalsIgnoreCase("warp")) {
            Player pp = (Player) sender;
            if (args.length < 1) {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c/warp <Location name>"));
                return true;
            }
            String targetLocation = args[0];
            Location location = WarpLocations.locations.getOrDefault(targetLocation.toLowerCase(), null);
            if (location != null) {
                pp.teleport(location);
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&b你已传送至 &a" + targetLocation + "&b！"));
            } else {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c未知位置！"));
            }
        } else if (command.getName().equalsIgnoreCase("rest")) {
            Player pp = (Player) sender;
            if (PermissionType.RESTART.has(pp)) {
                ChatUtil.serverChat(HezhongMessage.RESTART_MESSAGE);
                for (Player pp2 : Bukkit.getOnlinePlayers()) {
                    pp2.playSound(pp2.getLocation(), Sound.ANVIL_LAND, 1, 1);
                }
            } else {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c你没有权限执行此命令！"));
            }
        } else if (command.getName().equalsIgnoreCase("fly")) {
            Player pp = (Player) sender;
            if (pp.getAllowFlight()) {
                pp.setAllowFlight(false);
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l已关闭飞行模式。"));
            } else {
                pp.setAllowFlight(true);
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&a&l已开启飞行模式。"));
            }
        } else if (command.getName().equalsIgnoreCase("info")) {
            Player pp = (Player) sender;
            int ping = -1;
            try {
                Object entityPlayer = pp.getClass().getMethod("getHandle").invoke(pp);
                ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
            } catch (Exception ignored) {}
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7—————————————————————— &2Player Info 玩家信息&7 ——————————————————————")); // ————————
            PlayerData data = PlayerDataManager.get(pp);
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fDamage alerts 伤害显示 -> &b" + data.isEnableDamageShow()));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fFall damage 摔落伤害 -> &b" + !data.isNoFall()));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fDamage 伤害 -> &b" + !data.isNoDamage()));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fAlert 警报 -> &b" + data.isAlert()));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fSelected Anticheat 选中的反作弊 -> &b" + data.getSelectedAnticheats()));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fPing 延迟 -> &b" + ping));
        } else {
            // Help command
            Player pp = (Player) sender;
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7———————— &2Hezhong Test Server Command Help 河众测试服务器指令帮助&7 ————————"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/sword: &bGive you a diamond sword 给你一把钻石剑"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/block: &bGive you 10*64 stones 给你十组石头"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/gapple: &bGive you 64 Golden apples 给你一组金苹果"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/pickaxe: &bGive you a diamond pickaxe 给你一个钻石镐"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/alert: &bEnable alerts 开启警报"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/selac: &bSelect anticheat 选择反作弊"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/info: &bShow info 显示信息"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/ping | /pingpong: &bShow ping 查看ping"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/fly: &bToggle flight mode 开启/关闭飞行模式"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/d: &bEnable damage alerts 开启伤害显示"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/suicide: &bKill yourself 紫砂了"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/nofall: &bCancel fall damage? 取消摔落伤害"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/nodamage: &bCancel all damage? 取消伤害"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/tpa <player>: &bTeleport to a player 传送至一个玩家"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/tpaccept <player>: &bAccept a teleport request 接受一个传送请求"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/tpdeny <player>: &bDeny a teleport request 拒绝一个传送请求"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/tpcancel: &bCancel teleport request 取消传送请求"));
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/warp: &bTeleport to a point 传送至一个地点"));

        }

        return true;
    }
}
