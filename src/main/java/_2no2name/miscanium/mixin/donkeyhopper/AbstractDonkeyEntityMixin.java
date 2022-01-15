package _2no2name.miscanium.mixin.donkeyhopper;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractDonkeyEntity.class)
public abstract class AbstractDonkeyEntityMixin extends HorseBaseEntity implements Inventory {

    protected AbstractDonkeyEntityMixin(EntityType<? extends HorseBaseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow protected abstract int getInventorySize();

    @Override
    public int size() {
        return this.getInventorySize();
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.items.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return this.items.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return this.items.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.items.setStack(slot, stack);
    }

    @Override
    public void markDirty() {
        this.items.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.isRemoved()) {
            return false;
        } else {
            return !(player.squaredDistanceTo(this) > 64.0D);
        }
    }

    @Override
    public void clear() {
        this.items.clear();
    }
}
