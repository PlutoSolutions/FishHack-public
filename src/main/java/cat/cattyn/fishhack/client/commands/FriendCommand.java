package cat.cattyn.fishhack.client.commands;

import cat.cattyn.fishhack.FishHack;
import cat.cattyn.fishhack.api.command.Command;
import net.minecraft.util.Formatting;

public class FriendCommand extends Command {

    public FriendCommand() {
        super(new String[]{"f", "friend"});
    }

    @Override
    public void onCommand(String name, String[] args) {
        if (args.length < 2) {
            sendRawClientMessage("Invalid Syntax! Usage: " + getPrefix() + "f <add/del> <name>");
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (FishHack.getFriendManager().isFriend(args[1])) {
                sendRawClientMessage(Formatting.BLUE + args[1] + " is already ur friend");
            } else {
                FishHack.getFriendManager().getFriends().add(args[1]);
                sendRawClientMessage(Formatting.BLUE + args[1] + " is ur friend now");
            }
        } else if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("remove")) {
            if (FishHack.getFriendManager().getFriends().isEmpty()) {
                sendRawClientMessage("Retarded pig, you don't have friends and i don't think you will have them.");

            } else if (!FishHack.getFriendManager().isFriend(args[1])) {
                sendRawClientMessage(Formatting.RED + args[1] + " is already not ur friend");
            } else {
                sendRawClientMessage(Formatting.RED + args[1] + " is not ur friend now");
                FishHack.getFriendManager().getFriends().remove(args[1]);
            }
        } else {
            sendRawClientMessage("Invalid Syntax! Usage: " + getPrefix() + "f <add/del> <name>");
        }

    }

}
