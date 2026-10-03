package org.hzmsg.task;

import org.bukkit.ChatColor;
import org.hzmsg.HezhongMessage;
import org.hzmsg.utils.type.TPARequest;

import java.util.UUID;

public class TPARequestCleanupTask implements Runnable {
    @Override
    public void run() {
        // 删除TPA的过期请求
        for (UUID uuid : HezhongMessage.tpaRequests.keySet()) {
            TPARequest request = HezhongMessage.tpaRequests.get(uuid);
            if (request.time + (1000L * 60L * 1L) < System.currentTimeMillis()) {
                request.sender.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix +
                        "&c&l向&e&l" + request.receiver.getName() + "&c&l的传送请求已过期。"));
                request.receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', HezhongMessage.prefix +
                        "&e&l" + request.sender.getName() + "&c&l的传送请求已过期。"));
                HezhongMessage.tpaRequests.remove(uuid);
            }
        }
    }
}
