package cat.cattyn.fishhack.client.modules.player;

import cat.cattyn.fishhack.api.event.events.PacketEvent;
import cat.cattyn.fishhack.client.modules.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;

import java.util.Objects;

@Module.Info(name = "MountBypass", cat = Module.Category.Player)
public class MountBypass extends Module {

    @Subscribe
    public void onPacketSend(PacketEvent.Send e) {
        if (mc.world.getServer() == null) return;
        if (e.getPacket() instanceof PlayerInteractEntityC2SPacket && ((PlayerInteractEntityC2SPacket) e.getPacket()).getEntity(Objects.requireNonNull(mc.world.getServer()).getOverworld()) instanceof AbstractDonkeyEntity) {
            e.cancel();
        }
    }

}
