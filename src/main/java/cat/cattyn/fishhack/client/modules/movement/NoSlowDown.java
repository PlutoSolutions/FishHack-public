package cat.cattyn.fishhack.client.modules.movement;

import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Module.Info(name = "NoSlowDown", cat = Module.Category.Movement)
public class NoSlowDown extends Module {
    public static NoSlowDown INSTANCE = new NoSlowDown();

    public Setting<Boolean> items = register("Items", true);
    Setting<Boolean> inventory = register("Inventory", true);
    Setting<Boolean> shift = register("Shift", false);

    @Override
    public void onUpdate() {
        if (inventory.getValue() && mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen) && !(mc.currentScreen instanceof SignEditScreen) && !(mc.currentScreen instanceof BookScreen)) {
            for (KeyBinding k : new KeyBinding[]{mc.options.keyForward, mc.options.keyBack,
                    mc.options.keyLeft, mc.options.keyRight, mc.options.keyJump, mc.options.keySprint}) {
                k.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(),
                        InputUtil.fromTranslationKey(k.getBoundKeyTranslationKey()).getCode()));
            }
            if (shift.getValue()) mc.options.keySneak.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(),
                    InputUtil.fromTranslationKey(mc.options.keySneak.getBoundKeyTranslationKey()).getCode()));
        }
    }
}
