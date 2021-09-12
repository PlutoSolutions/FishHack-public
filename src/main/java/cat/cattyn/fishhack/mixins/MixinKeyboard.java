package cat.cattyn.fishhack.mixins;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.event.events.KeyEvent;
import cat.cattyn.fishhack.client.managers.ModuleManager;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author cattyngmd
 */

@Mixin(Keyboard.class)
public class MixinKeyboard {
    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "net/minecraft/client/util/InputUtil.isKeyPressed(JI)Z", ordinal = 5), cancellable = true)
    private void onKeyEvent(long windowPointer, int key, int scanCode, int action, int modifiers,
                            CallbackInfo callbackInfo) {
        KeyEvent event = new KeyEvent(key);
        FishHack.EventBus.post(event);
        ModuleManager.getModules().forEach(module -> {
            if (module.getBind() == key)
                module.toggle();
        });
    }
}
