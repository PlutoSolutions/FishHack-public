package cat.cattyn.fishhack.client.modules.movement;

import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import net.minecraft.util.math.MathHelper;

@Module.Info(name = "ElytraFly", cat = Module.Category.Movement)
public class ElytraFly extends Module {

    public Setting<Double> boost = register("Boost", 0.05D, 0, 1, 2);
    public Setting<Double> maxBoost = register("MaxBoost", 2.5D, 0, 5, 1);

    public void onUpdate() {
        double currentVel = Math.abs(mc.player.getVelocity().x) + Math.abs(mc.player.getVelocity().y) + Math.abs(mc.player.getVelocity().z);
        float radianYaw = (float) Math.toRadians(mc.player.getYaw());

        if (mc.player.isFallFlying() && currentVel <= maxBoost.getValue()) {
            if (mc.options.keyBack.isPressed()) {
                mc.player.addVelocity(MathHelper.sin(radianYaw) * boost.getValue(), 0, MathHelper.cos(radianYaw) * -boost.getValue());
            } else if (mc.player.getPitch() > 0) {
                mc.player.addVelocity(MathHelper.sin(radianYaw) * -boost.getValue(), 0, MathHelper.cos(radianYaw) * boost.getValue());
            }
        }
    }
}
