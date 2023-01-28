package net.danh.litesack.Listeners;

import net.danh.litesack.API.Data.Player.PlayerData;
import net.danh.litesack.API.Data.Player.SackType;
import net.danh.litesack.API.Utils.CooldownManager;
import net.danh.litesack.API.WorldGuard.LSWGuard;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockBreak implements Listener {

    public static final HashMap<Location, Material> blocks = new HashMap<>();
    public static final List<Location> locations = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        Location location = block.getLocation();
        if (LSWGuard.handleForLocation(p, e.getBlock().getLocation(), e, LSWGuard.getStateFlag("ls-mining"))) {
            if (PlayerData.bypass.containsKey(p) && PlayerData.bypass.get(p)) return;
            if (block.getBlockData() instanceof Ageable) {
                Ageable ageable = (Ageable) block.getBlockData();
                if (ageable.getAge() == ageable.getMaximumAge()) {
                    if (!PlayerData.canBreak(p, block.getType())) {
                        e.setCancelled(true);
                        return;
                    }
                    if (PlayerData.addSackData(p, block.getType(), SackType.BLOCK_BREAK)) {
                        e.setCancelled(true);
                        e.setDropItems(false);
                        block.getDrops().clear();
                        locations.add(location);
                        blocks.put(location, block.getType());
                        block.setType(Material.AIR);
                        CooldownManager.setCooldown(location, PlayerData.getRegenTime(block.getType(), SackType.BLOCK_BREAK));
                    }
                }
            } else {
                if (PlayerData.canBreak(p, block.getType())) {
                    if (PlayerData.addSackData(p, block.getType(), SackType.BLOCK_BREAK)) {
                        e.setCancelled(true);
                        e.setDropItems(false);
                        block.getDrops().clear();
                        locations.add(location);
                        blocks.put(location, block.getType());
                        block.setType(Material.BEDROCK);
                        CooldownManager.setCooldown(location, PlayerData.getRegenTime(block.getType(), SackType.BLOCK_BREAK));
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }
}
