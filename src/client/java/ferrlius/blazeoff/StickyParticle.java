package ferrlius.blazeoff;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ExplosionSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class StickyParticle extends ExplosionSmokeParticle {
    private boolean stuck = false;

    /**
     * Main constructor for your StickyParticle, copying logic from ExplosionSmokeParticle
     * but adding sticky behavior.
     */
    protected StickyParticle(
            ClientWorld world,
            double x, double y, double z,
            double vx, double vy, double vz,
            SpriteProvider spriteProvider
    ) {
        super(world, x, y, z, vx, vy, vz, spriteProvider);
        this.gravityStrength = 0.5F;
        this.collidesWithWorld = true; // Allows collision checks
    }

    @Override
    public void tick() {
        super.tick();

        // If already stuck, zero velocity & skip further movement
        if (this.stuck) {
            this.velocityX = 0;
            this.velocityY = 0;
            this.velocityZ = 0;
            return;
        }

        // Example "collision detection" by comparing velocity before & after move:
        double oldVelX = this.velocityX;
        double oldVelY = this.velocityY;
        double oldVelZ = this.velocityZ;

        // Move & let ExplosionSmokeParticle handle the visual smoke effect
        this.move(this.velocityX, this.velocityY, this.velocityZ);

        // If movement was clamped/blocked in any axis, we consider that a collision
        boolean collidedX = (this.velocityX != oldVelX);
        boolean collidedY = (this.velocityY != oldVelY);
        boolean collidedZ = (this.velocityZ != oldVelZ);

        // On collision, become "sticky"
        if (collidedX || collidedY || collidedZ) {
            this.stuck = true;
        }
    }

    /**
     * Factory for creating StickyParticle instances.
     * Register this in your ClientModInitializer with ParticleFactoryRegistry.
     */
    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(
                DefaultParticleType type,
                ClientWorld world,
                double x, double y, double z,
                double vx, double vy, double vz
        ) {
            // Pass everything to the StickyParticle constructor
            return new StickyParticle(world, x, y, z, vx, vy, vz, this.spriteProvider);
        }
    }
}