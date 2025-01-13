package ferrlius.blazeoff;

import ferrlius.blazeoff.BlazeOff;
import ferrlius.blazeoff.FireExtinguisherA;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item FIRE_EXTINGUISHER = new FireExtinguisherA(new Item.Settings().maxCount(1));
    public static final Item MANUAL_FOAM_ITEM = new ManualFoamItem(new Item.Settings().maxCount(1));

    public static void registerItems() {
        Registry.register(Registries.ITEM, new Identifier(BlazeOff.MOD_ID, "fire_extinguisher_a"), FIRE_EXTINGUISHER);
        Registry.register(Registries.ITEM, new Identifier(BlazeOff.MOD_ID, "manual_foam_item"), MANUAL_FOAM_ITEM);
    }
}