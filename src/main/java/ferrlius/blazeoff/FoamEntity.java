package ferrlius.blazeoff;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A foam projectile that has gravity and can be launched
 * in the direction the player looks.
 */
public class FoamEntity extends ProjectileEntity {

    private int life;                // Track lifetime (optional)
    private static final int MAX_LIFE = 100;  // 20 ticks → 1 sec

    public FoamEntity(EntityType<? extends FoamEntity> type, World world) {
        super(type, world);
        this.setNoGravity(false);
    }

    // Convenience constructor
    public FoamEntity(World world) {
        this(ModEntities.FOAM, world);
    }

    @Override
    public void tick() {
        super.tick();
        life++;
        if (life > MAX_LIFE) {
            this.discard(); // remove if it travels too long
        }
    }

    // Called when hitting a block
    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        BlockPos hitPos = blockHitResult.getBlockPos();
        extinguishArea(hitPos, 2); // radius 2 or your choice
        this.discard(); // die on hit
    }

    // Called when hitting an entity
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        // If it's burning, extinguish
        if (entityHitResult.getEntity().isOnFire()) {
            entityHitResult.getEntity().setOnFireFor(0);
        }
        // Also extinguish around the entity’s blockpos
        extinguishArea(entityHitResult.getEntity().getBlockPos(), 2);
        this.discard();
    }

    private void extinguishArea(BlockPos center, int radius) {
        World w = this.getWorld();
        for(int dx = -radius; dx <= radius; dx++) {
            for(int dy = -radius; dy <= radius; dy++) {
                for(int dz = -radius; dz <= radius; dz++) {
                    BlockPos checkPos = center.add(dx, dy, dz);
                    if (w.getBlockState(checkPos).isOf(Blocks.FIRE)) {
                        w.setBlockState(checkPos, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }

    @Override
    protected void initDataTracker() { }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.life = nbt.getInt("Life");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Life", this.life);
    }
}
