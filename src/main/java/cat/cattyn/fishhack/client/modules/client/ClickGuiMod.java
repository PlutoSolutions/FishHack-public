package cat.cattyn.fishhack.client.modules.client;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;

/**
 * @author cattyngmd
 */

@Module.Info(name = "ClickGuiMod", desc = "Toggles ClickGUI", cat = Module.Category.Client, key = 260, drawn = false)
public class ClickGuiMod extends Module {

    public static ClickGuiMod INSTANCE;
    public Setting<Double> r = register("Red", 20, 0, 255, 0);
    public Setting<Double> g = register("Green", 20, 0, 255, 0);
    public Setting<Double> b = register("Blue", 133, 0, 255, 0);
    public Setting<Double> a = register("Alpha", 231, 0, 255, 0);
    public Setting<Double> brighter = register("Brighter", 0.75, 0, 1, 2);
    public Setting<Boolean> tint = register("Tint", false);
    public Setting<Boolean> desc = register("Description", true);

    public ClickGuiMod() {
        INSTANCE = this;
    }

    public void onEnable() {
        if (!nullCheck())
            return;
        mc.setScreen(FishHack.getClickGui());
    }

    public void onDisable() {
        if (!nullCheck())
            return;
        mc.setScreen(null);
    }

}
