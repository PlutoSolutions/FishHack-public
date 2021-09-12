package cat.cattyn.fishhack.client.gui;

/**
 * @author cattyngmd
 */

public interface Component {
    void render(int mX, int mY);

    void mouseDown(int mX, int mY, int mB);

    void mouseUp(int mX, int mY);

    void keyPress(int key);

    void close();
}