package net.danh.litesack.Inventory;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.other.EventCreator;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem;
import net.danh.litesack.API.Data.Player.PlayerData;
import net.danh.litesack.LiteSack;
import net.xconfig.bukkit.TextUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class LiteGUI {
    public static @NotNull RyseInventory getGUIJob(String job) {
        job = job.toLowerCase();
        FileConfiguration file = LiteSack.getBukkitConfigurationModel().file("GUI", job + ".yml");
        if (file == null) {
            LiteSack.getLiteStack().getLogger().log(Level.INFO, job + " is null");
            return RyseInventory.builder().title(job + " is null - contact to admin").rows(1).provider(new InventoryProvider() {

            }).build(LiteSack.getLiteStack(), LiteSack.getInventoryManager());
        }
        String title = TextUtils.colorize(Objects.requireNonNull(file.getString("title")));
        return RyseInventory.builder().title(title).rows(file.getInt("size")).provider(new InventoryProvider() {
            @Override
            public void init(Player player, InventoryContents contents) {
                for (String item : Objects.requireNonNull(file.getConfigurationSection("items")).getKeys(false)) {
                    List<Integer> slots = new ArrayList<>();
                    if (file.contains("items." + item + ".slots")) {
                        slots = file.getIntegerList("items." + item + ".slots");
                    } else if (file.contains("items." + item + ".slot")) {
                        slots = Collections.singletonList(file.getInt("items." + item + ".slot"));
                    }
                    String materialTypeS = file.getString("items." + item + ".material_type");
                    ItemStack item_builder = null;
                    String materialS = file.getString("items." + item + ".material");
                    if (materialTypeS != null && materialTypeS.equalsIgnoreCase("VANILLA")) {
                        if (materialS != null) {
                            Material material = Material.getMaterial(materialS);
                            int amount = file.getInt("items." + item + ".amount");
                            String displayName = file.getString("items." + item + ".display");
                            List<String> lore = file.getStringList("items." + item + ".lore");
                            if (material != null && displayName != null && !lore.isEmpty()) {
                                item_builder = new ItemBuilder(material).amount(amount).displayName(TextUtils.colorize(displayName)).lore(TextUtils.colorize(lore)).flags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE).build();
                            }
                        }
                    } else if (materialTypeS != null && materialTypeS.equalsIgnoreCase("MMOITEMS")) {
                        if (materialS != null) {
                            String[] materialSS = materialS.split(";");
                            MMOItem mmoitem = MMOItems.plugin.getMMOItem(MMOItems.plugin.getTypes().get(materialSS[0]), materialSS[1]);
                            if (mmoitem != null) {
                                ItemStack mmo = mmoitem.newBuilder().build();
                                if (mmo != null) {
                                    item_builder = mmo;
                                }
                            }
                        }
                    }
                    for (int slot : slots) {
                        contents.set(slot, item_builder != null ? item_builder : new ItemStack(Material.STONE));
                    }
                }
            }
        }).listener(new EventCreator<>(InventoryClickEvent.class, e -> {
            for (String item : Objects.requireNonNull(file.getConfigurationSection("items")).getKeys(false)) {
                List<Integer> slots = new ArrayList<>();
                if (file.contains("items." + item + ".slots")) {
                    slots = file.getIntegerList("items." + item + ".slots");
                } else if (file.contains("items." + item + ".slot")) {
                    slots = Collections.singletonList(file.getInt("items." + item + ".slot"));
                }
                for (int slot : slots) {
                    if (e.getWhoClicked() instanceof Player) {
                        e.setCancelled(true);
                        if (e.getSlot() == slot) {
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
                    }
                }
            }
        })).build(LiteSack.getLiteStack(), LiteSack.getInventoryManager());
    }
}
