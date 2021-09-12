package cat.cattyn.fishhack.client.modules.player;

import cat.cattyn.fishhack.client.modules.Module;

@Module.Info(name = "AntiVoid", cat = Module.Category.Player)
public class AntiVoid extends Module {
    @Override
    public void onUpdate() {
        if (mc.player.getY() <= 0.5 && !mc.player.isOnGround()) {
            mc.player.setVelocity(0, 10, 0);
        }
    }

}
