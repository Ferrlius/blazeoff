package ferrlius.blazeoff;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

    public class ExtinguisherFoamEntityRenderer
            extends EntityRenderer<FoamEntity> {

        public ExtinguisherFoamEntityRenderer(EntityRendererFactory.Context ctx) {
            super(ctx);
        }


        public void render(FoamEntity entity, float yaw, float tickDelta,
                           MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
            // Do nothing = invisible
        }


        public Identifier getTexture(FoamEntity entity) {
           // Not used, return something or anything
            return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
        }
}