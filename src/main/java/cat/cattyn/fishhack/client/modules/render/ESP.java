package cat.cattyn.fishhack.client.modules.render;

import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.api.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;

@Module.Info(name = "ESP", cat = Module.Category.Render)
public class ESP extends Module {

    Setting<Boolean> players = register("Players", true);
    Setting<Boolean> animals = register("Animals", false);
    Setting<Boolean> monsters = register("Monsters", true);
    Setting<Boolean> other = register("Other", false);

    @Override
    public void onUpdate() {
        for (Entity e : mc.world.getEntities()) {
            if (e instanceof PlayerEntity && players.getValue()) {
                e.setGlowing(true);
            } else if ((e instanceof AnimalEntity || e instanceof VillagerEntity || e instanceof AmbientEntity || e instanceof WaterCreatureEntity || e instanceof GolemEntity) && animals.getValue()) {
                e.setGlowing(true);
            } else if (e instanceof Monster && monsters.getValue()) {
                e.setGlowing(true);
            } else e.setGlowing((e instanceof ItemEntity || e instanceof EndCrystalEntity) && other.getValue());
        }
    }

    @Override
    public void onDisable() {
        for (Entity e : mc.world.getEntities()) {
            e.setGlowing(false);
        }
    }

}
