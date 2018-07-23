package net.sleeplessdev.spigot;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Plugin(name = "fastgrasspaths", version = "@version@")
@Author("InsomniaKitten")
@ApiVersion(ApiVersion.Target.v1_13)
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
