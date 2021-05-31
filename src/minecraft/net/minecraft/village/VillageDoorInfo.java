package net.minecraft.village;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class VillageDoorInfo {
    private final BlockPos field_179859_a;
    private final BlockPos field_179857_b;
    private final EnumFacing field_179858_c;
    private int lastActivityTimestamp;
    private boolean isDetachedFromVillageFlag;
    private int doorOpeningRestrictionCounter;
    private static final String __OBFID = "CL_00001630";

    public VillageDoorInfo(BlockPos p_i45871_1_, int p_i45871_2_, int p_i45871_3_, int p_i45871_4_) {
        this(p_i45871_1_, VillageDoorInfo.func_179854_a(p_i45871_2_, p_i45871_3_), p_i45871_4_);
    }

    private static EnumFacing func_179854_a(int p_179854_0_, int p_179854_1_) {
        return p_179854_0_ < 0 ? EnumFacing.WEST : (p_179854_0_ > 0 ? EnumFacing.EAST : (p_179854_1_ < 0 ? EnumFacing.NORTH : EnumFacing.SOUTH));
    }

    public VillageDoorInfo(BlockPos p_i45872_1_, EnumFacing p_i45872_2_, int p_i45872_3_) {
        field_179859_a = p_i45872_1_;
        field_179858_c = p_i45872_2_;
        field_179857_b = p_i45872_1_.offset(p_i45872_2_, 2);
        lastActivityTimestamp = p_i45872_3_;
    }

    /**
     * Returns the squared distance between this door and the given coordinate.
     */
    public int getDistanceSquared(int p_75474_1_, int p_75474_2_, int p_75474_3_) {
        return (int) field_179859_a.distanceSq(p_75474_1_, p_75474_2_, p_75474_3_);
    }

    public int func_179848_a(BlockPos p_179848_1_) {
        return (int) p_179848_1_.distanceSq(func_179852_d());
    }

    public int func_179846_b(BlockPos p_179846_1_) {
        return (int) field_179857_b.distanceSq(p_179846_1_);
    }

    public boolean func_179850_c(BlockPos p_179850_1_) {
        int var2 = p_179850_1_.getX() - field_179859_a.getX();
        int var3 = p_179850_1_.getZ() - field_179859_a.getY();
        return var2 * field_179858_c.getFrontOffsetX() + var3 * field_179858_c.getFrontOffsetZ() >= 0;
    }

    public void resetDoorOpeningRestrictionCounter() {
        doorOpeningRestrictionCounter = 0;
    }

    public void incrementDoorOpeningRestrictionCounter() {
        ++doorOpeningRestrictionCounter;
    }

    public int getDoorOpeningRestrictionCounter() {
        return doorOpeningRestrictionCounter;
    }

    public BlockPos func_179852_d() {
        return field_179859_a;
    }

    public BlockPos func_179856_e() {
        return field_179857_b;
    }

    public int func_179847_f() {
        return field_179858_c.getFrontOffsetX() * 2;
    }

    public int func_179855_g() {
        return field_179858_c.getFrontOffsetZ() * 2;
    }

    public int getInsidePosY() {
        return lastActivityTimestamp;
    }

    public void func_179849_a(int p_179849_1_) {
        lastActivityTimestamp = p_179849_1_;
    }

    public boolean func_179851_i() {
        return isDetachedFromVillageFlag;
    }

    public void func_179853_a(boolean p_179853_1_) {
        isDetachedFromVillageFlag = p_179853_1_;
    }
}
