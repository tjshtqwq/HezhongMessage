package org.hzmsg.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;
        if (block != null) {
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();
            // player.sendRawMessage("x:" + x + " y:" + y + " z:" + z);
            if (x > 235 && x < 292 && y > 0 && y < 257 && z > 165 && z < 207) {
            } else {
                event.setCancelled(true);
            }
        }
    }

    // 检查水桶的放置
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            Block block = event.getClickedBlock();
            Player player = event.getPlayer();
            if (event.getMaterial().toString().contains("BUCKET")) {
                if (player.getGameMode() == GameMode.CREATIVE) return;
                if (block == null) return;
                int x = block.getX();
                int y = block.getY();
                int z = block.getZ();
                // player.sendRawMessage("x:" + x + " y:" + y + " z:" + z);
                if (x > 235 && x < 292 && y > 0 && y < 257 && z > 165 && z < 207) {}
                else{
                    event.setCancelled(true);
                }
            }
        }
    }
}
