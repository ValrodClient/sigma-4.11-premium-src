package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TextureCompass extends TextureAtlasSprite {
    /**
     * Current compass heading in radians
     */
    public double currentAngle;

    /**
     * Speed and direction of compass rotation
     */
    public double angleDelta;
    public static String field_176608_l;
    private static final String __OBFID = "CL_00001071";

    public TextureCompass(String p_i1286_1_) {
        super(p_i1286_1_);
        field_176608_l = p_i1286_1_;
    }

    public void updateAnimation() {
        Minecraft var1 = Minecraft.getMinecraft();

        if (var1.theWorld != null && var1.thePlayer != null) {
            this.updateCompass(var1.theWorld, var1.thePlayer.posX, var1.thePlayer.posZ, (double) var1.thePlayer.rotationYaw, false, false);
        } else {
            this.updateCompass((World) null, 0.0D, 0.0D, 0.0D, true, false);
        }
    }

    /**
     * Updates the compass based on the given x,z coords and camera direction
     */
    public void updateCompass(World worldIn, double p_94241_2_, double p_94241_4_, double p_94241_6_, boolean p_94241_8_, boolean p_94241_9_) {
        if (!this.framesTextureData.isEmpty()) {
            double var10 = 0.0D;

            if (worldIn != null && !p_94241_8_) {
                BlockPos var12 = worldIn.getSpawnPoint();
                double var13 = (double) var12.getX() - p_94241_2_;
                double var15 = (double) var12.getZ() - p_94241_4_;
                p_94241_6_ %= 360.0D;
                var10 = -((p_94241_6_ - 90.0D) * Math.PI / 180.0D - Math.atan2(var15, var13));

                if (!worldIn.provider.isSurfaceWorld()) {
                    var10 = Math.random() * Math.PI * 2.0D;
                }
            }

            if (p_94241_9_) {
                this.currentAngle = var10;
            } else {
                double var17;

                for (var17 = var10 - this.currentAngle; var17 < -Math.PI; var17 += (Math.PI * 2D)) {
                    ;
                }

                while (var17 >= Math.PI) {
                    var17 -= (Math.PI * 2D);
                }

                var17 = MathHelper.clamp_double(var17, -1.0D, 1.0D);
                this.angleDelta += var17 * 0.1D;
                this.angleDelta *= 0.8D;
                this.currentAngle += this.angleDelta;
            }

            int var18;

            for (var18 = (int) ((this.currentAngle / (Math.PI * 2D) + 1.0D) * (double) this.framesTextureData.size()) % this.framesTextureData.size(); var18 < 0; var18 = (var18 + this.framesTextureData.size()) % this.framesTextureData.size()) {
                ;
            }

            if (var18 != this.frameCounter) {
                this.frameCounter = var18;
                TextureUtil.uploadTextureMipmap((int[][]) this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
    }
}
