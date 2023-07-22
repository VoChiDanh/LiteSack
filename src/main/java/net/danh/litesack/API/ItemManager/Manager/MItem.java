package net.danh.litesack.API.ItemManager.Manager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class MItem {

    private final String ItemType;

    protected MItem(String itemType) {
        ItemType = itemType.toUpperCase();
    }

    public String getItemType() {
        return ItemType;
    }

    public void register() {
        IManager.addMItem(getItemType(), this);
    }

    public boolean checkMaterial(String item_data) {
        return false;
    }

    public ItemStack getItemStack(String item_data, Integer amount) {
        Material material = Material.getMaterial(item_data);
        return new ItemStack(material != null ? material : Material.STONE, amount);
    }

    public String getItemName(String item_data) {
        String name = getItemStack(item_data, 1).toString();
        return name != null ? name : "null";
    }

    public boolean compareItems(ItemStack origin, String item_data) {
        return false;
    }

    public MItem getMItem() {
        return this;
    }
}
