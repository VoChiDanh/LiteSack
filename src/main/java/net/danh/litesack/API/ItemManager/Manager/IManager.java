package net.danh.litesack.API.ItemManager.Manager;

import net.danh.litesack.API.ItemManager.Vanilla.VItem;
import net.danh.litesack.LiteSack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class IManager {
    private static final HashMap<String, MItem> ima = new HashMap<>();

    public static @NotNull MItem getMItem(String ItemType) {
        MItem mItem = ima.get(ItemType);
        return mItem != null ? mItem : new VItem();
    }

    @Contract(" -> new")
    public static @NotNull List<String> getListItemType() {
        return new ArrayList<>(ima.keySet());
    }

    @Contract(" -> new")
    public static @NotNull List<MItem> getListMItem() {
        return new ArrayList<>(ima.values());
    }

    public static void addMItem(String ItemType, MItem ItemData) {
        if (!ima.containsKey(ItemType)) {
            ima.put(ItemType, ItemData);
            LiteSack.getLiteStack().getLogger().log(Level.INFO, "Registered item type: " + ItemType);
        } else {
            LiteSack.getLiteStack().getLogger().log(Level.INFO, "Failed on register item type " + ItemType);
        }
    }

    public static void removeMItem(String ItemType) {
        if (ima.containsKey(ItemType)) {
            ima.remove(ItemType);
            LiteSack.getLiteStack().getLogger().log(Level.INFO, "Unregister item type " + ItemType);
        } else {
            LiteSack.getLiteStack().getLogger().log(Level.INFO, "Failed on unregister item type " + ItemType);
        }
    }
}
