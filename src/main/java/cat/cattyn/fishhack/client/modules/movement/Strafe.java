package cat.cattyn.fishhack.client.modules.movement;

import cat.cattyn.fishhack.api.event.events.EntityMoveEvent;
import cat.cattyn.fishhack.api.event.events.PacketEvent;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

@Module.Info(name = "Strafe", desc = "retracted", cat = Module.Category.Movement)
public class Strafe extends Module {

    public Setting<Double> speed = register("Speed", 2.6D, 0, 10, 1);

	//retracted lol

}
