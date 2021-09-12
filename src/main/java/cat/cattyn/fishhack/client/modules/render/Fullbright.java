package cat.cattyn.fishhack.client.modules.render;

import cat.cattyn.fishhack.client.modules.Module;

/**
 * @author cattyngmd
 */

@Module.Info(name = "Fullbright", desc = "gamme+++", cat = Module.Category.Render)
public class Fullbright extends Module {
    double gamma;

    @Override
    public void onEnable() {
        gamma = mc.options.gamma;
    }

    @Override
    public void onUpdate() {
        mc.options.gamma = 1337;
    }

    @Override
    public void onDisable() {
        mc.options.gamma = gamma;
    }

}
