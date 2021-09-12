package cat.cattyn.fishhack.client.gui.button.settings;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import cat.cattyn.fishhack.client.gui.button.SettingButton;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;

import java.awt.*;

/**
 * @author cattyngmd
 */

public class BooleanButton extends SettingButton {
    private final Setting<Boolean> setting;

    public BooleanButton(Module module, Setting setting, int X, int Y, int W, int H) {
        super(module, X, Y, W, H);
        this.setting = setting;
    }

    @Override
    public void render(int mX, int mY) {
        drawButton();

        if (setting.getValue()) {
            drawButton(mX, mY);
            mc.textRenderer.drawWithShadow(new MatrixStack(), setting.getName(), (float) (getX() + 6),
                    (float) (getY() + 4), -1);
        } else {
            if (isHover(getX(), getY(), getW(), getH() - 1, mX, mY)) {
                FishHack.getClickGui().drawGradient(getX() + 3, getY() + 1, getX() + getW() - 3, getY() + getH(),
                        new Color(45, 45, 45, 200).getRGB(), new Color(15, 15, 15, 200).getRGB());
            } else {
                FishHack.getClickGui().drawGradient(getX() + 3, getY() + 1, getX() + getW() - 3, getY() + getH(),
                        new Color(55, 55, 55, 200).getRGB(), new Color(25, 25, 25, 200).getRGB());
            }

            mc.textRenderer.drawWithShadow(new MatrixStack(), setting.getName(), (float) (getX() + 6),
                    (float) (getY() + 4), new Color(127, 127, 127, 255).getRGB());
        }
    }

    @Override
    public void mouseDown(int mX, int mY, int mB) {
        if (isHover(getX(), getY(), getW(), getH() - 1, mX, mY) && (mB == 0 || mB == 1)) {
            mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1f, 1f);
            setting.setValue(!setting.getValue());
        }
    }
}
