package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class BehaviorProjectileDispense extends BehaviorDefaultDispenseItem {
    private static final String __OBFID = "CL_00001394";

    /**
     * Dispense the specified stack, play the dispense sound and spawn
     * particles.
     */
    @Override
    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        World var3 = source.getWorld();
        IPosition var4 = BlockDispenser.getDispensePosition(source);
        EnumFacing var5 = BlockDispenser.getFacing(source.getBlockMetadata());
        IProjectile var6 = getProjectileEntity(var3, var4);
        var6.setThrowableHeading(var5.getFrontOffsetX(), var5.getFrontOffsetY() + 0.1F, var5.getFrontOffsetZ(), func_82500_b(), func_82498_a());
        var3.spawnEntityInWorld((Entity) var6);
        stack.splitStack(1);
        return stack;
    }

    /**
     * Play the dispense sound from the specified block.
     */
    @Override
    protected void playDispenseSound(IBlockSource source) {
        source.getWorld().playAuxSFX(1002, source.getBlockPos(), 0);
    }

    /**
     * Return the projectile entity spawned by this dispense behavior.
     */
    protected abstract IProjectile getProjectileEntity(World worldIn, IPosition position);

    protected float func_82498_a() {
        return 6.0F;
    }

    protected float func_82500_b() {
        return 1.1F;
    }
}
