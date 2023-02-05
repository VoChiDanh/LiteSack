package net.danh.litesack.PlaceholderAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.danh.litesack.API.Data.Sack.SackData;
import net.danh.litesack.API.Utils.Number;
import net.danh.litesack.LiteSack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LitePAPI extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "ls";
    }

    @Override
    public @NotNull String getAuthor() {
        return LiteSack.getLiteStack().getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return LiteSack.getLiteStack().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player p, @NotNull String args) {
        if (p == null) return null;
        if (args.startsWith("storage_sack_")) {
            String sackID = args.substring(13);
            return String.valueOf(SackData.getStorageSack(p, sackID));
        }
        if (args.startsWith("storage_sacks_")) {
            String sack = args.substring(14);
            return String.valueOf(SackData.getStorageSack(p, sack) * SackData.getItemList(sack).size());
        }
        if (args.startsWith("storage_item_")) {
            String item = args.substring(13);
            return String.valueOf(SackData.getSackItem(p, item));
        }
        if (args.startsWith("format_storage_sack_")) {
            String sackID = args.substring(20);
            return Number.settingFormat(SackData.getStorageSack(p, sackID));
        }
        if (args.startsWith("format_storage_sacks_")) {
            String sack = args.substring(21);
            return Number.settingFormat(SackData.getStorageSack(p, sack) * SackData.getItemList(sack).size());
        }
        if (args.startsWith("format_storage_item_")) {
            String item = args.substring(20);
            return Number.settingFormat(SackData.getSackItem(p, item));
        }
        return null;
    }
}
