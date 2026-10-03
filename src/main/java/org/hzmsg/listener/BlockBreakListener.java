package org.hzmsg.listener;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        if (x > 235 && x < 292 && y > 60 && y < 257 && z > 165 && z < 207){}
        else{
            event.setCancelled(true);
        }
    }
}

