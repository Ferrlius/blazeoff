// FireExtinguisherItem.java
package ferrlius.blazeoff;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FireExtinguisherItem extends Item {
    private final Block blockToPlace;
    private static final String FUEL_KEY = "Fuel";
    private static final int MAX_FUEL = 100;

    public FireExtinguisherItem(Block block, Settings settings) {
        super(settings.maxCount(1));
        this.blockToPlace = block;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        int fuel = getFuel(stack);
        tooltip.add(Text.literal("Fuel: " + fuel + "/" + MAX_FUEL));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        int fuel = getFuel(stack);
        if (fuel <= 0) {

            return TypedActionResult.fail(stack);
        }

        if (!world.isClient) {
            player.sendMessage(Text.literal("Огнетушитель активирован!"), true);
            setFuel(stack, fuel - 1);
        }
        return TypedActionResult.success(stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();

        // Проверка на зажатие Shift
        if (!player.isSneaking()) {
            return ActionResult.PASS; // Игнорировать обычное использование
        }

        BlockPos pos = context.getBlockPos();
        Direction side = context.getSide();
        BlockPos targetPos = pos.offset(side);

        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        BlockState existingState = world.getBlockState(targetPos);
        if (!existingState.isAir() && !existingState.isReplaceable()) {
            return ActionResult.FAIL;
        }

        // Проверка позиции игрока относительно targetPos
        if (player.getBoundingBox().intersects(targetPos.getX(), targetPos.getY(), targetPos.getZ(), targetPos.getX() + 1, targetPos.getY() + 1, targetPos.getZ() + 1)) {
            return ActionResult.FAIL;
        }

        ItemPlacementContext placementContext = new ItemPlacementContext(context);
        BlockState placementState = blockToPlace.getPlacementState(placementContext);

        if (placementState != null && blockToPlace.canPlaceAt(placementState, world, targetPos)) {
            world.setBlockState(targetPos, placementState);
            world.playSound(null, targetPos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);

            if (!player.getAbilities().creativeMode) {
                context.getStack().decrement(1); // Уменьшает количество предметов на 1
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

    public ActionResult onItemUseOnBlock(ItemStack stack, PlayerEntity player, World world, BlockPos pos, Hand hand, Direction hitFace) {
        ItemStack heldItem = player.getStackInHand(hand);
        if (heldItem.isOf(Items.POWDER_SNOW_BUCKET)) {
            int currentFuel = getFuel(stack);
            if (currentFuel < MAX_FUEL) {
                setFuel(stack, MAX_FUEL);
                player.setStackInHand(hand, new ItemStack(Items.BUCKET));

                return ActionResult.SUCCESS;
            } else {

                return ActionResult.FAIL;
            }
        }
        return ActionResult.PASS;
    }

    private int getFuel(ItemStack stack) {
        return stack.getOrCreateNbt().getInt(FUEL_KEY);
    }

    private void setFuel(ItemStack stack, int fuel) {
        stack.getOrCreateNbt().putInt(FUEL_KEY, Math.min(fuel, MAX_FUEL));
    }
}
