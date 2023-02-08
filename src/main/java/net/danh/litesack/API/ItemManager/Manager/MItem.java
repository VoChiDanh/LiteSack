package net.danh.litesack.API.ItemManager.Manager;

import net.danh.litesack.LiteSack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public abstract class MItem {

    private final String ItemType;

    protected MItem(String itemType) {
        ItemType = itemType.toUpperCase();
    }

    public String getItemType() {
        return ItemType;
    }

    public void register() {
        if (IManager.ima.get(getItemType()) == null) {
            IManager.ima.put(getItemType(), this);
            LiteSack.getLiteStack().getLogger().log(Level.INFO, "Registered Item Type " + getItemType());
        } else {
            LiteSack.getLiteStack().getLogger().log(Level.INFO, "Failed On Register Item Type " + getItemType());
        }
    }

    public boolean checkMaterial(String item_data) {
        return false;
    }

    public ItemStack getItemStack(String item_data, Integer amount) {
        Material material = Material.getMaterial(item_data);
        return new ItemStack(material != null ? material : Material.STONE, amount);
    }

    public boolean compareItems(ItemStack origin, String item_data) {
        return false;
    }
}
