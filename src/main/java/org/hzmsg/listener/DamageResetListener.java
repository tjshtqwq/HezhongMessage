package org.hzmsg.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.hzmsg.data.PlayerDataManager;

public class DamageResetListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        double damage = event.getDamage();
        try {
            if (PlayerDataManager.get(event.getDamager().getUniqueId()).isEnableDamageShow()) {
                ((Player) event.getDamager()).sendMessage(ChatColor.RED + "Damage: " + damage);
            }
        } catch (Exception e) {}
        if (event.getEntity() instanceof Zombie) {
            event.setDamage(0);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                boolean nofall = PlayerDataManager.get(event.getEntity().getUniqueId()).isNoFall();
                if (nofall) {
                    event.setDamage(0);
                }
            }
            boolean nodamage = PlayerDataManager.get(event.getEntity().getUniqueId()).isNoDamage();
            if (nodamage) {
                event.setDamage(0);
            }
        }
    }
}
