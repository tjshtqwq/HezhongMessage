package org.hzmsg.command;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hzmsg.HezhongMessage;
import org.hzmsg.utils.type.TPARequest;

import java.util.UUID;

public class TPACommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c此命令只能由玩家执行！"));
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("tpa")) {
            Player pp = (Player) sender;
            UUID uuid = pp.getUniqueId();
            if (args.length < 1) {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c/tpa <Player name>"));
                return true;
            }
            String target = args[0];
            Player targetPlayer = Bukkit.getPlayer(target);
            if (targetPlayer == null) {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l玩家未找到！"));
                return true;
            }
            if (targetPlayer.getUniqueId().equals(uuid)) {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l请勿传送给自己！"));
                return true;
            }
            if (HezhongMessage.tpaRequests.containsKey(uuid)) {
                TPARequest request = HezhongMessage.tpaRequests.get(uuid);
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l取消了以前对&e&l" + request.receiver.getName() + "&c&l的传送请求。"));
                request.receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix +
                        "&e&l" + request.sender.getName() + "&c&l取消了对你的传送请求。"));
                HezhongMessage.tpaRequests.remove(uuid);
            }

            TPARequest request = new TPARequest(pp, targetPlayer);
            HezhongMessage.tpaRequests.put(uuid, request);

            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix +
                    "&a&l已向&e&l" + targetPlayer.getName() + "&a&l发送传送请求。&e&l有效期1分钟。"));

            TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                    HezhongMessage.prefix + "&a&l" + pp.getName() + "&a&l请求传送至你。&e&l请在1分钟内选择。"));

            TextComponent acceptButton = new TextComponent(" [接受√] ");
            acceptButton.setColor(ChatColor.GREEN.asBungee());
            acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + pp.getName()));

            TextComponent denyButton = new TextComponent(" [拒绝×] ");
            denyButton.setColor(ChatColor.RED.asBungee());
            denyButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny " + pp.getName()));

            message.addExtra(acceptButton);
            message.addExtra(denyButton);

            targetPlayer.spigot().sendMessage(message);

        } else if (command.getName().equalsIgnoreCase("tpaccept")) {
            Player pp = (Player) sender;
            if (args.length < 1) {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c/tpaccept <Player name>"));
                return true;
            }
            String tpS = args[0];
            Player tpSender = Bukkit.getPlayer(tpS);
            if (tpSender == null) {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l玩家未找到！"));
                return true;
            }
            if (HezhongMessage.tpaRequests.containsKey(tpSender.getUniqueId())) {
                TPARequest request = HezhongMessage.tpaRequests.get(tpSender.getUniqueId());
                if (request.receiver.getUniqueId().equals(pp.getUniqueId()) && !request.accepted) {
                    request.accepted = true;
                    pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&a&l已接受&e&l" + tpSender.getName() + "&a&l的传送请求。正在传送......"));
                    tpSender.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&e&l" + pp.getName() + "&a&l接受了你的传送请求，正在传送......"));
                    tpSender.teleport(pp);
                    HezhongMessage.tpaRequests.remove(tpSender.getUniqueId());
                } else {
                    pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l玩家未发送传送请求！"));
                }
            } else {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l玩家未发送传送请求！"));
            }

        } else if (command.getName().equalsIgnoreCase("tpdeny")) {
            Player pp = (Player) sender;
            if (args.length < 1) {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c/tpdeny <Player name>"));
                return true;
            }
            String tpS = args[0];
            Player tpSender = Bukkit.getPlayer(tpS);
            if (tpSender == null) {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l玩家未找到！"));
                return true;
            }
            if (HezhongMessage.tpaRequests.containsKey(tpSender.getUniqueId())) {
                TPARequest request = HezhongMessage.tpaRequests.get(tpSender.getUniqueId());
                if (request.receiver.getUniqueId().equals(pp.getUniqueId()) && !request.accepted) {
                    request.accepted = true;
                    pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l已拒绝&e&l" + tpSender.getName() + "&a&l的传送请求。"));
                    tpSender.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&e&l" + pp.getName() + "&a&l拒绝了你的传送请求。"));
                    HezhongMessage.tpaRequests.remove(tpSender.getUniqueId());
                } else {
                    pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l玩家未发送传送请求！"));
                }
            } else {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l玩家未发送传送请求！"));
            }

        } else if (command.getName().equalsIgnoreCase("tpcancel")) {
            Player pp = (Player) sender;
            if (HezhongMessage.tpaRequests.containsKey(pp.getUniqueId())) {
                TPARequest request = HezhongMessage.tpaRequests.get(pp.getUniqueId());
                if (!request.accepted) {
                    request.sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l已取消向&e&l" + request.receiver.getName() + "&c&l的传送请求。"));
                    request.receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&e&l" + request.sender.getName() + "&c&l取消了传送请求。"));
                    HezhongMessage.tpaRequests.remove(pp.getUniqueId());
                }
            } else {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c&l你未发送传送请求！"));
            }
        }

        return true;
    }
}
