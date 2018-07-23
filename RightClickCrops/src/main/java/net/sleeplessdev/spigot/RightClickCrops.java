package net.sleeplessdev.spigot;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

@Plugin(name = "rightclickcrops", version = "@version@")
@Author("InsomniaKitten")
@ApiVersion(ApiVersion.Target.v1_13)
public final class RightClickCrops extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (Action.RIGHT_CLICK_BLOCK != event.getAction()) return;
        if (!event.hasBlock() || event.hasItem()) return;

        final Block block = event.getClickedBlock();

        if (!(block.getBlockData() instanceof Ageable)) return;

        final Ageable ageable = (Ageable) block.getBlockData();

        if (ageable.getAge() == ageable.getMaximumAge()) {
            final Location location = block.getLocation().clone().add(0.5, 0.5, 0.5);
            final Player player = event.getPlayer();

            block.getDrops().forEach(drop -> player.getWorld().dropItemNaturally(location, drop));
            player.getWorld().playSound(location, Sound.BLOCK_GRASS_HIT, 0.5F, 1.0F);
            player.setExhaustion(player.getExhaustion() - 0.005F);
            ageable.setAge(0);
            block.setBlockData(ageable);
        }
    }
}
