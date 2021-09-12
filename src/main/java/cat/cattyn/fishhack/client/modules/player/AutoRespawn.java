package cat.cattyn.fishhack.client.modules.player;

import cat.cattyn.fishhack.client.modules.Module;

@Module.Info(name = "AutoRespawn", desc = "Respawns you when you die", cat = Module.Category.Player)
public class AutoRespawn extends Module {
    public void onUpdate() {
        if (mc.player.isDead() || mc.player.getHealth() <= 0)
            mc.player.requestRespawn();
    }

}
