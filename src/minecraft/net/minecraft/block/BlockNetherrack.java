package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockNetherrack extends Block {
    private static final String __OBFID = "CL_00000275";

    public BlockNetherrack() {
        super(Material.rock);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.netherrackColor;
    }
}
