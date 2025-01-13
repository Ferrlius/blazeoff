package ferrlius.blazeoff;


import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;


public class RandomScatterParticleFactory implements ParticleFactory<DefaultParticleType> {
    private final SpriteProvider spriteProvider;

    public RandomScatterParticleFactory(SpriteProvider spriteProvider) {
        this.spriteProvider = spriteProvider;
    }

    @Override
    public Particle createParticle(
            DefaultParticleType parameters,
            ClientWorld world,
            double x, double y, double z,
            double velocityX, double velocityY, double velocityZ
    ) {
        // Note that we now pass spriteProvider to the constructor
        return new RandomScatterParticle(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);
    }
}
