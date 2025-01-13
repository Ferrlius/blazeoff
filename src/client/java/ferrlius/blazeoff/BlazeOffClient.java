package ferrlius.blazeoff;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.particle.DefaultParticleType;

public class BlazeOffClient implements ClientModInitializer {

	public static final DefaultParticleType RANDOM_SCATTER_PARTICLE = FabricParticleTypes.simple();

	@Override
	public void onInitializeClient() {
		FireExtinguisherItemRenderer.register();
		//blocks
		BlockRenderLayerMap.INSTANCE.putBlock(BlazeOff.FIRE_EXTINGUISHER_BLOCK, RenderLayer.getCutout());

		//particles
		ParticleFactoryRegistry.getInstance().register(MyParticles.RANDOM_SCATTER_PARTICLE,
				spriteProvider -> new RandomScatterParticleFactory(spriteProvider));

		ParticleFactoryRegistry.getInstance().register(MyParticles.STICKY_PARTICLE, StickyParticle.Factory::new);

		//entities
		EntityRendererRegistry.register(ModEntities.FOAM, ExtinguisherFoamEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.MANUAL_FOAM_ENTITY, (ctx) -> new InvisibleFoamRenderer(ctx));
	}
}