package net.minecraft.optifine;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockMycelium;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BetterGrass {
    private static IBakedModel modelEmpty = new SimpleBakedModel(new ArrayList(), new ArrayList(), false, false, (TextureAtlasSprite) null, (ItemCameraTransforms) null);
    private static IBakedModel modelCubeMycelium = null;
    private static IBakedModel modelCubeGrassSnowy = null;
    private static IBakedModel modelCubeGrass = null;

    public static void update() {
        BetterGrass.modelCubeGrass = BlockModelUtils.makeModelCube("minecraft:blocks/grass_top", 0);
        BetterGrass.modelCubeGrassSnowy = BlockModelUtils.makeModelCube("minecraft:blocks/snow", -1);
        BetterGrass.modelCubeMycelium = BlockModelUtils.makeModelCube("minecraft:blocks/mycelium_top", -1);
    }

    public static List getFaceQuads(IBlockAccess blockAccess, Block block, BlockPos blockPos, EnumFacing facing, List quads) {
        if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
            if (block instanceof BlockMycelium) {
                return Config.isBetterGrassFancy() ? (BetterGrass.getBlockAt(blockPos.offsetDown(), facing, blockAccess) == Blocks.mycelium ? BetterGrass.modelCubeMycelium.func_177551_a(facing) : quads) : BetterGrass.modelCubeMycelium.func_177551_a(facing);
            } else {
                if (block instanceof BlockGrass) {
                    Block blockUp = blockAccess.getBlockState(blockPos.offsetUp()).getBlock();
                    boolean snowy = blockUp == Blocks.snow || blockUp == Blocks.snow_layer;

                    if (!Config.isBetterGrassFancy()) {
                        if (snowy) {
                            return BetterGrass.modelCubeGrassSnowy.func_177551_a(facing);
                        }

                        return BetterGrass.modelCubeGrass.func_177551_a(facing);
                    }

                    if (snowy) {
                        if (BetterGrass.getBlockAt(blockPos, facing, blockAccess) == Blocks.snow_layer) {
                            return BetterGrass.modelCubeGrassSnowy.func_177551_a(facing);
                        }
                    } else if (BetterGrass.getBlockAt(blockPos.offsetDown(), facing, blockAccess) == Blocks.grass) {
                        return BetterGrass.modelCubeGrass.func_177551_a(facing);
                    }
                }

                return quads;
            }
        } else {
            return quads;
        }
    }

    private static Block getBlockAt(BlockPos blockPos, EnumFacing facing, IBlockAccess blockAccess) {
        BlockPos pos = blockPos.offset(facing);
        Block block = blockAccess.getBlockState(pos).getBlock();
        return block;
    }
}
