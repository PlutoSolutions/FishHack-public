package cat.cattyn.fishhack.mixins;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.event.events.EntityMoveEvent;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.client.managers.ModuleManager;
import cat.cattyn.fishhack.api.util.Wrapper;
import cat.cattyn.fishhack.client.modules.movement.NoSlowDown;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author cattyngmd
 */

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    @Inject(at = @At("RETURN"), method = "tick()V", cancellable = true)
    public void tick(CallbackInfo info) {
        if (Wrapper.mc.player != null && Wrapper.mc.world != null)
            ModuleManager.getModules().stream().filter(Module::isToggled).forEach(Module::onUpdate);
        else FishHack.getModuleManager().getModule("FakePlayer").disable();
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(MovementType type, Vec3d movement, CallbackInfo ci) {
        if (type == MovementType.SELF) {
            EntityMoveEvent event = new EntityMoveEvent(movement.x, movement.y, movement.z);
            FishHack.EventBus.post(event);
        }
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean proxy_tickMovement_isUsingItem(ClientPlayerEntity player) {
        if (FishHack.getModuleManager().getModule("NoSlowDown").isToggled() && NoSlowDown.INSTANCE.items.getValue()) {
            return false;
        }
        return player.isUsingItem();
    }
}
