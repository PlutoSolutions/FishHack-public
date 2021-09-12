package cat.cattyn.fishhack.client.modules.combat;

import cat.cattyn.fishhack.api.event.events.PacketEvent;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

import java.util.Objects;

@Module.Info(name = "Criticals", cat = Module.Category.Combat)
public class Criticals extends Module {
    Setting<Boolean> strict = register("Strict", false);

    @Subscribe
    public void onPacketSend(PacketEvent.Send e) {
        if (e.getPacket() instanceof PlayerInteractEntityC2SPacket) {
            if (((PlayerInteractEntityC2SPacket) e.getPacket()).getEntity(Objects.requireNonNull(mc.getServer()).getWorld(mc.player.world.getRegistryKey())) instanceof EndCrystalEntity)
                return;
            if (strict.getValue()) {
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.11, mc.player.getZ(), false));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.1100013579, mc.player.getZ(), false));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.0000013579, mc.player.getZ(), false));
            } else {
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.0625, mc.player.getZ(), false));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
            }
        }
    }

}
