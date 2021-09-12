package cat.cattyn.fishhack.client.gui;

import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.util.Wrapper;
import cat.cattyn.fishhack.client.modules.client.ClickGuiMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author cattyngmd
 */

public class ClickGui extends Screen {
    public static ArrayList<Window> windows = new ArrayList<>();

    public ClickGui() {
        super(new LiteralText("ClickGui"));
        windows = new ArrayList<>();
        int xOffset = 100;
        for (Module.Category category : Module.Category.values()) {
            new ItemStack(Items.COD);
            ItemStack thisIcon = switch (category.name()) {
                case "Combat" -> new ItemStack(Items.NETHERITE_SWORD);
                case "Player" -> new ItemStack(Items.NETHERITE_HELMET);
                case "Movement" -> new ItemStack(Items.FEATHER);
                case "Misc" -> new ItemStack(Items.STICK);
                case "Render" -> new ItemStack(Items.AMETHYST_SHARD);
                case "Client" -> new ItemStack(Items.GRASS_BLOCK);
                default -> new ItemStack(Items.COD);
            };
            windows.add(new Window(category, thisIcon, xOffset, 3, 95, 15));
            xOffset += 100;
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        if (ClickGuiMod.INSTANCE.tint.getValue()) {
            DrawableHelper.fill(matrices, 0, 0, MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight(), new Color(0, 0, 0, 125).getRGB());
        }
        for (Window window : windows) {
            window.render(mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        windows.forEach(window -> window.mouseDown((int) mouseX, (int) mouseY, mouseButton));
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int state) {
        windows.forEach(window -> window.mouseUp((int) mouseX, (int) mouseY));
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Window window : windows) {
            window.keyPress(keyCode);
        }

        if (keyCode == InputUtil.fromTranslationKey("key.keyboard.escape").getCode()) {
            Wrapper.mc.setScreen(null);
            ClickGuiMod.INSTANCE.toggle();
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void drawGradient(int left, int top, int right, int bottom, int startColor, int endColor) {
        // drawGradient(left, top, right, bottom, startColor, endColor);
        this.fillGradient(new MatrixStack(), left, top, right, bottom, startColor, endColor);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (amount < 0) {
            for (Window window : windows) {
                window.setY(window.getY() - 8);
            }
        } else if (amount > 0) {
            for (Window window : windows) {
                window.setY(window.getY() + 8);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
}
