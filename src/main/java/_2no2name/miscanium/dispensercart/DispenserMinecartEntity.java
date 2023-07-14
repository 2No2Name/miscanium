package _2no2name.miscanium.dispensercart;

import _2no2name.miscanium.mixin.dispensercart.DispenserBlockAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MinecartItem;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

public class DispenserMinecartEntity extends StorageMinecartEntity {

    public static Type DISPENSER_MINECART_TYPE;
    public static Item DISPENSER_MINECART_ITEM;

    public static Type[] MINECART_TYPES;

    @SuppressWarnings("Convert2MethodRef")
    public static final EntityType<DispenserMinecartEntity> DISPENSER_CART_ENTITY_TYPE =  Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("miscanium", "dispenser_minecart"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, (EntityType<DispenserMinecartEntity> type, World world) -> new DispenserMinecartEntity(type, world)).dimensions(EntityDimensions.fixed(0.98f, 0.7f)).trackRangeChunks(8).build()
    );

    private BlockPos triggerPos;

    public DispenserMinecartEntity(World world, double x, double y, double z) {
        super(DISPENSER_CART_ENTITY_TYPE, x, y, z, world);
    }

    protected DispenserMinecartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    public static void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            EntityRendererRegistry.register(DISPENSER_CART_ENTITY_TYPE, context -> new MinecartEntityRenderer<>(context, EntityModelLayers.MINECART));
        }
        MINECART_TYPES = Type.values();
    }

    public static void initializeDispenserMinecartType(Type dispenserMinecartType) {
        DISPENSER_MINECART_TYPE = dispenserMinecartType;
        DISPENSER_MINECART_ITEM = new MinecartItem(DISPENSER_MINECART_TYPE, new Item.Settings().maxCount(1).group(ItemGroup.TRANSPORTATION));
        Registry.register(Registry.ITEM, new Identifier("miscanium", "dispenser_minecart"), DISPENSER_MINECART_ITEM);
    }

    @Override
    public void onActivatorRail(int x, int y, int z, boolean powered) {
        BlockPos triggerPos = this.getLastTriggerPos();
        if (!powered && triggerPos != null) {
            this.setLastTriggerPos(null);
            return;
        }
        if (powered && (triggerPos == null || triggerPos.getX() != x || triggerPos.getY() != y || triggerPos.getZ() != z)) {
            this.setLastTriggerPos(new BlockPos(x, y, z));
            this.trigger();
        }
    }

    private void trigger() {
        if (!(this.world instanceof ServerWorld)) {
            return;
        }
        this.generateLoot(null);
        int slot = this.chooseDispenseSlot();
        if (slot < 0) {
            world.syncWorldEvent(WorldEvents.DISPENSER_FAILS, this.getBlockPos(), 0);
            world.emitGameEvent(GameEvent.DISPENSE_FAIL, this);
            return;
        }
        ItemStack itemStack = this.getStack(slot);
        DispenserBehavior dispenserBehavior = DispenserBlockAccessor.getBehaviorMap().get(itemStack.getItem());
        if (dispenserBehavior instanceof ProjectileDispenserBehavior projectileDispenserBehavior) {
            dispenserBehavior = getBehaviorWithOwner(this, projectileDispenserBehavior);
        }
        if (dispenserBehavior != DispenserBehavior.NOOP) {
            DispenserMinecartPointerImpl dispenserMinecartPointer = DispenserMinecartPointerImpl.from(this);
            this.setStack(slot, dispenserBehavior.dispense(dispenserMinecartPointer, itemStack));
        }

    }

    public static DispenserBehavior getBehaviorWithOwner(Entity owner, ProjectileDispenserBehavior behavior) {
        DispenserBehaviorWithOwner copy = ((DispenserBehaviorWithOwner) behavior).getCopy();
        if (copy != null) {
            copy.setOwner(owner);
            return copy;
        }
        return behavior;
    }


    private int chooseDispenseSlot() {
        int chosenSlot = -1;
        int previousOccupiedSlots = 0;
        for (int slot = 0; slot < this.size(); slot++) {
            if (!this.getStack(slot).isEmpty()) {
                boolean b = this.random.nextInt(previousOccupiedSlots + 1) == 0;
                previousOccupiedSlots++;
                if (b) {
                    chosenSlot = slot;
                }
            }
        }
        return chosenSlot;
    }

    public BlockPos getLastTriggerPos() {
        return this.triggerPos;
    }

    public void setLastTriggerPos(BlockPos triggerPos) {
        this.triggerPos = triggerPos;
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(DispenserMinecartEntity.DISPENSER_MINECART_ITEM);
    }


        @Override
    protected ScreenHandler getScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new Generic3x3ContainerScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public Type getMinecartType() {
        return DISPENSER_MINECART_TYPE;
    }

    @Override
    public int size() {
        return 9;
    }

    public BlockState getDefaultContainedBlock() {
        return Blocks.DISPENSER.getDefaultState();
    }

    public int addToFirstFreeSlot(ItemStack stack) {
        for (int i = 0; i < this.size(); ++i) {
            if (!this.getStack(i).isEmpty()) continue;
            this.setStack(i, stack);
            return i;
        }
        return -1;
    }
}
