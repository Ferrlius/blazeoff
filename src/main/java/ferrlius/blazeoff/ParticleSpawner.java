package ferrlius.blazeoff;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class ParticleSpawner {

    public static void spawnRandomScatterParticle(PlayerEntity player, int count) {
        World world = player.getWorld();
        if (!world.isClient) return; // Ensure we're on client side

        // 1. Get the direction the player is looking (unit vector)
        Vec3d lookVec = player.getRotationVec(1.0F);

        for (int i = 0; i < count; i++) {
            // 2. Spawn position: just in front of the player's eyes
            double spawnX = player.getX() + lookVec.x * 1.0;
            double spawnY = player.getEyeY();
            double spawnZ = player.getZ() + lookVec.z * 1.0;

            // 3. Generate small random offsets (e.g., ±0.2 in each axis)
            double offsetX = (Math.random() - 0.5) * 0.2;
            double offsetY = (Math.random() - 0.5) * 0.2;
            double offsetZ = (Math.random() - 0.5) * 0.2;

            // 4. Add these offsets to the look direction and normalize
            //    so we don't drastically change overall speed.
            Vec3d newDir = new Vec3d(
                    lookVec.x + offsetX,
                    lookVec.y + offsetY,
                    lookVec.z + offsetZ
            ).normalize();

            // 5. Multiply by a base speed, e.g. 0.5
            double speed = 0.5;
            newDir = newDir.multiply(speed);

            // 6. Spawn the particle with the new direction
            world.addParticle(
                    MyParticles.RANDOM_SCATTER_PARTICLE,
                    spawnX, spawnY, spawnZ,
                    newDir.x, newDir.y, newDir.z
            );
        }
    }

}
