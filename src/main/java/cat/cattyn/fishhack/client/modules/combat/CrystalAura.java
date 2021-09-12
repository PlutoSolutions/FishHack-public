package cat.cattyn.fishhack.client.modules.combat;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.event.events.EntitySpawnEvent;
import cat.cattyn.fishhack.api.event.events.Render3DEvent;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import cat.cattyn.fishhack.api.util.InventoryUtil;
import cat.cattyn.fishhack.api.util.Renderer3D;
import cat.cattyn.fishhack.api.util.RotationUtil;
import cat.cattyn.fishhack.client.modules.client.ClickGuiMod;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.Difficulty;
import net.minecraft.world.explosion.Explosion;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cattyngmd
 */

@Module.Info(name = "CrystalAura", cat = Module.Category.Combat)
public class CrystalAura extends Module {

    List<BlockPos> poses = new ArrayList<BlockPos>();
    BlockPos targetBlock;
    PlayerEntity target;
    int placeDelayCounter;
    int breakDelayCounter;
    boolean offhand;
    int placed = 0;

    Setting<Boolean> place = register("Place", true);
    Setting<Boolean> hit = register("Break", true);
    Setting<Boolean> predict = register("Predict", false);
    Setting<String> rotations = register("Rotate", Arrays.asList("Break", "Place", "Both", "None"), "Break");
    Setting<String> swing = register("Swing", Arrays.asList("Offhand", "Mainhand"), "Offhand");
    Setting<Double> breakDelay = register("BreakDelay", 1, 0, 20, 0);
    Setting<Double> placeDelay = register("PlaceDelay", 1, 0, 20, 0);
    Setting<Boolean> antiSuicide = register("AntiSuicide", true);
    Setting<Double> maxSelfDamage = register("MaxSelfDmg", 12f, 0, 36, 1);
    Setting<Double> minDamage = register("MinDmg", 6f, 0, 36, 1);
    Setting<Double> range = register("Range", 4.5, 0, 6, 2);
    Setting<Double> enemyRange = register("EnemyRange", 12f, 0, 16, 2);
    Setting<Boolean> multiplace = register("Multiplace", false);
    Setting<Boolean> faceplace = register("Faceplace", true);
    Setting<Double> faceplaceHealth = this.register("FPHealth", 6, 0, 36, 1);
    Setting<Boolean> antiWeakness = register("AntiWeakness", true);

    public static List<BlockPos> getSphere(final BlockPos pos, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleblocks = new ArrayList<>();
        final int cx = pos.getX();
        final int cy = pos.getY();
        final int cz = pos.getZ();
        for (int x = cx - (int) r; x <= cx + r; ++x) {
            for (int z = cz - (int) r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int) r) : cy; y < (sphere ? (cy + r) : ((float) (cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    private static double getDistance(double p_X, double p_Y, double p_Z, double x, double y, double z) {
        double d0 = p_X - x;
        double d1 = p_Y - y;
        double d2 = p_Z - z;
        return MathHelper.sqrt((float) (d0 * d0 + d1 * d1 + d2 * d2));
    }

    public static double getCrystalDamage(LivingEntity entity, BlockPos blockPos) {
        float damage = 0.0f;
        float f2 = 12.0f;
        double d7 = MathHelper.sqrt((float) entity.squaredDistanceTo(Vec3d.ofCenter(blockPos))) / f2;
        if (d7 <= 1.0D) {
            double d8 = entity.getX() - blockPos.getX();
            double d9 = entity.getEyeY() - blockPos.getY();
            double d10 = entity.getZ() - blockPos.getZ();
            double d11 = MathHelper.sqrt((float) (d8 * d8 + d9 * d9 + d10 * d10));
            if (d11 != 0.0D) {
                double d12 = Explosion.getExposure(Vec3d.ofCenter(blockPos), entity);
                double d13 = (1.0D - d7) * d12;
                damage = transformForDifficulty((float) ((int) ((d13 * d13 + d13) / 2.0D * 7.0D * (double) f2 + 1.0D)));
                if (entity instanceof PlayerEntity) {
                    damage = DamageUtil.getDamageLeft(damage, (float) entity.getArmor(), (float) entity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
                    damage = getReduction(((PlayerEntity) entity), damage, DamageSource.GENERIC);
                }
            }
        }

        return damage;
    }

    private static float transformForDifficulty(float f) {
        if (mc.world.getDifficulty() == Difficulty.PEACEFUL) f = 0.0F;
        if (mc.world.getDifficulty() == Difficulty.EASY) f = Math.min(f / 2.0F + 1.0F, f);
        if (mc.world.getDifficulty() == Difficulty.HARD) f = f * 3.0F / 2.0F;
        return f;
    }

    private static float getReduction(PlayerEntity player, float f, DamageSource damageSource) {
        if (player.hasStatusEffect(StatusEffects.RESISTANCE) && damageSource != DamageSource.OUT_OF_WORLD) {
            int i = (player.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1) * 5;
            int j = 25 - i;
            float f1 = f * (float) j;
            float f2 = f;
            f = Math.max(f1 / 25.0F, 0.0F);
            float f3 = f2 - f;
            if (f3 > 0.0F && f3 < 3.4028235E37F) {
                if (player instanceof ServerPlayerEntity) {
                    player.increaseStat(Stats.DAMAGE_RESISTED, Math.round(f3 * 10.0F));
                } else if (damageSource.getAttacker() instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) damageSource.getAttacker()).increaseStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(f3 * 10.0F));
                }
            }
        }

        if (f <= 0.0F) {
            return 0.0F;
        } else {
            int k = EnchantmentHelper.getProtectionAmount(player.getArmorItems(), damageSource);
            if (k > 0) {
                f = DamageUtil.getInflictedDamage(f, (float) k);
            }

            return f;
        }
    }

    @Override
    public void onEnable() {
        targetBlock = null;
        placed = 0;
        breakDelayCounter = 0;
        placeDelayCounter = 0;
    }

    @Override
    public void onUpdate() {
        if (breakDelayCounter >= breakDelay.getValue() && hit.getValue() && !predict.getValue()) {
            doBreak();
            breakDelayCounter = 0;
        } else {
            breakDelayCounter++;
        }

        if (placeDelayCounter >= placeDelay.getValue() && place.getValue()) {
            doPlace();
            placeDelayCounter = 0;
        } else {
            placeDelayCounter++;
        }

    }

    @Subscribe
    public void onSpawnCrystal(EntitySpawnEvent e) {
        if (e.entity instanceof EndCrystalEntity && predict.getValue() && hit.getValue()) {
            if (rotations.getValue().equalsIgnoreCase("Break") || rotations.getValue().equalsIgnoreCase("Both"))
                RotationUtil.rotate(e.entity);
            mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(e.entity, mc.player.isSneaking()));
            mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
            placed = 0;
        }
    }

    private void doBreak() {
        debug("Breaking...");
        for (Entity e : mc.world.getEntities()) {
            if (!(e instanceof EndCrystalEntity) || mc.player.distanceTo(e) > range.getValue()) continue;
            if (rotations.getValue().equalsIgnoreCase("Break") || rotations.getValue().equalsIgnoreCase("Both")) {
                debug("Rotate");
                RotationUtil.rotate(e);
            }
            debug("Send attack packet");
            mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(e, mc.player.isSneaking()));
            debug("Hand Swing");
            if (swing.getValue().equalsIgnoreCase("Mainhand")) mc.player.swingHand(Hand.MAIN_HAND);
            else mc.player.swingHand(Hand.OFF_HAND);
            placed = 0;
            targetBlock = null;
        }
    }

    private void doPlace() {
        debug("Placing...");
        for (Entity e : mc.world.getEntities()) {
            if (!(e instanceof PlayerEntity) || e == mc.player || mc.player.distanceTo(e) > enemyRange.getValue() || FishHack.getFriendManager().isFriend(e.getName().asString()))
                continue;
            target = (PlayerEntity) e;
            debug("Target = " + target.getDisplayName());
            if (isInHole((PlayerEntity) e) && faceplace.getValue() && ((PlayerEntity) e).getHealth() <= faceplaceHealth.getValue()) {
                debug("Faceplacing...");
                doFaceplace();
            } else {
                BlockPos pblock = getBestBlock();
                if (pblock != null) {
                    if (multiplace.getValue() || placed < 1 && getDistance(mc.player.getX(), mc.player.getY(), mc.player.getZ(), pblock.getX(), pblock.getY(), pblock.getZ()) <= range.getValue()) {
                        placeCrystal(pblock);
                        debug("Clearing positions");
                        poses.clear();
                        placed++;
                    }
                }
            }
        }
    }

    private void doFaceplace() {
        List<BlockPos> faceplacePos = new ArrayList<BlockPos>();
        for (BlockPos bp : new BlockPos[]{target.getBlockPos().north(), target.getBlockPos().south(), target.getBlockPos().east(), target.getBlockPos().west()}) {
            if (canPlaceCrystal(bp)) faceplacePos.add(bp);
        }
        faceplacePos.sort(Comparator.comparing(bp -> mc.player.squaredDistanceTo(new Vec3d(bp.getX(), bp.getY(), bp.getZ()))));
        if (!faceplacePos.isEmpty() && multiplace.getValue() || placed < 1) {
            if (!faceplacePos.isEmpty()) placeCrystal(faceplacePos.get(0));
            placed++;
        }
    }

    private boolean isInHole(PlayerEntity e) {
        int blocks = 0;
        for (BlockPos bp : new BlockPos[]{e.getBlockPos().north(), e.getBlockPos().south(), e.getBlockPos().east(), e.getBlockPos().west()}) {
            if (mc.world.getBlockState(bp).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(bp).getBlock() == Blocks.BEDROCK)
                blocks++;
        }
        return blocks == 4;
    }

    private void placeCrystal(BlockPos bp) {
        if (InventoryUtil.findSlot(Items.END_CRYSTAL) == -1 && mc.player.getOffHandStack().getItem() != Items.END_CRYSTAL)
            return;
        Vec3d vec = new Vec3d(bp.getX(), bp.getY(), bp.getZ());
        if (mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL) offhand = true;
        else {
            InventoryUtil.switchItem(InventoryUtil.findSlot(Items.END_CRYSTAL));
            offhand = false;
        }
        targetBlock = bp;
        if (rotations.getValue().equalsIgnoreCase("Place") || rotations.getValue().equalsIgnoreCase("Both"))
            RotationUtil.rotate(bp);
        mc.interactionManager.interactBlock(mc.player, mc.world, offhand ? Hand.OFF_HAND : Hand.MAIN_HAND, new BlockHitResult(vec, Direction.DOWN, bp, true));
    }

    private boolean canPlaceCrystal(BlockPos bp) {
        return (mc.world.getBlockState(bp).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(bp).getBlock() == Blocks.BEDROCK) && mc.world.getBlockState(bp.up()).getBlock() == Blocks.AIR && mc.world.getOtherEntities(null, new Box(bp.add(0, 1, 0))).isEmpty();
    }

    private BlockPos getBestBlock() {
        for (BlockPos pose : possiblePlacePositions(range.getValue().floatValue())) {
            if (!(getCrystalDamage(target, pose) > minDamage.getValue()) || !(getCrystalDamage(mc.player, pose) < maxSelfDamage.getValue())
                    || !antiSuicide.getValue() || (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - getCrystalDamage(mc.player, pose) > 0) {
                debug("Target DMG = " + getCrystalDamage(target, pose));
                System.out.println("Target DMG = " + getCrystalDamage(target, pose));
                poses.add(pose);
            }
        }
        if (!poses.isEmpty()) {
            poses.sort(Comparator.comparing(bp -> getCrystalDamage(target, bp)));
            return poses.get(0);
        }
        return null;
    }

    private List<BlockPos> possiblePlacePositions(final float placeRange) {
        List<BlockPos> positions = new ArrayList<BlockPos>();
        positions.addAll(getSphere(mc.player.getBlockPos(), placeRange, (int) placeRange, false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
        Collections.reverse(positions);
        return positions;
    }

    @Subscribe
    public void onRender3D(Render3DEvent e) {
        if (!nullCheck()) return;
        if (targetBlock != null) {
            Renderer3D.INSTANCE.setup3DRender(false);
            Vec3d vec3d = Renderer3D.INSTANCE.getRenderPosition(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ());
            Box box = new Box(vec3d.x, vec3d.y, vec3d.z, vec3d.x + 1, vec3d.y + 1, vec3d.z + 1);
            Renderer3D.INSTANCE.drawBox(e.getMatrixStack(), box, new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue(), 230).getRGB());
            Renderer3D.INSTANCE.drawOutlineBox(e.getMatrixStack(), box, new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue(), 230).getRGB());
            Renderer3D.INSTANCE.end3DRender();
        }
    }
}
