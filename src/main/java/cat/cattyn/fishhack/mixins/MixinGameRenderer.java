package cat.cattyn.fishhack.mixins;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.event.events.Render3DEvent;
import cat.cattyn.fishhack.api.event.events.Render3DEvent.EventRender3DNoBob;
import cat.cattyn.fishhack.api.util.Renderer3D;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Shadow
    private int ticks;
    @Shadow
    @Final
    private Camera camera;
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    public abstract void loadProjectionMatrix(Matrix4f matrix4f);

    @Shadow
    protected abstract void bobView(MatrixStack matrixStack, float f);

    @Shadow
    protected abstract void bobViewWhenHurt(MatrixStack matrixStack, float f);

    @Shadow
    protected abstract double getFov(Camera camera, float tickDelta, boolean changingFov);

    @Shadow
    public abstract Matrix4f getBasicProjectionMatrix(double d);

    @SuppressWarnings("resource")
    @Inject(method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V", at = @At(value = "INVOKE", target = "com/mojang/blaze3d/systems/RenderSystem.clear(IZ)V"))
    private void onRenderWorld(float partialTicks, long finishTimeNano, MatrixStack matrixStack1, CallbackInfo ci) {
        if (MinecraftClient.getInstance().getEntityRenderDispatcher().camera == null)
            return;
        RenderSystem.clearColor(1, 1, 1, 1);
        MatrixStack matrixStack = new MatrixStack();
        double d = this.getFov(camera, partialTicks, true);
        matrixStack.peek().getModel().multiply(this.getBasicProjectionMatrix(d));
        loadProjectionMatrix(matrixStack.peek().getModel());

        this.bobViewWhenHurt(matrixStack, partialTicks);
        Renderer3D.INSTANCE.applyCameraRots(matrixStack);
        loadProjectionMatrix(matrixStack.peek().getModel());
        EventRender3DNoBob eventnobob = new Render3DEvent.EventRender3DNoBob(matrixStack, partialTicks);
        FishHack.EventBus.post(eventnobob);
        Renderer3D.INSTANCE.fixCameraRots(matrixStack);
        if (this.client.options.bobView) {
            bobView(matrixStack, partialTicks);
        }
        Renderer3D.INSTANCE.applyCameraRots(matrixStack);
        loadProjectionMatrix(matrixStack.peek().getModel());
        //EventRenderGetPos eventgetpo EventRenderGetPos(matrixStack, partialTicks).run();
        Renderer3D.INSTANCE.fixCameraRots(matrixStack);
        loadProjectionMatrix(matrixStack.peek().getModel());

        Render3DEvent event = new Render3DEvent(matrixStack1, partialTicks);
        FishHack.EventBus.post(event);
    }


}
