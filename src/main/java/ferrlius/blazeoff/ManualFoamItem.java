package ferrlius.blazeoff;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.world.World;

/**
 * A simple item that spawns a ManualFoamEntity on right-click.
 */
public class ManualFoamItem extends Item {

    public ManualFoamItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Only do entity spawn logic on server side
        if (!world.isClient) {
            // 1) Create a new ManualFoamEntity
            ManualFoamEntity foam = new ManualFoamEntity(world);

            // 2) Calculate direction from player's pitch & yaw
            float pitchRad = (float) Math.toRadians(user.getPitch());
            float yawRad   = (float) Math.toRadians(user.getYaw());

            // Basic forward vector
            double vx = -Math.sin(yawRad) * Math.cos(pitchRad);
            double vy = -Math.sin(pitchRad);
            double vz =  Math.cos(yawRad) * Math.cos(pitchRad);

            // 3) Offset spawn position so it doesn't collide with player
            double offset = 0.2;
            double spawnX = user.getX() + vx * offset;
            double spawnY = user.getEyeY() - 0.1 + vy * offset;
            double spawnZ = user.getZ() + vz * offset;
            foam.updatePosition(spawnX, spawnY, spawnZ);

            // 4) Apply velocity
            float speed = 1.0F;
            foam.setVelocity(vx * speed, vy * speed, vz * speed);

            // 5) Actually add it to the world
            world.spawnEntity(foam);

            // (Optional) small throw sound
            world.playSound(
                    null,
                    user.getBlockPos(),
                    SoundEvents.ENTITY_SNOWBALL_THROW,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F
            );
        }

        // Return success so we don't consume the item
        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }
}//meow