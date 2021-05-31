package net.minecraft.world;

import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class WorldManager implements IWorldAccess {
    /**
     * Reference to the MinecraftServer object.
     */
    private MinecraftServer mcServer;

    /**
     * The WorldServer object.
     */
    private WorldServer theWorldServer;
    private static final String __OBFID = "CL_00001433";

    public WorldManager(MinecraftServer p_i1517_1_, WorldServer p_i1517_2_) {
        mcServer = p_i1517_1_;
        theWorldServer = p_i1517_2_;
    }

    @Override
    public void func_180442_a(int p_180442_1_, boolean p_180442_2_, double p_180442_3_, double p_180442_5_, double p_180442_7_, double p_180442_9_, double p_180442_11_, double p_180442_13_, int... p_180442_15_) {
    }

    /**
     * Called on all IWorldAccesses when an entity is created or loaded. On
     * client worlds, starts downloading any necessary textures. On server
     * worlds, adds the entity to the entity tracker.
     */
    @Override
    public void onEntityAdded(Entity entityIn) {
        theWorldServer.getEntityTracker().trackEntity(entityIn);
    }

    /**
     * Called on all IWorldAccesses when an entity is unloaded or destroyed. On
     * client worlds, releases any downloaded textures. On server worlds,
     * removes the entity from the entity tracker.
     */
    @Override
    public void onEntityRemoved(Entity entityIn) {
        theWorldServer.getEntityTracker().untrackEntity(entityIn);
    }

    /**
     * Plays the specified sound. Arg: soundName, x, y, z, volume, pitch
     */
    @Override
    public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {
        mcServer.getConfigurationManager().sendToAllNear(x, y, z, volume > 1.0F ? (double) (16.0F * volume) : 16.0D, theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));
    }

    /**
     * Plays sound to all near players except the player reference given
     */
    @Override
    public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch) {
        mcServer.getConfigurationManager().sendToAllNearExcept(except, x, y, z, volume > 1.0F ? (double) (16.0F * volume) : 16.0D, theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));
    }

    /**
     * On the client, re-renders all blocks in this range, inclusive. On the
     * server, does nothing. Args: min x, min y, min z, max x, max y, max z
     */
    @Override
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
    }

    @Override
    public void markBlockForUpdate(BlockPos pos) {
        theWorldServer.getPlayerManager().func_180244_a(pos);
    }

    @Override
    public void notifyLightSet(BlockPos pos) {
    }

    @Override
    public void func_174961_a(String p_174961_1_, BlockPos p_174961_2_) {
    }

    @Override
    public void func_180439_a(EntityPlayer p_180439_1_, int p_180439_2_, BlockPos p_180439_3_, int p_180439_4_) {
        mcServer.getConfigurationManager().sendToAllNearExcept(p_180439_1_, p_180439_3_.getX(), p_180439_3_.getY(), p_180439_3_.getZ(), 64.0D, theWorldServer.provider.getDimensionId(), new S28PacketEffect(p_180439_2_, p_180439_3_, p_180439_4_, false));
    }

    @Override
    public void func_180440_a(int p_180440_1_, BlockPos p_180440_2_, int p_180440_3_) {
        mcServer.getConfigurationManager().sendPacketToAllPlayers(new S28PacketEffect(p_180440_1_, p_180440_2_, p_180440_3_, true));
    }

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
        Iterator var4 = mcServer.getConfigurationManager().playerEntityList.iterator();

        while (var4.hasNext()) {
            EntityPlayerMP var5 = (EntityPlayerMP) var4.next();

            if (var5 != null && var5.worldObj == theWorldServer && var5.getEntityId() != breakerId) {
                double var6 = pos.getX() - var5.posX;
                double var8 = pos.getY() - var5.posY;
                double var10 = pos.getZ() - var5.posZ;

                if (var6 * var6 + var8 * var8 + var10 * var10 < 1024.0D) {
                    var5.playerNetServerHandler.sendPacket(new S25PacketBlockBreakAnim(breakerId, pos, progress));
                }
            }
        }
    }
}
