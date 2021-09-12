package cat.cattyn.fishhack.api.event.events;

import cat.cattyn.fishhack.api.event.FishEvent;
import net.minecraft.client.util.math.MatrixStack;

/**
 * @author cattyngmd
 */

public class RenderEvent extends FishEvent {

    private final MatrixStack stack;

    public RenderEvent(MatrixStack stack) {
        this.stack = stack;
    }

    public MatrixStack getMatrixStack() {
        return stack;
    }

}
