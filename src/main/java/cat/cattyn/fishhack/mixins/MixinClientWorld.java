package cat.cattyn.fishhack.mixins;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.event.events.EntitySpawnEvent;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class MixinClientWorld {

    @Inject(method = "addEntityPrivate", at = @At("TAIL"))
    private void onSpawnEntityPrivate(int id, Entity entity, CallbackInfo info) {
        if (entity != null) FishHack.EventBus.post(new EntitySpawnEvent(entity));
    }

}
