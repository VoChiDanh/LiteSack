package net.danh.litesack.API.WorldGuard;

import net.danh.litesack.API.Utils.Chat;
import net.danh.litesack.LiteSack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.Plugin;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.flag.IWrappedFlag;
import org.codemc.worldguardwrapper.flag.WrappedState;

import java.util.Optional;
import java.util.logging.Level;

public class LSWGuard {

    public static void register(final LiteSack plugin) {
        Plugin worldGuard = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (worldGuard != null) {
            WorldGuardWrapper wrapper = WorldGuardWrapper.getInstance();
            Optional<IWrappedFlag<WrappedState>> miningFlag = wrapper.registerFlag("ls-mining", WrappedState.class, WrappedState.DENY);
            miningFlag.ifPresent(wrappedStateIWrappedFlag -> plugin.getLogger().log(Level.INFO, "Registered flag " + wrappedStateIWrappedFlag.getName()));
        }
    }

    public static boolean handleForLocation(Player player, Location loc, Cancellable e, IWrappedFlag<WrappedState> flag) {
        if (flag == null) {
            return true;
        }

        WrappedState state = WorldGuardWrapper.getInstance().queryFlag(player, loc, flag).orElse(WrappedState.DENY);
        if (state == WrappedState.DENY) {
            e.setCancelled(true);
            Chat.debug(player, "Cancel Reason: WorldGuard");
            return false;
        }
        return true;
    }

    public static IWrappedFlag<WrappedState> getStateFlag(String flagName) {
        Optional<IWrappedFlag<WrappedState>> flagOptional = WorldGuardWrapper.getInstance().getFlag(flagName, WrappedState.class);
        if (!flagOptional.isPresent()) {
            Chat.debug(Bukkit.getConsoleSender(), "Failed to get WorldGuard state flag '" + flagName + "'.");
            Chat.debug(Bukkit.getConsoleSender(), "WorldGuard state flag '" + flagName + "' is not present!");
            return null;
        }
        return flagOptional.get();
    }
}
