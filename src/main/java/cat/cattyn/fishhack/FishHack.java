package cat.cattyn.fishhack;

import cat.cattyn.fishhack.client.managers.*;
import cat.cattyn.fishhack.client.managers.ConfigManager;
import cat.cattyn.fishhack.api.util.text.CustomFontRenderer;
import cat.cattyn.fishhack.api.util.text.CustomRenderStack;
import cat.cattyn.fishhack.client.gui.ClickGui;
import com.google.common.eventbus.EventBus;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

/**
 * @author cattyngmd
 */

public class FishHack implements ModInitializer {

    public static final String MODNAME = "FishHack";
    public static final String MODID = "fishhack";
    public static final String MODVER = "1.2";

    public static final EventBus EventBus = new EventBus();
    private static CustomRenderStack renderStack;
    private static CustomFontRenderer fontRender;
    private static ModuleManager moduleManager;
    private static CommandManager commandManager;
    private static FriendManager friendManager;
    private static ConfigManager configUtil;
    private static SettingManager settingManager;
    private static ClickGui clickGui;

    public static MinecraftClient mc = MinecraftClient.getInstance();
    public static FishHack INSTANCE;

    public FishHack() {
        INSTANCE = this;
    }

    @Override public void onInitialize() {
        renderStack = new CustomRenderStack();
        fontRender = new CustomFontRenderer("/assets/font/font.ttf", 64);
        settingManager = new SettingManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        clickGui = new ClickGui();
        friendManager = new FriendManager();
        configUtil = new ConfigManager();
        configUtil.load();
        System.out.println("FishHack initialized!");
        Runtime.getRuntime().addShutdownHook(new ConfigManager());
    }

    public static CustomRenderStack getRenderStack() {
        return renderStack;
    }

    public static CustomFontRenderer getFontRender() {
        return fontRender;
    }

    public static ModuleManager getModuleManager() {
        return moduleManager;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static FriendManager getFriendManager() {
        return friendManager;
    }

    public static ConfigManager getConfigUtil() {
        return configUtil;
    }

    public static SettingManager getSettingManager() {
        return settingManager;
    }

    public static ClickGui getClickGui() {
        return clickGui;
    }
}
