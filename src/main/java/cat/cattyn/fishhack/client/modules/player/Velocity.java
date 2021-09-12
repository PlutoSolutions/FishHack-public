package cat.cattyn.fishhack.client.modules.player;

import cat.cattyn.fishhack.api.event.events.PacketEvent;
import cat.cattyn.fishhack.client.modules.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

@Module.Info(name = "Velocity", desc = "Prevents you from taking knockback", cat = Module.Category.Player)
public class Velocity extends Module {
    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        if ((event.getPacket() instanceof EntityVelocityUpdateS2CPacket) || (event.getPacket() instanceof ExplosionS2CPacket))
            event.cancel();
    }
}
