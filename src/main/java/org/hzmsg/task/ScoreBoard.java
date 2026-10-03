package org.hzmsg.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.hzmsg.Anticheat;
import org.hzmsg.HezhongMessage;
import org.hzmsg.data.PlayerDataManager;
import org.hzmsg.utils.MathUtil;
import org.hzmsg.utils.PlayerPosition;
import org.hzmsg.utils.PositionTracker;
import org.hzmsg.utils.TpsUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreBoard implements Runnable {
    public static Map boardMap = new HashMap<UUID, ScoreBoard>();
    private Map objectiveMap = new HashMap<UUID, Objective>();
    private Map tpsMap = new HashMap<UUID, Team>();
    private Map sprintingMap = new HashMap<UUID, Team>();
    private Map sneakingMap = new HashMap<UUID, Team>();
    private Map blockingMap = new HashMap<UUID, Team>();
    private Map groundMap = new HashMap<UUID, Team>();
    private Map bpsMap = new HashMap<UUID, Team>();
    private Map acMap = new HashMap<UUID, Team>();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!boardMap.containsKey(player.getUniqueId())) {
                ScoreboardManager manager = Bukkit.getScoreboardManager();
                Scoreboard scoreboard = manager.getNewScoreboard();

                Objective objective = scoreboard.registerNewObjective("TatakoTest", "dummy");
                objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&b&lHezhong Test Server"));
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                PositionTracker.addPlayer(player);

                PlayerPosition p = PositionTracker.getPlayerPosition(player);

                double deltaX = p.getX() - p.getLastX();
                double deltaZ = p.getZ() - p.getLastZ();
                double deltaXZ = Math.hypot(deltaX, deltaZ);
                double bpsD = deltaXZ * 20;

                Team sprintingTeam = scoreboard.registerNewTeam("sprintingTeam");
                sprintingTeam.addEntry(ChatColor.translateAlternateColorCodes('&', "&bSprinting: "));
                Team sneakingTeam = scoreboard.registerNewTeam("sneakingTeam");
                sneakingTeam.addEntry(ChatColor.translateAlternateColorCodes('&', "&bSneaking: "));
                Team blockingTeam = scoreboard.registerNewTeam("blockingTeam");
                blockingTeam.addEntry(ChatColor.translateAlternateColorCodes('&', "&bBlocking: "));
                Team groundTeam = scoreboard.registerNewTeam("groundTeam");
                groundTeam.addEntry(ChatColor.translateAlternateColorCodes('&', "&bonGround: "));
                Team bpsTeam = scoreboard.registerNewTeam("bpsTeam");
                bpsTeam.addEntry(ChatColor.translateAlternateColorCodes('&', "&bBPS: "));
                Team tpsTeam = scoreboard.registerNewTeam("tpsTeam");
                tpsTeam.addEntry(ChatColor.translateAlternateColorCodes('&', "&bTPS: "));
                Team anticheatTeam = scoreboard.registerNewTeam("acTeam");
                anticheatTeam.addEntry(ChatColor.translateAlternateColorCodes('&', "&bAnticheat: "));

                objective.getScore(ChatColor.translateAlternateColorCodes('&', "&bSprinting: ")).setScore(1);
                objective.getScore(ChatColor.translateAlternateColorCodes('&', "&bSneaking: ")).setScore(2);
                objective.getScore(ChatColor.translateAlternateColorCodes('&', "&bBlocking: ")).setScore(3);
                objective.getScore(ChatColor.translateAlternateColorCodes('&', "&bonGround: ")).setScore(4);
                objective.getScore(ChatColor.translateAlternateColorCodes('&', "&bBPS: ")).setScore(5);
                objective.getScore(ChatColor.translateAlternateColorCodes('&', "&bTPS: ")).setScore(6);
                objective.getScore(ChatColor.translateAlternateColorCodes('&', "&bAnticheat: ")).setScore(7);

                sprintingTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&a" + player.isSprinting()));
                sneakingTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&a" + player.isSneaking()));
                blockingTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&a" + player.isBlocking()));
                groundTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&a" + player.isOnGround()));
                bpsTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&a" + MathUtil.format1(bpsD)));
                tpsTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&a" + MathUtil.format1(TpsUtil.getCurrentTps())));
                String anticheats = "";
                for (Anticheat ac : PlayerDataManager.get(player).getSelectedAnticheats()) {
                    if (ac == Anticheat.VANILLA) {
                        anticheats += "Vanilla ";
                    } else {
                        anticheats += HezhongMessage.allAnticheats.get(ac).name + " ";
                    }
                }
                anticheatTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&a" + (PlayerDataManager.get(player).getSelectedAnticheats().size() >= 2 ? "Multi" : anticheats)));

                objectiveMap.put(player.getUniqueId(), objective);
                boardMap.put(player.getUniqueId(), scoreboard);
                sprintingMap.put(player.getUniqueId(), sprintingTeam);
                sneakingMap.put(player.getUniqueId(), sneakingTeam);
                blockingMap.put(player.getUniqueId(), blockingTeam);
                groundMap.put(player.getUniqueId(), groundTeam);
                bpsMap.put(player.getUniqueId(), bpsTeam);
                tpsMap.put(player.getUniqueId(), tpsTeam);
                acMap.put(player.getUniqueId(), anticheatTeam);

                player.setScoreboard(scoreboard);
            }
            try {
                PositionTracker.addPlayer(player);
                PlayerPosition p = PositionTracker.getPlayerPosition(player);

                double deltaX = p.getX() - p.getLastX();
                double deltaZ = p.getZ() - p.getLastZ();
                double deltaXZ = Math.hypot(deltaX, deltaZ);
                double bpsD = deltaXZ * 20;

                Team sprintingTeam = (Team) sprintingMap.get(player.getUniqueId());
                Team sneakingTeam = (Team) sneakingMap.get(player.getUniqueId());
                Team blockingTeam = (Team) blockingMap.get(player.getUniqueId());
                Team groundTeam = (Team) groundMap.get(player.getUniqueId());
                Team bpsTeam = (Team) bpsMap.get(player.getUniqueId());
                Team tpsTeam = (Team) tpsMap.get(player.getUniqueId());
                Team anticheatTeam = (Team) acMap.get(player.getUniqueId());

                sprintingTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&e" + player.isSprinting()));
                sneakingTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&e" + player.isSneaking()));
                blockingTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&e" + player.isBlocking()));
                groundTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&e" + player.isOnGround()));
                bpsTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&e" + MathUtil.format1(bpsD)));
                tpsTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&e" + MathUtil.format1(TpsUtil.getCurrentTps())));
                String anticheats = "";
                 for (Anticheat ac : PlayerDataManager.get(player).getSelectedAnticheats()) {
                    if (ac == Anticheat.VANILLA) {
                        anticheats += "Vanilla ";
                    } else {
                        anticheats += HezhongMessage.allAnticheats.get(ac).name + " ";
                    }
                }
                anticheatTeam.setSuffix(ChatColor.translateAlternateColorCodes('&', "&a" + (PlayerDataManager.get(player).getSelectedAnticheats().size() >= 2 ? "Multi" : anticheats)));

                sprintingMap.put(player.getUniqueId(), sprintingTeam);
                sneakingMap.put(player.getUniqueId(), sneakingTeam);
                blockingMap.put(player.getUniqueId(), blockingTeam);
                groundMap.put(player.getUniqueId(), groundTeam);
                bpsMap.put(player.getUniqueId(), bpsTeam);
                tpsMap.put(player.getUniqueId(), tpsTeam);
                acMap.put(player.getUniqueId(), anticheatTeam);

            } catch (Exception ignored) {}
        }
    }
}
