package org.hzmsg.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.hzmsg.HezhongMessage;
import org.hzmsg.utils.type.AnticheatInfo;

import java.util.ArrayList;
import java.util.List;

public class TabHandler implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("warp")) {
            if (args.length == 1) {
                return StringUtil.copyPartialMatches(args[0], WarpLocations.names, new ArrayList<>());
            }
        } else if (command.getName().equalsIgnoreCase("selac")) {
            ArrayList<String> list = new ArrayList<>();
            for (AnticheatInfo anticheatInfo : HezhongMessage.allAnticheats.values()) {
                list.add(anticheatInfo.name);
            }
            return StringUtil.copyPartialMatches(args[args.length - 1], list, new ArrayList<>());
        } else if (command.getName().equalsIgnoreCase("tpa") || command.getName().equalsIgnoreCase("tpaccept") || command.getName().equalsIgnoreCase("tpdeny")
        || command.getName().equalsIgnoreCase("tpcancel")) {
            if (args.length == 1) {
                List<String> list = new ArrayList<>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    list.add(p.getName());
                }
                return StringUtil.copyPartialMatches(args[args.length - 1], list, new ArrayList<>());
            }
        }
        return null;
    }
}
