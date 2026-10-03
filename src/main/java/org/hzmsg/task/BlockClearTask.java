package org.hzmsg.task;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.hzmsg.HezhongMessage;
import org.hzmsg.utils.ChatUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BlockClearTask implements Runnable {

    private final World world;
    private final int minX, maxX, minZ, maxZ, minY, maxY;

    private Queue<Location> cleanQueue = new LinkedList<>();

    public BlockClearTask(World world, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        this.world = world;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() > HezhongMessage.nextScaffoldClean) {
            cleanQueue.clear();
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        cleanQueue.add(new Location(world, x, y, z));
                    }
                }
            }
            ChatUtil.serverChat("&cScaffold area cleaned!");
            HezhongMessage.lastScaffoldClean = System.currentTimeMillis();
            HezhongMessage.nextScaffoldClean = System.currentTimeMillis() + HezhongMessage.scaffoldCleanInterval;
            HezhongMessage.scaffoldCleanAlertOn5Minutes = false;
            HezhongMessage.scaffoldCleanAlertOn1Minutes = false;
            HezhongMessage.scaffoldCleanAlertOn30Seconds = false;
            HezhongMessage.scaffoldCleanAlertOn5Seconds = false;
        }
        if (!cleanQueue.isEmpty()) {
            for (int i = 0; i < Math.min(cleanQueue.size(), 8192); i++) {
                Location blockPos = cleanQueue.poll();
                Block block = blockPos.getBlock();
                if (block.getType() != Material.AIR) {
                    block.setType(Material.AIR);
                }
            }
        }

    }
}

