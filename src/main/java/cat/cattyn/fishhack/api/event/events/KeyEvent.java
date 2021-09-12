package cat.cattyn.fishhack.api.event.events;

import cat.cattyn.fishhack.api.event.FishEvent;

/**
 * @author cattyngmd
 */

public class KeyEvent extends FishEvent {

    private int code;

    public KeyEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
