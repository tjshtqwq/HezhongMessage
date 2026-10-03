package org.hzmsg.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.hzmsg.HezhongMessage;

public class ItemCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&c此命令只能由玩家执行！"));
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("sword")) {
            giveItems(player, Material.DIAMOND_SWORD, 1);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&b你已获得钻石剑！"));
        } else if (command.getName().equalsIgnoreCase("block")) {
            giveItems(player, Material.COBBLESTONE, 10 * 64);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&b你已获得10组圆石！"));
        } else if (command.getName().equalsIgnoreCase("gapple")) {
            giveItems(player, Material.GOLDEN_APPLE, 64);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&b你已获得64个金苹果！"));
        } else if (command.getName().equalsIgnoreCase("pickaxe")) {
            Player pp = (Player) sender;
            giveItems(pp, Material.DIAMOND_PICKAXE, 1);
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix + "&b&l已给你一个钻石镐"));
        }

        return true;
    }

    private void giveItems(Player player, Material material, int amount) {
        ItemStack itemStack = new ItemStack(material, amount);
        player.getInventory().addItem(itemStack);
    }
}
