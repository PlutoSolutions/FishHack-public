package cat.cattyn.fishhack.mixins;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.event.events.RenderEvent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author cattyngmd
 */

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/scoreboard/Scoreboard.getObjectiveForSlot(I)Lnet/minecraft/scoreboard/ScoreboardObjective;"))
    public void render(MatrixStack matrixStack, float float_1, CallbackInfo ci) {

        RenderSystem.setShaderColor(1, 1, 1, 1);

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        RenderSystem.disableCull();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        //if(texture) RenderSystem.enableTexture();
        /*else */
        RenderSystem.disableTexture();

        // actually draw
        RenderEvent event = new RenderEvent(matrixStack);
        FishHack.EventBus.post(event);
        //Tessellator.getInstance().draw();

        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        //GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
