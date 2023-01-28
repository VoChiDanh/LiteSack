package net.danh.litesack.API.Data.Player;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.danh.litesack.API.Data.Sack.SackData;
import net.danh.litesack.API.Utils.Chat;
import net.danh.litesack.API.Utils.File;
import net.danh.litesack.API.Utils.Number;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerData {

    public static HashMap<Player, Boolean> bypass = new HashMap<>();
    public static boolean checkTool(Player p, String sackID, boolean bypass) {
        AtomicBoolean atomic = new AtomicBoolean(false);
        if (!bypass) {
            ItemStack itemStack = p.getInventory().getItemInMainHand();
            List<String> tools = File.getSetting().getStringList("TOOLS." + sackID.toUpperCase());
            tools.forEach(tool -> {
                String[] tool_split = tool.split(";");
                if (tool_split[0].equalsIgnoreCase("MMOITEMS")) {
                    NBTItem nbtItem = NBTItem.get(itemStack);
                    if (nbtItem == null) {
                        atomic.set(false);
                        return;
                    }
                    if (!nbtItem.hasType()) {
                        atomic.set(false);
                        return;
                    }
                    if (nbtItem.getString("MMOITEMS_ITEM_ID") == null) {
                        atomic.set(false);
                        return;
                    }
                    String[] tool_2 = tool_split[1].split("-");
                    if (nbtItem.getType().equalsIgnoreCase(tool_2[0])) {
                        atomic.set(nbtItem.getString("MMOITEMS_ITEM_ID").equalsIgnoreCase(tool_2[1]));
                    } else {
                        atomic.set(false);
                    }
                }
            });
        }
        return atomic.get();
    }

    public static boolean canBreak(Player p, Material itemStack) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        SackData.getSackList().forEach(sackID -> SackData.getItemList(sackID).forEach(item -> SackData.sItemFrom.get(sackID + "_" + item).forEach(from -> {
            String[] fromData = from.split(";");
            String fromType = fromData[0];
            String fromMaterial = fromData[1];
            if (fromType.equalsIgnoreCase("VANILLA")) {
                Material material = Material.getMaterial(fromMaterial);
                if (material != null) {
                    if (itemStack.equals(material)) {
                        atomicBoolean.set(checkTool(p, sackID, false));
                    }
                }
            }
        })));
        return atomicBoolean.get();
    }

    public static int getRegenTime(Material itemStack, SackType sackType) {
        AtomicInteger added = new AtomicInteger(5);
        SackData.getSackList().forEach(sackID -> SackData.getItemList(sackID).forEach(item -> SackData.sItemFrom.get(sackID + "_" + item).forEach(from -> {
            String[] fromData = from.split(";");
            String fromType = fromData[0];
            String fromMaterial = fromData[1];
            String regenTime = fromData[3];
            if (sackType.equals(SackType.BLOCK_BREAK)) {
                if (fromType.equalsIgnoreCase("VANILLA")) {
                    Material material = Material.getMaterial(fromMaterial);
                    if (material != null) {
                        if (itemStack.equals(material)) {
                            if (regenTime != null) {
                                added.set(Number.getInteger(regenTime));
                            }
                        }
                    }
                }
            }
        })));
        return added.get();
    }

    public static boolean addSackData(Player p, Material itemStack, SackType sackType) {
        AtomicBoolean added = new AtomicBoolean(false);
        SackData.getSackList().forEach(sackID -> SackData.getItemList(sackID).forEach(item -> SackData.sItemFrom.get(sackID + "_" + item).forEach(from -> {
            String[] fromData = from.split(";");
            String fromType = fromData[0];
            String fromMaterial = fromData[1];
            String fromAmount = fromData[2];
            if (sackType.equals(SackType.BLOCK_BREAK)) {
                if (fromType.equalsIgnoreCase("VANILLA")) {
                    Material material = Material.getMaterial(fromMaterial);
                    if (material != null) {
                        if (itemStack.equals(material)) {
                            ItemStack tool = new ItemStack(p.getInventory().getItemInMainHand());
                            NBTItem nbtItem = NBTItem.get(tool);
                            if (nbtItem != null
                                    && nbtItem.getType() != null
                                    && nbtItem.getString("MMOITEMS_ITEM_ID") != null
                                    && nbtItem.getDouble("MMOITEMS_MULTI") > 0d) {
                                int multi = (int) nbtItem.getDouble("MMOITEMS_MULTI");
                                added.set(PlayerData.increaseSackData(p, sackID, item, String.valueOf((new ItemStack(itemStack).getAmount() * Number.getInteger(fromAmount)) + multi)));
                            } else {
                                added.set(PlayerData.increaseSackData(p, sackID, item, String.valueOf(new ItemStack(itemStack).getAmount() * Number.getInteger(fromAmount))));
                            }
                        }
                    }
                }
            }
            if (sackType.equals(SackType.ITEM_PICKUP)) {
                String[] material_drop = item.split(";");
                String materialFrom = material_drop[0];
                String materialType = material_drop[1];
                if (materialFrom.equalsIgnoreCase("VANILLA")) {
                    Material material = Material.getMaterial(materialType.toUpperCase());
                    if (material != null) {
                        if (itemStack.equals(material)) {
                            added.set(PlayerData.increaseSackData(p, sackID, item, String.valueOf(new ItemStack(itemStack).getAmount())));
                        }
                    }
                }
                if (materialFrom.equalsIgnoreCase("MMOITEMS")) {
                    String[] mmoitems = materialType.split("-");
                    String type = mmoitems[0];
                    String id = mmoitems[1];
                    MMOItem mmoitem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(type), id);
                    if (mmoitem != null) {
                        ItemStack mmo = mmoitem.newBuilder().build();
                        if (mmo != null) {
                            added.set(PlayerData.increaseSackData(p, sackID, item, String.valueOf(mmo.getAmount())));
                        }
                    }
                }
            }
        })));
        return added.get();
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
                        if (item_type.equalsIgnoreCase("VANILLA")) {
                            Material material = Material.getMaterial(item_data);
                            Integer number = Number.getInteger(amount);
                            if (material != null) {
                                if (decreaseSackData(p, sackID, item, String.valueOf(number))) {
                                    p.getInventory().addItem(new ItemStack(material, number));
                                    removed.set(true);
                                    Chat.sendPlayerMessage(p, Objects.requireNonNull(File.getMessage().getString("COMMAND.WITHDRAW.WITHDRAW_SUCCESS")).replace("<name>", item_data).replace("<amount>", String.valueOf(number)));
                                } else {
                                    Chat.sendPlayerMessage(p, Objects.requireNonNull(File.getMessage().getString("COMMAND.WITHDRAW.NOT_ENOUGH")).replace("<name>", item_data).replace("<withdraw>", String.valueOf(number)).replace("<amount>", String.valueOf(SackData.pSackData.get(p.getName() + "_" + item))));
                                }
                            } else {
                                Chat.sendPlayerMessage(p, Objects.requireNonNull(File.getMessage().getString("COMMAND.WITHDRAW.MATERIAL_IS_NULL")).replace("<name>", item_data));
                            }
                        }
                        if (item_type.equalsIgnoreCase("MMOITEMS")) {
                            String[] mmoitems = item_data.split("-");
                            String type = mmoitems[0];
                            String id = mmoitems[1];
                            MMOItem mmoitem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(type), id);
                            if (mmoitem != null) {
                                ItemStack mmo = mmoitem.newBuilder().build();
                                if (mmo != null) {
                                    Integer number = Number.getInteger(amount);
                                    mmo.setAmount(number);
                                    if (decreaseSackData(p, sackID, item, String.valueOf(number))) {
                                        p.getInventory().addItem(mmo);
                                        removed.set(true);
                                        Chat.sendPlayerMessage(p, Objects.requireNonNull(File.getMessage().getString("COMMAND.WITHDRAW.WITHDRAW_SUCCESS")).replace("<name>", item_data).replace("<amount>", String.valueOf(number)));
                                    } else {
                                        Chat.sendPlayerMessage(p, Objects.requireNonNull(File.getMessage().getString("COMMAND.WITHDRAW.NOT_ENOUGH")).replace("<name>", item_data).replace("<withdraw>", String.valueOf(number)).replace("<amount>", String.valueOf(SackData.pSackData.get(p.getName() + "_" + item))));
                                    }
                                } else {
                                    Chat.sendPlayerMessage(p, Objects.requireNonNull(File.getMessage().getString("COMMAND.WITHDRAW.MATERIAL_IS_NULL")).replace("<name>", item_data));
                                }
                            } else {
                                Chat.sendPlayerMessage(p, Objects.requireNonNull(File.getMessage().getString("COMMAND.WITHDRAW.MATERIAL_IS_NULL")).replace("<name>", item_data));
                            }
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
