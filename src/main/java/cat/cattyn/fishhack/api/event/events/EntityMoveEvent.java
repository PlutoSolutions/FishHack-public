package cat.cattyn.fishhack.api.event.events;

import cat.cattyn.fishhack.api.event.FishEvent;

public class EntityMoveEvent extends FishEvent {
    public double motionX, motionY, motionZ;

    public EntityMoveEvent(final double motionX, final double motionY, final double motionZ) {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }
}
