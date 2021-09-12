package cat.cattyn.fishhack.client.modules.combat;

import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import cat.cattyn.fishhack.api.util.InventoryUtil;
import cat.cattyn.fishhack.api.util.RotationUtil;
import net.minecraft.block.AirBlock;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

@Module.Info(name = "Surround", cat = Module.Category.Combat)
public class Surround extends Module {

    Setting<String> autoDisable = register("Disable", Arrays.asList("Jump", "Toggle", "Ypos", "None"), "Jump");
    Setting<Boolean> autoCenter = register("AutoCenter", true);
    Setting<Boolean> airVelocity = register("AirVelocity", true);
    Setting<Boolean> rotate = register("Rotate", false);
    Setting<Double> bpt = register("BPT", 4, 1, 8, 0);
    private int oldSlot;
    private double startPos;
    private boolean onEchest;

    @Override
    public void onEnable() {
        oldSlot = mc.player.getInventory().selectedSlot;
        startPos = mc.player.getY();

        if (autoCenter.getValue()) {
            double playerX = Math.floor(mc.player.getX());
            double playerZ = Math.floor(mc.player.getZ());
            mc.player.updatePosition(playerX + 0.5, mc.player.getY(), playerZ + 0.5);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(playerX + 0.5, mc.player.getY(), playerZ + 0.5, mc.player.isOnGround()));
        }
    }

    @Override
    public void onUpdate() {
        if (autoDisable.getValue().equalsIgnoreCase("Jump") && mc.options.keyJump.isPressed()) {
            toggle();
        }

        if (autoDisable.getValue().equalsIgnoreCase("Ypos") && startPos != mc.player.getY()) {
            toggle();
        }
        if (InventoryUtil.findSlot(Items.OBSIDIAN) == -1) return;

        onEchest = mc.player.getY() < Math.round(mc.player.getY());

        placeBlocks();

        if (autoDisable.getValue().equalsIgnoreCase("Toggle")) {
            toggle();
        }

    }

    private void placeBlocks() {
        int j = 0;
        for (BlockPos b : new BlockPos[]{
                //new BlockPos(mc.player.getBlockPos().down().getX(), mc.player.getBlockPos().down().getY(), mc.player.getBlockPos().down().getZ()),
                new BlockPos(mc.player.getBlockPos().north().getX(), mc.player.getBlockPos().north().getY() + (onEchest ? 1 : 0), mc.player.getBlockPos().north().getZ()),
                new BlockPos(mc.player.getBlockPos().east().getX(), mc.player.getBlockPos().east().getY() + (onEchest ? 1 : 0), mc.player.getBlockPos().east().getZ()),
                new BlockPos(mc.player.getBlockPos().south().getX(), mc.player.getBlockPos().south().getY() + (onEchest ? 1 : 0), mc.player.getBlockPos().south().getZ()),
                new BlockPos(mc.player.getBlockPos().west().getX(), mc.player.getBlockPos().west().getY() + (onEchest ? 1 : 0), mc.player.getBlockPos().west().getZ())}) {

            if (j >= bpt.getValue()) {
                return;
            }

            if (mc.world.getBlockState(b).getBlock() instanceof AirBlock) {

                InventoryUtil.switchItem(InventoryUtil.findSlot(Items.OBSIDIAN));

                if (!mc.player.isOnGround() && airVelocity.getValue()) {
                    mc.player.setVelocity(0, 0, 0);
                }

                Vec3d vec = new Vec3d(b.getX(), b.getY(), b.getZ());

                if (rotate.getValue()) RotationUtil.rotate(b);

                mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(
                        vec, Direction.DOWN, b, true
                ));

                InventoryUtil.switchItem(oldSlot);
                j++;
            }

        }
    }

}
