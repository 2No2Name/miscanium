package _2no2name.miscanium.mixin.dispensercart;

import _2no2name.miscanium.dispensercart.DispenserMinecartEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public class AbstractMinecartEntityMixin {

    @Inject(
            method = "create",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void create(World world, double x, double y, double z, AbstractMinecartEntity.Type type, CallbackInfoReturnable<AbstractMinecartEntity> cir) {
        if (type == DispenserMinecartEntity.DISPENSER_MINECART_TYPE) {
            cir.setReturnValue(new DispenserMinecartEntity(world, x, y, z));
        }
    }
}
