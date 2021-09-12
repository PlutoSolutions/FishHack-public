package cat.cattyn.fishhack.client.modules.movement;

import cat.cattyn.fishhack.api.event.events.PacketEvent;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.util.FabricReflect;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@Module.Info(name = "AntiHunger", cat = Module.Category.Movement)
public class AntiHunger extends Module {
    @Subscribe
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket) {
            FabricReflect.writeField(event.getPacket(), false, "field_12891", "onGround");
        }
    }

}
