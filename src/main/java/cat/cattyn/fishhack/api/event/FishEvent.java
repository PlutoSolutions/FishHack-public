package cat.cattyn.fishhack.api.event;

/**
 * @author cattyngmd
 */

public class FishEvent {

    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel() {
        cancelled = true;
    }

}
