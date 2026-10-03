package org.hzmsg.listener;

import com.tjshawa.api.check.TatakoCheck;
import com.tjshawa.api.listener.TatakoFlagEvent;
import kireiko.dev.anticheat.api.events.MXFlagEvent;
import me.frep.vulcan.api.check.Check;
import me.frep.vulcan.api.event.VulcanFlagEvent;
import me.liwk.karhu.api.data.CheckData;
import me.liwk.karhu.api.event.KarhuEvent;
import me.liwk.karhu.api.event.KarhuListener;
import me.liwk.karhu.api.event.impl.KarhuAlertEvent;
import me.nana.nanadetector.api.event.ViolationEvent;
import me.rerere.matrix.api.HackType;
import me.rerere.matrix.api.events.PlayerViolationEvent;
import me.siuank.acd.api.ACPlayerFlagEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.tjshawa.api.listener.TatakoSendAlertEvent;
import org.hzmsg.Anticheat;
import org.hzmsg.HezhongMessage;
import org.hzmsg.data.PlayerData;
import org.hzmsg.data.PlayerDataManager;
import org.spectrum.orca.api.events.ORCAFlagEvent;

public class AnticheatAlertsEvent implements Listener, KarhuListener {
    // public class TatakoAnticheatAlertsEvent implements Listener, VerboseListener {


    @EventHandler
    public void onTatakoFlagPlayerEvent(TatakoFlagEvent event) {
        Player player = event.getPlayer();
        if (!PlayerDataManager.get(player.getUniqueId()).getSelectedAnticheats().contains(Anticheat.TATAKO)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onTatakoAnticheatAlertsEvent(TatakoSendAlertEvent event) {
        if (event.isCancelled()) return;
        TatakoCheck check = event.getCheck();
        Player pp = event.getPlayer();
        String info = event.getInfo();
        String des = check.getInfo().getDescription();
        String name = check.getInfo().getName();
        int maxVL = check.getMaxVl();
        int vl = check.getVl();
        int maxLength = 20;
        String g = "&7|";
        String bar = "";
        int length = (int) Math.round(((double) vl / (double) maxVL) * maxLength);
        for (int i = 0; i < maxLength; i++) {
            if (i < length) {
                bar += "&f|";
            } else {
                bar += g;
            }
        }
        if (PlayerDataManager.get(pp.getUniqueId()).isAlert()) {
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&7[&c&lTatako&7] &7" + pp.getName() + "&f&l failed &b&l" + name + " &7 | &f" + des +
                            "&7 | &f" + info + " | &7(&a" + (vl - 1) + "&7 -> &b" + vl + "&7 / &c" + maxVL + "&7) "));
        }
    }

    @EventHandler
    public void onMxFlag(MXFlagEvent event) {
        Player player = event.getPlayer();
        String check = event.getCheck();
        String component = event.getComponent();
        String info = event.getInfo();
        double vl = event.getVl();
        double vlLimit = event.getVlLimit();
        int maxLength = 20;
        String g = "&7|";
        String bar = "";
        int length = (int) Math.round(((double) vl / (double) vlLimit) * maxLength);
        for (int i = 0; i < maxLength; i++) {
            if (i < length) {
                bar += "&f|";
            } else {
                bar += g;
            }
        }
        if (PlayerDataManager.get(player.getUniqueId()).isAlert()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&7[&a&lMX&7] &7" + player.getName() + "&f&l failed &b&l" + check + " &7 | &f&l" + component + "&7 | &f" + info +
                            " | &7(&a" + (vl - 1) + "&7 -> &b" + vl + "&7 / &c" + vlLimit + "&7) "));
        }
    }

    /*
    @Override
    public void receive(Alert alert) {
        Player player = Bukkit.getPlayer(alert.getUuid());
        if (player == null) return;
        if (!HezhongMessage.selectedAnticheats.getOrDefault(player.getUniqueId(), null).contains(Anticheat.ARTEMIS)) {
            alert.setCancelled(true);
            return;
        }
        String check = alert.getCheck().getType().name();
        String type = alert.getCheck().getVar();
        int vl = alert.count();
        int maxVl = alert.getCheck().getMaxVl();
        int maxLength = 20;
        String g = "&7|";
        String bar = "";
        int length = (int) Math.round(((double) vl / (double) maxVl) * maxLength);
        for (int i = 0; i < maxLength; i++) {
            if (i < length) {
                bar += "&f|";
            } else {
                bar += g;
            }
        }
        if (PlayerDataManager.get(player.getUniqueId()).isAlert()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&b&lArtemis&7] &7" + player.getName() + "&f&l failed &b&l" + check + " &7 | &f&l" + type + "&7 | (&a" + (vl - 1) + "&7 -> &b" + vl + "&7 / &c" + maxVl + "&f) " + bar));
        }
    }
    */

    @Override
    public void onEvent(KarhuEvent event) {
        if (event instanceof KarhuAlertEvent) {
            KarhuAlertEvent event2 = (KarhuAlertEvent) event;
            Player player = event2.getPlayer();
            CheckData checkData = event2.getCheck();
            String check = checkData.getName();
            int vl = event2.getViolations();
            int maxVl = event2.getDebug().getMaxVl();
            String debugMessage = event2.getDebug().getDebug();
            int maxLength = 20;
            String g = "&7|";
            String bar = "";
            int length = (int) Math.round(((double) vl / (double) maxVl) * maxLength);
            for (int i = 0; i < maxLength; i++) {
                if (i < length) {
                    bar += "&f|";
                } else {
                    bar += g;
                }
            }
            if (PlayerDataManager.get(player.getUniqueId()).isAlert()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&b&lKarhu&7] &7" + player.getName() + "&f&l failed &b&l" +
                        check + " &7 | &f" + debugMessage + "&7 | (&a" + (vl - 1) + "&7 -> &b" + vl + "&7 / &c" + maxVl + "&7) "));
            }
        }
    }

    @EventHandler
    public void onOrcaFlag(ORCAFlagEvent event) {
        Player player = event.getPlayer();
        String check = event.getCheck();
        String debug = event.getDebug();
        double vl = event.getVl();
        double maxVl = event.getVlmax();
        if (PlayerDataManager.get(player.getUniqueId()).isAlert()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&e&lORCA&7] &7" + player.getName() + "&f&l failed &b&l" + check +
                    " &7 | &f" + debug + "&7 | (&a" + (vl - 1) + "&7 -> &b" + vl + "&7 / &c" + maxVl + "&7)"));
        }
    }

    @EventHandler
    public void onNanaFlag(ViolationEvent event) {
        Player player = event.getPlayer();
        String check = event.getCheckName();
        String type = event.getCheckType();
        String desp = event.getDescription();
        boolean exp = event.getExperimental();

        double vl = event.getViolations();
        int maxVl = event.getPunishVL();
        if (PlayerDataManager.get(player.getUniqueId()).isAlert()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&d&lNana&7] &7" +
                    player.getName() + "&f&l failed &b&l" + check + " &7 | &f&l" + type + "&7 | &f" + desp + "&7 | (&a" + (vl - 1) + "&7 -> &b" + vl + "&7 / &c" + maxVl + "&7)" + (exp ? "&7*" : "")));
        }
    }

    @EventHandler
    public void onACDV1Flag(ACPlayerFlagEvent event) {
        Player player = event.getPlayer();
        String check = event.getCheckName();
        String type = event.getCheckType();
        String info = event.getInformation();
        double vl = event.getCurrentVL();
        double addedVl = event.getAddVL();

        // 格式化vl，addedVl到2位小数

        if (PlayerDataManager.get(player.getUniqueId()).isAlert()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3&lACD&7] &7" +
                    player.getName() + "&f&l failed &b&l" + check + " &7 | &f&l" + type + "&7 | &f" + info + "&7 | (&a" + (String.format("%.2f", vl - addedVl)) + "&7 -> &b" +
                    String.format("%.2f", vl) + "&7)"));
        }
    }

    @EventHandler
    // Vulcan
    public void onVulcanFlag(VulcanFlagEvent event) {
        if (event.isCancelled()) return;
        Check check = event.getCheck();
        Player pp = event.getPlayer();
        String info = event.getInfo();
        String des = check.getDescription();
        String name = check.getDisplayName();
        String type = String.valueOf(check.getDisplayType());
        int maxVL = check.getMaxVl();
        int vl = check.getVl();
        int maxLength = 20;
        String g = "&7|";
        String bar = "";
        int length = (int) Math.round(((double) vl / (double) maxVL) * maxLength);
        for (int i = 0; i < maxLength; i++) {
            if (i < length) {
                bar += "&f|";
            } else {
                bar += g;
            }
        }
        if (PlayerDataManager.get(pp.getUniqueId()).isAlert()) {
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&7[&4&lVulcan&7] &7" + pp.getName() + "&f&l failed &b&l" + name + " (" + type + ")" + " &7 | &f" + des +
                            "&7 | &f" + info + " | &7(&a" + (vl - 1) + "&7 -> &b" + vl + "&7 / &c" + maxVL + "&7) "));
        }
    }


    @EventHandler
    // Matrix
    public void onMatrixFlag(PlayerViolationEvent event) {
        Player pp = event.getPlayer();
        PlayerData pd =  PlayerDataManager.get(pp);
        if (pd != null && pd.getSelectedAnticheats().contains(Anticheat.MATRIX)) {
            if (event.isCancelled()) return;
            HackType hackType = event.getHackType();
            String type = event.getComponent();
            String info = event.getMessage();
            String name = hackType.name();
            int vl = event.getViolations();
            if (PlayerDataManager.get(pp.getUniqueId()).isAlert()) {
                pp.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&7[&9&lMatrix&7] &7" + pp.getName() + "&f&l failed &b&l" + name + "&7[&f" + type + "&7]" + " &7 | &f" +
                                info + " | &7(" + "&b" + vl + "&7) "));
            }
        } else {
            event.setCancelled(true);
        }

    }



    /*
    @EventHandler
    public void onEternalFlag(FlagEvent event) {
        Player pp = event.getPlayer();
        String debug = event.getDebugInfo();
        Check check = event.getCheck();
        if (PlayerDataManager.get(pp.getUniqueId()).isAlert()) {
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&3&lEternal&7] &7" +
                    pp.getName() + "&f&l failed &b&l" + check.getName() + " &7 | &f" + debug + "&7 | (&b" + (check.getViolations(pp)) + " / &c" + check.getMaxViolations() + "&7)"));
        }
    }

     */

    /*
    @EventHandler
    public void onAmLegitFlag(PlayerCheckEvent event) {
        Player pp = event.getPlayer();
        String check = event.getCheckDescription().name() + " (" + event.getCheckDescription().type() + ")";
        String debug = event.getAlertMessage();
        if (PlayerDataManager.get(pp.getUniqueId()).isAlert()) {
            pp.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&b&lAmLegit&7] &7" +
                    pp.getName() + "&f&l failed &b&l" + check + " &7 | &f" + debug));
        }
    }

     */
}
