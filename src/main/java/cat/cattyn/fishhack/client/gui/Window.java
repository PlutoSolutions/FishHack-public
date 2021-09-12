package cat.cattyn.fishhack.client.gui;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.client.managers.ConfigManager;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.client.managers.ModuleManager;
import cat.cattyn.fishhack.api.util.ColorUtil;
import cat.cattyn.fishhack.api.util.Wrapper;
import cat.cattyn.fishhack.client.gui.button.ModuleButton;
import cat.cattyn.fishhack.client.gui.button.SettingButton;
import cat.cattyn.fishhack.client.modules.client.ClickGuiMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author cattyngmd
 */

public class Window implements Component {
    private final ArrayList<ModuleButton> buttons = new ArrayList<>();
    private final Module.Category category;
    private final int W;
    private final int H;
    private final ArrayList<ModuleButton> buttonsBeforeClosing = new ArrayList<>();
    private int X;
    private int Y;
    private int dragX;
    private int dragY;
    private boolean open = true;
    private boolean dragging;
    private int totalHeightForBars;
    private int showingButtonCount;
    private boolean opening;
    private boolean closing;
    private final ItemStack Icon;

    public Window(Module.Category category, ItemStack icon, int x, int y, int w, int h) {
        this.category = category;

        try {
            FileReader reader = new FileReader(ConfigManager.getMainFolder().getAbsolutePath() + "\\Gui.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith(category.name())) {
                    String[] positions = line.split(":");

                    X = Integer.parseInt(positions[1]);
                    Y = Integer.parseInt(positions[2]);
                } else continue;
            }
        } catch (IOException e) {
            e.printStackTrace();
            X = x;
            Y = y;
        }

        W = w;
        H = h;
        Icon = icon;

        int yOffset = Y + H;

        for (Module module : ModuleManager.getModules(category)) {
            ModuleButton button = new ModuleButton(module, X, yOffset, W, H);
            buttons.add(button);
            yOffset += H;
            totalHeightForBars++;
        }
        showingButtonCount = buttons.size();
    }

    @Override
    public void render(int mX, int mY) {

        if (dragging) {
            X = dragX + mX;
            Y = dragY + mY;
        }

        FishHack.getClickGui().drawGradient(X, Y, X + W, Y + H,
                new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                        ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()).getRGB(),
                ColorUtil.brighter(new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(),
                        ClickGuiMod.INSTANCE.b.getValue().intValue(), ClickGuiMod.INSTANCE.a.getValue().intValue()), ClickGuiMod.INSTANCE.brighter.getValue().floatValue()).getRGB());
        Wrapper.mc.textRenderer.drawWithShadow(new MatrixStack(), category.name(), X + 4, Y + 4,
                new Color(234, 234, 234, 255).getRGB());

        //icon
        if (Icon != null) {
            boolean isBlock = Icon != null && Icon.getItem() instanceof BlockItem;
            RenderSystem.getModelViewStack().push();
            RenderSystem.getModelViewStack().scale(0.8f, 0.8f, 1f);

            DiffuseLighting.enableGuiDepthLighting();
            if (!isBlock) Icon.addEnchantment(Enchantments.MENDING, 1);
            MinecraftClient.getInstance().getItemRenderer().renderInGui(
                    Icon, (int) ((X + W - (isBlock ? 13 : 14)) / 0.8), (int) ((Y + 1) / 0.8));
            DiffuseLighting.disableGuiDepthLighting();

            RenderSystem.getModelViewStack().pop();
            RenderSystem.applyModelViewMatrix();
        }

        if (open || opening || closing) {
            DrawableHelper.fill(new MatrixStack(), X, Y + H, X + W, Y + H + 1, new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue()).darker().darker().getRGB());

            int modY = Y + H + 1;
            totalHeightForBars = 0;

            int moduleRenderCount = 0;
            for (ModuleButton moduleButton : buttons) {
                moduleRenderCount++;

                if (moduleRenderCount < showingButtonCount + 1) {
                    moduleButton.setX(X);
                    moduleButton.setY(modY);

                    moduleButton.render(mX, mY);

                    if (!moduleButton.isOpen() && opening && buttonsBeforeClosing.contains(moduleButton)) {
                        moduleButton.processRightClick();
                    }

                    modY += H;
                    totalHeightForBars++;

                    if (moduleButton.isOpen() || moduleButton.isOpening() || moduleButton.isClosing()) {

                        int settingRenderCount = 0;
                        for (SettingButton settingButton : moduleButton.getButtons()) {
                            settingRenderCount++;

                            if (settingRenderCount < moduleButton.getShowingModuleCount() + 1) {
                                settingButton.setX(X);
                                settingButton.setY(modY);

                                settingButton.render(mX, mY);

                                modY += H;
                                totalHeightForBars++;
                            }
                        }
                    }
                }
            }
            DrawableHelper.fill(new MatrixStack(), X, Y + H + ((totalHeightForBars) * H) + 2, X + W,
                    Y + H + ((totalHeightForBars) * H) + 3, new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue()).darker().darker().getRGB());
        }

        if (opening) {
            showingButtonCount++;
            if (showingButtonCount == buttons.size()) {
                opening = false;
                open = true;
                buttonsBeforeClosing.clear();
            }
        }

        if (closing) {
            showingButtonCount--;
            if (showingButtonCount == 0) {
                closing = false;
                open = false;
            }
        }

    }

    @Override
    public void mouseDown(int mX, int mY, int mB) {
        if (isHover(X, Y, W, H, mX, mY)) {
            if (mB == 0) {
                dragging = true;
                dragX = X - mX;
                dragY = Y - mY;
            } else if (mB == 1) {
                if (open && !opening && !closing) {
                    showingButtonCount = buttons.size();
                    closing = true;
                    for (ModuleButton button : buttons) {
                        if (button.isOpen()) {
                            button.processRightClick();
                            buttonsBeforeClosing.add(button);
                        }
                    }
                } else if (!open && !opening && !closing) {
                    showingButtonCount = 1;
                    opening = true;
                }
            }
        }

        if (open) {
            for (ModuleButton button : buttons) {
                button.mouseDown(mX, mY, mB);
            }
        }
    }

    @Override
    public void mouseUp(int mX, int mY) {
        dragging = false;

        if (open) {
            for (ModuleButton button : buttons) {
                button.mouseUp(mX, mY);
            }
        }
    }

    @Override
    public void keyPress(int key) {
        if (open) {
            for (ModuleButton button : buttons) {
                button.keyPress(key);
            }
        }
    }

    @Override
    public void close() {
        for (ModuleButton button : buttons) {
            button.close();
        }
    }

    private boolean isHover(int X, int Y, int W, int H, int mX, int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }

    public Module.Category getCategory() {
        return category;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getH() {
        return H;
    }

}
