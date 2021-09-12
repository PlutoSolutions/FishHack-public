package cat.cattyn.fishhack.client.modules.misc;

import cat.cattyn.fishhack.client.modules.Module;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity.RemovalReason;

import java.util.UUID;

@Module.Info(name = "FakePlayer", cat = Module.Category.Misc)
public class FakePlayer extends Module {
    OtherClientPlayerEntity fakePlayer;

    @Override
    public void onEnable() {
        fakePlayer = new OtherClientPlayerEntity(mc.world, new GameProfile(UUID.fromString("e1964300-7751-4630-9c53-4d2a8767ed45"), name));
        fakePlayer.copyPositionAndRotation(mc.player);
        fakePlayer.setHealth(12);
        mc.world.addEntity(-101, fakePlayer);
    }

    @Override
    public void onDisable() {
        if (fakePlayer != null) fakePlayer.remove(RemovalReason.DISCARDED);
    }

    @Override
    public void onUpdate() {
        if (!nullCheck()) toggle();
    }

}
