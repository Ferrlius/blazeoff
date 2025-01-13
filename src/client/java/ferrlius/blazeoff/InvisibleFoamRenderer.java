package ferrlius.blazeoff;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class InvisibleFoamRenderer extends EntityRenderer<ManualFoamEntity> {

    public InvisibleFoamRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(ManualFoamEntity entity, float yaw, float tickDelta,
                       net.minecraft.client.util.math.MatrixStack matrices,
                       net.minecraft.client.render.VertexConsumerProvider vertexConsumers,
                       int light) {
        // Do nothing => invisible
    }

    @Override
    public Identifier getTexture(ManualFoamEntity entity) {
        // Return anything or a blank fallback
        return new Identifier("minecraft", "textures/block/white_concrete.png");
    }
}