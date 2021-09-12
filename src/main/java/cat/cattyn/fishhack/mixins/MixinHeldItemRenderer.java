package cat.cattyn.fishhack.mixins;

import cat.cattyn.fishhack.client.modules.render.ItemViewModel;
import com.google.common.base.MoreObjects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class MixinHeldItemRenderer {

//	private Random random = new Random( );

    @ModifyVariable(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "STORE", ordinal = 0), index = 6)
    private float modifySwing(float swingProgress) {
        ItemViewModel module = ItemViewModel.INSTANCE;
        @SuppressWarnings("resource")
        Hand hand = MoreObjects.firstNonNull(MinecraftClient.getInstance().player.preferredHand, Hand.MAIN_HAND);

        if (module.isToggled()) {
            if (hand == Hand.OFF_HAND) {
                return (float) (swingProgress + module.swingLeft.getValue());
            }
            if (hand == Hand.MAIN_HAND) {
                return (float) (swingProgress + module.swingRight.getValue());
            }
        }
        return swingProgress;
    }

    @Inject(at = @At("INVOKE"), method = "renderFirstPersonItem")
    private void renderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
        ItemViewModel module = ItemViewModel.INSTANCE;
        if (module.isToggled()) matrices.translate(module.x.getValue(), module.y.getValue(), module.z.getValue());
//        if (KillAura.INSTANCE.isToggled() && KillAura.INSTANCE.animations.getValue() && KillAura.INSTANCE.target != null) {
////        	GL11.glPushMatrix();
////        	GL11.glRotatef((random.nextInt( 360 )), 0.0f, 0.0f, 1.0f);
////        	GL11.glPopMatrix();
//        	matrices.push( );
//        	matrices.translate(0.0D, 0.1, 0.0D);
//        	//matrices.multiply( Vec3f.POSITIVE_Y.getDegreesQuaternion( 180f ) );
//        }
    }

}
