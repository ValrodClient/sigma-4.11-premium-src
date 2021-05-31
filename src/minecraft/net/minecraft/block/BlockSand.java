package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockSand extends BlockFalling {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", BlockSand.EnumType.class);
    private static final String __OBFID = "CL_00000303";

    public BlockSand() {
        setDefaultState(blockState.getBaseState().withProperty(BlockSand.VARIANT_PROP, BlockSand.EnumType.SAND));
    }

    /**
     * Get the damage value that this Block should drop
     */
    @Override
    public int damageDropped(IBlockState state) {
        return ((BlockSand.EnumType) state.getValue(BlockSand.VARIANT_PROP)).func_176688_a();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood
     * returns 4 blocks)
     */
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        BlockSand.EnumType[] var4 = BlockSand.EnumType.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            BlockSand.EnumType var7 = var4[var6];
            list.add(new ItemStack(itemIn, 1, var7.func_176688_a()));
        }
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state) {
        return ((BlockSand.EnumType) state.getValue(BlockSand.VARIANT_PROP)).func_176687_c();
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockSand.VARIANT_PROP, BlockSand.EnumType.func_176686_a(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((BlockSand.EnumType) state.getValue(BlockSand.VARIANT_PROP)).func_176688_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockSand.VARIANT_PROP});
    }

    public static enum EnumType implements IStringSerializable {
        SAND("SAND", 0, 0, "sand", "default", MapColor.sandColor), RED_SAND("RED_SAND", 1, 1, "red_sand", "red", MapColor.dirtColor);
        private static final BlockSand.EnumType[] field_176695_c = new BlockSand.EnumType[EnumType.values().length];
        private final int field_176692_d;
        private final String field_176693_e;
        private final MapColor field_176690_f;
        private final String field_176691_g;

        private static final BlockSand.EnumType[] $VALUES = new BlockSand.EnumType[]{SAND, RED_SAND};
        private static final String __OBFID = "CL_00002069";

        private EnumType(String p_i45687_1_, int p_i45687_2_, int p_i45687_3_, String p_i45687_4_, String p_i45687_5_, MapColor p_i45687_6_) {
            field_176692_d = p_i45687_3_;
            field_176693_e = p_i45687_4_;
            field_176690_f = p_i45687_6_;
            field_176691_g = p_i45687_5_;
        }

        public int func_176688_a() {
            return field_176692_d;
        }

        @Override
        public String toString() {
            return field_176693_e;
        }

        public MapColor func_176687_c() {
            return field_176690_f;
        }

        public static BlockSand.EnumType func_176686_a(int p_176686_0_) {
            if (p_176686_0_ < 0 || p_176686_0_ >= EnumType.field_176695_c.length) {
                p_176686_0_ = 0;
            }

            return EnumType.field_176695_c[p_176686_0_];
        }

        @Override
        public String getName() {
            return field_176693_e;
        }

        public String func_176685_d() {
            return field_176691_g;
        }

        static {
            BlockSand.EnumType[] var0 = EnumType.values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                BlockSand.EnumType var3 = var0[var2];
                EnumType.field_176695_c[var3.func_176688_a()] = var3;
            }
        }
    }
}
