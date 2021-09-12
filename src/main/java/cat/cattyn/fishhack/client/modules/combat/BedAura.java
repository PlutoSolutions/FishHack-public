package cat.cattyn.fishhack.client.modules.combat;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.event.events.Render3DEvent;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import cat.cattyn.fishhack.api.util.InventoryUtil;
import cat.cattyn.fishhack.api.util.Renderer3D;
import cat.cattyn.fishhack.api.util.RotationUtil;
import cat.cattyn.fishhack.api.util.Wrapper;
import cat.cattyn.fishhack.client.modules.client.ClickGuiMod;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BedItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Module.Info(name = "BedAura", cat = Module.Category.Combat)
public class BedAura extends Module {
    BlockPos targetBlock;
    short yaw;
    short ticks, refillTicks;
    Setting<Boolean> autoPlace = register("AutoPlace", false); //todo make less buggy
    Setting<Double> range = register("Range", 4, 0, 8, 2);
    Setting<Double> delay = register("Delay", 4, 0, 20, 0);
    Setting<Double> refillDelay = register("RefillDelay", 2, 0, 10, 0);
    Setting<Double> refillSlot = register("RefillSlot", 9, 0, 9, 0);
    Setting<Boolean> rotate = register("Rotate", true);
    Setting<Boolean> airPlace = register("AirPlace", true);
    Setting<Boolean> render = register("Render", true);
    private final List<BlockPos> possiblePosses = new ArrayList<>();
    private PlayerEntity target;
    private BlockPos placePos;

    @Override
    public void onEnable() {
        ticks = 0;
    }

    @Override
    public void onUpdate() {
        if (mc.world.getRegistryKey().getValue().getPath().equalsIgnoreCase("overworld")) return;
        target = findTarget();
        if (target != null) {
            if (refillTicks >= refillDelay.getValue()) {
                doRefill();
                refillTicks = 0;
            }
            if (autoPlace.getValue()) {
                doPlace();
            }
            if (ticks >= delay.getValue() && placePos != null) {
                doBreak(placePos);
                ticks = 0;
            }
        }
        ticks++;
        refillTicks++;
    }

    private PlayerEntity findTarget() {
        for (Entity e : mc.world.getEntities()) {
            if (e instanceof PlayerEntity && mc.player.distanceTo(e) <= range.getValue() && !FishHack.getFriendManager().isFriend(e.getName().getString()) && e != mc.player)
                return (PlayerEntity) e;
        }
        return null;
    }

    private void doPlace() {
        if (InventoryUtil.findSlot(BedItem.class) == -1 || placePos != null) return;
        InventoryUtil.switchItem(InventoryUtil.findSlot(BedItem.class));
        debug("Target = " + target.getDisplayName());

        debug("Getting target poses");
        getPossiblePoses(target);
        possiblePosses.sort(Comparator.comparing(bp -> mc.player.squaredDistanceTo(bp.getX(), bp.getY(), bp.getZ())));
        if (!possiblePosses.isEmpty() && mc.player.getMainHandStack().getItem() instanceof BedItem) {
            BlockPos targetPos = target.getBlockPos().up();
            targetBlock = possiblePosses.get(0);
            Vec3d vec = new Vec3d(targetBlock.getX() + 0.5, targetBlock.getY(), targetBlock.getZ() + 0.5);
            yaw = 0;
            if (targetBlock.equals(targetPos.east())) yaw = 90;
            else if (targetBlock.equals(targetPos.west())) yaw = -90;
            else if (targetBlock.equals(targetPos.south())) yaw = 180;
            if (rotate.getValue()) RotationUtil.rotate(targetBlock);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, mc.player.getPitch(), Wrapper.mc.player.isOnGround()));
            mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(vec, Direction.UP, targetBlock, true));
            placePos = targetBlock;
            possiblePosses.clear();
        }
    }

    private void doBreak(BlockPos pos) {
        Vec3d vec = new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        if (rotate.getValue()) RotationUtil.rotate(pos);
        mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(vec, Direction.UP, pos, false));
        placePos = null;
    }

    private void getPossiblePoses(PlayerEntity e) {
        BlockPos targetPos = e.getBlockPos().up();
        for (BlockPos bp : new BlockPos[]{targetPos.north(), targetPos.east(), targetPos.south(), targetPos.west()}) {
            if ((airPlace.getValue() || mc.world.getBlockState(bp.down()).getBlock() != Blocks.AIR) && (mc.world.getBlockState(bp).getBlock() == Blocks.AIR || mc.world.getBlockState(bp).getBlock() == Blocks.FIRE || mc.world.getBlockState(bp).getBlock() == Blocks.LAVA) && mc.world.getOtherEntities(null, new Box(bp)).isEmpty()) {
                possiblePosses.add(bp);
            }
        }
    }

    private void doRefill() {
        debug("Refilling...");
        int mainBedSlot = refillSlot.getValue().intValue();
        if (!(mc.player.getInventory().getStack(mainBedSlot - 1).getItem() instanceof BedItem) && !mc.player.isCreative()) {
            Integer bedSlot = null;
            for (int slot = 0; slot < 36; slot++) {
                ItemStack stack = mc.player.getInventory().getStack(slot);
                if (stack.getItem() instanceof BedItem) bedSlot = slot;
            }
            if (bedSlot == null || bedSlot == mainBedSlot - 1) return;
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, mainBedSlot + 35, 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, bedSlot < 9 ? (bedSlot + 36) : (bedSlot), 0, SlotActionType.PICKUP, mc.player);
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, mainBedSlot + 35, 0, SlotActionType.PICKUP, mc.player);

            debug("Refilled");
        }
    }

    @Subscribe
    public void onRender3D(Render3DEvent e) {
        if (!nullCheck()) return;
        if (targetBlock != null && render.getValue()) {
            Renderer3D.INSTANCE.setup3DRender(false);
            Vec3d vec3d = Renderer3D.INSTANCE.getRenderPosition(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ());
            Box box = new Box(vec3d.x, vec3d.y, vec3d.z, vec3d.x + 1, vec3d.y + 0.6, vec3d.z + 1);
            Renderer3D.INSTANCE.drawBox(e.getMatrixStack(), box, new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue(), 230).getRGB());
            Renderer3D.INSTANCE.drawOutlineBox(e.getMatrixStack(), box, new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue(), 230).getRGB());
            Vec3d secondVec = null;
            switch (yaw) {
                case 0:
                    secondVec = Renderer3D.INSTANCE.getRenderPosition(targetBlock.south().getX(), targetBlock.south().getY(), targetBlock.south().getZ());
                    break;
                case 90:
                    secondVec = Renderer3D.INSTANCE.getRenderPosition(targetBlock.west().getX(), targetBlock.west().getY(), targetBlock.west().getZ());
                    break;
                case 180:
                    secondVec = Renderer3D.INSTANCE.getRenderPosition(targetBlock.north().getX(), targetBlock.north().getY(), targetBlock.north().getZ());
                    break;
                case -90:
                    secondVec = Renderer3D.INSTANCE.getRenderPosition(targetBlock.east().getX(), targetBlock.east().getY(), targetBlock.east().getZ());
                    break;
            }
            assert false;
            Renderer3D.INSTANCE.drawBox(e.getMatrixStack(), new Box(secondVec.x, secondVec.y, secondVec.z, secondVec.x + 1, secondVec.y + 0.6, secondVec.z + 1), new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue(), 230).getRGB());
            Renderer3D.INSTANCE.drawOutlineBox(e.getMatrixStack(), new Box(secondVec.x, secondVec.y, secondVec.z, secondVec.x + 1, secondVec.y + 0.6, secondVec.z + 1), new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue(), 230).getRGB());
            Renderer3D.INSTANCE.end3DRender();
        }
    }
}