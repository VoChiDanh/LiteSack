package net.danh.litesack.API.ItemManager.MMOItems;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.danh.litesack.API.ItemManager.Manager.MItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MMOItems extends MItem {
    public MMOItems() {
        super("MMOITEMS");
    }

    @Override
    public boolean checkMaterial(@NotNull String item_data) {
        String[] mmoitems = item_data.split("-");
        String type = mmoitems[0];
        String id = mmoitems[1];
        return net.Indyuce.mmoitems.MMOItems.plugin.getItem(type, id) != null;
    }

    @Override
    public ItemStack getItemStack(@NotNull String item_data, Integer amount) {
        String[] mmoitems = item_data.split("-");
        String type = mmoitems[0];
        String id = mmoitems[1];
        ItemStack itemStack = net.Indyuce.mmoitems.MMOItems.plugin.getItem(type, id);
        return itemStack != null ? itemStack : new ItemStack(Material.STONE);
    }

    @Override
    public boolean compareItems(ItemStack origin, @NotNull String item_data) {
        String[] mmoitems = item_data.split("-");
        String type = mmoitems[0];
        String id = mmoitems[1];
        NBTItem nbtItem = NBTItem.get(origin);
        return nbtItem != null && nbtItem.getType().equalsIgnoreCase(type) && nbtItem.getString("MMOITEMS_ITEM_ID").equalsIgnoreCase(id);
    }
}
