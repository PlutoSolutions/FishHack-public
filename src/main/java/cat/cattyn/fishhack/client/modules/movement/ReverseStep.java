package cat.cattyn.fishhack.client.modules.movement;

import cat.cattyn.fishhack.client.modules.Module;

/**
 * @author zipfile
 */

@Module.Info(name = "ReverseStep", desc = "Moves you down faster", cat = Module.Category.Movement)
public class ReverseStep extends Module {
    public void onUpdate() {
        if (mc.player.isInLava() || mc.player.isTouchingWater() || mc.player.isHoldingOntoLadder()) return;
        if (mc.player.isOnGround()) {
            mc.player.addVelocity(0, -1, 0);
        }
    }

}