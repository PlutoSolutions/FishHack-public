package cat.cattyn.fishhack.mixins;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author seasnail8169
 */

@Mixin(ChatHud.class)
public interface IChatHud {
    @Invoker("addMessage")
    void add(Text msg, int messageId);
}