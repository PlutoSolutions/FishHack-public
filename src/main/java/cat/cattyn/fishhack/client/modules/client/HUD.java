package cat.cattyn.fishhack.client.modules.client;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.event.events.RenderEvent;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.client.managers.ModuleManager;
import cat.cattyn.fishhack.api.setting.Setting;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author cattyngmd
 */

@Module.Info(name = "Hud", desc = "Different HUD elements", cat = Module.Category.Client)
public class HUD extends Module {

    public static HUD INSTANCE = new HUD();
    Setting<String> arraySort = register("Sort", Arrays.asList("Length", "ABC"), "Length");
    Setting<String> rendering = register("Rendering", Arrays.asList("Up", "Down"), "Up");
    Setting<Boolean> arrayList = register("ArrayList", true);
    Setting<Boolean> coords = register("Coords", true);
    Setting<Boolean> netherCoords = register("NetherCoords", true);
    Setting<Boolean> direction = register("Direction", true);
    Setting<Boolean> greeter = register("Greeter", false);

    private static float moveTowards(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = (end - current) * smoothSpeed;

        if (movement > 0) {
            movement = Math.max(minSpeed, movement);
            movement = Math.min(end - current, movement);
        } else if (movement < 0) {
            movement = Math.min(-minSpeed, movement);
            movement = Math.max(end - current, movement);
        }

        return current + movement;
    }

    @Subscribe
    public void onRender(RenderEvent event) {
        if (!nullCheck())
            return;
        int modCount = 0;

        //drawGradientSideways


        mc.textRenderer.drawWithShadow(event.getMatrixStack(),
                "Hack " + FishHack.MODVER, 2 + mc.textRenderer.getWidth("Fish"), 2, -1);

        mc.textRenderer.drawWithShadow(event.getMatrixStack(),
                "Fish", 2, 2, new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue()).getRGB());

        if (coords.getValue()) {
            mc.textRenderer.drawWithShadow(event.getMatrixStack(),
                    Formatting.GRAY + "XYZ " + Formatting.WHITE + String.format("%.1f", mc.player.getX()).replace(',', '.') + Formatting.GRAY + ", " + Formatting.WHITE + String.format("%.1f", mc.player.getY()).replace(',', '.') + Formatting.GRAY + ", " + Formatting.WHITE + String.format("%.1f", mc.player.getZ()).replace(',', '.') +
                            (netherCoords.getValue() ? Formatting.GRAY + " [" + Formatting.WHITE + String.format("%.1f", mc.world.getRegistryKey().getValue().getPath().equalsIgnoreCase("the_nether") ? mc.player.getX() * 8 : mc.player.getX() / 8).replace(',', '.') + Formatting.GRAY + ", " + Formatting.WHITE + String.format("%.1f", mc.world.getRegistryKey().getValue().getPath().equalsIgnoreCase("the_nether") ? mc.player.getZ() * 8 : mc.player.getZ() / 8).replace(',', '.') + Formatting.GRAY + "]" : ""),
                    2, mc.getWindow().getScaledHeight() - mc.textRenderer.fontHeight - (mc.currentScreen instanceof ChatScreen ? 15 : 1), -1);
        }

        if (direction.getValue()) {
            mc.textRenderer.drawWithShadow(event.getMatrixStack(), getFacing() + Formatting.GRAY + " [" + Formatting.WHITE + getTowards() + Formatting.GRAY + "]",
                    2, mc.getWindow().getScaledHeight() - mc.textRenderer.fontHeight * (coords.getValue() ? 2 : 1) - (mc.currentScreen instanceof ChatScreen ? 15 : 1), -1);

        }

        if (greeter.getValue()) {
            mc.textRenderer.drawWithShadow(event.getMatrixStack(), "Welcome " + mc.getSession().getUsername() + "!",
                    (mc.getWindow().getScaledWidth() / 2) - (mc.textRenderer.getWidth("Welcome " + mc.getSession().getUsername() + "!") / 2), 3,
                    new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue()).getRGB());
        }

        if (arrayList.getValue()) {
            int[] counter = {1};
            // Formatting.GRAY + " [" + Formatting.WHITE + info + Formatting.GRAY + "]"
            ArrayList<Module> modules = new ArrayList<>(ModuleManager.getModules());
            switch (arraySort.getValue()) {
                case ("Length"):
                    modules.sort(Comparator.comparing(m -> -(mc.textRenderer.getWidth(m.getName()) + mc.textRenderer.getWidth(m.getDisplayInfo()))));
                case ("ABC"):
                    modules.sort(Comparator.comparing(m -> getName() + getDisplayInfo()));
            }
            for (int i = 0; i < modules.size(); i++) {
                Module module;
                module = modules.get(i);
                if (module.isDrawn() && module.isToggled()) {
                    int x = mc.getWindow().getScaledWidth();
                    int y = rendering.getValue().equalsIgnoreCase("Up") ? 3 + (modCount * 10) : mc.getWindow().getScaledHeight() - mc.textRenderer.fontHeight - 1 - (modCount * 10);
                    int lWidth = mc.textRenderer.getWidth(module.getName() + module.getDisplayInfo());
                    if (module.animPos < lWidth && module.isToggled()) {
                        module.animPos = moveTowards(module.animPos, lWidth + 1, 0.01f + 3 / 30, 0.1f);
                    } else if (module.animPos > 1.5f && !module.isToggled()) {
                        module.animPos = moveTowards(module.animPos, -1.5f, 0.01f + 3 / 30, 0.1f);
                    } else if (module.animPos <= 1.5f && !module.isToggled()) {
                        module.animPos = -1f;
                    }
                    if (module.animPos > lWidth && module.isToggled()) {
                        module.animPos = lWidth;
                    }

                    x -= module.animPos;

                    mc.textRenderer.drawWithShadow(event.getMatrixStack(), module.getName() + module.getDisplayInfo(), x - 2, y,
                            modCount % 2 == 0 ? new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue()).getRGB() : new Color(255, 255, 255).getRGB());

                    y += mc.textRenderer.fontHeight;
                    modCount++;
                    counter[0]++;
                }
            }
        }

    }

    private String getFacing() {
        switch (MathHelper.floor((double) (mc.player.getYaw() * 8.0F / 360.0F) + 0.5D) & 7) {
            case 0:
                return "South";
            case 1:
                return "South West";
            case 2:
                return "West";
            case 3:
                return "North West";
            case 4:
                return "North";
            case 5:
                return "North East";
            case 6:
                return "East";
            case 7:
                return "South East";
        }
        return "Invalid";
    }

    private String getTowards() {
        switch (MathHelper.floor((double) (mc.player.getYaw() * 8.0F / 360.0F) + 0.5D) & 7) {
            case 0:
                return "+Z";
            case 1:
                return "-X +Z";
            case 2:
                return "-X";
            case 3:
                return "-X -Z";
            case 4:
                return "-Z";
            case 5:
                return "+X -Z";
            case 6:
                return "+X";
            case 7:
                return "+X +Z";
        }
        return "Invalid";
    }

}
