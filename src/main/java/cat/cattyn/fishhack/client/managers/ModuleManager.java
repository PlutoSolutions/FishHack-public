package cat.cattyn.fishhack.client.managers;

import cat.cattyn.fishhack.client.modules.Module;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author cattyngmd
 */

public class ModuleManager {

    private static ArrayList<Module> modules;

    public ModuleManager() {
        modules = new ArrayList<>();

        Set<Class<? extends Module>> reflections = new Reflections("cat.cattyn.fishhack.client.modules").getSubTypesOf(Module.class);
        reflections.forEach(aClass -> {
            try {
                if (aClass.isAnnotationPresent(Module.Info.class)) modules.add(aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        modules.sort(Comparator.comparing(Module::getName));
    }

    public static ArrayList<Module> getModules() {
        return ModuleManager.modules;
    }

    public static List<Module> getModules(Module.Category c) {
        List<Module> modules = new ArrayList<Module>();
        for (Module m : ModuleManager.modules) {
            if (m.getCategory() == c)
                modules.add(m);
        }
        return modules;
    }

    public Module getModule(String name) {
        for (Module m : ModuleManager.modules) {
            if (m.getName().equalsIgnoreCase(name))
                return m;
        }
        return null;
    }
}
