package cat.cattyn.fishhack.client.managers;

import cat.cattyn.fishhack.api.command.Command;
import net.minecraft.util.Formatting;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

/**
 * @author cattyngmd
 */

public class CommandManager {

    private ArrayList<Command> commands;
    private boolean commandFound = false;

    public CommandManager() {
        commands = new ArrayList<>();

        Set<Class<? extends Command>> reflections = new Reflections("cat.cattyn.fishhack.client.commands").getSubTypesOf(Command.class);
        reflections.forEach(aClass -> {
            try {
                commands.add(aClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        commands.sort(Comparator.comparing(command -> command.getName()[0]));
    }

    public void runCommand(String input) {
        String[] argss = input.split(" ");
        String command = argss[0];
        String args = input.substring(command.length()).trim();

        commands.forEach(c -> {
            for (String name : c.getName()) {
                if (argss[0].equalsIgnoreCase(name)) {
                    c.onCommand(command, args.split(" "));
                    commandFound = true;
                }
            }
            if (!commandFound) {
                Command.sendRawClientMessage(Formatting.RED + "Invalid Syntax");
            }
        });
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}
