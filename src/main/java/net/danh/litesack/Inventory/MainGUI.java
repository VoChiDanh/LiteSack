package net.danh.litesack.Inventory;

import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.danh.litesack.API.Data.Player.PlayerData;
import net.danh.litesack.API.Utils.File;
import net.xconfig.bukkit.TextUtils;
import org.browsit.milkgui.gui.GUI;
import org.browsit.milkgui.gui.type.BasicGUI;
import org.browsit.milkgui.item.Item;
import org.browsit.milkgui.util.Rows;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainGUI extends BasicGUI {

    public MainGUI() {
        super(new GUI(File.getMainGUI().getString("title"), Rows.fromInt(File.getMainGUI().getInt("size"))));
        FileConfiguration file = File.getMainGUI();
        for (String item : Objects.requireNonNull(file.getConfigurationSection("items")).getKeys(false)) {
            List<Integer> slots = new ArrayList<>();
            if (file.contains("items." + item + ".slots")) {
                slots = file.getIntegerList("items." + item + ".slots");
            } else if (file.contains("items." + item + ".slot")) {
                slots = Collections.singletonList(file.getInt("items." + item + ".slot"));
            }
            String materialTypeS = file.getString("items." + item + ".material_type");
            Item item_builder = null;
            String materialS = file.getString("items." + item + ".material");
            if (materialTypeS != null && materialTypeS.equalsIgnoreCase("VANILLA")) {
                if (materialS != null) {
                    Material material = Material.getMaterial(materialS);
                    int amount = file.getInt("items." + item + ".amount");
                    String displayName = file.getString("items." + item + ".display");
                    List<String> lore = file.getStringList("items." + item + ".lore");
                    if (material != null && displayName != null && !lore.isEmpty()) {
                        item_builder = new Item.ItemBuilder(material).amount(amount).displayName(TextUtils.colorize(displayName)).lore(TextUtils.colorize(lore)).build().addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
                    }
                }
            } else if (materialTypeS != null && materialTypeS.equalsIgnoreCase("MMOITEMS")) {
                if (materialS != null) {
                    String[] materialSS = materialS.split(";");
                    MMOItem mmoitem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(materialSS[0]), materialSS[1]);
                    if (mmoitem != null) {
                        ItemStack mmo = mmoitem.newBuilder().build();
                        if (mmo != null) {
                            item_builder = new Item(mmo);
                        }
                    }
                }
            }
            for (int slot : slots) {
                setItem(slot, item_builder);
                addResponse(slot, e -> {
                    if (e.getWhoClicked() instanceof Player) {
                        e.setCancelled(true);
                        Player p = (Player) e.getWhoClicked();
                        String taskD = file.getString("items." + item + ".action." + e.getClick().name());
                        if (taskD != null) {
                            if (taskD.contains(";")) {
                                String[] taskS = taskD.split(";");
                                if (taskS.length == 5) {
                                    String do_type = taskS[0];
                                    String sackID = taskS[1];
                                    String item_type = taskS[2];
                                    String item_data = taskS[3];
                                    String item_amount = taskS[4];
                                    if (do_type.equalsIgnoreCase("WITHDRAW")) {
                                        PlayerData.removeSackData(p, sackID, item_type + ";" + item_data, String.valueOf(PlayerData.getAmount(p, item_type + ";" + item_data, item_amount)));
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }
    }
}
