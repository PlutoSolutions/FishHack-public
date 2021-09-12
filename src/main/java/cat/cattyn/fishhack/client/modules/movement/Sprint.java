package cat.cattyn.fishhack.client.modules.movement;

import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;

import java.util.Arrays;

/**
 * @author cattyngmd
 */

@Module.Info(name = "Sprint", desc = "Auto Sprint", cat = Module.Category.Movement)
public class Sprint extends Module {
    Setting<String> mode = register("Mode", Arrays.asList("Legit", "Rage"), "Legit");

    public void onUpdate() {
        if (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0) {
            if (mode.getValue().equalsIgnoreCase("Legit") && mc.player.forwardSpeed != 0) {
                mc.player.setSprinting(true);
            } else if (mode.getValue().equalsIgnoreCase("Rage"))
                mc.player.setSprinting(true);
        }
    }

}
