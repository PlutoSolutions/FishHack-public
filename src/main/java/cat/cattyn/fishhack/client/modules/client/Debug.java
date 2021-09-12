package cat.cattyn.fishhack.client.modules.client;

import cat.cattyn.fishhack.client.modules.Module;

@Module.Info(name = "Debug", desc = "Helps you to debug shit.", cat = Module.Category.Client)
public class Debug extends Module {
    public static Debug getInstance;

    public Debug() {
        getInstance = this;
    }

}
