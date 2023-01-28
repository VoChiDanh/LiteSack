package net.danh.litesack;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.danh.litesack.API.Data.Sack.SackData;
import net.danh.litesack.API.Utils.CooldownManager;
import net.danh.litesack.API.Utils.File;
import net.danh.litesack.API.WorldGuard.LSWGuard;
import net.danh.litesack.CMD.LiteSackCMD;
import net.danh.litesack.Listeners.BlockBreak;
import net.danh.litesack.Listeners.ItemPickup;
import net.danh.litesack.Listeners.JoinQuit;
import net.danh.litesack.PlaceholderAPI.LSPapi;
import net.danh.litesack.Stats.Multi;
import net.xconfig.bukkit.XConfigBukkit;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class LiteSack extends JavaPlugin {

    private static LiteSack liteStack;
    private static SimpleConfigurationManager bukkitConfigurationModel;

    public static LiteSack getLiteStack() {
        return liteStack;
    }

    public static SimpleConfigurationManager getBukkitConfigurationModel() {
        return bukkitConfigurationModel;
    }

    @Override
    public void onLoad() {
        liteStack = this;
        bukkitConfigurationModel = XConfigBukkit.newConfigurationManager(liteStack);
        loadFiles(getBukkitConfigurationModel(), getLogger());
        if (File.getSetting().getBoolean("WORLD_GUARD.USE_FLAG")) {
            LSWGuard.register(liteStack);
        }
        MMOItems.plugin.getStats().register(new Multi());
    }

    @Override
    public void onEnable() {
        SackData.loadSack(Bukkit.getConsoleSender());
        new LiteSackCMD();
        registerEvents(new JoinQuit(), new BlockBreak(), new ItemPickup());
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new LSPapi().register();
        }
        Bukkit.getOnlinePlayers().forEach(SackData::loadPlayerData);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(liteStack, () -> {
            if (!BlockBreak.locations.isEmpty()) {
                for (int i = 0; i < BlockBreak.locations.size(); i++) {
                    Location location = BlockBreak.locations.get(i);
                    int times = CooldownManager.getCooldown(location);
                    if (Math.abs(times) > 0) {
                        CooldownManager.setCooldown(location, --times);
                        if (Math.abs(times) == 0) {
                            Material block_type = BlockBreak.blocks.get(location);
                            location.getBlock().setType(block_type);
                            BlockBreak.blocks.remove(location, block_type);
                            BlockBreak.locations.remove(location);
                        }
                    } else {
                        Material block_type = BlockBreak.blocks.get(location);
                        location.getBlock().setType(block_type);
                        BlockBreak.blocks.remove(location, block_type);
                        BlockBreak.locations.remove(location);
                    }
                }
            }
        }, 20L, 20L);
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(SackData::savePlayerData);
        for (Location location : BlockBreak.locations) {
            location.getBlock().setType(BlockBreak.blocks.get(location));
        }
    }

    private void loadFiles(SimpleConfigurationManager bukkitConfigurationModel, Logger logger) {
        bukkitConfigurationModel.build("", "config.yml", "settings.yml", "message.yml");
        logger.log(Level.INFO, "Loaded Files");
    }

    private void registerEvents(Listener... listeners) {
        Arrays.stream(listeners).collect(Collectors.toList()).forEach(events -> Bukkit.getPluginManager().registerEvents(events, liteStack));
    }
}
