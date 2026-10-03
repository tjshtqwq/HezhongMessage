package org.hzmsg.listener;

import com.connorlinfoot.titleapi.TitleAPI;
import dev.diona.pluginhooker.PluginHooker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.hzmsg.HezhongMessage;
import org.hzmsg.utils.type.AnticheatInfo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Deepseek科技
// 我需要开始Vibe coding提升我的效率
// 硬匹配包名进行Kick拦截
public class KickHandler implements Listener {

    private static final long TITLE_COOLDOWN = 1000L;

    private final Set<ClassLoader> anticheatLoaders = new HashSet<>();
    private final Set<String> anticheatPrefixes = new HashSet<>();
    private final Map<UUID, Long> lastTitle = new ConcurrentHashMap<>(); // 防止刷屏到爆

    public KickHandler() {
        if (PluginHooker.getPluginManager() == null) {
            return;
        }
        List<Plugin> anticheats = new ArrayList<>();
        for (AnticheatInfo info : HezhongMessage.allAnticheats.values()) {
            anticheats.add(info.plugin);
        }
        for (Plugin plugin : anticheats) {
            if (plugin == null) {
                continue;
            }
            ClassLoader loader = plugin.getClass().getClassLoader();
            if (loader != null) {
                anticheatLoaders.add(loader);
            }
            String pkg = plugin.getClass().getPackage() == null ? null : plugin.getClass().getPackage().getName();
            if (pkg != null && !pkg.isEmpty()) {
                anticheatPrefixes.add(pkg);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onKick(PlayerKickEvent event) {
        String source = findAnticheatFrame();
        if (source == null) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();
        sendTitle(player, event.getReason());
        HezhongMessage.instance.getLogger().info("Hezhong Message Blocked anticheat kick for "
                + player.getName() + " by " + source + " reason=" + event.getReason());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        lastTitle.remove(event.getPlayer().getUniqueId());
    }

    private String findAnticheatFrame() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            String className = element.getClassName();
            if (isAnticheatClass(className)) {
                return className;
            }
        }
        return null;
    }

    // 找类，判定
    private boolean isAnticheatClass(String className) {
        for (String prefix : anticheatPrefixes) {
            if (className.startsWith(prefix)) {
                return true;
            }
        }
        for (ClassLoader loader : anticheatLoaders) {
            try {
                Class<?> clazz = Class.forName(className, false, loader);
                if (clazz.getClassLoader() == loader) {
                    return true;
                }
            } catch (Throwable ignored) {
            }
        }
        return false;
    }

    private void sendTitle(Player player, String reason) {
        long now = System.currentTimeMillis();
        Long last = lastTitle.get(player.getUniqueId());
        if (last != null && now - last < TITLE_COOLDOWN) {
            return;
        }
        lastTitle.put(player.getUniqueId(), now);
        TitleAPI.sendTitle(player, 30, 60, 30, reason == null ? "" : reason, "Get Kicked");
    }
}
