package net.danh.litesack.API.ItemManager.Vanilla;

import net.danh.litesack.API.ItemManager.Manager.MItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class VItem extends MItem {
    public VItem() {
        super("VANILLA");
    }

    @Override
    public boolean checkMaterial(String item_data) {
        Material material = Material.getMaterial(item_data);
        return material != null;
    }

    @Override
    public ItemStack getItemStack(String item_data, Integer amount) {
        Material material = Material.getMaterial(item_data);
        return new ItemStack(material != null ? material : Material.STONE, amount);
    }

    @Override
    public boolean compareItems(@NotNull ItemStack origin, String item_data) {
        Material material = Material.getMaterial(item_data);
        ItemStack itemStack = new ItemStack(material != null ? material : Material.STONE);
        return itemStack.getType().equals(origin.getType());
    }
}
