package _2no2name.miscanium.dispensercart;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;

public record DispenserMinecartPointerImpl(DispenserMinecartEntity dispenserMinecartEntity, ServerWorld world) implements BlockPointer {
    public static DispenserMinecartPointerImpl from(DispenserMinecartEntity dispenserMinecartEntity) {
        return new DispenserMinecartPointerImpl(dispenserMinecartEntity, (ServerWorld) dispenserMinecartEntity.getWorld());
    }

    @Override
    public double getX() {
        return this.dispenserMinecartEntity().getX();
    }

    @Override
    public double getY() {
        return this.dispenserMinecartEntity().getY() + 0.5D;
    }

    @Override
    public double getZ() {
        return this.dispenserMinecartEntity().getZ();
    }

    @Override
    public BlockPos getPos() {
        return this.dispenserMinecartEntity().getBlockPos();
    }

    @Override
    public BlockState getBlockState() {
        return Blocks.DISPENSER.getDefaultState().with(DispenserBlock.FACING, this.dispenserMinecartEntity().getHorizontalFacing());
    }

    @Override
    public <T extends BlockEntity> T getBlockEntity() {
        //noinspection unchecked
        return (T) new DispenserMinecartBlockEntityWrapper(this.dispenserMinecartEntity());
    }

    @Override
    public ServerWorld getWorld() {
        return this.world();
    }
}
