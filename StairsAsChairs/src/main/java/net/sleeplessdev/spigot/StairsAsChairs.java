package net.sleeplessdev.spigot;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import static org.bukkit.potion.PotionEffectType.DAMAGE_RESISTANCE;
import static org.bukkit.potion.PotionEffectType.INVISIBILITY;

@Plugin(name = "stairs_as_chairs", version = "@version@")
@Author("InsomniaKitten")
@ApiVersion(ApiVersion.Target.v1_13)
public final class StairsAsChairs extends JavaPlugin implements Listener {
    private static final String CHAIR_ENTITY_ID = "stairsaschairs:chair_entity";
    private static final int MAX_EFFECT_DURATION = 1000000;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Listener) this);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (!event.hasBlock() || event.hasItem()) return;
        if (!(event.getClickedBlock().getBlockData() instanceof Stairs)) return;

        final Action action = event.getAction();
        final World world = event.getPlayer().getWorld();
        final Location location = event.getClickedBlock().getLocation();

        if (Action.RIGHT_CLICK_BLOCK == action) {
            if (!event.getClickedBlock().getRelative(BlockFace.UP).isEmpty()) return;

            final Stairs stairs = (Stairs) event.getClickedBlock().getBlockData();
            final BlockFace face = event.getBlockFace();
            final BlockFace facing = stairs.getFacing();

            if (face == BlockFace.DOWN || face == facing) return;
            if (stairs.getHalf() != Bisected.Half.BOTTOM) return;

            final BlockFace opposite = facing.getOppositeFace();

            final double locX = location.getBlockX() + 0.5;
            final double locY = location.getBlockY() - 0.45;
            final double locZ = location.getBlockZ() + 0.5;
            final double dirX = opposite.getModX();
            final double dirY = opposite.getModY();
            final double dirZ = opposite.getModZ();

            final Location target = new Location(world, locX, locY, locZ);

            target.setDirection(new Vector(dirX, dirY, dirZ));
            world.spawn(target, Pig.class, e -> {
                e.setCollidable(false);
                e.setAdult();
                e.setAI(false);
                e.setGravity(false);
                e.setInvulnerable(true);
                e.setSilent(true);
                e.setCustomName(CHAIR_ENTITY_ID);
                e.setCustomNameVisible(false);
                e.addPotionEffect(new PotionEffect(INVISIBILITY, MAX_EFFECT_DURATION, 0, false, false));
                // TODO Remove if ineffective? Goal is to prevent other creative players from damaging chairs
                e.addPotionEffect(new PotionEffect(DAMAGE_RESISTANCE, MAX_EFFECT_DURATION, 5, false, false));
                e.addPassenger(event.getPlayer());
            });
        } else if (Action.LEFT_CLICK_BLOCK == action) {
            world.getNearbyEntities(location, 1.0, 1.0, 1.0).stream()
                .filter(e -> CHAIR_ENTITY_ID.equals(e.getCustomName()))
                .filter(e -> e.getPassengers().contains(event.getPlayer()))
                .findFirst().ifPresent(Entity::remove);
        }
    }

    @EventHandler
    public void onVehicleExit(final VehicleExitEvent event) {
        if (CHAIR_ENTITY_ID.equals(event.getVehicle().getCustomName())) {
            // TODO Move passenger upwards to avoid sinking into block
            event.getVehicle().remove();
        }
    }
}
