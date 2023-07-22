package net.danh.litesack.API.Data.Player;

import net.danh.litesack.API.Data.Sack.SackData;
import net.danh.litesack.API.ItemManager.Manager.IManager;
import net.danh.litesack.API.Resources.Chat;
import net.danh.litesack.API.Resources.File;
import net.danh.litesack.API.Utils.Number;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerData {

    public static HashMap<Player, Boolean> bypass = new HashMap<>();

    public static boolean canBreak(Material itemStack) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        SackData.getSackList().forEach(sackID -> SackData.getItemList(sackID).forEach(item -> SackData.sItemFrom.get(sackID + "_" + item).forEach(from -> {
            String[] fromData = from.split(";");
            String fromType = fromData[0];
            String fromMaterial = fromData[1];
            if (IManager.getMItem(fromType).checkMaterial(fromMaterial)) {
                if (IManager.getMItem(fromType).compareItems(new ItemStack(itemStack), fromMaterial)) {
                    atomicBoolean.set(true);
                }
            }
        })));
        return atomicBoolean.get();
    }

    public static int getRegenTime(Material itemStack) {
        AtomicInteger added = new AtomicInteger(5);
        SackData.getSackList().forEach(sackID -> SackData.getItemList(sackID).forEach(item -> SackData.sItemFrom.get(sackID + "_" + item).forEach(from -> {
            String[] fromData = from.split(";");
            String fromType = fromData[0];
            String fromMaterial = fromData[1];
            String regenTime = fromData[3];
            if (IManager.getMItem(fromType).checkMaterial(fromMaterial)) {
                if (IManager.getMItem(fromType).compareItems(new ItemStack(itemStack), fromMaterial)) {
                    if (regenTime != null) {
                        added.set(Number.getInteger(regenTime));
                    }
                }
            }
        })));
        return added.get();
    }

    public static boolean addSackData(Player p, ItemStack itemStack) {
        AtomicBoolean added = new AtomicBoolean(false);
        SackData.getSackList().forEach(sackID -> SackData.getItemList(sackID).forEach(item -> SackData.sItemFrom.get(sackID + "_" + item).forEach(from -> {
            String[] fromData = from.split(";");
            String fromType = fromData[0];
            String fromMaterial = fromData[1];
            String fromAmount = fromData[2];
            if (IManager.getMItem(fromType).checkMaterial(fromMaterial)) {
                if (IManager.getMItem(fromType).compareItems(IManager.getMItem(fromType).getItemStack(fromMaterial, 1), fromMaterial)) {
                    added.set(PlayerData.increaseSackData(p, sackID, item, String.valueOf(itemStack.getAmount() * Number.getInteger(fromAmount))));
                } else {
                    added.set(false);
                }
            } else {
                added.set(false);
            }
        })));
        return added.get();
    }

    public static int getAmount(Player p, String item, String number) {
        try {
            return BigDecimal.valueOf(Long.parseLong(number)).intValue();
        } catch (NumberFormatException | NullPointerException e) {
            return SackData.pSackData.get(p.getName() + "_" + item);
        }
    }

    public static boolean removeSackData(Player p, String sackID, String item, String amount) {
        AtomicBoolean removed = new AtomicBoolean(false);
        SackData.getSackList().forEach(sID -> {
            if (sID.equalsIgnoreCase(sackID)) {
                SackData.getItemList(sID).forEach(sItem -> {
                    if (sItem.equalsIgnoreCase(item)) {
                        String[] items_split = item.split(";");
                        String item_type = items_split[0];
                        String item_data = items_split[1];
                        Integer number = Number.getInteger(amount);
                        ItemStack itemStack = IManager.getMItem(item_type).getItemStack(item_data, number);
                        if (number <= 0) return;
                        if (IManager.getMItem(item_type).checkMaterial(item_data)) {
                            if (decreaseSackData(p, sackID, item, String.valueOf(number))) {
                                p.getInventory().addItem(itemStack);
                                removed.set(true);
                                Chat.sendPlayerMessage(p, Objects.requireNonNull(File.getMessage().getString("COMMAND.WITHDRAW.WITHDRAW_SUCCESS")).replace("<name>", IManager.getMItem(item_type).getItemName(item_data)).replace("<amount>", String.valueOf(number)));
                            } else {
                                Chat.sendPlayerMessage(p, Objects.requireNonNull(File.getMessage().getString("COMMAND.WITHDRAW.NOT_ENOUGH")).replace("<name>", IManager.getMItem(item_type).getItemName(item_data)).replace("<withdraw>", String.valueOf(number)).replace("<amount>", String.valueOf(SackData.pSackData.get(p.getName() + "_" + item))));
                            }
                        } else {
                            Chat.sendPlayerMessage(p, Objects.requireNonNull(File.getMessage().getString("COMMAND.WITHDRAW.MATERIAL_IS_NULL")).replace("<name>", item_data));
                        }
                    }
                });
            }
        });
        return removed.get();
    }

    public static boolean increaseSackData(Player p, String sackID, String item, String amount) {
        int increase = SackData.pSackData.get(p.getName() + "_" + item) + Number.getInteger(amount);
        if (SackData.pSackData.get(p.getName() + "_" + sackID) >= increase) {
            SackData.pSackData.replace(p.getName() + "_" + item, increase);
            return true;
        }
        return false;
    }

    public static boolean decreaseSackData(Player p, String sackID, String item, String amount) {
        int increase = SackData.pSackData.get(p.getName() + "_" + item) - Number.getInteger(amount);
        if (SackData.pSackData.get(p.getName() + "_" + sackID) >= increase && increase >= 0) {
            SackData.pSackData.replace(p.getName() + "_" + item, increase);
            return true;
        }
        return false;
    }

    public static void setSackStorageData(Player p, String sackID, String amount) {
        SackData.pSackData.replace(p.getName() + "_" + sackID, Number.getInteger(amount));
    }
}
