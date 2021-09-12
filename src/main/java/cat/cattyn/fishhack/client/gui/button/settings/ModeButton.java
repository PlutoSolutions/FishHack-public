package cat.cattyn.fishhack.client.gui.button.settings;

import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import cat.cattyn.fishhack.client.gui.button.SettingButton;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;

import java.awt.*;

/**
 * @author cattyngmd
 */

public class ModeButton extends SettingButton {
    private final Setting<String> setting;

    public ModeButton(Module module, Setting setting, int X, int Y, int W, int H) {
        super(module, X, Y, W, H);
        this.setting = setting;
    }

    @Override
    public void render(int mX, int mY) {
        drawButton();

        drawButton(mX, mY);

        mc.textRenderer.drawWithShadow(new MatrixStack(), setting.getName(), (float) (getX() + 6), (float) (getY() + 4),
                new Color(255, 255, 255, 255).getRGB());
        mc.textRenderer.drawWithShadow(new MatrixStack(), setting.getValue(),
                (float) ((getX() + getW() - 6) - mc.textRenderer.getWidth(setting.getValue())),
                (float) (getY() + 4), new Color(255, 255, 255, 255).getRGB());

    }

    @Override
    public void mouseDown(int mX, int mY, int mB) {
        if (isHover(getX(), getY(), getW(), getH() - 1, mX, mY)) {
            mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1f, 1f);
            if (mB == 0) {
                int i = 0;
                int enumIndex = 0;
                for (String enumName : setting.getModes()) {
                    if (enumName.equals(setting.getValue()))
                        enumIndex = i;
                    i++;
                }
                if (enumIndex == setting.getModes().size() - 1) {
                    setting.setValue(setting.getModes().get(0));
                } else {
                    enumIndex++;
                    i = 0;
                    for (String enumName : setting.getModes()) {
                        if (i == enumIndex)
                            setting.setValue(enumName);
                        i++;
                    }
                }
            } else if (mB == 1) {
                int i = 0;
                int enumIndex = 0;
                for (String enumName : setting.getModes()) {
                    if (enumName.equals(setting.getValue()))
                        enumIndex = i;
                    i++;
                }
                if (enumIndex == 0) {
                    setting.setValue(setting.getModes().get(setting.getModes().size() - 1));
                } else {
                    enumIndex--;
                    i = 0;
                    for (String enumName : setting.getModes()) {
                        if (i == enumIndex)
                            setting.setValue(enumName);
                        i++;
                    }
                }
            }
        }
    }
}
