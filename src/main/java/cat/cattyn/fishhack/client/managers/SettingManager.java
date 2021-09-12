package cat.cattyn.fishhack.client.managers;

import cat.cattyn.fishhack.api.setting.Setting;
import cat.cattyn.fishhack.client.modules.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cattyngmd
 */

public class SettingManager {

    private final List<Setting> settings;

    public SettingManager() {
        this.settings = new ArrayList<>();
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public void addSetting(final Setting setting) {
        this.settings.add(setting);
    }

    public Setting getSettingByNameAndMod(final String name, final Module parent) {
        return this.settings.stream().filter(s -> s.getParent().equals(parent))
                .filter(s -> s.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<Setting> getSettingsForMod(final Module parent) {
        return this.settings.stream().filter(s -> s.getParent().equals(parent)).collect(Collectors.toList());
    }

    public Setting getSettingByName(String name) {
        for (Setting set : getSettings()) {
            if (set.getName().equalsIgnoreCase(name)) {
                return set;
            }
        }
        return null;
    }
}
