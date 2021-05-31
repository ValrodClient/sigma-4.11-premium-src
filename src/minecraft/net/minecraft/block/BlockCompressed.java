package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class BlockCompressed extends Block {
    private final MapColor mapColor;
    private static final String __OBFID = "CL_00000268";

    public BlockCompressed(MapColor p_i45414_1_) {
        super(Material.iron);
        mapColor = p_i45414_1_;
        setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state) {
        return mapColor;
    }
}
