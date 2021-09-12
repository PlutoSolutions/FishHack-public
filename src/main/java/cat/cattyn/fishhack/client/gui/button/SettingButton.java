package cat.cattyn.fishhack.client.gui.button;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.util.ColorUtil;
import cat.cattyn.fishhack.client.gui.Component;
import cat.cattyn.fishhack.client.modules.client.ClickGuiMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

/**
 * @author cattyngmd
 */

public class SettingButton implements Component {
    public final MinecraftClient mc = MinecraftClient.getInstance();
    private final int H;
    private Module module;
    private int X;
    private int Y;
    private int W;

    public SettingButton(Module module, int x, int y, int w, int h) {
        this.module = module;
        X = x;
        Y = y;
        W = w;
        H = h;
    }

    @Override
    public void render(int mX, int mY) {
    }

    @Override
    public void mouseDown(int mX, int mY, int mB) {
    }

    @Override
    public void mouseUp(int mX, int mY) {
    }

    @Override
    public void keyPress(int key) {
    }

    @Override
    public void close() {
    }

    public void drawButton() {
        Color darkOutlineColor = new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue()).darker().darker();

        DrawableHelper.fill(new MatrixStack(), getX(), getY(), getX() + 3, getY() + getH() + 1,
                darkOutlineColor.getRGB());
        DrawableHelper.fill(new MatrixStack(), getX(), getY(), getX() + getW(), getY() + 1, darkOutlineColor.getRGB());
        DrawableHelper.fill(new MatrixStack(), getX() + getW(), getY(), getX() + getW() - 3, getY() + getH() + 1,
                darkOutlineColor.getRGB());
        DrawableHelper.fill(new MatrixStack(), getX(), getY() + getH(), getX() + getW(), getY() + getH() + 1,
                darkOutlineColor.getRGB());
    }

    public void drawButton(int mX, int mY) {
        if (isHover(getX(), getY(), getW(), getH() - 1, mX, mY)) {
            FishHack.getClickGui().drawGradient(getX() + 3, getY() + 1, getX() + getW() - 3, getY() + getH(),
                    new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                            ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()).getRGB(),
                    new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                            ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()).darker().getRGB());
        } else {
            FishHack.getClickGui().drawGradient(getX() + 3, getY() + 1, getX() + getW() - 3, getY() + getH(),
                    ColorUtil.brighter(new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                            ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()), ClickGuiMod.INSTANCE.brighter.getValue().floatValue()).getRGB(),
                    new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                            ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()).getRGB());
        }
    }

    public int getHeight() {
        return H;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getW() {
        return W;
    }

    public void setW(int w) {
        W = w;
    }

    public int getH() {
        return H;
    }

    public boolean isHover(int X, int Y, int W, int H, int mX, int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }
}
