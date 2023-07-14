package _2no2name.miscanium.mixin.benigncreeper;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity {

    @Shadow public abstract boolean isIgnited();

    private boolean benign;

    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(EntityType<?> entityType, World world, CallbackInfo ci) {
         this.benign = this.random.nextInt(1024) == 0;
    }

    @ModifyVariable(method = "setFuseSpeed", at = @At(value = "HEAD"), argsOnly = true)
    private int behaveBenign(int fuseSpeed) {
        if (this.benign && !this.isIgnited()) {
            fuseSpeed = -1;
        }
        return fuseSpeed;
    }


    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void addCustomData(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("Benign", this.benign);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readCustomData(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("Benign", 99)) {
            this.benign = nbt.getBoolean("Benign");
        }
    }
}
