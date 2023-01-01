package net.danh.litesack;

import net.danh.litesack.API.Data.Sack.SackData;
import net.danh.litesack.API.Utils.File;
import net.danh.litesack.API.WorldGuard.LSWGuard;
import net.danh.litesack.CMD.LiteSackCMD;
import net.danh.litesack.Listeners.BlockBreak;
import net.danh.litesack.Listeners.ItemPickup;
import net.danh.litesack.Listeners.JoinQuit;
import net.danh.litesack.PlaceholderAPI.LSPapi;
import net.xconfig.bukkit.XConfigBukkit;
import net.xconfig.bukkit.config.BukkitConfigurationModel;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LiteSack extends JavaPlugin {

    private static LiteSack liteStack;
    private static BukkitConfigurationModel bukkitConfigurationModel;

    public static LiteSack getLiteStack() {
        return liteStack;
    }

    public static BukkitConfigurationModel getBukkitConfigurationModel() {
        return bukkitConfigurationModel;
    }

    @Override
    public void onLoad() {
        liteStack = this;
        bukkitConfigurationModel = XConfigBukkit.manager(liteStack);
        loadFiles(getBukkitConfigurationModel(), getLogger());
        if (File.getSetting().getBoolean("WORLD_GUARD.USE_FLAG")) {
            LSWGuard.register(liteStack);
        }
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
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(SackData::savePlayerData);
    }

    private void loadFiles(BukkitConfigurationModel bukkitConfigurationModel, Logger logger) {
        bukkitConfigurationModel.build("", "config.yml", "settings.yml", "message.yml");
        logger.log(Level.INFO, "Loaded Files");
    }

    private void registerEvents(Listener... listeners) {
        Arrays.stream(listeners).toList().forEach(events -> Bukkit.getPluginManager().registerEvents(events, liteStack));
    }
}
