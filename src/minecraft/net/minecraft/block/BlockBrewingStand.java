package net.minecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockBrewingStand extends BlockContainer {
    public static final PropertyBool[] BOTTLE_PROPS = new PropertyBool[]{PropertyBool.create("has_bottle_0"), PropertyBool.create("has_bottle_1"), PropertyBool.create("has_bottle_2")};
    private final Random rand = new Random();
    private static final String __OBFID = "CL_00000207";

    public BlockBrewingStand() {
        super(Material.iron);
        setDefaultState(blockState.getBaseState().withProperty(BlockBrewingStand.BOTTLE_PROPS[0], Boolean.valueOf(false)).withProperty(BlockBrewingStand.BOTTLE_PROPS[1], Boolean.valueOf(false)).withProperty(BlockBrewingStand.BOTTLE_PROPS[2], Boolean.valueOf(false)));
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType() {
        return 3;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing
     * the block.
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBrewingStand();
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    /**
     * Add all collision boxes of this Block to the list that intersect with the
     * given mask.
     *
     * @param collidingEntity the Entity colliding with this Block
     */
    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        setBlockBounds(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        setBlockBoundsForItemRender();
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
    public void setBlockBoundsForItemRender() {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        } else {
            TileEntity var9 = worldIn.getTileEntity(pos);

            if (var9 instanceof TileEntityBrewingStand) {
                playerIn.displayGUIChest((TileEntityBrewingStand) var9);
            }

            return true;
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity var6 = worldIn.getTileEntity(pos);

            if (var6 instanceof TileEntityBrewingStand) {
                ((TileEntityBrewingStand) var6).func_145937_a(stack.getDisplayName());
            }
        }
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        double var5 = pos.getX() + 0.4F + rand.nextFloat() * 0.2F;
        double var7 = pos.getY() + 0.7F + rand.nextFloat() * 0.3F;
        double var9 = pos.getZ() + 0.4F + rand.nextFloat() * 0.2F;
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var5, var7, var9, 0.0D, 0.0D, 0.0D, new int[0]);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity var4 = worldIn.getTileEntity(pos);

        if (var4 instanceof TileEntityBrewingStand) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityBrewingStand) var4);
        }

        super.breakBlock(worldIn, pos, state);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.brewing_stand;
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return Items.brewing_stand;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory(worldIn.getTileEntity(pos));
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState var2 = getDefaultState();

        for (int var3 = 0; var3 < 3; ++var3) {
            var2 = var2.withProperty(BlockBrewingStand.BOTTLE_PROPS[var3], Boolean.valueOf((meta & 1 << var3) > 0));
        }

        return var2;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;

        for (int var3 = 0; var3 < 3; ++var3) {
            if (((Boolean) state.getValue(BlockBrewingStand.BOTTLE_PROPS[var3])).booleanValue()) {
                var2 |= 1 << var3;
            }
        }

        return var2;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{BlockBrewingStand.BOTTLE_PROPS[0], BlockBrewingStand.BOTTLE_PROPS[1], BlockBrewingStand.BOTTLE_PROPS[2]});
    }
}
