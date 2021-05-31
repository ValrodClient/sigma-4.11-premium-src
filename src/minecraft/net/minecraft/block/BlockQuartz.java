package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public class BlockQuartz extends Block {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", BlockQuartz.EnumType.class);
    private static final String __OBFID = "CL_00000292";

    public BlockQuartz() {
        super(Material.rock);
        setDefaultState(blockState.getBaseState().withProperty(BlockQuartz.VARIANT_PROP, BlockQuartz.EnumType.DEFAULT));
        setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (meta == BlockQuartz.EnumType.LINES_Y.getMetaFromState()) {
            switch (BlockQuartz.SwitchAxis.field_180101_a[facing.getAxis().ordinal()]) {
                case 1:
                    return getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, BlockQuartz.EnumType.LINES_Z);

                case 2:
                    return getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, BlockQuartz.EnumType.LINES_X);

                case 3:
                default:
                    return getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, BlockQuartz.EnumType.LINES_Y);
            }
        } else {
            return meta == BlockQuartz.EnumType.CHISELED.getMetaFromState() ? getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, BlockQuartz.EnumType.CHISELED) : getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, BlockQuartz.EnumType.DEFAULT);
        }
    }

    /**
     * Get the damage value that this Block should drop
     */
    @Override
    public int damageDropped(IBlockState state) {
        BlockQuartz.EnumType var2 = (BlockQuartz.EnumType) state.getValue(BlockQuartz.VARIANT_PROP);
        return var2 != BlockQuartz.EnumType.LINES_X && var2 != BlockQuartz.EnumType.LINES_Z ? var2.getMetaFromState() : BlockQuartz.EnumType.LINES_Y.getMetaFromState();
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        BlockQuartz.EnumType var2 = (BlockQuartz.EnumType) state.getValue(BlockQuartz.VARIANT_PROP);
        return var2 != BlockQuartz.EnumType.LINES_X && var2 != BlockQuartz.EnumType.LINES_Z ? super.createStackedBlock(state) : new ItemStack(Item.getItemFromBlock(this), 1, BlockQuartz.EnumType.LINES_Y.getMetaFromState());
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood
     * returns 4 blocks)
     */
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, BlockQuartz.EnumType.DEFAULT.getMetaFromState()));
        list.add(new ItemStack(itemIn, 1, BlockQuartz.EnumType.CHISELED.getMetaFromState()));
        list.add(new ItemStack(itemIn, 1, BlockQuartz.EnumType.LINES_Y.getMetaFromState()));
    }

    /**
     * Get the MapColor for this Block and the given BlockState
     */
    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.quartzColor;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockQuartz.VARIANT_PROP, BlockQuartz.EnumType.func_176794_a(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((BlockQuartz.EnumType) state.getValue(BlockQuartz.VARIANT_PROP)).getMetaFromState();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockQuartz.VARIANT_PROP});
    }

    public static enum EnumType implements IStringSerializable {
        DEFAULT("DEFAULT", 0, 0, "default", "default"), CHISELED("CHISELED", 1, 1, "chiseled", "chiseled"), LINES_Y("LINES_Y", 2, 2, "lines_y", "lines"), LINES_X("LINES_X", 3, 3, "lines_x", "lines"), LINES_Z("LINES_Z", 4, 4, "lines_z", "lines");
        private static final BlockQuartz.EnumType[] TYPES_ARRAY = new BlockQuartz.EnumType[EnumType.values().length];
        private final int field_176798_g;
        private final String field_176805_h;
        private final String field_176806_i;

        private static final BlockQuartz.EnumType[] $VALUES = new BlockQuartz.EnumType[]{DEFAULT, CHISELED, LINES_Y, LINES_X, LINES_Z};
        private static final String __OBFID = "CL_00002074";

        private EnumType(String p_i45691_1_, int p_i45691_2_, int p_i45691_3_, String p_i45691_4_, String p_i45691_5_) {
            field_176798_g = p_i45691_3_;
            field_176805_h = p_i45691_4_;
            field_176806_i = p_i45691_5_;
        }

        public int getMetaFromState() {
            return field_176798_g;
        }

        @Override
        public String toString() {
            return field_176806_i;
        }

        public static BlockQuartz.EnumType func_176794_a(int p_176794_0_) {
            if (p_176794_0_ < 0 || p_176794_0_ >= EnumType.TYPES_ARRAY.length) {
                p_176794_0_ = 0;
            }

            return EnumType.TYPES_ARRAY[p_176794_0_];
        }

        @Override
        public String getName() {
            return field_176805_h;
        }

        static {
            BlockQuartz.EnumType[] var0 = EnumType.values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                BlockQuartz.EnumType var3 = var0[var2];
                EnumType.TYPES_ARRAY[var3.getMetaFromState()] = var3;
            }
        }
    }

    static final class SwitchAxis {
        static final int[] field_180101_a = new int[EnumFacing.Axis.values().length];
        private static final String __OBFID = "CL_00002075";

        static {
            try {
                SwitchAxis.field_180101_a[EnumFacing.Axis.Z.ordinal()] = 1;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                SwitchAxis.field_180101_a[EnumFacing.Axis.X.ordinal()] = 2;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                SwitchAxis.field_180101_a[EnumFacing.Axis.Y.ordinal()] = 3;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
