package ferrlius.blazeoff;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.world.World;

// meow
public class FireExtinguisherA extends Item {

    public FireExtinguisherA(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Only do entity spawn logic on server side

        user.setCurrentHand(hand);
        // Return success so we don't consume the item
        return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        // 72k ticks is the bow default, basically indefinite hold
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        // Bow action => pulling animation (optional)
        return UseAction.BOW;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) {
            return;
        }

        // 1) Client side: spawn your muzzle particles
        if (world.isClient) {
            spawnScatterParticles(world, player);
        }

        // 2) Server side: spawn your foam projectile stream
        if (!world.isClient) {
            // For instance, spawn 1 foam projectile every 5 ticks
            if (world.getTime() % 5 == 0) {
                spawnFoamProjectile(world, player);
            }
        }
    }

    private void spawnScatterParticles(World world, PlayerEntity player) {
        // same code as above snippet
        float pitchRad = (float)Math.toRadians(player.getPitch());
        float yawRad   = (float)Math.toRadians(player.getYaw());

        double dx = -Math.sin(yawRad) * Math.cos(pitchRad);
        double dy = -Math.sin(pitchRad);
        double dz =  Math.cos(yawRad) * Math.cos(pitchRad);

        double startX = player.getX();
        double startY = player.getEyeY();
        double startZ = player.getZ();

        startY -= 0.4;

        double rightYaw = yawRad + Math.PI / 2;
        double shiftRight = 0.2;
        double rightX = Math.cos(rightYaw);
        double rightZ = Math.sin(rightYaw);

        startX += rightX * shiftRight;
        startZ += rightZ * shiftRight;

        int count = 3;
        float speed = 0.6F;
        double spread = 0.4D;

        for (int i = 0; i < count; i++) {
            double vx = dx + (player.getRandom().nextDouble() - 0.5) * spread;
            double vy = dy + (player.getRandom().nextDouble() - 0.5) * spread;
            double vz = dz + (player.getRandom().nextDouble() - 0.5) * spread;

            world.addParticle(
                    // your custom type
                    MyParticles.STICKY_PARTICLE,

                    // position
                    startX,
                    startY,
                    startZ,

                    // velocity
                    vx * speed,
                    vy * speed,
                    vz * speed
            );
        }
    }

    private void spawnFoamProjectile(World world, PlayerEntity user) {
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
            float speed = 0.6F;
            foam.setVelocity(vx * speed, vy * speed, vz * speed);

            // 5) Actually add it to the world
            world.spawnEntity(foam);

            // (Optional) small throw sound
            world.playSound(
                    null,
                    user.getBlockPos(),
                    SoundEvents.ITEM_BUCKET_FILL_POWDER_SNOW,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F
            );
        }
    }
}

