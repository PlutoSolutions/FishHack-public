package cat.cattyn.fishhack.api.util.text;

public interface IFontRenderer {
    double drawChar(char c, double x, double y);

    void drawString(String text, double x, double y);

    void drawString(String text, double x, double y, ColorUtil color, boolean shadow);

    void drawString(String text, double x, double y, ColorUtil color);

    void drawStringWithShadow(String text, double x, double y, ColorUtil color);

    int drawStringWithCustomWidthWithShadow(String text, double x, double y, ColorUtil color, double width);

    int drawStringWithCustomWidth(String text, double x, double y, ColorUtil color, double width);

    int drawStringWithCustomWidth(String text, double x, double y, ColorUtil color, double width, boolean shadow);

    int drawStringWithCustomHeightWithShadow(String text, double x, double y, ColorUtil color, double height);

    int drawStringWithCustomHeight(String text, double x, double y, ColorUtil color, double height);

    int drawStringWithCustomHeight(String text, double x, double y, ColorUtil color, double height, boolean shadow);

    int drawSplitString(String text, double x, double y, ColorUtil color, double width);

    double drawSplitString(String text, double x, double y, ColorUtil color, double width, double height);

    double getCharWidth(char c);

    double getStringWidth(String text);

    double getStringWidth(String text, double height);

    int getFontHeight();

    double getFontHeightWithCustomWidth(String text, double width);
}