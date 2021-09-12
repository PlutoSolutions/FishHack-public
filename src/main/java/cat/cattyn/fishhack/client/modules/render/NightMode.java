package cat.cattyn.fishhack.client.modules.render;

import cat.cattyn.fishhack.api.event.events.PacketEvent;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

import java.util.Arrays;

/**
 * @author 1Amfero1
 */

@Module.Info(name = "NightMode", desc = "Changes client world time", cat = Module.Category.Render)
public class NightMode extends Module {
    Setting<String> modes = register("Test", Arrays.asList("Night", "Sun", "Custom"), "Night");

    Setting<Double> custom = register("Custom", 10, 1, 24, 1);

    @Override
    public void onUpdate() {
        setDisplayInfo(modes.getValue());

        assert mc.world != null;
        if (modes.getValue().equals("Night"))
            mc.world.setTimeOfDay(18000);
        if (modes.getValue().equals("Sun"))
            mc.world.setTimeOfDay(1000);
        if (modes.getValue().equals("Custom")) {
            mc.world.setTimeOfDay((long) (custom.getValue() * 1000));
        }
    }

    @Subscribe
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            event.cancel();
        }
    }
}
