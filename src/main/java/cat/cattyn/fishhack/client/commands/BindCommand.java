package cat.cattyn.fishhack.client.commands;

import cat.cattyn.fishhack.api.command.Command;
import cat.cattyn.fishhack.client.modules.Module;
import cat.cattyn.fishhack.client.managers.ModuleManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;

/**
 * @author cattyn
 */

public class BindCommand extends Command {

    public BindCommand() {
        super(new String[]{"b", "bind"});
    }

    @Override
    public void onCommand(String name, String[] args) {

        boolean found = false;

        if (args.length == 0) {
            sendRawClientMessage(Formatting.DARK_RED + "Invalid Syntax");
            return;
        }

        for (Module m : ModuleManager.getModules()) {
            if (m.getName().equalsIgnoreCase(args[0])) {
                found = true;
                try {
                    m.setBind(InputUtil.fromTranslationKey("key.keyboard." + args[1].toLowerCase()).getCode());
                    sendRawClientMessage(
                            Formatting.GREEN + m.getName() + " was bound to " + args[1].toUpperCase() + "!");
                } catch (Exception e) {
                    e.printStackTrace();
                    sendRawClientMessage(Formatting.RED + "Invalid Synatax. Usage: " + getPrefix() + "bind <Module> <Key>");
                }
                break;
            }
        }

        if (!found)
            sendRawClientMessage(Formatting.RED + "Module not found.");

    }

}
