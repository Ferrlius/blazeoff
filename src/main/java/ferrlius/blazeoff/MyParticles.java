package ferrlius.blazeoff;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static ferrlius.blazeoff.BlazeOff.MOD_ID;

public class MyParticles {
    public static final DefaultParticleType RANDOM_SCATTER_PARTICLE = FabricParticleTypes.simple();
    public static final DefaultParticleType STICKY_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(
                Registries.PARTICLE_TYPE,
                new Identifier(MOD_ID, "random_scatter"),
                RANDOM_SCATTER_PARTICLE
        );
        Registry.register(
                Registries.PARTICLE_TYPE,
                new Identifier(MOD_ID, "sticky_particle"),
                STICKY_PARTICLE
        );
    }
}