package cat.cattyn.fishhack.client.gui.button.settings;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import cat.cattyn.fishhack.api.util.ColorUtil;
import cat.cattyn.fishhack.client.gui.button.SettingButton;
import cat.cattyn.fishhack.client.modules.client.ClickGuiMod;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

/**
 * @author cattyngmd
 */

public class SliderButton extends SettingButton {
    private final Setting<Double> setting;
    protected boolean dragging;
    protected double sliderWidth;

    SliderButton(Module module, Setting setting, int X, int Y, int W, int H) {
        super(module, X, Y, W, H);
        this.dragging = false;
        this.sliderWidth = 0;
        this.setting = setting;
    }

    protected void updateSlider(int mouseX) {
    }

    @Override
    public void render(int mX, int mY) {
        updateSlider(mX);

        drawButton();

        if (isHover(getX(), getY(), getW(), getH() - 1, mX, mY)) {
            FishHack.getClickGui().drawGradient((int) (getX() + 3 + (sliderWidth)), getY() + 1, getX() + getW() - 3,
                    getY() + getH(), new Color(45, 45, 45, 200).getRGB(), new Color(15, 15, 15, 200).getRGB());
            FishHack.getClickGui().drawGradient(getX() + 3, getY() + 1, (int) (getX() - 2 + (sliderWidth) + 5),
                    getY() + getH(),
                    new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                            ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()).getRGB(),
                    new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                            ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()).darker().getRGB());
        } else {
            FishHack.getClickGui().drawGradient((int) (getX() + 3 + (sliderWidth)), getY() + 1, getX() + getW() - 3,
                    getY() + getH(), new Color(55, 55, 55, 200).getRGB(), new Color(25, 25, 25, 200).getRGB());
            FishHack.getClickGui().drawGradient(getX() + 3, getY() + 1, (int) (getX() - 2 + (sliderWidth) + 5),
                    getY() + getH(),
                    ColorUtil.brighter(new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                            ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()), ClickGuiMod.INSTANCE.brighter.getValue().floatValue()).getRGB(),
                    new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                            ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()).getRGB());
        }

        mc.textRenderer.drawWithShadow(new MatrixStack(), setting.getName(), (float) (getX() + 6), (float) (getY() + 4),
                new Color(255, 255, 255, 255).getRGB());

        mc.textRenderer.drawWithShadow(new MatrixStack(),
                String.format("%." + setting.getInc() + "f", setting.getValue()).replace(",", "."),
                (float) ((getX() + getW() - 6) - mc.textRenderer.getWidth(
                        String.valueOf(String.format("%." + setting.getInc() + "f", setting.getValue())).replace(",", "."))),
                (float) (getY() + 4), new Color(255, 255, 255, 255).getRGB());

    }

    @Override
    public void mouseDown(int mX, int mY, int mB) {
        if (isHover(getX(), getY(), getW(), getH() - 1, mX, mY)) {
            dragging = true;
        }
    }

    @Override
    public void mouseUp(int mouseX, int mouseY) {
        dragging = false;
    }

    @Override
    public void close() {
        dragging = false;
    }

    public static class Slider extends SliderButton {
        private final Setting<Double> intSetting;

        public Slider(Module module, Setting setting, int X, int Y, int W, int H) {
            super(module, setting, X, Y, W, H);
            intSetting = setting;
        }

        @Override
        protected void updateSlider(final int mouseX) {
            final double diff = Math.min(getW(), Math.max(0, mouseX - getX()));
            final double min = intSetting.getMin();
            final double max = intSetting.getMax();
            sliderWidth = (getW() - 6) * (intSetting.getValue() - min) / (max - min);
            if (dragging) {
                if (diff == 0.0) {
                    intSetting.setValue(intSetting.getMin());
                } else {
                    intSetting.setValue(Double.parseDouble(String.format("%." + intSetting.getInc() + "f", diff / getW() * (max - min) + min).replace(",", ".")));
                }
            }
        }
    }
}
