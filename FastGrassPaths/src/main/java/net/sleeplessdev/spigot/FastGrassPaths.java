package net.sleeplessdev.spigot;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class FastGrassPaths extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        if (Material.GRASS_PATH == event.getTo().getBlock().getType()) {
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 0, false, false));
        }
    }
}
