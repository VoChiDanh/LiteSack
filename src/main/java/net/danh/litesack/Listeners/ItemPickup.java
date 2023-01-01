package net.danh.litesack.Listeners;

import net.danh.litesack.API.Data.Player.PlayerData;
import net.danh.litesack.API.Data.Player.SackType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class ItemPickup implements Listener {

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (PlayerData.addSackData(p, e.getItem().getItemStack(), SackType.ITEM_PICKUP)) {
                e.setCancelled(true);
                e.getItem().remove();
            }
        }
    }
}
