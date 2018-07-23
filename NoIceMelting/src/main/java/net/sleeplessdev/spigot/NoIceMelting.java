package net.sleeplessdev.spigot;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class NoIceMelting extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBlockFade(final BlockFadeEvent event) {
        if (Material.ICE == event.getBlock().getType()) {
            event.setCancelled(true);
        }
    }
}
