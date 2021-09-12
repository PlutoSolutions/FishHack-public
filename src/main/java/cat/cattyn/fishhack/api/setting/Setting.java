package cat.cattyn.fishhack.api.setting;

import cat.cattyn.fishhack.client.modules.Module;

import java.util.List;

/**
 * @author cattyngmd
 */

public class Setting<T> {

    private final String name;
    private final Module parent;
    private final Module.Category category;
    private final Type type;

    private T value, max, min;
    private int inc;
    private List<String> modes;

    public Setting(final String name, final Module parent, final Module.Category category, T value) {
        this.name = name;
        this.parent = parent;
        this.type = Type.B;
        this.category = category;
        this.value = value;
    }

    public Setting(final String name, final Module parent, final Module.Category category, T value, T min, T max, int inc) {
        this.name = name;
        this.parent = parent;
        this.type = Type.N;
        this.category = category;
        this.value = value;
        this.min = min;
        this.max = max;
        this.inc = inc;
    }

    public Setting(final String name, final Module parent, final Module.Category category, List<String> modes, T value) {
        this.name = name;
        this.parent = parent;
        this.type = Type.M;
        this.category = category;
        this.value = value;
        this.modes = modes;
    }

    public String getName() {
        return this.name;
    }

    public Module getParent() {
        return this.parent;
    }

    public Type getType() {
        return this.type;
    }

    public Module.Category getCategory() {
        return this.category;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    public int getInc() {
        return inc;
    }

    public List<String> getModes() {
        return modes;
    }

    public enum Type {
        N, B, M
    }
}
