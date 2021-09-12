package cat.cattyn.fishhack.api.util;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtil {

    public static float startYaw, startPitch;

    private static final Vec3d eyesPos = new Vec3d(Wrapper.mc.player.getX(),
            Wrapper.mc.player.getY() + Wrapper.mc.player.getEyeHeight(Wrapper.mc.player.getPose()),
            Wrapper.mc.player.getZ());

    public static void rotate(BlockPos bp) {
        startYaw = Wrapper.mc.player.getYaw();
        startPitch = Wrapper.mc.player.getPitch();
        Vec3d vec = new Vec3d(bp.getX(), bp.getY(), bp.getZ());
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        float[] rotations = {
                Wrapper.mc.player.getYaw()
                        + MathHelper.wrapDegrees(yaw - Wrapper.mc.player.getYaw()),
                Wrapper.mc.player.getPitch() + MathHelper
                        .wrapDegrees(pitch - Wrapper.mc.player.getPitch())};

        Wrapper.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(rotations[0], rotations[1], Wrapper.mc.player.isOnGround()));
    }

    public static void rotate(Entity e) {
        startYaw = Wrapper.mc.player.getYaw();
        startPitch = Wrapper.mc.player.getPitch();
        Vec3d vec = new Vec3d(e.getX(), e.getY(), e.getZ());
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        float[] rotations = {
                Wrapper.mc.player.getYaw()
                        + MathHelper.wrapDegrees(yaw - Wrapper.mc.player.getYaw()),
                Wrapper.mc.player.getPitch() + MathHelper
                        .wrapDegrees(pitch - Wrapper.mc.player.getPitch())};

        Wrapper.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(rotations[0], rotations[1], Wrapper.mc.player.isOnGround()));
    }

    public static void rotate(float yaw, float pitch) {
        startYaw = Wrapper.mc.player.getYaw();
        startPitch = Wrapper.mc.player.getPitch();
        float[] rotations = {
                Wrapper.mc.player.getYaw()
                        + MathHelper.wrapDegrees(yaw - Wrapper.mc.player.getYaw()),
                Wrapper.mc.player.getPitch() + MathHelper
                        .wrapDegrees(pitch - Wrapper.mc.player.getPitch())};

        Wrapper.mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(rotations[0], rotations[1], Wrapper.mc.player.isOnGround()));
    }

}
