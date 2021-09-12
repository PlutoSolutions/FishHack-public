package cat.cattyn.fishhack.client.modules.player;

import cat.cattyn.fishhack.client.modules.Module;
import net.minecraft.block.AirBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@Module.Info(name = "Scaffold", cat = Module.Category.Player)
public class Scaffold extends Module {

    int blockSlot;
    int oldSlot;

    @Override
    public void onEnable() {
        oldSlot = mc.player.getInventory().selectedSlot;
    }

    @Override
    public void onUpdate() {
        if (!mc.player.isOnGround()) {

            if (!(mc.player.getInventory().getStack(oldSlot).getItem() instanceof BlockItem)) {
                for (int i = 0; i < 9; i++) {
                    if (mc.player.getInventory().getStack(i).getItem() instanceof BlockItem) {
                        blockSlot = i;
                        break;
                    }
                }
            } else
                blockSlot = mc.player.getInventory().selectedSlot;

            if (blockSlot == -1) {
                return;
            }

            BlockPos bp = new BlockPos(mc.player.getBlockPos().down().getX(), mc.player.getBlockPos().getY(), mc.player.getBlockPos().down().getZ());

            if (mc.world.getBlockState(bp).getBlock() instanceof AirBlock) {

                mc.player.getInventory().selectedSlot = blockSlot;

                Vec3d vec = new Vec3d(bp.getX(), bp.getY(), bp.getZ());

                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                        vec, Direction.DOWN, bp, false
                ));

                mc.player.swingHand(Hand.MAIN_HAND);

                mc.player.getInventory().selectedSlot = oldSlot;
            }
        }
    }
}
