package _2no2name.miscanium.mixin.dispensercart;

import _2no2name.miscanium.dispensercart.DispenserMinecartEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Arrays;

@Mixin(AbstractMinecartEntity.Type.class)
public class AbstractMinecartEntityTypeMixin {
    @Mutable
    @Shadow @Final private static AbstractMinecartEntity.Type[] field_7673;


    @SuppressWarnings("unused")
    public AbstractMinecartEntityTypeMixin(String dispenser, int ordinal) { }


    @Inject(
            method = "<clinit>",
            at = @At("RETURN"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void addMinecartType(CallbackInfo ci) {
        int enumLength = field_7673.length;
        field_7673 = Arrays.copyOf(field_7673, enumLength + 1);
        DispenserMinecartEntity.initializeDispenserMinecartType(field_7673[enumLength] = ((AbstractMinecartEntity.Type) (Object) new AbstractMinecartEntityTypeMixin("DISPENSER", enumLength + 1)));
    }
}
