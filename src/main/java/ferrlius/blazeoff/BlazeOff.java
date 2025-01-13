package ferrlius.blazeoff;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Мод на огнетушитель Fabric 1.20.1
public class BlazeOff implements ModInitializer {

	public static final String MOD_ID = "blazeoff";
	private static final Logger LOGGER = LoggerFactory.getLogger("BlazeOff");

	// Блок огнетушителя
	public static final Block FIRE_EXTINGUISHER_BLOCK = new FireExtinguisherBlock(FabricBlockSettings.create()
			.mapColor(MapColor.TEAL).strength(0.1f).sounds(BlockSoundGroup.METAL).nonOpaque());

	// Огнетушитель
	public static final Item FIRE_EXTINGUISHER = new FireExtinguisherItem(
			FIRE_EXTINGUISHER_BLOCK,
			new Item.Settings().maxCount(1)
	);

	@Override
	public void onInitialize() {
		LOGGER.info("THE FIRE IS SPREADING");

		ModItems.registerItems();
		ModEntities.register();
		MyParticles.registerParticles();

		// Регистрация огнетушителя
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "fire_extinguisher"), FIRE_EXTINGUISHER);

		// Регистрация блока огнетушителя
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "fire_extinguisher_block"), FIRE_EXTINGUISHER_BLOCK);

		// Регистрация группы предметов
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
			content.add(FIRE_EXTINGUISHER);
		});
	}
}
