package net.minecraft.block;

import com.google.common.base.Predicate;

import java.util.List;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockOldLog extends BlockLog {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate() {
        private static final String __OBFID = "CL_00002084";

        public boolean func_180200_a(BlockPlanks.EnumType p_180200_1_) {
            return p_180200_1_.func_176839_a() < 4;
        }

        @Override
        public boolean apply(Object p_apply_1_) {
            return func_180200_a((BlockPlanks.EnumType) p_apply_1_);
        }
    });
    private static final String __OBFID = "CL_00000281";

    public BlockOldLog() {
        setDefaultState(blockState.getBaseState().withProperty(BlockOldLog.VARIANT_PROP, BlockPlanks.EnumType.OAK).withProperty(BlockLog.AXIS_PROP, BlockLog.EnumAxis.Y));
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood
     * returns 4 blocks)
     */
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.OAK.func_176839_a()));
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.SPRUCE.func_176839_a()));
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.BIRCH.func_176839_a()));
        list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.JUNGLE.func_176839_a()));
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState var2 = getDefaultState().withProperty(BlockOldLog.VARIANT_PROP, BlockPlanks.EnumType.func_176837_a((meta & 3) % 4));

        switch (meta & 12) {
            case 0:
                var2 = var2.withProperty(BlockLog.AXIS_PROP, BlockLog.EnumAxis.Y);
                break;

            case 4:
                var2 = var2.withProperty(BlockLog.AXIS_PROP, BlockLog.EnumAxis.X);
                break;

            case 8:
                var2 = var2.withProperty(BlockLog.AXIS_PROP, BlockLog.EnumAxis.Z);
                break;

            default:
                var2 = var2.withProperty(BlockLog.AXIS_PROP, BlockLog.EnumAxis.NONE);
        }

        return var2;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        byte var2 = 0;
        int var3 = var2 | ((BlockPlanks.EnumType) state.getValue(BlockOldLog.VARIANT_PROP)).func_176839_a();

        switch (BlockOldLog.SwitchEnumAxis.field_180203_a[((BlockLog.EnumAxis) state.getValue(BlockLog.AXIS_PROP)).ordinal()]) {
            case 1:
                var3 |= 4;
                break;

            case 2:
                var3 |= 8;
                break;

            case 3:
                var3 |= 12;
        }

        return var3;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockOldLog.VARIANT_PROP, BlockLog.AXIS_PROP});
    }

    @Override
    protected ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType) state.getValue(BlockOldLog.VARIANT_PROP)).func_176839_a());
    }

    /**
     * Get the damage value that this Block should drop
     */
    @Override
    public int damageDropped(IBlockState state) {
        return ((BlockPlanks.EnumType) state.getValue(BlockOldLog.VARIANT_PROP)).func_176839_a();
    }

    static final class SwitchEnumAxis {
        static final int[] field_180203_a = new int[BlockLog.EnumAxis.values().length];
        private static final String __OBFID = "CL_00002083";

        static {
            try {
                SwitchEnumAxis.field_180203_a[BlockLog.EnumAxis.X.ordinal()] = 1;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                SwitchEnumAxis.field_180203_a[BlockLog.EnumAxis.Z.ordinal()] = 2;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                SwitchEnumAxis.field_180203_a[BlockLog.EnumAxis.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
