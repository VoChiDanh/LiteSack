package net.danh.litesack.API.Utils;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager {

    private static final Map<Location, Integer> cooldowns = new HashMap<>();


    public static void setCooldown(Location location, int time) {
        if (time < 1) {
            cooldowns.remove(location);
        } else {
            cooldowns.put(location, time);
        }
    }

    public static int getCooldown(Location location) {
        return cooldowns.getOrDefault(location, 0);
    }
}
