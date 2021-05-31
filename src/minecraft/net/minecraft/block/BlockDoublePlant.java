package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public class BlockDoublePlant extends BlockBush implements IGrowable {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", BlockDoublePlant.EnumPlantType.class);
    public static final PropertyEnum HALF_PROP = PropertyEnum.create("half", BlockDoublePlant.EnumBlockHalf.class);
    private static final String __OBFID = "CL_00000231";

    public BlockDoublePlant() {
        super(Material.vine);
        setDefaultState(blockState.getBaseState().withProperty(BlockDoublePlant.VARIANT_PROP, BlockDoublePlant.EnumPlantType.SUNFLOWER).withProperty(BlockDoublePlant.HALF_PROP, BlockDoublePlant.EnumBlockHalf.LOWER));
        setHardness(0.0F);
        setStepSound(Block.soundTypeGrass);
        setUnlocalizedName("doublePlant");
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public BlockDoublePlant.EnumPlantType func_176490_e(IBlockAccess p_176490_1_, BlockPos p_176490_2_) {
        IBlockState var3 = p_176490_1_.getBlockState(p_176490_2_);

        if (var3.getBlock() == this) {
            var3 = getActualState(var3, p_176490_1_, p_176490_2_);
            return (BlockDoublePlant.EnumPlantType) var3.getValue(BlockDoublePlant.VARIANT_PROP);
        } else {
            return BlockDoublePlant.EnumPlantType.FERN;
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.offsetUp());
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for
     * e.g. tall grass)
     */
    @Override
    public boolean isReplaceable(World worldIn, BlockPos pos) {
        IBlockState var3 = worldIn.getBlockState(pos);

        if (var3.getBlock() != this) {
            return true;
        } else {
            BlockDoublePlant.EnumPlantType var4 = (BlockDoublePlant.EnumPlantType) getActualState(var3, worldIn, pos).getValue(BlockDoublePlant.VARIANT_PROP);
            return var4 == BlockDoublePlant.EnumPlantType.FERN || var4 == BlockDoublePlant.EnumPlantType.GRASS;
        }
    }

    @Override
    protected void func_176475_e(World worldIn, BlockPos p_176475_2_, IBlockState p_176475_3_) {
        if (!canBlockStay(worldIn, p_176475_2_, p_176475_3_)) {
            boolean var4 = p_176475_3_.getValue(BlockDoublePlant.HALF_PROP) == BlockDoublePlant.EnumBlockHalf.UPPER;
            BlockPos var5 = var4 ? p_176475_2_ : p_176475_2_.offsetUp();
            BlockPos var6 = var4 ? p_176475_2_.offsetDown() : p_176475_2_;
            Object var7 = var4 ? this : worldIn.getBlockState(var5).getBlock();
            Object var8 = var4 ? worldIn.getBlockState(var6).getBlock() : this;

            if (var7 == this) {
                worldIn.setBlockState(var5, Blocks.air.getDefaultState(), 3);
            }

            if (var8 == this) {
                worldIn.setBlockState(var6, Blocks.air.getDefaultState(), 3);

                if (!var4) {
                    dropBlockAsItem(worldIn, var6, p_176475_3_, 0);
                }
            }
        }
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos p_180671_2_, IBlockState p_180671_3_) {
        if (p_180671_3_.getValue(BlockDoublePlant.HALF_PROP) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            return worldIn.getBlockState(p_180671_2_.offsetDown()).getBlock() == this;
        } else {
            IBlockState var4 = worldIn.getBlockState(p_180671_2_.offsetUp());
            return var4.getBlock() == this && super.canBlockStay(worldIn, p_180671_2_, var4);
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (state.getValue(BlockDoublePlant.HALF_PROP) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            return null;
        } else {
            BlockDoublePlant.EnumPlantType var4 = (BlockDoublePlant.EnumPlantType) state.getValue(BlockDoublePlant.VARIANT_PROP);
            return var4 == BlockDoublePlant.EnumPlantType.FERN ? null : (var4 == BlockDoublePlant.EnumPlantType.GRASS ? (rand.nextInt(8) == 0 ? Items.wheat_seeds : null) : Item.getItemFromBlock(this));
        }
    }

    /**
     * Get the damage value that this Block should drop
     */
    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(BlockDoublePlant.HALF_PROP) != BlockDoublePlant.EnumBlockHalf.UPPER && state.getValue(BlockDoublePlant.VARIANT_PROP) != BlockDoublePlant.EnumPlantType.GRASS ? ((BlockDoublePlant.EnumPlantType) state.getValue(BlockDoublePlant.VARIANT_PROP)).func_176936_a() : 0;
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        BlockDoublePlant.EnumPlantType var4 = func_176490_e(worldIn, pos);
        return var4 != BlockDoublePlant.EnumPlantType.GRASS && var4 != BlockDoublePlant.EnumPlantType.FERN ? 16777215 : BiomeColorHelper.func_180286_a(worldIn, pos);
    }

    public void func_176491_a(World worldIn, BlockPos p_176491_2_, BlockDoublePlant.EnumPlantType p_176491_3_, int p_176491_4_) {
        worldIn.setBlockState(p_176491_2_, getDefaultState().withProperty(BlockDoublePlant.HALF_PROP, BlockDoublePlant.EnumBlockHalf.LOWER).withProperty(BlockDoublePlant.VARIANT_PROP, p_176491_3_), p_176491_4_);
        worldIn.setBlockState(p_176491_2_.offsetUp(), getDefaultState().withProperty(BlockDoublePlant.HALF_PROP, BlockDoublePlant.EnumBlockHalf.UPPER), p_176491_4_);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos.offsetUp(), getDefaultState().withProperty(BlockDoublePlant.HALF_PROP, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te) {
        if (worldIn.isRemote || playerIn.getCurrentEquippedItem() == null || playerIn.getCurrentEquippedItem().getItem() != Items.shears || state.getValue(BlockDoublePlant.HALF_PROP) != BlockDoublePlant.EnumBlockHalf.LOWER || !func_176489_b(worldIn, pos, state, playerIn)) {
            super.harvestBlock(worldIn, playerIn, pos, state, te);
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        if (state.getValue(BlockDoublePlant.HALF_PROP) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            if (worldIn.getBlockState(pos.offsetDown()).getBlock() == this) {
                if (!playerIn.capabilities.isCreativeMode) {
                    IBlockState var5 = worldIn.getBlockState(pos.offsetDown());
                    BlockDoublePlant.EnumPlantType var6 = (BlockDoublePlant.EnumPlantType) var5.getValue(BlockDoublePlant.VARIANT_PROP);

                    if (var6 != BlockDoublePlant.EnumPlantType.FERN && var6 != BlockDoublePlant.EnumPlantType.GRASS) {
                        worldIn.destroyBlock(pos.offsetDown(), true);
                    } else if (!worldIn.isRemote) {
                        if (playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
                            func_176489_b(worldIn, pos, var5, playerIn);
                            worldIn.setBlockToAir(pos.offsetDown());
                        } else {
                            worldIn.destroyBlock(pos.offsetDown(), true);
                        }
                    } else {
                        worldIn.setBlockToAir(pos.offsetDown());
                    }
                } else {
                    worldIn.setBlockToAir(pos.offsetDown());
                }
            }
        } else if (playerIn.capabilities.isCreativeMode && worldIn.getBlockState(pos.offsetUp()).getBlock() == this) {
            worldIn.setBlockState(pos.offsetUp(), Blocks.air.getDefaultState(), 2);
        }

        super.onBlockHarvested(worldIn, pos, state, playerIn);
    }

    private boolean func_176489_b(World worldIn, BlockPos p_176489_2_, IBlockState p_176489_3_, EntityPlayer p_176489_4_) {
        BlockDoublePlant.EnumPlantType var5 = (BlockDoublePlant.EnumPlantType) p_176489_3_.getValue(BlockDoublePlant.VARIANT_PROP);

        if (var5 != BlockDoublePlant.EnumPlantType.FERN && var5 != BlockDoublePlant.EnumPlantType.GRASS) {
            return false;
        } else {
            p_176489_4_.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
            int var6 = (var5 == BlockDoublePlant.EnumPlantType.GRASS ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).func_177044_a();
            Block.spawnAsEntity(worldIn, p_176489_2_, new ItemStack(Blocks.tallgrass, 2, var6));
            return true;
        }
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood
     * returns 4 blocks)
     */
    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        BlockDoublePlant.EnumPlantType[] var4 = BlockDoublePlant.EnumPlantType.values();
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            BlockDoublePlant.EnumPlantType var7 = var4[var6];
            list.add(new ItemStack(itemIn, 1, var7.func_176936_a()));
        }
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        return func_176490_e(worldIn, pos).func_176936_a();
    }

    @Override
    public boolean isStillGrowing(World worldIn, BlockPos p_176473_2_, IBlockState p_176473_3_, boolean p_176473_4_) {
        BlockDoublePlant.EnumPlantType var5 = func_176490_e(worldIn, p_176473_2_);
        return var5 != BlockDoublePlant.EnumPlantType.GRASS && var5 != BlockDoublePlant.EnumPlantType.FERN;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random p_180670_2_, BlockPos p_180670_3_, IBlockState p_180670_4_) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_) {
        Block.spawnAsEntity(worldIn, p_176474_3_, new ItemStack(this, 1, func_176490_e(worldIn, p_176474_3_).func_176936_a()));
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return (meta & 8) > 0 ? getDefaultState().withProperty(BlockDoublePlant.HALF_PROP, BlockDoublePlant.EnumBlockHalf.UPPER) : getDefaultState().withProperty(BlockDoublePlant.HALF_PROP, BlockDoublePlant.EnumBlockHalf.LOWER).withProperty(BlockDoublePlant.VARIANT_PROP, BlockDoublePlant.EnumPlantType.func_176938_a(meta & 7));
    }

    /**
     * Get the actual Block state of this Block at the given position. This
     * applies properties not visible in the metadata, such as fence
     * connections.
     */
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (state.getValue(BlockDoublePlant.HALF_PROP) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            IBlockState var4 = worldIn.getBlockState(pos.offsetDown());

            if (var4.getBlock() == this) {
                state = state.withProperty(BlockDoublePlant.VARIANT_PROP, var4.getValue(BlockDoublePlant.VARIANT_PROP));
            }
        }

        return state;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockDoublePlant.HALF_PROP) == BlockDoublePlant.EnumBlockHalf.UPPER ? 8 : ((BlockDoublePlant.EnumPlantType) state.getValue(BlockDoublePlant.VARIANT_PROP)).func_176936_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockDoublePlant.HALF_PROP, BlockDoublePlant.VARIANT_PROP});
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered
     * slightly offset.
     */
    @Override
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }

    static enum EnumBlockHalf implements IStringSerializable {
        UPPER("UPPER", 0), LOWER("LOWER", 1);

        private static final BlockDoublePlant.EnumBlockHalf[] $VALUES = new BlockDoublePlant.EnumBlockHalf[]{UPPER, LOWER};
        private static final String __OBFID = "CL_00002122";

        private EnumBlockHalf(String p_i45724_1_, int p_i45724_2_) {
        }

        @Override
        public String toString() {
            return getName();
        }

        @Override
        public String getName() {
            return this == UPPER ? "upper" : "lower";
        }
    }

    public static enum EnumPlantType implements IStringSerializable {
        SUNFLOWER("SUNFLOWER", 0, 0, "sunflower"), SYRINGA("SYRINGA", 1, 1, "syringa"), GRASS("GRASS", 2, 2, "double_grass", "grass"), FERN("FERN", 3, 3, "double_fern", "fern"), ROSE("ROSE", 4, 4, "double_rose", "rose"), PAEONIA("PAEONIA", 5, 5, "paeonia");
        private static final BlockDoublePlant.EnumPlantType[] field_176941_g = new BlockDoublePlant.EnumPlantType[EnumPlantType.values().length];
        private final int field_176949_h;
        private final String field_176950_i;
        private final String field_176947_j;

        private static final BlockDoublePlant.EnumPlantType[] $VALUES = new BlockDoublePlant.EnumPlantType[]{SUNFLOWER, SYRINGA, GRASS, FERN, ROSE, PAEONIA};
        private static final String __OBFID = "CL_00002121";

        private EnumPlantType(String p_i45722_1_, int p_i45722_2_, int p_i45722_3_, String p_i45722_4_) {
            this(p_i45722_1_, p_i45722_2_, p_i45722_3_, p_i45722_4_, p_i45722_4_);
        }

        private EnumPlantType(String p_i45723_1_, int p_i45723_2_, int p_i45723_3_, String p_i45723_4_, String p_i45723_5_) {
            field_176949_h = p_i45723_3_;
            field_176950_i = p_i45723_4_;
            field_176947_j = p_i45723_5_;
        }

        public int func_176936_a() {
            return field_176949_h;
        }

        @Override
        public String toString() {
            return field_176950_i;
        }

        public static BlockDoublePlant.EnumPlantType func_176938_a(int p_176938_0_) {
            if (p_176938_0_ < 0 || p_176938_0_ >= EnumPlantType.field_176941_g.length) {
                p_176938_0_ = 0;
            }

            return EnumPlantType.field_176941_g[p_176938_0_];
        }

        @Override
        public String getName() {
            return field_176950_i;
        }

        public String func_176939_c() {
            return field_176947_j;
        }

        static {
            BlockDoublePlant.EnumPlantType[] var0 = EnumPlantType.values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                BlockDoublePlant.EnumPlantType var3 = var0[var2];
                EnumPlantType.field_176941_g[var3.func_176936_a()] = var3;
            }
        }
    }
}
