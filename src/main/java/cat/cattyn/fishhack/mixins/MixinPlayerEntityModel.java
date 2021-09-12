package cat.cattyn.fishhack.mixins;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.util.Wrapper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityModel {
    private final Random random = new Random();

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(AbstractClientPlayerEntity playerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (FishHack.getModuleManager().getModule("UnhittableAA").toggled && playerEntity == Wrapper.mc.player) {
            matrixStack.push();
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((float) random.nextInt(360)));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) random.nextInt(360)));
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float) random.nextInt(360)));
        }
    }

    @Inject(at = @At("RETURN"), method = "render", cancellable = true)
    public void renderReturn(AbstractClientPlayerEntity playerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (FishHack.getModuleManager().getModule("UnhittableAA").toggled && playerEntity == Wrapper.mc.player)
            matrixStack.pop();
    }
}
