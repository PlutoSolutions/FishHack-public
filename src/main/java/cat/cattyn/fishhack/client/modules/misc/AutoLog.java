package cat.cattyn.fishhack.client.modules.misc;

import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module.Info(name = "AutoLog", desc = "Disconnects you from the server if your health is lower or equal to selected", cat = Module.Category.Misc)
public class AutoLog extends Module {
    List<Entity> entities;
    Setting<String> mode = register("Mode", Arrays.asList("HP", "Creeper"), "HP");
    Setting<Double> hp = register("HP", 10, 1, 32, 1);
    Setting<Double> range = this.register("Range", 3, 0, 12, 2);

    public void onUpdate() {
        entities = new ArrayList<>();
        mc.world.getEntities().forEach(entities::add);

        if (mc.player.getHealth() <= hp.getValue() && mode.getValue().equalsIgnoreCase("HP")) {
            mc.player.networkHandler.getConnection()
                    .disconnect(new LiteralText("your hp is : " + mc.player.getHealth()));
            toggle();
        } else if (mode.getValue().equalsIgnoreCase("Creeper")) {
            for (Entity e : entities) {
                if (e instanceof CreeperEntity && mc.player.distanceTo(e) <= range.getValue()) {
                    mc.player.networkHandler.getConnection().disconnect(new LiteralText("Creeper is in range!"));
                    toggle();
                }
            }
        }

    }

}
