package cat.cattyn.fishhack.client.gui.button;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import cat.cattyn.fishhack.api.util.ColorUtil;
import cat.cattyn.fishhack.client.gui.Component;
import cat.cattyn.fishhack.client.gui.button.settings.*;
import cat.cattyn.fishhack.client.modules.client.ClickGuiMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author cattyngmd
 */

public class ModuleButton implements Component {
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final Module module;
    private final ArrayList<SettingButton> buttons = new ArrayList<>();
    private final int W;
    private final int H;
    private int X;
    private int Y;
    private boolean open;
    private int showingModuleCount;
    private boolean opening;
    private boolean closing;

    public ModuleButton(Module module, int x, int y, int w, int h) {
        this.module = module;
        X = x;
        Y = y;
        W = w;
        H = h;

        int n = 0;
        for (Setting setting : FishHack.getSettingManager().getSettingsForMod(module)) {

            SettingButton settingButton = null;

            switch (setting.getType()) {
                case B: {
                    settingButton = new BooleanButton(module, setting, X, Y + H + n, W, H);
                    break;
                }
                case N: {
                    settingButton = new SliderButton.Slider(module, setting, X, Y + H + n, W, H);
                    break;
                }
                case M: {
                    settingButton = new ModeButton(module, setting, X, Y + H + n, W, H);
                    break;
                }

                default:
                    break;
            }

            buttons.add(settingButton);
            n += H;

        }

        buttons.add(new DrawnButton(module, X, Y + H + n, W, H));
        buttons.add(new BindButton(module, X, Y + H + n, W, H));
    }

    @Override
    public void render(int mX, int mY) {
        Color darkOutlineColor = new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue()).darker().darker();

        DrawableHelper.fill(new MatrixStack(), X, Y, X + 2, Y + H + 2, darkOutlineColor.getRGB());
        DrawableHelper.fill(new MatrixStack(), X, Y, X + W, Y + 1, darkOutlineColor.getRGB());
        DrawableHelper.fill(new MatrixStack(), X + W, Y, X + W - 2, Y + H + 2, darkOutlineColor.getRGB());
        DrawableHelper.fill(new MatrixStack(), X, Y + H, X + W, Y + H + 1, darkOutlineColor.getRGB());

        if (module.isToggled()) {
            if (isHover(X, Y, W, H - 1, mX, mY)) {
                FishHack.getClickGui().drawGradient(X + 2, Y + 1, X + W - 2, Y + H,
                        new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                                ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()).getRGB(),
                        new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                                ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()).darker()
                                .getRGB());
            } else {
                FishHack.getClickGui().drawGradient(X + 2, Y + 1, X + W - 2, Y + H,
                        ColorUtil.brighter(new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                                ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()), ClickGuiMod.INSTANCE.brighter.getValue().floatValue()).getRGB(),
                        new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                                ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()).getRGB());
            }
            mc.textRenderer.drawWithShadow(new MatrixStack(), module.getName(), (float) (X + 5), (float) (Y + 4), -1);
        } else {
            if (isHover(X, Y, W, H - 1, mX, mY)) {
                FishHack.getClickGui().drawGradient(X + 2, Y + 1, X + W - 2, Y + H, new Color(45, 45, 45, 200).getRGB(),
                        new Color(15, 15, 15, 200).getRGB());
            } else {
                FishHack.getClickGui().drawGradient(X + 2, Y + 1, X + W - 2, Y + H, new Color(55, 55, 55, 200).getRGB(),
                        new Color(25, 25, 25, 200).getRGB());
            }

            mc.textRenderer.drawWithShadow(new MatrixStack(), module.getName(), (float) (X + 5), (float) (Y + 4),
                    new Color(127, 127, 127, 255).getRGB());
        }

        if (!FishHack.getSettingManager().getSettingsForMod(module).isEmpty()) {
//			private final ResourceLocation logo = new ResourceLocation("plutonium/gui/gear.png");
            MatrixStack stack = new MatrixStack();
            if (module.isToggled()) {
                mc.textRenderer.drawWithShadow(stack, "...",
                        (float) (X + W - mc.textRenderer.getWidth("...") - 4), (float) (Y + 4),
                        new Color(255, 255, 255, 255).getRGB());
            } else {
                mc.textRenderer.drawWithShadow(stack, "...",
                        (float) (X + W - mc.textRenderer.getWidth("...") - 4), (float) (Y + 4),
                        new Color(127, 127, 127, 255).getRGB());
            }

        }

        if (opening) {
            showingModuleCount++;
            if (showingModuleCount == buttons.size()) {
                opening = false;
                open = true;
            }
        }

        if (closing) {
            showingModuleCount--;
            if (showingModuleCount == 0) {
                closing = false;
                open = false;
            }
        }

        if (ClickGuiMod.INSTANCE.desc.getValue() && isHover(X, Y, W, H - 1, mX, mY)) {
            //DrawableHelper.fill(new MatrixStack(), mX - 2, mY - 2, (int) (mX + mc.textRenderer.getWidth(module.getDescription()) + 1 * 1.5D), mY + mc.textRenderer.fontHeight + 2, new Color(50, 50, 50).getRGB());
            mc.textRenderer.drawWithShadow(new MatrixStack(), module.getDescription(), 0, mc.getWindow().getScaledHeight() - 10, -1);
        }
    }

    @Override
    public void mouseDown(int mX, int mY, int mB) {
        if (isHover(X, Y, W, H - 1, mX, mY)) {
            if (mB == 0) {
                mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1f, 1f);
                module.toggle();
                if (module.getName().equals("ClickGUI")) {
                    mc.setScreen(null);
                }
            } else if (mB == 1) {
                processRightClick();
            }
        }

        if (open) {
            for (SettingButton settingButton : buttons) {
                settingButton.mouseDown(mX, mY, mB);
            }
        }
    }

    @Override
    public void mouseUp(int mX, int mY) {
        for (SettingButton settingButton : buttons) {
            settingButton.mouseUp(mX, mY);
        }
    }

    @Override
    public void keyPress(int key) {
        for (SettingButton settingButton : buttons) {
            settingButton.keyPress(key);
        }
    }

    @Override
    public void close() {
        for (SettingButton button : buttons) {
            button.close();
        }
    }

    private boolean isHover(int X, int Y, int W, int H, int mX, int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }

    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    public boolean isOpen() {
        return open;
    }

    public Module getModule() {
        return module;
    }

    public ArrayList<SettingButton> getButtons() {
        return buttons;
    }

    public int getShowingModuleCount() {
        return showingModuleCount;
    }

    public boolean isOpening() {
        return opening;
    }

    public boolean isClosing() {
        return closing;
    }

    public void processRightClick() {
        if (!open) {
            showingModuleCount = 0;
            opening = true;
        } else {
            showingModuleCount = buttons.size();
            closing = true;
        }
    }
}
