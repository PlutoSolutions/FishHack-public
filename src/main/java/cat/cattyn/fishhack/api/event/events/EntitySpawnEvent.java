package cat.cattyn.fishhack.api.event.events;

import cat.cattyn.fishhack.api.event.FishEvent;
import net.minecraft.entity.Entity;

public class EntitySpawnEvent extends FishEvent {

    public Entity entity;

    public EntitySpawnEvent(Entity entity) {
        this.entity = entity;
    }

}
