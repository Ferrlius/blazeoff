package ferrlius.blazeoff;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.entity.EntityType;

public class ModEntities {

    public static final EntityType<FoamEntity> FOAM = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(BlazeOff.MOD_ID, "foam"),
            EntityType.Builder
                    .<FoamEntity>create(FoamEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.25F, 0.25F)
                    .maxTrackingRange(64)
                    .trackingTickInterval(10)
                    .build("foam")
    );

    public static final EntityType<ManualFoamEntity> MANUAL_FOAM_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(BlazeOff.MOD_ID, "manual_foam"),
            EntityType.Builder
                    .<ManualFoamEntity>create(ManualFoamEntity::new, SpawnGroup.MISC)
                    .setDimensions(0.25F, 0.25F)
                    .maxTrackingRange(64)
                    .trackingTickInterval(10)
                    .build("manual_foam")
    );

    public static void register() {
        // If needed, do other entity registrations here
    }
}