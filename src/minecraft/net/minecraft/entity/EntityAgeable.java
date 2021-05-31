package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAgeable extends EntityCreature {
    protected int field_175504_a;
    protected int field_175502_b;
    protected int field_175503_c;
    private float field_98056_d = -1.0F;
    private float field_98057_e;
    private static final String __OBFID = "CL_00001530";

    public EntityAgeable(World worldIn) {
        super(worldIn);
    }

    public abstract EntityAgeable createChild(EntityAgeable p_90011_1_);

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow,
     * gets into the saddle on a pig.
     */
    @Override
    public boolean interact(EntityPlayer p_70085_1_) {
        ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

        if (var2 != null && var2.getItem() == Items.spawn_egg) {
            if (!worldObj.isRemote) {
                Class var3 = EntityList.getClassFromID(var2.getMetadata());

                if (var3 != null && this.getClass() == var3) {
                    EntityAgeable var4 = createChild(this);

                    if (var4 != null) {
                        var4.setGrowingAge(-24000);
                        var4.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
                        worldObj.spawnEntityInWorld(var4);

                        if (var2.hasDisplayName()) {
                            var4.setCustomNameTag(var2.getDisplayName());
                        }

                        if (!p_70085_1_.capabilities.isCreativeMode) {
                            --var2.stackSize;

                            if (var2.stackSize <= 0) {
                                p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, (ItemStack) null);
                            }
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataWatcher.addObject(12, Byte.valueOf((byte) 0));
    }

    /**
     * The age value may be negative or positive or zero. If it's negative, it
     * get's incremented on each tick, if it's positive, it get's decremented
     * each tick. Don't confuse this with EntityLiving.getAge. With a negative
     * value the Entity is considered a child.
     */
    public int getGrowingAge() {
        return worldObj.isRemote ? dataWatcher.getWatchableObjectByte(12) : field_175504_a;
    }

    public void func_175501_a(int p_175501_1_, boolean p_175501_2_) {
        int var3 = getGrowingAge();
        int var4 = var3;
        var3 += p_175501_1_ * 20;

        if (var3 > 0) {
            var3 = 0;

            if (var4 < 0) {
                func_175500_n();
            }
        }

        int var5 = var3 - var4;
        setGrowingAge(var3);

        if (p_175501_2_) {
            field_175502_b += var5;

            if (field_175503_c == 0) {
                field_175503_c = 40;
            }
        }

        if (getGrowingAge() == 0) {
            setGrowingAge(field_175502_b);
        }
    }

    /**
     * "Adds the value of the parameter times 20 to the age of this entity. If
     * the entity is an adult (if the entity's age is greater than 0), it will
     * have no effect."
     */
    public void addGrowth(int p_110195_1_) {
        func_175501_a(p_110195_1_, false);
    }

    /**
     * The age value may be negative or positive or zero. If it's negative, it
     * get's incremented on each tick, if it's positive, it get's decremented
     * each tick. With a negative value the Entity is considered a child.
     */
    public void setGrowingAge(int p_70873_1_) {
        dataWatcher.updateObject(12, Byte.valueOf((byte) MathHelper.clamp_int(p_70873_1_, -1, 1)));
        field_175504_a = p_70873_1_;
        setScaleForAge(isChild());
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Age", getGrowingAge());
        tagCompound.setInteger("ForcedAge", field_175502_b);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        setGrowingAge(tagCompund.getInteger("Age"));
        field_175502_b = tagCompund.getInteger("ForcedAge");
    }

    /**
     * Called frequently so the entity can update its state every tick as
     * required. For example, zombies and skeletons use this to react to
     * sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (worldObj.isRemote) {
            if (field_175503_c > 0) {
                if (field_175503_c % 4 == 0) {
                    worldObj.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, posX + rand.nextFloat() * width * 2.0F - width, posY + 0.5D + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0F - width, 0.0D, 0.0D, 0.0D, new int[0]);
                }

                --field_175503_c;
            }

            setScaleForAge(isChild());
        } else {
            int var1 = getGrowingAge();

            if (var1 < 0) {
                ++var1;
                setGrowingAge(var1);

                if (var1 == 0) {
                    func_175500_n();
                }
            } else if (var1 > 0) {
                --var1;
                setGrowingAge(var1);
            }
        }
    }

    protected void func_175500_n() {
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    @Override
    public boolean isChild() {
        return getGrowingAge() < 0;
    }

    /**
     * "Sets the scale for an ageable entity according to the boolean parameter, which says if it's a child."
     */
    public void setScaleForAge(boolean p_98054_1_) {
        setScale(p_98054_1_ ? 0.5F : 1.0F);
    }

    /**
     * Sets the width and height of the entity. Args: width, height
     */
    @Override
    protected final void setSize(float width, float height) {
        boolean var3 = field_98056_d > 0.0F;
        field_98056_d = width;
        field_98057_e = height;

        if (!var3) {
            setScale(1.0F);
        }
    }

    protected final void setScale(float p_98055_1_) {
        super.setSize(field_98056_d * p_98055_1_, field_98057_e * p_98055_1_);
    }
}
