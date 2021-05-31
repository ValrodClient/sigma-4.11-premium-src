package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockPumpkin extends BlockDirectional {
    private BlockPattern field_176394_a;
    private BlockPattern field_176393_b;
    private BlockPattern field_176395_M;
    private BlockPattern field_176396_O;
    private static final String __OBFID = "CL_00000291";

    protected BlockPumpkin() {
        super(Material.gourd);
        setDefaultState(blockState.getBaseState().withProperty(BlockDirectional.AGE, EnumFacing.NORTH));
        setTickRandomly(true);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        createGolem(worldIn, pos);
    }

    public boolean func_176390_d(World worldIn, BlockPos p_176390_2_) {
        return func_176392_j().func_177681_a(worldIn, p_176390_2_) != null || func_176389_S().func_177681_a(worldIn, p_176390_2_) != null;
    }

    private void createGolem(World worldIn, BlockPos p_180673_2_) {
        BlockPattern.PatternHelper var3;
        int var4;
        int var6;

        if ((var3 = func_176391_l().func_177681_a(worldIn, p_180673_2_)) != null) {
            for (var4 = 0; var4 < func_176391_l().func_177685_b(); ++var4) {
                BlockWorldState var5 = var3.func_177670_a(0, var4, 0);
                worldIn.setBlockState(var5.getPos(), Blocks.air.getDefaultState(), 2);
            }

            EntitySnowman var9 = new EntitySnowman(worldIn);
            BlockPos var11 = var3.func_177670_a(0, 2, 0).getPos();
            var9.setLocationAndAngles(var11.getX() + 0.5D, var11.getY() + 0.05D, var11.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntityInWorld(var9);

            for (var6 = 0; var6 < 120; ++var6) {
                worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, var11.getX() + worldIn.rand.nextDouble(), var11.getY() + worldIn.rand.nextDouble() * 2.5D, var11.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
            }

            for (var6 = 0; var6 < func_176391_l().func_177685_b(); ++var6) {
                BlockWorldState var7 = var3.func_177670_a(0, var6, 0);
                worldIn.func_175722_b(var7.getPos(), Blocks.air);
            }
        } else if ((var3 = func_176388_T().func_177681_a(worldIn, p_180673_2_)) != null) {
            for (var4 = 0; var4 < func_176388_T().func_177684_c(); ++var4) {
                for (int var12 = 0; var12 < func_176388_T().func_177685_b(); ++var12) {
                    worldIn.setBlockState(var3.func_177670_a(var4, var12, 0).getPos(), Blocks.air.getDefaultState(), 2);
                }
            }

            BlockPos var10 = var3.func_177670_a(1, 2, 0).getPos();
            EntityIronGolem var13 = new EntityIronGolem(worldIn);
            var13.setPlayerCreated(true);
            var13.setLocationAndAngles(var10.getX() + 0.5D, var10.getY() + 0.05D, var10.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.spawnEntityInWorld(var13);

            for (var6 = 0; var6 < 120; ++var6) {
                worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, var10.getX() + worldIn.rand.nextDouble(), var10.getY() + worldIn.rand.nextDouble() * 3.9D, var10.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
            }

            for (var6 = 0; var6 < func_176388_T().func_177684_c(); ++var6) {
                for (int var14 = 0; var14 < func_176388_T().func_177685_b(); ++var14) {
                    BlockWorldState var8 = var3.func_177670_a(var6, var14, 0);
                    worldIn.func_175722_b(var8.getPos(), Blocks.air);
                }
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().blockMaterial.isReplaceable() && World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown());
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(BlockDirectional.AGE, placer.func_174811_aO().getOpposite());
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockDirectional.AGE, EnumFacing.getHorizontal(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(BlockDirectional.AGE)).getHorizontalIndex();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockDirectional.AGE});
    }

    protected BlockPattern func_176392_j() {
        if (field_176394_a == null) {
            field_176394_a = FactoryBlockPattern.start().aisle(new String[]{" ", "#", "#"}).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
        }

        return field_176394_a;
    }

    protected BlockPattern func_176391_l() {
        if (field_176393_b == null) {
            field_176393_b = FactoryBlockPattern.start().aisle(new String[]{"^", "#", "#"}).where('^', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.pumpkin))).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
        }

        return field_176393_b;
    }

    protected BlockPattern func_176389_S() {
        if (field_176395_M == null) {
            field_176395_M = FactoryBlockPattern.start().aisle(new String[]{"~ ~", "###", "~#~"}).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }

        return field_176395_M;
    }

    protected BlockPattern func_176388_T() {
        if (field_176396_O == null) {
            field_176396_O = FactoryBlockPattern.start().aisle(new String[]{"~^~", "###", "~#~"}).where('^', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.pumpkin))).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }

        return field_176396_O;
    }
}
