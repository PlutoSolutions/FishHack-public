package cat.cattyn.fishhack.api.command;

import cat.cattyn.fishhack.mixins.IChatHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author cattyngmd
 */

public class Command {

    public static MinecraftClient mc;
    private static String prefix = "$";

    static {
        mc = MinecraftClient.getInstance();
    }

    String[] name;

    public Command(String[] name) {
        this.name = name;
    }

    public static void sendRawClientMessage(String msg) {
        ((IChatHud) mc.inGameHud.getChatHud()).add(new LiteralText("" + Formatting.WHITE + "<" + Formatting.BLUE
                + "Fish" + Formatting.WHITE + "Hack" + Formatting.WHITE + "> " + Formatting.WHITE + msg), 59436);
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void setPrefix(String p) {
        prefix = p;
    }

	public String[] getName() {
        return name;
    }

    public void onCommand(String command, String[] args) {
    }
}
