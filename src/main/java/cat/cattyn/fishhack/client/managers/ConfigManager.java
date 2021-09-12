package cat.cattyn.fishhack.client.managers;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.command.Command;
import cat.cattyn.fishhack.api.util.Wrapper;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.client.gui.ClickGui;
import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author cattyngmd
 */

public class ConfigManager extends Thread implements Wrapper {
    private static final File mainFolder = new File("FishHack");
    private static final String modulesFolder = mainFolder.getAbsolutePath() + "/modules";
    private static final String PREFIX = "Prefix.json";
    private static final String FRIEND = "Friends.json";
    private static final String GUI = "Gui.txt";

    public static File getMainFolder() { return mainFolder; }

    public void load() {
        try {
            loadPrefix();
            loadModules();
            loadFriends();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadModules() throws IOException {
        for (Module m : ModuleManager.getModules()) {
            loadModule(m);
        }
    }

    private void loadModule(Module m) throws IOException {
        Path path = Path.of(modulesFolder, m.getName() + ".json");
        if (!path.toFile().exists()) return;
        String rawJson = loadFile(path.toFile());
        JsonObject jsonObject = new JsonParser().parse(rawJson).getAsJsonObject();
        if (jsonObject.get("Enabled") != null && jsonObject.get("Drawn") != null && jsonObject.get("KeyBind") != null) {
            if (jsonObject.get("Enabled").getAsBoolean()) m.toggle();
            m.setDrawn(jsonObject.get("Drawn").getAsBoolean());
            m.setBind(jsonObject.get("KeyBind").getAsInt());
        }
        FishHack.getSettingManager().getSettingsForMod(m).forEach(s -> {
            JsonElement settingObject = jsonObject.get(s.getName());
            if (settingObject != null) {
                switch (s.getType()) {
                    case B -> s.setValue(settingObject.getAsBoolean());
                    case N -> s.setValue(settingObject.getAsDouble());
                    case M -> s.setValue(settingObject.getAsString());
                }
            }
        });
    }

    private void loadFriends() throws IOException {
        Path path = Path.of(mainFolder.getAbsolutePath(), FRIEND);
        if (!path.toFile().exists()) return;
        String rawJson = loadFile(path.toFile());
        JsonObject jsonObject = new JsonParser().parse(rawJson).getAsJsonObject();
        if (jsonObject.get("Friends") != null) {
            JsonArray friendObject = jsonObject.get("Friends").getAsJsonArray();
            friendObject.forEach(object -> FishHack.getFriendManager().getFriends().add(object.getAsString()));
        }
    }

    private void loadPrefix() throws IOException {
        Path path = Path.of(mainFolder.getAbsolutePath(), PREFIX);
        if (!path.toFile().exists()) return;
        String rawJson = loadFile(path.toFile());
        JsonObject jsonObject = new JsonParser().parse(rawJson).getAsJsonObject();
        if (jsonObject.get("Prefix") != null) {
            Command.setPrefix(jsonObject.get("Prefix").getAsString());
        }
    }

    public String loadFile(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file.getAbsolutePath());
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    @Override
    public void run() {
        if (!mainFolder.exists() && !mainFolder.mkdirs()) System.out.println("Failed to create config folder");
        if (!new File(modulesFolder).exists() && !new File(modulesFolder).mkdirs())
            System.out.println("Failed to create modules folder");
        try {
            saveModules();
            saveFriends();
            savePrefix();
            saveGui();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveModules() throws IOException {
        for (Module m : ModuleManager.getModules()) {
            saveModule(m);
        }
    }

    private void saveModule(Module m) throws IOException {
        Path path = Path.of(modulesFolder, m.getName() + ".json");
        createFile(path);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("Enabled", new JsonPrimitive(m.isToggled()));
        jsonObject.add("Drawn", new JsonPrimitive(m.isDrawn()));
        jsonObject.add("KeyBind", new JsonPrimitive(m.getBind()));
        FishHack.getSettingManager().getSettingsForMod(m).forEach(s -> {
            switch (s.getType()) {
                case M -> jsonObject.add(s.getName(), new JsonPrimitive((String) s.getValue()));
                case B -> jsonObject.add(s.getName(), new JsonPrimitive((Boolean) s.getValue()));
                case N -> jsonObject.add(s.getName(), new JsonPrimitive((Double) s.getValue()));
            }
        });
        Files.write(path, gson.toJson(new JsonParser().parse(jsonObject.toString())).getBytes());
    }

    private void saveFriends() throws IOException {
        Path path = Path.of(mainFolder.getAbsolutePath(), FRIEND);
        createFile(path);
        JsonObject jsonObject = new JsonObject();
        JsonArray friends = new JsonArray();
        FishHack.getFriendManager().getFriends().forEach(friends::add);
        jsonObject.add("Friends", friends);
        Files.write(path, gson.toJson(new JsonParser().parse(jsonObject.toString())).getBytes());
    }

    private void savePrefix() throws IOException {
        Path path = Path.of(mainFolder.getAbsolutePath(), PREFIX);
        createFile(path);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("Prefix", new JsonPrimitive(Command.getPrefix()));
        Files.write(path, gson.toJson(new JsonParser().parse(jsonObject.toString())).getBytes());
    }

    private void saveGui() throws IOException {
        Path path = Path.of(mainFolder.getAbsolutePath(), GUI);
        createFile(path);
        ClickGui.windows.forEach(window -> {
            try {
                Files.write(path, (window.getCategory().name() + ":" + window.getX() + ":" + window.getY() + "\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void createFile(Path path) {
        if (Files.exists(path)) new File(path.normalize().toString()).delete();
        try {
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
