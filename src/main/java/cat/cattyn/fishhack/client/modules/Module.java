package cat.cattyn.fishhack.client.modules;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.command.Command;
import cat.cattyn.fishhack.api.setting.Setting;
import cat.cattyn.fishhack.client.modules.client.Debug;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cattyngmd
 */

public class Module {

    public static MinecraftClient mc = MinecraftClient.getInstance();

    public String name;
    public String displayInfo;
    public String desc;
    public int keybind;
    public boolean toggled;
    public boolean drawn;
    public float animPos = -1;
    public List<Setting> settings = new ArrayList<Setting>();
    Category category;

    public Module() {
        Info info = getClass().getAnnotation(Info.class);
        this.keybind = info.key();
        this.name = info.name();
        this.displayInfo = "";
        this.desc = info.desc();
        this.category = info.cat();
        this.toggled = false;
        this.drawn = info.drawn();
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onUpdate() {
    }

    public String getName() {
        return name;
    }

    public String getDisplayInfo() {
        return displayInfo;
    }

    public void setDisplayInfo(String info) {
        this.displayInfo = Formatting.GRAY + " [" + Formatting.WHITE + info + Formatting.GRAY + "]";
    }

    public String getDescription() {
        return desc;
    }

    public Category getCategory() {
        return category;
    }

    public int getBind() {
        return keybind;
    }

    public void setBind(int key) {
        this.keybind = key;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
        if (toggled)
            enable();
        else if (!toggled)
            disable();
    }

    public void toggle() {
        this.toggled = !this.toggled;
        if (toggled)
            enable();
        else if (!toggled)
            disable();
    }

    public boolean isDrawn() {
        return drawn;
    }

    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    public void enable() {
        FishHack.EventBus.register(this);
        animPos = -1;
        onEnable();
    }

    public void disable() {
        FishHack.EventBus.unregister(this);
        onDisable();
    }

    protected Setting<Double> register(final String name, final double value, final double min, final double max, final int inc) {
        final Setting<Double> s = new Setting<>(name, this, getCategory(), value, min, max, inc);
        FishHack.getSettingManager().addSetting(s);
        return s;
    }

    protected Setting<Boolean> register(final String name, final boolean value) {
        final Setting<Boolean> s = new Setting<>(name, this, getCategory(), value);
        FishHack.getSettingManager().addSetting(s);
        return s;
    }

    protected Setting<String> register(final String name, final List<String> modes, final String value) {
        final Setting<String> s = new Setting<>(name, this, getCategory(), modes, value);
        FishHack.getSettingManager().addSetting(s);
        return s;
    }

    public boolean nullCheck() {
        return mc.player != null && mc.world != null;
    }

    public void debug(String message) {
        // Добавил сюда по причине я ленивый
        if (Debug.getInstance.isToggled()) Command.sendRawClientMessage(message);
    }

    public enum Category {
        Combat, Player, Movement, Misc, Render, Client
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Info {
        String name();

        String desc() default "No description";

        Module.Category cat();

        boolean drawn() default true;

        int key() default -1;
    }

}
