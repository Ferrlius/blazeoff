package ferrlius.blazeoff;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class FireExtinguisherItemRenderer {
    public static void register() {
        net.minecraft.client.item.ModelPredicateProviderRegistry.register(
                ferrlius.blazeoff.BlazeOff.FIRE_EXTINGUISHER,
                new Identifier("fuel"),
                (stack, world, entity, seed) -> {
                    if (stack.hasNbt() && stack.getNbt().contains("Fuel")) {
                        return stack.getNbt().getInt("Fuel") / 100.0F;
                    }
                    return 0.0F;
                }
        );
    }
}