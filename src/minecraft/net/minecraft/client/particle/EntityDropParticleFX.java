package net.minecraft.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDropParticleFX extends EntityFX {
    /**
     * the material type for dropped items/blocks
     */
    private Material materialType;

    /**
     * The height of the current bob
     */
    private int bobTimer;
    private static final String __OBFID = "CL_00000901";

    protected EntityDropParticleFX(World worldIn, double p_i1203_2_, double p_i1203_4_, double p_i1203_6_, Material p_i1203_8_) {
        super(worldIn, p_i1203_2_, p_i1203_4_, p_i1203_6_, 0.0D, 0.0D, 0.0D);
        this.motionX = this.motionY = this.motionZ = 0.0D;

        if (p_i1203_8_ == Material.water) {
            this.particleRed = 0.0F;
            this.particleGreen = 0.0F;
            this.particleBlue = 1.0F;
        } else {
            this.particleRed = 1.0F;
            this.particleGreen = 0.0F;
            this.particleBlue = 0.0F;
        }

        this.setParticleTextureIndex(113);
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.materialType = p_i1203_8_;
        this.bobTimer = 40;
        this.particleMaxAge = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
        this.motionX = this.motionY = this.motionZ = 0.0D;
    }

    public int getBrightnessForRender(float p_70070_1_) {
        return this.materialType == Material.water ? super.getBrightnessForRender(p_70070_1_) : 257;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float p_70013_1_) {
        return this.materialType == Material.water ? super.getBrightness(p_70013_1_) : 1.0F;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.materialType == Material.water) {
            this.particleRed = 0.2F;
            this.particleGreen = 0.3F;
            this.particleBlue = 1.0F;
        } else {
            this.particleRed = 1.0F;
            this.particleGreen = 16.0F / (float) (40 - this.bobTimer + 16);
            this.particleBlue = 4.0F / (float) (40 - this.bobTimer + 8);
        }

        this.motionY -= (double) this.particleGravity;

        if (this.bobTimer-- > 0) {
            this.motionX *= 0.02D;
            this.motionY *= 0.02D;
            this.motionZ *= 0.02D;
            this.setParticleTextureIndex(113);
        } else {
            this.setParticleTextureIndex(112);
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }

        if (this.onGround) {
            if (this.materialType == Material.water) {
                this.setDead();
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
            } else {
                this.setParticleTextureIndex(114);
            }

            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        BlockPos var1 = new BlockPos(this);
        IBlockState var2 = this.worldObj.getBlockState(var1);
        Material var3 = var2.getBlock().getMaterial();

        if (var3.isLiquid() || var3.isSolid()) {
            double var4 = 0.0D;

            if (var2.getBlock() instanceof BlockLiquid) {
                var4 = (double) BlockLiquid.getLiquidHeightPercent(((Integer) var2.getValue(BlockLiquid.LEVEL)).intValue());
            }

            double var6 = (double) (MathHelper.floor_double(this.posY) + 1) - var4;

            if (this.posY < var6) {
                this.setDead();
            }
        }
    }

    public static class LavaFactory implements IParticleFactory {
        private static final String __OBFID = "CL_00002607";

        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_) {
            return new EntityDropParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, Material.lava);
        }
    }

    public static class WaterFactory implements IParticleFactory {
        private static final String __OBFID = "CL_00002606";

        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int... p_178902_15_) {
            return new EntityDropParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, Material.water);
        }
    }
}
