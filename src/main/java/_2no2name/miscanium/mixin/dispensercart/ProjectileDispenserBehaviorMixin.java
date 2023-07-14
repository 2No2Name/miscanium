package _2no2name.miscanium.mixin.dispensercart;

import _2no2name.miscanium.dispensercart.DispenserBehaviorWithOwner;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ProjectileDispenserBehavior.class)
public class ProjectileDispenserBehaviorMixin extends ItemDispenserBehavior implements Cloneable, DispenserBehaviorWithOwner {

    private Entity owner = null;

    @Override
    public void setOwner(Entity entity) {
        this.owner = entity;
    }

    @Override
    public DispenserBehaviorWithOwner getCopy() {
        try {
            return (DispenserBehaviorWithOwner) this.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Inject(
            method = "dispenseSilently",
            locals = LocalCapture.CAPTURE_FAILHARD,
            at = @At(
                    value = "INVOKE", shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/entity/projectile/ProjectileEntity;setVelocity(DDDFF)V")
    )
    private void setProjectileOwner(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir, World world, Position position, Direction direction, ProjectileEntity projectileEntity) {
        if (this.owner != null) {
            projectileEntity.setOwner(this.owner);
        }
    }
}
