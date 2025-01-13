package ferrlius.blazeoff;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ManualFoamEntity extends Entity {
    private int life;
    private static final int MAX_LIFE = 20;

    public ManualFoamEntity(EntityType<? extends ManualFoamEntity> type, World world) {
        super(type, world);
        // Optional: ensure gravity is off or on as you prefer
        // this.setNoGravity(false);
    }

    public ManualFoamEntity(World world) {
        this(ModEntities.MANUAL_FOAM_ENTITY, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isAlive()) return;

        // Increment lifetime
        life++;
        if (life > MAX_LIFE) {
            this.discard();
            return;
        }

        // Store old position (for interpolation, etc.)
        this.prevX = this.getX();
        this.prevY = this.getY();
        this.prevZ = this.getZ();

        // Current velocity
        Vec3d velocity = this.getVelocity();

        // 1) Gravity check: Yarn often calls it hasNoGravity()
        //    If you do want gravity, do something like:
        if (!this.hasNoGravity()) {
            // Vanilla gravity is ~0.08, but 0.04 is also common in older code.
            velocity = velocity.add(0, -0.04, 0);
        }

        // 2) Basic friction if on ground
        float friction = 0.98F;
        if (this.isOnGround()) {
            // Instead of getBlockPosBelowThatAffectsMyMovement(), just do:
            BlockPos groundPos = BlockPos.ofFloored(getX(), getY() - 0.2, getZ());
            // Then get friction from that block
            float blockFriction = this.getWorld()
                    .getBlockState(groundPos)
                    .getBlock()
                    .getSlipperiness();
            friction = blockFriction * 0.98F;
        }

        // Apply friction & mild air resistance
        double vx = velocity.x * friction;
        double vy = velocity.y * 0.98;
        double vz = velocity.z * friction;
        velocity = new Vec3d(vx, vy, vz);

        // 3) Update velocity
        this.setVelocity(velocity);

        // 4) Move the entity
        //    MovementType.SELF means "move by its own velocity"
        this.move(MovementType.SELF, velocity);

        // 5) Simple collision check
        if (this.horizontalCollision || this.verticalCollision) {
            // Extinguish area around the collision
            BlockPos collisionPos = this.getBlockPos();
            extinguishArea(collisionPos);
            this.discard();
        }
    }

    private void extinguishArea(BlockPos center) {
        // Remove fire blocks in a small 3×3×3 area
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos pos = center.add(dx, dy, dz);
                    if (this.getWorld().getBlockState(pos).isOf(Blocks.FIRE)) {
                        this.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }

    // -----------------------
    // Required Entity methods
    // -----------------------

    @Override
    protected void initDataTracker() {}

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.life = nbt.getInt("Life");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("Life", this.life);
    }
}