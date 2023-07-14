package _2no2name.miscanium.dispensercart;

import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.entity.Entity;

public interface DispenserBehaviorWithOwner extends DispenserBehavior {
    void setOwner(Entity entity);

    DispenserBehaviorWithOwner getCopy();
}
