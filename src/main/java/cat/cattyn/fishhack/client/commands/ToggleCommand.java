package cat.cattyn.fishhack.client.commands;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.command.Command;
import cat.cattyn.fishhack.client.modules.Module;
import net.minecraft.util.Formatting;

/**
 * @author cattyngmd
 */

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super(new String[]{"toggle", "t"});
    }

    @Override
    public void onCommand(String name, String[] args) {
        if (args.length == 0) {
            sendRawClientMessage("Invalid Syntax! Usage: " + getPrefix() + "toggle <Module>");
            return;
        }

        Module module = FishHack.getModuleManager().getModule(args[0]);
        if (module != null) {
            module.toggle();
            if (module.isToggled())
                sendRawClientMessage(module.getName() + Formatting.GREEN + " enabled.");
            else
                sendRawClientMessage(module.getName() + Formatting.RED + " disabled.");
        } else
            sendRawClientMessage("Module not found!");
    }
}
