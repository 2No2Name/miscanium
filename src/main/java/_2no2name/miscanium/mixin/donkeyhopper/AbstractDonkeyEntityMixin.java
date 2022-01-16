package _2no2name.miscanium.mixin.donkeyhopper;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.stream.IntStream;

@Mixin(AbstractDonkeyEntity.class)
public class AbstractDonkeyEntityMixin extends HorseBaseEntity implements SidedInventory {

    protected AbstractDonkeyEntityMixin(EntityType<? extends HorseBaseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow protected native int getInventorySize();

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
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return IntStream.range(2, this.getInventorySize()).toArray();
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }
}
