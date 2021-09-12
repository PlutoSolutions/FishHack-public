package cat.cattyn.fishhack.client.modules.render;

import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;

@Module.Info(name = "ItemViewModel", cat = Module.Category.Render)
public class ItemViewModel extends Module {

    public static ItemViewModel INSTANCE;

    public Setting<Double> x = register("x", 0.0, -2, 2, 3);
    public Setting<Double> y = register("y", 0.0, -2, 2, 3);
    public Setting<Double> z = register("z", 0.0, -2, 2, 3);

    public Setting<Double> swingLeft = this.register("swingLeft", 0, 0, 1, 1);
    public Setting<Double> swingRight = this.register("swingRight", 0, 0, 1, 1);

    public ItemViewModel() {
        INSTANCE = this;
    }

}
