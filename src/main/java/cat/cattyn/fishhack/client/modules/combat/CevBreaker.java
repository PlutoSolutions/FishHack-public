package cat.cattyn.fishhack.client.modules.combat;

import cat.cattyn.fishhack.api.event.events.Render3DEvent;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import cat.cattyn.fishhack.api.util.InventoryUtil;
import cat.cattyn.fishhack.api.util.Renderer3D;
import cat.cattyn.fishhack.api.util.RotationUtil;
import cat.cattyn.fishhack.client.modules.client.ClickGuiMod;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.AirBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

@Module.Info(name = "CevBreaker", cat = Module.Category.Combat)
public class CevBreaker extends Module {

    public PlayerEntity target;
    public Setting<Double> distance = register("Distance", 4.5, 1, 6, 2);
    public Setting<Double> delay = register("Delay", 2, 0, 10, 0);
    public Setting<Double> instantDelay = register("InstantDelay", 0, 0, 10, 0);
    public Setting<Boolean> instant = register("Instant", false);
    public Setting<Boolean> rotate = register("Rotate", true);
    public Setting<Boolean> ak47 = register("AK47", false);
    public Setting<Boolean> render = register("Render", true);
    boolean offhand = false;
    boolean started = false;
    boolean placed = false;
    BlockPos targetPos;
    int ticks = 0;
    int instaTicks = 0;

    @Override
    public void onEnable() {
        started = false;
        target = null;
        ticks = 0;
    }

    @Override
    public void onDisable() {
        started = false;
        target = null;
        ticks = 0;
    }

    @Override
    public void onUpdate() {
        target = getTarget();
        if (target != null) {
            debug("Target = " + target.getDisplayName());
            targetPos = target.getBlockPos().add(0, 2, 0);
            Vec3d vec = new Vec3d(targetPos.getX(), targetPos.getY(), targetPos.getZ());
            if (!(mc.world.getBlockState(targetPos.up()).getBlock() instanceof AirBlock)) return;
            if (ticks == delay.getValue()) {
                if (InventoryUtil.findSlot(Items.OBSIDIAN) == -1) return;
                InventoryUtil.switchItem(InventoryUtil.findSlot(Items.OBSIDIAN));
                if (mc.world.getBlockState(targetPos).getBlock() instanceof AirBlock) {
                    setDisplayInfo("Placing");
                    if (rotate.getValue()) {
                        RotationUtil.rotate(targetPos);
                    }
                    mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND, new BlockHitResult(vec, Direction.UP, targetPos, true));
                }
            }
            if (ticks == delay.getValue() * 2) {
                if (InventoryUtil.findSlot(Items.END_CRYSTAL) == -1 && mc.player.getOffHandStack().getItem() != Items.END_CRYSTAL)
                    return;
                if (mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL) {
                    InventoryUtil.switchItem(InventoryUtil.findSlot(PickaxeItem.class));
                    offhand = true;
                } else {
                    InventoryUtil.switchItem(InventoryUtil.findSlot(Items.END_CRYSTAL));
                    offhand = false;
                }
                if (rotate.getValue()) {
                    RotationUtil.rotate(targetPos);
                }
                mc.interactionManager.interactBlock(mc.player, mc.world, offhand ? Hand.OFF_HAND : Hand.MAIN_HAND, new BlockHitResult(vec, Direction.DOWN, targetPos, true));
                placed = true;
            }
            if (ticks == delay.getValue() * 3) {
                if (InventoryUtil.findSlot(PickaxeItem.class) == -1) return;
                InventoryUtil.switchItem(InventoryUtil.findSlot(PickaxeItem.class));
                if (!instant.getValue()) {
                    setDisplayInfo("Breaking");
                    if (rotate.getValue()) RotationUtil.rotate(targetPos);
                    mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, targetPos, Direction.UP));
                    mc.player.swingHand(Hand.MAIN_HAND);
                    mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, targetPos, Direction.UP));
                }
            }
            if (ticks >= delay.getValue() * 3) {
                if (mc.world.getBlockState(targetPos).getBlock() instanceof AirBlock) {
                    if (getCrystal(target) != null) {
                        if (rotate.getValue()) RotationUtil.rotate(getCrystal(target));
                        mc.interactionManager.attackEntity(mc.player, getCrystal(target));
                        if (ak47.getValue()) getCrystal(target).remove(RemovalReason.KILLED);
                    }
                    placed = false;
                    ticks = -1;
                } else if (instant.getValue()) {
                    setDisplayInfo("Breaking");
                    if (started == false) {
                        if (rotate.getValue()) RotationUtil.rotate(targetPos);
                        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, targetPos, Direction.UP));
                        started = true;
                    }
                    if (instaTicks >= instantDelay.getValue()) {
                        instaTicks = 0;
                        if (shouldMine(targetPos)) {
                            mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, targetPos, Direction.UP));
                            mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                        }
                    } else instaTicks++;
                }
            }
            if (placed && getCrystal(target) == null) {
                int prevSlot = mc.player.getInventory().selectedSlot;
                if (InventoryUtil.findSlot(Items.END_CRYSTAL) == -1) return;
                InventoryUtil.switchItem(InventoryUtil.findSlot(Items.END_CRYSTAL));
                if (rotate.getValue()) RotationUtil.rotate(targetPos);
                mc.interactionManager.interactBlock(mc.player, mc.world, offhand ? Hand.OFF_HAND : Hand.MAIN_HAND, new BlockHitResult(vec, Direction.DOWN, targetPos, true));
                InventoryUtil.switchItem(prevSlot);
            }
            ticks++;
        } else {
            targetPos = null;
            setDisplayInfo("Nothing");
        }
    }

    @Subscribe
    public void onWorldRender(Render3DEvent e) {
        if (!nullCheck()) return;
        if (render.getValue() && targetPos != null) {
            Vec3d vec3d = Renderer3D.INSTANCE.getRenderPosition(targetPos.getX(), targetPos.getY(), targetPos.getZ());
            Box box = new Box(vec3d.x, vec3d.y, vec3d.z, vec3d.x + 1, vec3d.y + 1, vec3d.z + 1);
            Renderer3D.INSTANCE.drawBox(e.getMatrixStack(), box, new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue(), 230).getRGB());
            Renderer3D.INSTANCE.drawOutlineBox(e.getMatrixStack(), box, new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue(), 230).getRGB());
        }
    }

    private EndCrystalEntity getCrystal(PlayerEntity target) {
        for (Entity e : mc.world.getEntities()) {
            if (e instanceof EndCrystalEntity) {
                if (Math.ceil(e.getX()) == Math.ceil(target.getX()) && Math.ceil(e.getZ()) == Math.ceil(target.getZ()))
                    return (EndCrystalEntity) e;
            }
        }
        return null;
    }

    private boolean shouldMine(BlockPos bp) {
        if (bp.getY() == -1) return false;
        if (mc.world.getBlockState(bp).getHardness(mc.world, bp) < 0) return false;
        if (mc.world.getBlockState(bp).isAir()) return false;
        return mc.player.getMainHandStack().getItem() == Items.DIAMOND_PICKAXE || mc.player.getMainHandStack().getItem() == Items.NETHERITE_PICKAXE;
    }

    private PlayerEntity getTarget() {
        for (Entity e : mc.world.getEntities()) {
            if (mc.player.distanceTo(e) <= distance.getValue() && e instanceof PlayerEntity && e != mc.player) {
                return (PlayerEntity) e;
            }
        }
        return null;
    }

}
