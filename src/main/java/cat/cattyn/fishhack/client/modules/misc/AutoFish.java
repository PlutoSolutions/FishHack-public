package cat.cattyn.fishhack.client.modules.misc;

import cat.cattyn.fishhack.api.event.events.PacketEvent;
import cat.cattyn.fishhack.client.modules.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.util.Hand;

@Module.Info(name = "AutoFish", cat = Module.Category.Misc)
public class AutoFish extends Module {
    boolean shouldThrow = false;
    short ticks = 0;

    @Override
    public void onUpdate() {
        if (shouldThrow) {
            ticks++;
            if (ticks == 20) {
                mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
                ticks = 0;
                shouldThrow = false;
            }
        }
    }

    @Subscribe
    public void onPacketReceive(PacketEvent.Receive e) {
        if (e.getPacket() instanceof PlaySoundS2CPacket) {
            if (((PlaySoundS2CPacket) e.getPacket()).getSound().getId().toString().equalsIgnoreCase("minecraft:entity.fishing_bobber.splash")) {
                mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
                shouldThrow = true;
            }
        }
    }
}
