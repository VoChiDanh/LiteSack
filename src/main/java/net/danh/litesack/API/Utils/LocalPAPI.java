package net.danh.litesack.API.Utils;

import net.danh.litesack.API.Data.Sack.SackData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class LocalPAPI {

    @NotNull
    public static List<String> parse(@NotNull Player p, @NotNull List<String> placeholder) {
        return placeholder.stream().map(s -> parse(p, s)).collect(Collectors.toList());
    }

    @NotNull
    public static String parse(@NotNull Player p, @NotNull String args) {
        AtomicReference<String> atomic = new AtomicReference<>(args);
        if (args.contains("<") && args.contains(">")) {
            if (args.contains("<storage_sack_")) {
                SackData.sName.keySet().forEach(sackID -> {
                    String v = "<storage_sack_" + sackID + ">";
                    if (args.contains(v)) {
                        atomic.set(args.replace(v, String.valueOf(SackData.getStorageSack(p, sackID))));
                    }
                });
            }
            if (args.contains("<storage_sacks_")) {
                SackData.sName.keySet().forEach(sackID -> {
                    String v = "<storage_sacks_" + sackID + ">";
                    if (args.contains(v)) {
                        atomic.set(args.replace(v, String.valueOf(SackData.getStorageSack(p, sackID) * SackData.getItemList(sackID).size())));
                    }
                });
            }
            if (args.contains("<storage_item_")) {
                SackData.sName.keySet().forEach(sackID -> {
                    SackData.sItems.get(sackID).forEach(item -> {
                        String v = "<storage_item_" + item + ">";
                        if (args.contains(v)) {
                            atomic.set(args.replace(v, String.valueOf(SackData.getSackItem(p, item))));
                        }
                    });
                });
            }
            if (args.contains("<format_storage_sack_")) {
                SackData.sName.keySet().forEach(sackID -> {
                    String v = "<format_storage_sack_" + sackID + ">";
                    if (args.contains(v)) {
                        atomic.set(args.replace(v, Number.settingFormat(SackData.getStorageSack(p, sackID))));
                    }
                });
            }
            if (args.contains("<format_storage_sacks_")) {
                SackData.sName.keySet().forEach(sackID -> {
                    String v = "<format_storage_sacks_" + sackID + ">";
                    if (args.contains(v)) {
                        atomic.set(args.replace(v, Number.settingFormat(SackData.getStorageSack(p, sackID) * SackData.getItemList(sackID).size())));
                    }
                });
            }
            if (args.contains("<format_storage_item_")) {
                SackData.sName.keySet().forEach(sackID -> {
                    SackData.sItems.get(sackID).forEach(item -> {
                        String v = "<format_storage_item_" + item + ">";
                        if (args.contains(v)) {
                            atomic.set(args.replace(v, Number.settingFormat(SackData.getSackItem(p, item))));
                        }
                    });
                });
            }
        }
        return atomic.get();
    }
}
