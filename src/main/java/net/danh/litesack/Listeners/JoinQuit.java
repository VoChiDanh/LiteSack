package net.danh.litesack.Listeners;

import net.danh.litesack.API.Data.Sack.SackData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuit implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        SackData.loadPlayerData(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        SackData.savePlayerData(e.getPlayer());
    }
}
