package org.hzmsg.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.connorlinfoot.titleapi.TitleAPI;

import java.util.ArrayList;
import java.util.List;

public class KickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("kick") || command.getName().equalsIgnoreCase("ban")) {
            if (args.length < 1) return true;

            int realIndex = -1;
            for (int i = 0; i < args.length; i++) {
                if (args[i].equalsIgnoreCase("-real") || args[i].equalsIgnoreCase("-r")) {
                    realIndex = i;
                    break;
                }
            }

            if (realIndex != -1) {

                List<String> newArgs = new ArrayList<>();
                for (int i = 0; i < args.length; i++) {
                    if (i != realIndex) {
                        newArgs.add(args[i]);
                    }
                }


                String newCommand = command.getName() + " " + String.join(" ", newArgs);

                Bukkit.dispatchCommand(sender, newCommand);

                return true;
            }
            Player target = sender.getServer().getPlayer(args[0]);
            if (target == null) {
                return false;
            }
            StringBuilder j = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                j.append(args[i]).append(" ");
            }
            TitleAPI.sendTitle(target, 30, 60, 30, j.toString(), "Kicked");
            return true;
        }
        return true;
    }
}
