package cat.cattyn.fishhack.client.modules.combat;

import cat.cattyn.fishhack.client.modules.Module;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

@Module.Info(name = "AutoTotem", desc = "Puts a totem in your offhand", cat = Module.Category.Combat)
public class AutoTotem extends Module {
    public void onUpdate() {
        if (mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
            for (int i = 0; i < 44; i++) {
                if (mc.player.getInventory().getStack(i).getItem().equals(Items.TOTEM_OF_UNDYING)) {
                    mc.interactionManager.clickSlot(0, i < 9 ? i + 36 : i, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(0, 45, 0, SlotActionType.PICKUP, mc.player);
                    mc.interactionManager.clickSlot(0, i < 9 ? i + 36 : i, 0, SlotActionType.PICKUP, mc.player);
                    return;
                }
            }
        }

    }

}
