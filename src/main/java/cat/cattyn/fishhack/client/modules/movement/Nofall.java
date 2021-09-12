package cat.cattyn.fishhack.client.modules.movement;

import cat.cattyn.fishhack.client.modules.Module;
import net.minecraft.block.AirBlock;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;

@Module.Info(name = "Nofall", cat = Module.Category.Movement)
public class Nofall extends Module {
    @Override
    public void onUpdate() {
        if (mc.player.fallDistance > 3 && predict(new BlockPos(mc.player.getX(), mc.player.getY(), mc.player.getZ()))) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), 0, mc.player.getZ(), true));
        }
    }

    private boolean predict(BlockPos blockPos) {
        return !(mc.world.getBlockState(blockPos.add(0, -3, 0)).getBlock() instanceof AirBlock);
    }
}
