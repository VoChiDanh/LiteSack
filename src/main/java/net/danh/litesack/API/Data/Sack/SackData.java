package net.danh.litesack.API.Data.Sack;

import net.danh.litesack.API.Utils.Chat;
import net.danh.litesack.API.Utils.File;
import net.danh.litesack.LiteSack;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SackData {
    public static HashMap<String, String> sName = new HashMap<>();
    public static HashMap<String, Integer> sStorage = new HashMap<>();
    public static HashMap<String, List<String>> sItems = new HashMap<>();
    public static HashMap<String, List<String>> sItemFrom = new HashMap<>();
    public static HashMap<String, Integer> pSackData = new HashMap<>();

    public static void loadSack(CommandSender c) {
        sName.clear();
        sStorage.clear();
        sItemFrom.clear();
        sItems.clear();
        Objects.requireNonNull(File.getConfig().getConfigurationSection("SACK")).getKeys(false).stream().toList().forEach(sackID -> {
            String sackName = Chat.colorize(File.getConfig().getString("SACK." + sackID + ".DISPLAY"));
            sName.put(sackID, sackName);
            Integer sackStorage = File.getConfig().getInt("SACK." + sackID + ".DEFAULT_STORAGE");
            sStorage.put(sackID, sackStorage);
            List<String> sid = new ArrayList<>();
            Objects.requireNonNull(File.getConfig().getConfigurationSection("SACK." + sackID + ".ITEM")).getKeys(false).stream().toList().forEach(sackItems -> {
                List<String> sackFrom = File.getConfig().getStringList("SACK." + sackID + ".ITEM." + sackItems + ".FROM");
                sid.add(sackItems);
                sItemFrom.put(sackID + "_" + sackItems, sackFrom);
            });
            sItems.put(sackID, sid);
        });
        Chat.sendCommandSenderMessage(c, Objects.requireNonNull(File.getMessage().getString("SACK.LOAD")).replace("<amount>", String.valueOf(sItems.keySet().size())));
    }

    public static List<String> getSackList() {
        return sItems.keySet().stream().toList();
    }

    public static List<String> getItemList(String sackID) {
        return sItems.get(sackID).stream().toList();
    }

    public static void loadPlayerData(Player p) {
        if (!LiteSack.getBukkitConfigurationModel().isCreated("PlayerData", p.getName() + ".yml")) {
            LiteSack.getBukkitConfigurationModel().buildCustom("PlayerData", p.getName() + ".yml");
            FileConfiguration pData = LiteSack.getBukkitConfigurationModel().file("PlayerData", p.getName() + ".yml");
            getSackList().forEach(sackID -> {
                pData.set("SACK." + sackID + ".STORAGE", sStorage.get(sackID));
                getItemList(sackID).forEach(sid -> pData.set("SACK." + sackID + ".ITEM." + sid, 0));
            });
            LiteSack.getBukkitConfigurationModel().save("PlayerData", p.getName() + ".yml");
        }
        FileConfiguration pData = LiteSack.getBukkitConfigurationModel().file("PlayerData", p.getName() + ".yml");
        Objects.requireNonNull(pData.getConfigurationSection("SACK")).getKeys(false).forEach(sackID -> {
            pSackData.put(p.getName() + "_" + sackID, pData.getInt("SACK." + sackID + ".STORAGE"));
            Objects.requireNonNull(pData.getConfigurationSection("SACK." + sackID + ".ITEM")).getKeys(false).forEach(item -> pSackData.put(p.getName() + "_" + item, pData.getInt("SACK." + sackID + ".ITEM." + item)));
        });
    }

    public static void savePlayerData(Player p) {
        FileConfiguration pData = LiteSack.getBukkitConfigurationModel().file("PlayerData", p.getName() + ".yml");
        getSackList().forEach(sackID -> {
            pData.set("SACK." + sackID + ".STORAGE", getStorageSack(p, sackID));
            getItemList(sackID).forEach(sid -> pData.set("SACK." + sackID + ".ITEM." + sid, getSackItem(p, sid)));
        });
        LiteSack.getBukkitConfigurationModel().save("PlayerData", p.getName() + ".yml");
    }

    public static Integer getStorageSack(Player p, String sackID) {
        return pSackData.get(p.getName() + "_" + sackID);
    }

    public static Integer getSackItem(Player p, String item) {
        return pSackData.get(p.getName() + "_" + item);
    }

    public static void setStorageSack(Player p, String sackID, Integer amount) {
        pSackData.put(p.getName() + "_" + sackID, amount);
    }

    public static void addStorageSack(Player p, String sackID, Integer amount) {
        pSackData.replace(p.getName() + "_" + sackID, getSackItem(p, sackID) + amount);
    }

    public static void removeStorageSack(Player p, String sackID, Integer amount) {
        if (getSackItem(p, sackID) - amount >= 0) {
            pSackData.replace(p.getName() + "_" + sackID, getSackItem(p, sackID) - amount);
        }
    }

    public static void setSackItem(Player p, String sackID, String item, Integer amount) {
        int sackStorage = getStorageSack(p, sackID);
        int sackItem = getSackItem(p, item);
        if (amount <= sackStorage) {
            pSackData.put(p.getName() + "_" + item, amount);
        }
    }

    public static void addSackItem(Player p, String sackID, String item, Integer amount) {
        int sackStorage = getStorageSack(p, sackID);
        int sackItem = getSackItem(p, item);
        if (sackItem + amount <= sackStorage) {
            pSackData.replace(p.getName() + "_" + item, sackItem + amount);
        }
    }

    public static void removeSackItem(Player p, String sackID, String item, Integer amount) {
        int sackStorage = getStorageSack(p, sackID);
        int sackItem = getSackItem(p, item);
        if (sackItem - amount <= sackStorage && sackItem - amount >= 0) {
            pSackData.replace(p.getName() + "_" + item, sackItem - amount);
        }
    }
}
