package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class BlockObsidian extends Block {
    private static final String __OBFID = "CL_00000279";

    public BlockObsidian() {
        super(Material.rock);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.obsidian);
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.obsidianColor;
    }
}
