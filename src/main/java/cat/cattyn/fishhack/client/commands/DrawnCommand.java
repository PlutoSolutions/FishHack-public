package cat.cattyn.fishhack.client.commands;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.command.Command;
import cat.cattyn.fishhack.client.modules.Module;
import net.minecraft.util.Formatting;

/**
 * @author cattyngmd
 */

public class DrawnCommand extends Command {

    boolean found = false;

    public DrawnCommand() {
        super(new String[]{"drawn"});
    }

    @Override
    public void onCommand(String name, String[] args) {
        if (args.length == 0) {
            sendRawClientMessage("Invalid Syntax! Usage: " + getPrefix() + "drawn <Module>");
            return;
        }

        Module module = FishHack.getModuleManager().getModule(args[0]);
        if (module != null) {
            module.setDrawn(!module.drawn);
            if (module.isDrawn())
                sendRawClientMessage(module.getName() + Formatting.GREEN + " drawn");
            else
                sendRawClientMessage(module.getName() + Formatting.RED + " undrawn");
        } else
            sendRawClientMessage("Module not found!");
    }

}
