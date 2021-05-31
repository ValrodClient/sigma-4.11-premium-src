package net.minecraft.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWallSign extends BlockSign {
    public static final PropertyDirection field_176412_a = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final String __OBFID = "CL_00002047";

    public BlockWallSign() {
        setDefaultState(blockState.getBaseState().withProperty(BlockWallSign.field_176412_a, EnumFacing.NORTH));
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        EnumFacing var3 = (EnumFacing) access.getBlockState(pos).getValue(BlockWallSign.field_176412_a);
        float var4 = 0.28125F;
        float var5 = 0.78125F;
        float var6 = 0.0F;
        float var7 = 1.0F;
        float var8 = 0.125F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

        switch (BlockWallSign.SwitchEnumFacing.field_177331_a[var3.ordinal()]) {
            case 1:
                setBlockBounds(var6, var4, 1.0F - var8, var7, var5, 1.0F);
                break;

            case 2:
                setBlockBounds(var6, var4, 0.0F, var7, var5, var8);
                break;

            case 3:
                setBlockBounds(1.0F - var8, var4, var6, 1.0F, var5, var7);
                break;

            case 4:
                setBlockBounds(0.0F, var4, var6, var8, var5, var7);
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing var5 = (EnumFacing) state.getValue(BlockWallSign.field_176412_a);

        if (!worldIn.getBlockState(pos.offset(var5.getOpposite())).getBlock().getMaterial().isSolid()) {
            dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }

        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing var2 = EnumFacing.getFront(meta);

        if (var2.getAxis() == EnumFacing.Axis.Y) {
            var2 = EnumFacing.NORTH;
        }

        return getDefaultState().withProperty(BlockWallSign.field_176412_a, var2);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(BlockWallSign.field_176412_a)).getIndex();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockWallSign.field_176412_a});
    }

    static final class SwitchEnumFacing {
        static final int[] field_177331_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002046";

        static {
            try {
                SwitchEnumFacing.field_177331_a[EnumFacing.NORTH.ordinal()] = 1;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                SwitchEnumFacing.field_177331_a[EnumFacing.SOUTH.ordinal()] = 2;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                SwitchEnumFacing.field_177331_a[EnumFacing.WEST.ordinal()] = 3;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                SwitchEnumFacing.field_177331_a[EnumFacing.EAST.ordinal()] = 4;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
