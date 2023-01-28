package net.danh.litesack.API.Utils;

import net.danh.litesack.LiteSack;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class File {

    public static FileConfiguration getSetting() {
        return LiteSack.getBukkitConfigurationModel().file("", "settings.yml");
    }

    public static FileConfiguration getConfig() {
        return LiteSack.getBukkitConfigurationModel().file("", "config.yml");
    }

    public static FileConfiguration getMessage() {
        return LiteSack.getBukkitConfigurationModel().file("", "message.yml");
    }

    public static FileConfiguration getMainGUI() {
        return LiteSack.getBukkitConfigurationModel().file("GUI", "MainGUI.yml");
    }

    public static void reloadFiles(CommandSender c) {
        LiteSack.getBukkitConfigurationModel().reload("", "settings.yml");
        LiteSack.getBukkitConfigurationModel().reload("", "config.yml");
        LiteSack.getBukkitConfigurationModel().reload("", "message.yml");
        LiteSack.getBukkitConfigurationModel().reload("GUI", "MainGUI.yml");
        Chat.sendCommandSenderMessage(c, "&aReloaded");
    }
}
