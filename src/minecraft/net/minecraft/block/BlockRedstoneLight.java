package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRedstoneLight extends Block {
    private final boolean isOn;
    private static final String __OBFID = "CL_00000297";

    public BlockRedstoneLight(boolean p_i45421_1_) {
        super(Material.redstoneLight);
        isOn = p_i45421_1_;

        if (p_i45421_1_) {
            setLightLevel(1.0F);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            if (isOn && !worldIn.isBlockPowered(pos)) {
                worldIn.setBlockState(pos, Blocks.redstone_lamp.getDefaultState(), 2);
            } else if (!isOn && worldIn.isBlockPowered(pos)) {
                worldIn.setBlockState(pos, Blocks.lit_redstone_lamp.getDefaultState(), 2);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote) {
            if (isOn && !worldIn.isBlockPowered(pos)) {
                worldIn.scheduleUpdate(pos, this, 4);
            } else if (!isOn && worldIn.isBlockPowered(pos)) {
                worldIn.setBlockState(pos, Blocks.lit_redstone_lamp.getDefaultState(), 2);
            }
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (isOn && !worldIn.isBlockPowered(pos)) {
                worldIn.setBlockState(pos, Blocks.redstone_lamp.getDefaultState(), 2);
            }
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.redstone_lamp);
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(Blocks.redstone_lamp);
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Blocks.redstone_lamp);
    }
}
