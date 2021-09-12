package cat.cattyn.fishhack.client.commands;

import cat.cattyn.fishhack.api.command.Command;
import net.minecraft.util.Formatting;

/**
 * @author oyzipfile
 */

public class PrefixCommand extends Command {

    public PrefixCommand() {
        super(new String[]{"prefix", "p"});
    }

    @Override
    public void onCommand(String name, String[] args) {
        if (args.length == 0) {
            sendRawClientMessage("Invalid Syntax! Usage: " + getPrefix() + "p <value>");
            return;
        }

        Command.setPrefix(String.valueOf(args[0]));
        sendRawClientMessage(Formatting.RED + "new prefix: " + Formatting.GOLD + args[0]);
    }
}
