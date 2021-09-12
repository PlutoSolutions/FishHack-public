package cat.cattyn.fishhack.client.modules.combat;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import cat.cattyn.fishhack.api.util.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Module.Info(name = "KillAura", cat = Module.Category.Combat)
public class KillAura extends Module {

    public static KillAura INSTANCE;
    List<Entity> entities;
    int oldSlot;
    int swordSlot;
    Setting<String> mode = register("Mode", Arrays.asList("Switch", "OnlySword", "None"), "Switch");
    Setting<String> sort = register("Sort", Arrays.asList("Distance", "Health"), "Distance");
    Setting<Double> range = register("Range", 4.5, 0, 6, 2);
    Setting<Boolean> players = register("Players", true);
    Setting<Boolean> animals = register("Animals", false);
    Setting<Boolean> hostiles = register("Hostiles", false);
    Setting<Boolean> raytrace = register("Raytrace", false);
    Setting<Boolean> rotate = register("Rotate", true);

    public KillAura() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        entities = null;
        swordSlot = -1;
        oldSlot = mc.player.getInventory().selectedSlot;
    }

    @Override
    public void onDisable() {
        entities = null;
        if (mode.getValue().equalsIgnoreCase("Switch")) mc.player.getInventory().selectedSlot = oldSlot;
    }

    private float getHealth(LivingEntity e) {
        return e.getHealth() + e.getAbsorptionAmount();
    }

    @Override
    public void onUpdate() {
        entities = new ArrayList<Entity>();
        mc.world.getEntities().forEach(entities::add);
        entities.stream()
                .sorted(Comparator.comparing(e -> sort.getValue().equalsIgnoreCase("Health") && e instanceof LivingEntity ? getHealth((LivingEntity) e) : mc.player.distanceTo(e)))
                .collect(Collectors.toList());
        for (Entity e : entities) {
            if (mc.player.getAttackCooldownProgress(mc.getTickDelta()) == 1.0f && e.isLiving() && e.isAttackable()
                    && e != mc.player && mc.player.distanceTo(e) <= range.getValue() && e != mc.player.getVehicle() && (
                    (e instanceof PlayerEntity && players.getValue() && !FishHack.getFriendManager().isFriend(e.getName().getString()))
                            || ((e instanceof PassiveEntity || e instanceof WaterCreatureEntity || e instanceof AmbientEntity || e instanceof SnowGolemEntity || e instanceof IronGolemEntity) && animals.getValue())
                            || (e instanceof Monster && hostiles.getValue()))
                    && (!raytrace.getValue() || mc.player.canSee(e))) {
                debug("Targeted entity " + e.getDisplayName());

                switch (mode.getValue()) {
                    case "Switch":
                        if (mc.player.getMainHandStack().getItem() instanceof SwordItem) {
                            if (rotate.getValue()) {
                                debug("Rotating...");
                                RotationUtil.rotate(e);
                            }
                            debug("Attacking player...");
                            mc.interactionManager.attackEntity(mc.player, e);
                            mc.player.swingHand(Hand.MAIN_HAND);
                        } else {
                            for (int j = 0; j < 9; j++) {
                                if (mc.player.getInventory().getStack(j).getItem() instanceof SwordItem) {
                                    swordSlot = j;
                                    break;
                                }
                            }
                            if (swordSlot != -1) {
                                mc.player.getInventory().selectedSlot = swordSlot;
                                if (rotate.getValue()) {
                                    debug("Rotating...");
                                    RotationUtil.rotate(e);
                                }
                                debug("Attacking player...");
                                mc.interactionManager.attackEntity(mc.player, e);
                                mc.player.swingHand(Hand.MAIN_HAND);
                            }
                        }
                        break;
                    case "OnlySword":
                        if (mc.player.getMainHandStack().getItem() instanceof SwordItem) {
                            if (rotate.getValue()) {
                                debug("Rotating...");
                                RotationUtil.rotate(e);
                            }
                            debug("Attacking player...");
                            mc.interactionManager.attackEntity(mc.player, e);
                            mc.player.swingHand(Hand.MAIN_HAND);
                        }
                        break;
                    case "None":
                        if (rotate.getValue()) {
                            debug("Rotating...");
                            RotationUtil.rotate(e);
                        }
                        debug("Attacking player...");
                        mc.interactionManager.attackEntity(mc.player, e);
                        mc.player.swingHand(Hand.MAIN_HAND);
                        break;
                }
            }
        }
    }

}
