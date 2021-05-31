package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;

import info.sigmaclient.event.EventSystem;
import info.sigmaclient.event.impl.EventLiquidCollide;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public abstract class BlockLiquid extends Block {
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);
    private static final String __OBFID = "CL_00000265";

    protected BlockLiquid(Material p_i45413_1_) {
        super(p_i45413_1_);
        setDefaultState(blockState.getBaseState().withProperty(BlockLiquid.LEVEL, Integer.valueOf(0)));
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        setTickRandomly(true);
    }

    @Override
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return blockMaterial != Material.lava;
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return blockMaterial == Material.water ? BiomeColorHelper.func_180288_c(worldIn, pos) : 16777215;
    }

    /**
     * Returns the percentage of the liquid block that is air, based on the
     * given flow decay of the liquid
     */
    public static float getLiquidHeightPercent(int p_149801_0_) {
        if (p_149801_0_ >= 8) {
            p_149801_0_ = 0;
        }

        return (p_149801_0_ + 1) / 9.0F;
    }

    protected int func_176362_e(IBlockAccess p_176362_1_, BlockPos p_176362_2_) {
        return p_176362_1_.getBlockState(p_176362_2_).getBlock().getMaterial() == blockMaterial ? ((Integer) p_176362_1_.getBlockState(p_176362_2_).getValue(BlockLiquid.LEVEL)).intValue() : -1;
    }

    protected int func_176366_f(IBlockAccess p_176366_1_, BlockPos p_176366_2_) {
        int var3 = func_176362_e(p_176366_1_, p_176366_2_);
        return var3 >= 8 ? 0 : var3;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean p_176209_2_) {
        return (p_176209_2_ && ((Integer) state.getValue(BlockLiquid.LEVEL)).intValue() == 0);
    }

    /**
     * Whether this Block is solid on the given Side
     */
    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        Material var4 = worldIn.getBlockState(pos).getBlock().getMaterial();
        return var4 == blockMaterial ? false : (side == EnumFacing.UP ? true : (var4 == Material.ice ? false : super.isBlockSolid(worldIn, pos, side)));
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return worldIn.getBlockState(pos).getBlock().getMaterial() == blockMaterial ? false : (side == EnumFacing.UP ? true : super.shouldSideBeRendered(worldIn, pos, side));
    }

    public boolean func_176364_g(IBlockAccess p_176364_1_, BlockPos p_176364_2_) {
        for (int var3 = -1; var3 <= 1; ++var3) {
            for (int var4 = -1; var4 <= 1; ++var4) {
                IBlockState var5 = p_176364_1_.getBlockState(p_176364_2_.add(var3, 0, var4));
                Block var6 = var5.getBlock();
                Material var7 = var6.getMaterial();

                if (var7 != blockMaterial && !var6.isFullBlock()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        EventLiquidCollide eb = (EventLiquidCollide) EventSystem.getInstance(EventLiquidCollide.class);
        eb.fire(this, pos, new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ));
        if (eb.isCancelled()) {
            return eb.getBounds();
        }
        return null;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType() {
        return 1;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    protected Vec3 func_180687_h(IBlockAccess p_180687_1_, BlockPos p_180687_2_) {
        Vec3 var3 = new Vec3(0.0D, 0.0D, 0.0D);
        int var4 = func_176366_f(p_180687_1_, p_180687_2_);
        Iterator var5 = EnumFacing.Plane.HORIZONTAL.iterator();
        EnumFacing var6;
        BlockPos var7;

        while (var5.hasNext()) {
            var6 = (EnumFacing) var5.next();
            var7 = p_180687_2_.offset(var6);
            int var8 = func_176366_f(p_180687_1_, var7);
            int var9;

            if (var8 < 0) {
                if (!p_180687_1_.getBlockState(var7).getBlock().getMaterial().blocksMovement()) {
                    var8 = func_176366_f(p_180687_1_, var7.offsetDown());

                    if (var8 >= 0) {
                        var9 = var8 - (var4 - 8);
                        var3 = var3.addVector((var7.getX() - p_180687_2_.getX()) * var9, (var7.getY() - p_180687_2_.getY()) * var9, (var7.getZ() - p_180687_2_.getZ()) * var9);
                    }
                }
            } else if (var8 >= 0) {
                var9 = var8 - var4;
                var3 = var3.addVector((var7.getX() - p_180687_2_.getX()) * var9, (var7.getY() - p_180687_2_.getY()) * var9, (var7.getZ() - p_180687_2_.getZ()) * var9);
            }
        }

        if (((Integer) p_180687_1_.getBlockState(p_180687_2_).getValue(BlockLiquid.LEVEL)).intValue() >= 8) {
            var5 = EnumFacing.Plane.HORIZONTAL.iterator();

            while (var5.hasNext()) {
                var6 = (EnumFacing) var5.next();
                var7 = p_180687_2_.offset(var6);

                if (isBlockSolid(p_180687_1_, var7, var6) || isBlockSolid(p_180687_1_, var7.offsetUp(), var6)) {
                    var3 = var3.normalize().addVector(0.0D, -6.0D, 0.0D);
                    break;
                }
            }
        }

        return var3.normalize();
    }

    @Override
    public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion) {
        return motion.add(func_180687_h(worldIn, pos));
    }

    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(World worldIn) {
        return blockMaterial == Material.water ? 5 : (blockMaterial == Material.lava ? (worldIn.provider.getHasNoSky() ? 10 : 30) : 0);
    }

    @Override
    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
        int var3 = worldIn.getCombinedLight(pos, 0);
        int var4 = worldIn.getCombinedLight(pos.offsetUp(), 0);
        int var5 = var3 & 255;
        int var6 = var4 & 255;
        int var7 = var3 >> 16 & 255;
        int var8 = var4 >> 16 & 255;
        return (var5 > var6 ? var5 : var6) | (var7 > var8 ? var7 : var8) << 16;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return blockMaterial == Material.water ? EnumWorldBlockLayer.TRANSLUCENT : EnumWorldBlockLayer.SOLID;
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        double var5 = pos.getX();
        double var7 = pos.getY();
        double var9 = pos.getZ();

        if (blockMaterial == Material.water) {
            int var11 = ((Integer) state.getValue(BlockLiquid.LEVEL)).intValue();

            if (var11 > 0 && var11 < 8) {
                if (rand.nextInt(64) == 0) {
                    worldIn.playSound(var5 + 0.5D, var7 + 0.5D, var9 + 0.5D, "liquid.water", rand.nextFloat() * 0.25F + 0.75F, rand.nextFloat() * 1.0F + 0.5F, false);
                }
            } else if (rand.nextInt(10) == 0) {
                worldIn.spawnParticle(EnumParticleTypes.SUSPENDED, var5 + rand.nextFloat(), var7 + rand.nextFloat(), var9 + rand.nextFloat(), 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }

        if (blockMaterial == Material.lava && worldIn.getBlockState(pos.offsetUp()).getBlock().getMaterial() == Material.air && !worldIn.getBlockState(pos.offsetUp()).getBlock().isOpaqueCube()) {
            if (rand.nextInt(100) == 0) {
                double var18 = var5 + rand.nextFloat();
                double var13 = var7 + maxY;
                double var15 = var9 + rand.nextFloat();
                worldIn.spawnParticle(EnumParticleTypes.LAVA, var18, var13, var15, 0.0D, 0.0D, 0.0D, new int[0]);
                worldIn.playSound(var18, var13, var15, "liquid.lavapop", 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }

            if (rand.nextInt(200) == 0) {
                worldIn.playSound(var5, var7, var9, "liquid.lava", 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
            }
        }

        if (rand.nextInt(10) == 0 && World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown())) {
            Material var19 = worldIn.getBlockState(pos.offsetDown(2)).getBlock().getMaterial();

            if (!var19.blocksMovement() && !var19.isLiquid()) {
                double var12 = var5 + rand.nextFloat();
                double var14 = var7 - 1.05D;
                double var16 = var9 + rand.nextFloat();

                if (blockMaterial == Material.water) {
                    worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, var12, var14, var16, 0.0D, 0.0D, 0.0D, new int[0]);
                } else {
                    worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, var12, var14, var16, 0.0D, 0.0D, 0.0D, new int[0]);
                }
            }
        }
    }

    public static double func_180689_a(IBlockAccess p_180689_0_, BlockPos p_180689_1_, Material p_180689_2_) {
        Vec3 var3 = BlockLiquid.getDynamicLiquidForMaterial(p_180689_2_).func_180687_h(p_180689_0_, p_180689_1_);
        return var3.xCoord == 0.0D && var3.zCoord == 0.0D ? -1000.0D : Math.atan2(var3.zCoord, var3.xCoord) - (Math.PI / 2D);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        func_176365_e(worldIn, pos, state);
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        func_176365_e(worldIn, pos, state);
    }

    public boolean func_176365_e(World worldIn, BlockPos p_176365_2_, IBlockState p_176365_3_) {
        if (blockMaterial == Material.lava) {
            boolean var4 = false;
            EnumFacing[] var5 = EnumFacing.values();
            int var6 = var5.length;

            for (int var7 = 0; var7 < var6; ++var7) {
                EnumFacing var8 = var5[var7];

                if (var8 != EnumFacing.DOWN && worldIn.getBlockState(p_176365_2_.offset(var8)).getBlock().getMaterial() == Material.water) {
                    var4 = true;
                    break;
                }
            }

            if (var4) {
                Integer var9 = (Integer) p_176365_3_.getValue(BlockLiquid.LEVEL);

                if (var9.intValue() == 0) {
                    worldIn.setBlockState(p_176365_2_, Blocks.obsidian.getDefaultState());
                    func_180688_d(worldIn, p_176365_2_);
                    return true;
                }

                if (var9.intValue() <= 4) {
                    worldIn.setBlockState(p_176365_2_, Blocks.cobblestone.getDefaultState());
                    func_180688_d(worldIn, p_176365_2_);
                    return true;
                }
            }
        }

        return false;
    }

    protected void func_180688_d(World worldIn, BlockPos p_180688_2_) {
        double var3 = p_180688_2_.getX();
        double var5 = p_180688_2_.getY();
        double var7 = p_180688_2_.getZ();
        worldIn.playSoundEffect(var3 + 0.5D, var5 + 0.5D, var7 + 0.5D, "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

        for (int var9 = 0; var9 < 8; ++var9) {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var3 + Math.random(), var5 + 1.2D, var7 + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BlockLiquid.LEVEL, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((Integer) state.getValue(BlockLiquid.LEVEL)).intValue();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockLiquid.LEVEL});
    }

    public static BlockDynamicLiquid getDynamicLiquidForMaterial(Material p_176361_0_) {
        if (p_176361_0_ == Material.water) {
            return Blocks.flowing_water;
        } else if (p_176361_0_ == Material.lava) {
            return Blocks.flowing_lava;
        } else {
            throw new IllegalArgumentException("Invalid material");
        }
    }

    public static BlockStaticLiquid getStaticLiquidForMaterial(Material p_176363_0_) {
        if (p_176363_0_ == Material.water) {
            return Blocks.water;
        } else if (p_176363_0_ == Material.lava) {
            return Blocks.lava;
        } else {
            throw new IllegalArgumentException("Invalid material");
        }
    }
}
