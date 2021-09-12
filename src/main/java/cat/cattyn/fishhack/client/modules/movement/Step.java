package cat.cattyn.fishhack.client.modules.movement;

import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;

/**
 * @author zipfile
 */

@Module.Info(name = "Step", desc = "Auto Step", cat = Module.Category.Movement)
public class Step extends Module {
    Setting<Double> height = register("Height", 1f, 0, 3, 1);

    public void onDisable() {
        mc.player.stepHeight = 0.6f;

    }

    public void onUpdate() {
        mc.player.stepHeight = height.getValue().floatValue();
    }

}
