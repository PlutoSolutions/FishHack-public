package cat.cattyn.fishhack.client.modules.client;

import cat.cattyn.fishhack.api.event.events.KeyEvent;
import cat.cattyn.fishhack.api.event.events.RenderEvent;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.client.managers.ModuleManager;
import cat.cattyn.fishhack.api.util.Renderer;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.InputUtil;

import java.awt.*;
import java.util.List;

/**
 * @author cattyngmd
 */

@Module.Info(name = "TabGui", desc = "GUI on the side of your screen", cat = Module.Category.Client)
public class TabGui extends Module {
    private int selection;
    private int moduleSelection;
    private boolean open;

    @Subscribe
    public void onRender(RenderEvent event) {
        if (!nullCheck())
            return;

        Renderer.INSTANCE.setup2DRender(true);

        List<Module> modules = ModuleManager.getModules(Category.values()[selection]);

        int y = 0;
        int yo = 0;

        DrawableHelper.fill(event.getMatrixStack(), 2, 18, 63,
                (mc.textRenderer.fontHeight + 2) * Category.values().length + 19, new Color(0, 0, 0, 125).getRGB());
        if (!open)
            DrawableHelper.fill(event.getMatrixStack(), 3, 18 + selection * (mc.textRenderer.fontHeight + 2) + 1, 62,
                    18 + selection * (mc.textRenderer.fontHeight + 2) + 11, new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue(), 200).getRGB());

        for (Category c : Category.values()) {
            mc.textRenderer.drawWithShadow(event.getMatrixStack(), c.toString(), 9, 20 + y, -1);
            y += mc.textRenderer.fontHeight + 2;
        }

        if (open) {

            if (modules.size() == 0)
                return;

            DrawableHelper.fill(event.getMatrixStack(), 63, 18, 133,
                    (mc.textRenderer.fontHeight + 2) * modules.size() + 19, new Color(0, 0, 0, 125).getRGB());
            DrawableHelper.fill(event.getMatrixStack(), 64, 18 + moduleSelection * (mc.textRenderer.fontHeight + 2) + 1,
                    132, 18 + moduleSelection * (mc.textRenderer.fontHeight + 2) + 11,
                    new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue(), 200).getRGB());

            for (Module m : ModuleManager.getModules(Category.values()[selection])) {
                mc.textRenderer.drawWithShadow(event.getMatrixStack(), m.getName(), 66, 20 + yo,
                        m.isToggled() ? new Color(ClickGuiMod.INSTANCE.r.getValue().intValue(), ClickGuiMod.INSTANCE.g.getValue().intValue(), ClickGuiMod.INSTANCE.b.getValue().intValue()).getRGB() : -1);
                yo += mc.textRenderer.fontHeight + 2;
            }
        }

        Renderer.INSTANCE.end2DRender();

    }

    @Subscribe
    public void onKey(KeyEvent event) {
        List<Module> modules = ModuleManager.getModules(Category.values()[selection]);
        int code = event.getCode();
        if (code == InputUtil.fromTranslationKey("key.keyboard.up").getCode()) {
            if (!open) {
                if (selection <= 0)
                    selection = Category.values().length - 1;
                else
                    selection--;
            } else {
                if (moduleSelection <= 0)
                    moduleSelection = modules.size() - 1;
                else
                    moduleSelection--;
            }
        }

        if (code == InputUtil.fromTranslationKey("key.keyboard.down").getCode()) {
            if (!open) {
                if (selection >= Category.values().length - 1)
                    selection = 0;
                else
                    selection++;
            } else {
                if (moduleSelection >= modules.size() - 1)
                    moduleSelection = 0;
                else
                    moduleSelection++;
            }
        }

        if (code == InputUtil.fromTranslationKey("key.keyboard.right").getCode()
                || code == InputUtil.fromTranslationKey("key.keyboard.enter").getCode()) {
            if (!open) {
                open = !modules.isEmpty();
            } else {
                if (!modules.get(moduleSelection).getName().equalsIgnoreCase("TabGui"))
                    modules.get(moduleSelection).toggle();
            }
        }

        if (code == InputUtil.fromTranslationKey("key.keyboard.left").getCode()) {
            open = false;
            moduleSelection = 0;
        }
    }

}
