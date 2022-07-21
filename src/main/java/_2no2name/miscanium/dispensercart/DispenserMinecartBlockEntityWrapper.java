package _2no2name.miscanium.dispensercart;

import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class DispenserMinecartBlockEntityWrapper extends DispenserBlockEntity {
    private final DispenserMinecartEntity dispenserMinecartEntity;

    public DispenserMinecartBlockEntityWrapper(DispenserMinecartEntity dispenserMinecartEntity) {
        super(null, BlockPos.ORIGIN, null);
        this.dispenserMinecartEntity = dispenserMinecartEntity;
    }

    @Override
    public int addToFirstFreeSlot(ItemStack stack) {
        return this.dispenserMinecartEntity.addToFirstFreeSlot(stack);
    }
}
