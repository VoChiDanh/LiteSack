package net.danh.litesack.Listeners;

import net.danh.litesack.API.Data.Player.PlayerData;
import net.danh.litesack.API.Data.Player.SackType;
import net.danh.litesack.API.WorldGuard.LSWGuard;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BlockBreak implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        if (LSWGuard.handleForLocation(p, e.getBlock().getLocation(), e, LSWGuard.getStateFlag("ls-mining"))) {
            if (block.getBlockData() instanceof Ageable) {
                Ageable ageable = (Ageable) block.getBlockData();
                if (ageable.getAge() == ageable.getMaximumAge()) {
                    new ArrayList<>(block.getDrops()).forEach(itemStack -> {
                        if (PlayerData.addSackData(p, itemStack, SackType.BLOCK_BREAK)) {
                            e.setDropItems(false);
                            e.getBlock().getDrops().clear();
                        }
                    });
                    return;
                }
                return;
            }
            if (PlayerData.addSackData(p, new ItemStack(block.getType()), SackType.BLOCK_BREAK)) {
                e.setDropItems(false);
                e.getBlock().getDrops().clear();
            }
        }
    }
}
