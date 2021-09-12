package cat.cattyn.fishhack.client.commands;

import cat.cattyn.fishhack.api.command.Command;
import net.minecraft.util.Formatting;

/**
 * @author oyzipfile, cattyngmd
 */

public class VClipCommand extends Command {

    Double offset;

    public VClipCommand() {
        super(new String[]{"vclip", "vc"});
    }

    // Говно модуль

    @Override
    public void onCommand(String name, String[] args) {
        if (args.length == 0) {
            sendRawClientMessage("Invalid Syntax! Usage: " + getPrefix() + "vclip <value>");
            return;
        }

        try {
            offset = Double.parseDouble(args[0]);
        } catch (Exception e) {
            sendRawClientMessage(Formatting.RED + "Usage: vclip <value>");
            return;
        }

        if (mc.player.getVehicle() != null)
            mc.player.getVehicle().updatePosition(mc.player.getX(), mc.player.getY() + offset, mc.player.getZ());
        else
            mc.player.updatePosition(mc.player.getX(), mc.player.getY() + offset, mc.player.getZ());
    }
}
