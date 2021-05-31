package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;

public class BlockRendererDispatcher implements IResourceManagerReloadListener {
    private BlockModelShapes field_175028_a;
    private final GameSettings field_175026_b;
    private final BlockModelRenderer blockModelRenderer = new BlockModelRenderer();
    private final ChestRenderer chestRenderer = new ChestRenderer();
    private final BlockFluidRenderer fluidRenderer = new BlockFluidRenderer();
    private static final String __OBFID = "CL_00002520";

    public BlockRendererDispatcher(BlockModelShapes p_i46237_1_, GameSettings p_i46237_2_) {
        this.field_175028_a = p_i46237_1_;
        this.field_175026_b = p_i46237_2_;
    }

    public BlockModelShapes func_175023_a() {
        return this.field_175028_a;
    }

    public void func_175020_a(IBlockState p_175020_1_, BlockPos p_175020_2_, TextureAtlasSprite p_175020_3_, IBlockAccess p_175020_4_) {
        Block var5 = p_175020_1_.getBlock();
        int var6 = var5.getRenderType();

        if (var6 == 3) {
            p_175020_1_ = var5.getActualState(p_175020_1_, p_175020_4_, p_175020_2_);
            IBakedModel var7 = this.field_175028_a.func_178125_b(p_175020_1_);
            IBakedModel var8 = (new SimpleBakedModel.Builder(var7, p_175020_3_)).func_177645_b();
            this.blockModelRenderer.func_178259_a(p_175020_4_, var8, p_175020_1_, p_175020_2_, Tessellator.getInstance().getWorldRenderer());
        }
    }

    public boolean func_175018_a(IBlockState p_175018_1_, BlockPos p_175018_2_, IBlockAccess p_175018_3_, WorldRenderer p_175018_4_) {
        try {
            int var5 = p_175018_1_.getBlock().getRenderType();

            if (var5 == -1) {
                return false;
            } else {
                switch (var5) {
                    case 1:
                        return this.fluidRenderer.func_178270_a(p_175018_3_, p_175018_1_, p_175018_2_, p_175018_4_);

                    case 2:
                        return false;

                    case 3:
                        IBakedModel var9 = this.getModelFromBlockState(p_175018_1_, p_175018_3_, p_175018_2_);
                        return this.blockModelRenderer.func_178259_a(p_175018_3_, var9, p_175018_1_, p_175018_2_, p_175018_4_);

                    default:
                        return false;
                }
            }
        } catch (Throwable var8) {
            CrashReport var6 = CrashReport.makeCrashReport(var8, "Tesselating block in world");
            CrashReportCategory var7 = var6.makeCategory("Block being tesselated");
            CrashReportCategory.addBlockInfo(var7, p_175018_2_, p_175018_1_.getBlock(), p_175018_1_.getBlock().getMetaFromState(p_175018_1_));
            throw new ReportedException(var6);
        }
    }

    public BlockModelRenderer func_175019_b() {
        return this.blockModelRenderer;
    }

    private IBakedModel func_175017_a(IBlockState p_175017_1_, BlockPos p_175017_2_) {
        IBakedModel var3 = this.field_175028_a.func_178125_b(p_175017_1_);

        if (p_175017_2_ != null && this.field_175026_b.field_178880_u && var3 instanceof WeightedBakedModel) {
            var3 = ((WeightedBakedModel) var3).func_177564_a(MathHelper.func_180186_a(p_175017_2_));
        }

        return var3;
    }

    public IBakedModel getModelFromBlockState(IBlockState p_175022_1_, IBlockAccess p_175022_2_, BlockPos p_175022_3_) {
        Block var4 = p_175022_1_.getBlock();

        if (p_175022_2_.getWorldType() != WorldType.DEBUG_WORLD) {
            try {
                p_175022_1_ = var4.getActualState(p_175022_1_, p_175022_2_, p_175022_3_);
            } catch (Exception var6) {
                ;
            }
        }

        IBakedModel var5 = this.field_175028_a.func_178125_b(p_175022_1_);

        if (p_175022_3_ != null && this.field_175026_b.field_178880_u && var5 instanceof WeightedBakedModel) {
            var5 = ((WeightedBakedModel) var5).func_177564_a(MathHelper.func_180186_a(p_175022_3_));
        }

        return var5;
    }

    public void func_175016_a(IBlockState p_175016_1_, float p_175016_2_) {
        int var3 = p_175016_1_.getBlock().getRenderType();

        if (var3 != -1) {
            switch (var3) {
                case 1:
                default:
                    break;

                case 2:
                    this.chestRenderer.func_178175_a(p_175016_1_.getBlock(), p_175016_2_);
                    break;

                case 3:
                    IBakedModel var4 = this.func_175017_a(p_175016_1_, (BlockPos) null);
                    this.blockModelRenderer.func_178266_a(var4, p_175016_1_, p_175016_2_, true);
            }
        }
    }

    public boolean func_175021_a(Block p_175021_1_, int p_175021_2_) {
        if (p_175021_1_ == null) {
            return false;
        } else {
            int var3 = p_175021_1_.getRenderType();
            return var3 == 3 ? false : var3 == 2;
        }
    }

    public void onResourceManagerReload(IResourceManager p_110549_1_) {
        this.fluidRenderer.func_178268_a();
    }
}
