package cat.cattyn.fishhack.client.gui.button.settings;

import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.client.gui.button.SettingButton;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;

import java.awt.*;

/**
 * @author cattyngmd
 */

public class BindButton extends SettingButton {
    private final Module module;
    private boolean binding;

    public BindButton(Module module, int x, int y, int w, int h) {
        super(module, x, y, w, h);
        this.module = module;
    }

    @Override
    public void render(int mX, int mY) {
        drawButton();

        drawButton(mX, mY);

        mc.textRenderer.drawWithShadow(new MatrixStack(), "Bind", (float) (getX() + 6), (float) (getY() + 4),
                new Color(255, 255, 255, 255).getRGB());

        if (binding) {
            mc.textRenderer.drawWithShadow(new MatrixStack(), "...",
                    (float) ((getX() + getW() - 6) - mc.textRenderer.getWidth("...")), (float) (getY() + 4),
                    new Color(255, 255, 255, 255).getRGB());
        } else {
            mc.textRenderer.drawWithShadow(new MatrixStack(),
                    module.getBind() == InputUtil.fromTranslationKey("key.keyboard.unknown").getCode()
                            ? new LiteralText("NONE")
                            : InputUtil.fromKeyCode(module.getBind(), -1).getLocalizedText(),
                    (float) ((getX() + getW() - 6) - mc.textRenderer
                            .getWidth(module.getBind() == InputUtil.fromTranslationKey("key.keyboard.unknown").getCode()
                                    ? new LiteralText("NONE")
                                    : InputUtil.fromKeyCode(module.getBind(), -1).getLocalizedText())),
                    (float) (getY() + 4), new Color(255, 255, 255, 255).getRGB());
        }
    }

    @Override
    public void mouseDown(int mX, int mY, int mB) {
        if (isHover(getX(), getY(), getW(), getH() - 1, mX, mY)) {
            mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1f, 1f);
            binding = !binding;
        }
    }

    @Override
    public void keyPress(int key) {
        if (binding) {
            if (key == InputUtil.fromTranslationKey("key.keyboard.delete").getCode()
                    || key == InputUtil.fromTranslationKey("key.keyboard.backspace").getCode()) {
                getModule().setBind(InputUtil.fromTranslationKey("key.keyboard.unknown").getCode());
            } else {
                getModule().setBind(key);
            }
            binding = false;
        }
    }
}
