package cat.cattyn.fishhack.api.util.text;

/**
 * @author Tigermouthbear
 */
public class ColorUtil {
    public static final ColorUtil BLACK = new ColorUtil(0, 0, 0, 0.8f);
    public static final ColorUtil ARES_RED = new ColorUtil(0.54f, 0.03f, 0.03f, 1);
    public static final ColorUtil GRAY = new ColorUtil(0.5f, 0.5f, 0.5f, 1);
    public static final ColorUtil WHITE = new ColorUtil(1, 1, 1, 1);
    public static final ColorUtil RED = new ColorUtil(1, 0, 0, 1);
    public static final ColorUtil GREEN = new ColorUtil(0, 1, 0, 1);

    private float r;
    private float g;
    private float b;
    private float a;

    public ColorUtil(int rgb) {
        this((float) (rgb >> 16) / 255.0F, (float) (rgb >> 8 & 255) / 255.0F, (float) (rgb & 255) / 255.0F, 1);
    }

    public ColorUtil(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        if (saturation == 0)
            return convert(brightness, brightness, brightness, 0);
        if (saturation < 0 || saturation > 1 || brightness < 0 || brightness > 1)
            throw new IllegalArgumentException();
        hue = hue - (float) Math.floor(hue);
        int i = (int) (6 * hue);
        float f = 6 * hue - i;
        float p = brightness * (1 - saturation);
        float q = brightness * (1 - saturation * f);
        float t = brightness * (1 - saturation * (1 - f));
        switch (i) {
            case 0:
                return convert(brightness, t, p, 0);
            case 1:
                return convert(q, brightness, p, 0);
            case 2:
                return convert(p, brightness, t, 0);
            case 3:
                return convert(p, q, brightness, 0);
            case 4:
                return convert(t, p, brightness, 0);
            case 5:
                return convert(brightness, p, q, 0);
            default:
                throw new InternalError("impossible");
        }
    }

    private static int convert(float red, float green, float blue, float alpha) {
        if (red < 0 || red > 1 || green < 0 || green > 1 || blue < 0 || blue > 1
                || alpha < 0 || alpha > 1)
            throw new IllegalArgumentException("Bad RGB values");
        int redval = Math.round(255 * red);
        int greenval = Math.round(255 * green);
        int blueval = Math.round(255 * blue);
        int alphaval = Math.round(255 * alpha);
        return (alphaval << 24) | (redval << 16) | (greenval << 8) | blueval;
    }

    public float getRed() {
        return r;
    }

    public float getGreen() {
        return g;
    }

    public float getBlue() {
        return b;
    }

    public float getAlpha() {
        return a;
    }

    public ColorUtil setR(float value) {
        r = value;
        return this;
    }

    public ColorUtil setG(float value) {
        g = value;
        return this;
    }

    public ColorUtil setB(float value) {
        b = value;
        return this;
    }

    //
    //  java.awt.Color
    //

    public ColorUtil setA(float value) {
        a = value;
        return this;
    }

    public int getRGB() {
        return (((int) (getAlpha() * 255 + 0.5) & 0xFF) << 24) |
                (((int) (getRed() * 255 + 0.5) & 0xFF) << 16) |
                (((int) (getGreen() * 255 + 0.5) & 0xFF) << 8) |
                (((int) (getBlue() * 255 + 0.5) & 0xFF));
    }
}