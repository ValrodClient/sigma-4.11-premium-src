package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockBarrier extends Block {
    private static final String __OBFID = "CL_00002139";

    protected BlockBarrier() {
        super(Material.barrier);
        setBlockUnbreakable();
        setResistance(6000001.0F);
        disableStats();
        translucent = true;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * Returns the default ambient occlusion value based on block opacity
     */
    @Override
    public float getAmbientOcclusionLightValue() {
        return 1.0F;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *
     * @param chance  The chance that each Item is actually spawned (1.0 = always,
     *                0.0 = never)
     * @param fortune The player's fortune level
     */
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    }
}
