package net.minecraft.optifine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

public class ConnectedTextures {
    private static Map[] spriteQuadMaps = null;
    private static ConnectedProperties[][] blockProperties = null;
    private static ConnectedProperties[][] tileProperties = null;
    private static boolean multipass = false;
    private static final int Y_NEG_DOWN = 0;
    private static final int Y_POS_UP = 1;
    private static final int Z_NEG_NORTH = 2;
    private static final int Z_POS_SOUTH = 3;
    private static final int X_NEG_WEST = 4;
    private static final int X_POS_EAST = 5;
    private static final int Y_AXIS = 0;
    private static final int Z_AXIS = 1;
    private static final int X_AXIS = 2;
    private static final String[] propSuffixes = new String[]{"", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static final int[] ctmIndexes = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 0, 0, 0, 0, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 0, 0, 0, 0, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 0, 0, 0, 0, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 0, 0, 0, 0, 0};
    public static final IBlockState AIR_DEFAULT_STATE = Blocks.air.getDefaultState();
    private static TextureAtlasSprite emptySprite = null;

    public static synchronized BakedQuad getConnectedTexture(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, BakedQuad quad, RenderEnv renderEnv) {
        TextureAtlasSprite spriteIn = quad.getSprite();

        if (spriteIn == null) {
            return quad;
        } else {
            Block block = blockState.getBlock();

            if (block instanceof BlockPane && spriteIn.getIconName().startsWith("minecraft:blocks/glass_pane_top")) {
                IBlockState side = blockAccess.getBlockState(blockPos.offset(quad.getFace()));

                if (side == blockState) {
                    return ConnectedTextures.getQuad(ConnectedTextures.emptySprite, block, blockState, quad);
                }
            }

            EnumFacing side1 = quad.getFace();
            TextureAtlasSprite sprite = ConnectedTextures.getConnectedTextureMultiPass(blockAccess, blockState, blockPos, side1, spriteIn, renderEnv);
            return sprite == spriteIn ? quad : ConnectedTextures.getQuad(sprite, block, blockState, quad);
        }
    }

    private static BakedQuad getQuad(TextureAtlasSprite sprite, Block block, IBlockState blockState, BakedQuad quadIn) {
        if (ConnectedTextures.spriteQuadMaps == null) {
            return quadIn;
        } else {
            int spriteIndex = sprite.getIndexInMap();

            if (spriteIndex >= 0 && spriteIndex < ConnectedTextures.spriteQuadMaps.length) {
                Object quadMap = ConnectedTextures.spriteQuadMaps[spriteIndex];

                if (quadMap == null) {
                    quadMap = new IdentityHashMap(1);
                    ConnectedTextures.spriteQuadMaps[spriteIndex] = (Map) quadMap;
                }

                BakedQuad quad = (BakedQuad) ((Map) quadMap).get(quadIn);

                if (quad == null) {
                    quad = ConnectedTextures.makeSpriteQuad(quadIn, sprite);
                    ((Map) quadMap).put(quadIn, quad);
                }

                return quad;
            } else {
                return quadIn;
            }
        }
    }

    private static BakedQuad makeSpriteQuad(BakedQuad quad, TextureAtlasSprite sprite) {
        int[] data = quad.func_178209_a().clone();
        TextureAtlasSprite spriteFrom = quad.getSprite();

        for (int bq = 0; bq < 4; ++bq) {
            ConnectedTextures.fixVertex(data, bq, spriteFrom, sprite);
        }

        BakedQuad var5 = new BakedQuad(data, quad.func_178211_c(), quad.getFace(), sprite);
        return var5;
    }

    private static void fixVertex(int[] data, int vertex, TextureAtlasSprite spriteFrom, TextureAtlasSprite spriteTo) {
        int pos = 7 * vertex;
        float u = Float.intBitsToFloat(data[pos + 4]);
        float v = Float.intBitsToFloat(data[pos + 4 + 1]);
        double su16 = spriteFrom.getSpriteU16(u);
        double sv16 = spriteFrom.getSpriteV16(v);
        data[pos + 4] = Float.floatToRawIntBits(spriteTo.getInterpolatedU(su16));
        data[pos + 4 + 1] = Float.floatToRawIntBits(spriteTo.getInterpolatedV(sv16));
    }

    private static TextureAtlasSprite getConnectedTextureMultiPass(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, EnumFacing side, TextureAtlasSprite icon, RenderEnv renderEnv) {
        TextureAtlasSprite newIcon = ConnectedTextures.getConnectedTextureSingle(blockAccess, blockState, blockPos, side, icon, true, renderEnv);

        if (!ConnectedTextures.multipass) {
            return newIcon;
        } else if (newIcon == icon) {
            return newIcon;
        } else {
            TextureAtlasSprite mpIcon = newIcon;

            for (int i = 0; i < 3; ++i) {
                TextureAtlasSprite newMpIcon = ConnectedTextures.getConnectedTextureSingle(blockAccess, blockState, blockPos, side, mpIcon, false, renderEnv);

                if (newMpIcon == mpIcon) {
                    break;
                }

                mpIcon = newMpIcon;
            }

            return mpIcon;
        }
    }

    public static TextureAtlasSprite getConnectedTextureSingle(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, EnumFacing facing, TextureAtlasSprite icon, boolean checkBlocks, RenderEnv renderEnv) {
        Block block = blockState.getBlock();
        int blockId;
        ConnectedProperties[] cps;
        int metadata;
        int side;
        int i;
        ConnectedProperties cp;

        if (ConnectedTextures.tileProperties != null) {
            blockId = icon.getIndexInMap();

            if (blockId >= 0 && blockId < ConnectedTextures.tileProperties.length) {
                cps = ConnectedTextures.tileProperties[blockId];

                if (cps != null) {
                    metadata = renderEnv.getMetadata();
                    side = ConnectedTextures.getSide(facing);

                    for (i = 0; i < cps.length; ++i) {
                        cp = cps[i];

                        if (cp != null) {
                            int newIcon = renderEnv.getBlockId();

                            if (cp.matchesBlock(newIcon)) {
                                TextureAtlasSprite newIcon1 = ConnectedTextures.getConnectedTexture(cp, blockAccess, blockState, blockPos, side, icon, metadata, renderEnv);

                                if (newIcon1 != null) {
                                    return newIcon1;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (ConnectedTextures.blockProperties != null && checkBlocks) {
            blockId = renderEnv.getBlockId();

            if (blockId >= 0 && blockId < ConnectedTextures.blockProperties.length) {
                cps = ConnectedTextures.blockProperties[blockId];

                if (cps != null) {
                    metadata = renderEnv.getMetadata();
                    side = ConnectedTextures.getSide(facing);

                    for (i = 0; i < cps.length; ++i) {
                        cp = cps[i];

                        if (cp != null && cp.matchesIcon(icon)) {
                            TextureAtlasSprite var16 = ConnectedTextures.getConnectedTexture(cp, blockAccess, blockState, blockPos, side, icon, metadata, renderEnv);

                            if (var16 != null) {
                                return var16;
                            }
                        }
                    }
                }
            }
        }

        return icon;
    }

    private static int getSide(EnumFacing facing) {
        if (facing == null) {
            return -1;
        } else {
            switch (ConnectedTextures.NamelessClass719841125.$SwitchMap$net$minecraft$util$EnumFacing[facing.ordinal()]) {
                case 1:
                    return 0;

                case 2:
                    return 1;

                case 3:
                    return 5;

                case 4:
                    return 4;

                case 5:
                    return 2;

                case 6:
                    return 3;

                default:
                    return -1;
            }
        }
    }

    private static EnumFacing getFacing(int side) {
        switch (side) {
            case 0:
                return EnumFacing.DOWN;

            case 1:
                return EnumFacing.UP;

            case 2:
                return EnumFacing.NORTH;

            case 3:
                return EnumFacing.SOUTH;

            case 4:
                return EnumFacing.WEST;

            case 5:
                return EnumFacing.EAST;

            default:
                return EnumFacing.UP;
        }
    }

    private static TextureAtlasSprite getConnectedTexture(ConnectedProperties cp, IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, int side, TextureAtlasSprite icon, int metadata, RenderEnv renderEnv) {
        int y = blockPos.getY();

        if (y >= cp.minHeight && y <= cp.maxHeight) {
            if (cp.biomes != null) {
                BiomeGenBase vertAxis = blockAccess.getBiomeGenForCoords(blockPos);
                boolean metadataCheck = false;

                for (BiomeGenBase mds : cp.biomes) {
                    if (vertAxis == mds) {
                        metadataCheck = true;
                        break;
                    }
                }

                if (!metadataCheck) {
                    return null;
                }
            }

            int var15 = 0;
            int var16 = metadata;
            Block var17 = blockState.getBlock();

            if (var17 instanceof BlockRotatedPillar) {
                var15 = ConnectedTextures.getWoodAxis(side, metadata);
                var16 = metadata & 3;
            }

            if (var17 instanceof BlockQuartz) {
                var15 = ConnectedTextures.getQuartzAxis(side, metadata);

                if (var16 > 2) {
                    var16 = 2;
                }
            }

            if (side >= 0 && cp.faces != 63) {
                int var18 = side;

                if (var15 != 0) {
                    var18 = ConnectedTextures.fixSideByAxis(side, var15);
                }

                if ((1 << var18 & cp.faces) == 0) {
                    return null;
                }
            }

            if (cp.metadatas != null) {
                int[] var19 = cp.metadatas;
                boolean metadataFound = false;

                for (int element : var19) {
                    if (element == var16) {
                        metadataFound = true;
                        break;
                    }
                }

                if (!metadataFound) {
                    return null;
                }
            }

            switch (cp.method) {
                case 1:
                    return ConnectedTextures.getConnectedTextureCtm(cp, blockAccess, blockState, blockPos, side, icon, metadata, renderEnv);

                case 2:
                    return ConnectedTextures.getConnectedTextureHorizontal(cp, blockAccess, blockState, blockPos, var15, side, icon, metadata);

                case 3:
                    return ConnectedTextures.getConnectedTextureTop(cp, blockAccess, blockState, blockPos, var15, side, icon, metadata);

                case 4:
                    return ConnectedTextures.getConnectedTextureRandom(cp, blockPos, side);

                case 5:
                    return ConnectedTextures.getConnectedTextureRepeat(cp, blockPos, side);

                case 6:
                    return ConnectedTextures.getConnectedTextureVertical(cp, blockAccess, blockState, blockPos, var15, side, icon, metadata);

                case 7:
                    return ConnectedTextures.getConnectedTextureFixed(cp);

                case 8:
                    return ConnectedTextures.getConnectedTextureHorizontalVertical(cp, blockAccess, blockState, blockPos, var15, side, icon, metadata);

                case 9:
                    return ConnectedTextures.getConnectedTextureVerticalHorizontal(cp, blockAccess, blockState, blockPos, var15, side, icon, metadata);

                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    private static int fixSideByAxis(int side, int vertAxis) {
        switch (vertAxis) {
            case 0:
                return side;

            case 1:
                switch (side) {
                    case 0:
                        return 2;

                    case 1:
                        return 3;

                    case 2:
                        return 1;

                    case 3:
                        return 0;

                    default:
                        return side;
                }

            case 2:
                switch (side) {
                    case 0:
                        return 4;

                    case 1:
                        return 5;

                    case 2:
                    case 3:
                    default:
                        return side;

                    case 4:
                        return 1;

                    case 5:
                        return 0;
                }

            default:
                return side;
        }
    }

    private static int getWoodAxis(int side, int metadata) {
        int orient = (metadata & 12) >> 2;

        switch (orient) {
            case 1:
                return 2;

            case 2:
                return 1;

            default:
                return 0;
        }
    }

    private static int getQuartzAxis(int side, int metadata) {
        switch (metadata) {
            case 3:
                return 2;

            case 4:
                return 1;

            default:
                return 0;
        }
    }

    private static TextureAtlasSprite getConnectedTextureRandom(ConnectedProperties cp, BlockPos blockPos, int side) {
        if (cp.tileIcons.length == 1) {
            return cp.tileIcons[0];
        } else {
            int face = side / cp.symmetry * cp.symmetry;
            int rand = Config.getRandom(blockPos, face) & Integer.MAX_VALUE;
            int index = 0;

            if (cp.weights == null) {
                index = rand % cp.tileIcons.length;
            } else {
                int randWeight = rand % cp.sumAllWeights;
                int[] sumWeights = cp.sumWeights;

                for (int i = 0; i < sumWeights.length; ++i) {
                    if (randWeight < sumWeights[i]) {
                        index = i;
                        break;
                    }
                }
            }

            return cp.tileIcons[index];
        }
    }

    private static TextureAtlasSprite getConnectedTextureFixed(ConnectedProperties cp) {
        return cp.tileIcons[0];
    }

    private static TextureAtlasSprite getConnectedTextureRepeat(ConnectedProperties cp, BlockPos blockPos, int side) {
        if (cp.tileIcons.length == 1) {
            return cp.tileIcons[0];
        } else {
            int x = blockPos.getX();
            int y = blockPos.getY();
            int z = blockPos.getZ();
            int nx = 0;
            int ny = 0;

            switch (side) {
                case 0:
                    nx = x;
                    ny = z;
                    break;

                case 1:
                    nx = x;
                    ny = z;
                    break;

                case 2:
                    nx = -x - 1;
                    ny = -y;
                    break;

                case 3:
                    nx = x;
                    ny = -y;
                    break;

                case 4:
                    nx = z;
                    ny = -y;
                    break;

                case 5:
                    nx = -z - 1;
                    ny = -y;
            }

            nx %= cp.width;
            ny %= cp.height;

            if (nx < 0) {
                nx += cp.width;
            }

            if (ny < 0) {
                ny += cp.height;
            }

            int index = ny * cp.width + nx;
            return cp.tileIcons[index];
        }
    }

    private static TextureAtlasSprite getConnectedTextureCtm(ConnectedProperties cp, IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, int side, TextureAtlasSprite icon, int metadata, RenderEnv renderEnv) {
        boolean[] borders = renderEnv.getBorderFlags();

        switch (side) {
            case 0:
                borders[0] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest(), side, icon, metadata);
                borders[1] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast(), side, icon, metadata);
                borders[2] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetNorth(), side, icon, metadata);
                borders[3] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetSouth(), side, icon, metadata);
                break;

            case 1:
                borders[0] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest(), side, icon, metadata);
                borders[1] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast(), side, icon, metadata);
                borders[2] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetSouth(), side, icon, metadata);
                borders[3] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetNorth(), side, icon, metadata);
                break;

            case 2:
                borders[0] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast(), side, icon, metadata);
                borders[1] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest(), side, icon, metadata);
                borders[2] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetDown(), side, icon, metadata);
                borders[3] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp(), side, icon, metadata);
                break;

            case 3:
                borders[0] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest(), side, icon, metadata);
                borders[1] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast(), side, icon, metadata);
                borders[2] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetDown(), side, icon, metadata);
                borders[3] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp(), side, icon, metadata);
                break;

            case 4:
                borders[0] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetNorth(), side, icon, metadata);
                borders[1] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetSouth(), side, icon, metadata);
                borders[2] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetDown(), side, icon, metadata);
                borders[3] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp(), side, icon, metadata);
                break;

            case 5:
                borders[0] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetSouth(), side, icon, metadata);
                borders[1] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetNorth(), side, icon, metadata);
                borders[2] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetDown(), side, icon, metadata);
                borders[3] = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp(), side, icon, metadata);
        }

        byte index = 0;

        if (borders[0] & !borders[1] & !borders[2] & !borders[3]) {
            index = 3;
        } else if (!borders[0] & borders[1] & !borders[2] & !borders[3]) {
            index = 1;
        } else if (!borders[0] & !borders[1] & borders[2] & !borders[3]) {
            index = 12;
        } else if (!borders[0] & !borders[1] & !borders[2] & borders[3]) {
            index = 36;
        } else if (borders[0] & borders[1] & !borders[2] & !borders[3]) {
            index = 2;
        } else if (!borders[0] & !borders[1] & borders[2] & borders[3]) {
            index = 24;
        } else if (borders[0] & !borders[1] & borders[2] & !borders[3]) {
            index = 15;
        } else if (borders[0] & !borders[1] & !borders[2] & borders[3]) {
            index = 39;
        } else if (!borders[0] & borders[1] & borders[2] & !borders[3]) {
            index = 13;
        } else if (!borders[0] & borders[1] & !borders[2] & borders[3]) {
            index = 37;
        } else if (!borders[0] & borders[1] & borders[2] & borders[3]) {
            index = 25;
        } else if (borders[0] & !borders[1] & borders[2] & borders[3]) {
            index = 27;
        } else if (borders[0] & borders[1] & !borders[2] & borders[3]) {
            index = 38;
        } else if (borders[0] & borders[1] & borders[2] & !borders[3]) {
            index = 14;
        } else if (borders[0] & borders[1] & borders[2] & borders[3]) {
            index = 26;
        }

        if (index == 0) {
            return cp.tileIcons[index];
        } else if (!Config.isConnectedTexturesFancy()) {
            return cp.tileIcons[index];
        } else {
            switch (side) {
                case 0:
                    borders[0] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast().offsetNorth(), side, icon, metadata);
                    borders[1] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest().offsetNorth(), side, icon, metadata);
                    borders[2] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast().offsetSouth(), side, icon, metadata);
                    borders[3] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest().offsetSouth(), side, icon, metadata);
                    break;

                case 1:
                    borders[0] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast().offsetSouth(), side, icon, metadata);
                    borders[1] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest().offsetSouth(), side, icon, metadata);
                    borders[2] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast().offsetNorth(), side, icon, metadata);
                    borders[3] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest().offsetNorth(), side, icon, metadata);
                    break;

                case 2:
                    borders[0] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest().offsetDown(), side, icon, metadata);
                    borders[1] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast().offsetDown(), side, icon, metadata);
                    borders[2] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest().offsetUp(), side, icon, metadata);
                    borders[3] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast().offsetUp(), side, icon, metadata);
                    break;

                case 3:
                    borders[0] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast().offsetDown(), side, icon, metadata);
                    borders[1] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest().offsetDown(), side, icon, metadata);
                    borders[2] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast().offsetUp(), side, icon, metadata);
                    borders[3] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest().offsetUp(), side, icon, metadata);
                    break;

                case 4:
                    borders[0] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetDown().offsetSouth(), side, icon, metadata);
                    borders[1] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetDown().offsetNorth(), side, icon, metadata);
                    borders[2] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp().offsetSouth(), side, icon, metadata);
                    borders[3] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp().offsetNorth(), side, icon, metadata);
                    break;

                case 5:
                    borders[0] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetDown().offsetNorth(), side, icon, metadata);
                    borders[1] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetDown().offsetSouth(), side, icon, metadata);
                    borders[2] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp().offsetNorth(), side, icon, metadata);
                    borders[3] = !ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp().offsetSouth(), side, icon, metadata);
            }

            if (index == 13 && borders[0]) {
                index = 4;
            } else if (index == 15 && borders[1]) {
                index = 5;
            } else if (index == 37 && borders[2]) {
                index = 16;
            } else if (index == 39 && borders[3]) {
                index = 17;
            } else if (index == 14 && borders[0] && borders[1]) {
                index = 7;
            } else if (index == 25 && borders[0] && borders[2]) {
                index = 6;
            } else if (index == 27 && borders[3] && borders[1]) {
                index = 19;
            } else if (index == 38 && borders[3] && borders[2]) {
                index = 18;
            } else if (index == 14 && !borders[0] && borders[1]) {
                index = 31;
            } else if (index == 25 && borders[0] && !borders[2]) {
                index = 30;
            } else if (index == 27 && !borders[3] && borders[1]) {
                index = 41;
            } else if (index == 38 && borders[3] && !borders[2]) {
                index = 40;
            } else if (index == 14 && borders[0] && !borders[1]) {
                index = 29;
            } else if (index == 25 && !borders[0] && borders[2]) {
                index = 28;
            } else if (index == 27 && borders[3] && !borders[1]) {
                index = 43;
            } else if (index == 38 && !borders[3] && borders[2]) {
                index = 42;
            } else if (index == 26 && borders[0] && borders[1] && borders[2] && borders[3]) {
                index = 46;
            } else if (index == 26 && !borders[0] && borders[1] && borders[2] && borders[3]) {
                index = 9;
            } else if (index == 26 && borders[0] && !borders[1] && borders[2] && borders[3]) {
                index = 21;
            } else if (index == 26 && borders[0] && borders[1] && !borders[2] && borders[3]) {
                index = 8;
            } else if (index == 26 && borders[0] && borders[1] && borders[2] && !borders[3]) {
                index = 20;
            } else if (index == 26 && borders[0] && borders[1] && !borders[2] && !borders[3]) {
                index = 11;
            } else if (index == 26 && !borders[0] && !borders[1] && borders[2] && borders[3]) {
                index = 22;
            } else if (index == 26 && !borders[0] && borders[1] && !borders[2] && borders[3]) {
                index = 23;
            } else if (index == 26 && borders[0] && !borders[1] && borders[2] && !borders[3]) {
                index = 10;
            } else if (index == 26 && borders[0] && !borders[1] && !borders[2] && borders[3]) {
                index = 34;
            } else if (index == 26 && !borders[0] && borders[1] && borders[2] && !borders[3]) {
                index = 35;
            } else if (index == 26 && borders[0] && !borders[1] && !borders[2] && !borders[3]) {
                index = 32;
            } else if (index == 26 && !borders[0] && borders[1] && !borders[2] && !borders[3]) {
                index = 33;
            } else if (index == 26 && !borders[0] && !borders[1] && borders[2] && !borders[3]) {
                index = 44;
            } else if (index == 26 && !borders[0] && !borders[1] && !borders[2] && borders[3]) {
                index = 45;
            }

            return cp.tileIcons[index];
        }
    }

    private static boolean isNeighbour(ConnectedProperties cp, IBlockAccess iblockaccess, IBlockState blockState, BlockPos blockPos, int side, TextureAtlasSprite icon, int metadata) {
        IBlockState neighbourState = iblockaccess.getBlockState(blockPos);

        if (cp.connect == 2) {
            if (neighbourState == null) {
                return false;
            } else if (neighbourState == ConnectedTextures.AIR_DEFAULT_STATE) {
                return false;
            } else {
                TextureAtlasSprite neighbourIcon = ConnectedTextures.getNeighbourIcon(neighbourState, side);
                return neighbourIcon == icon;
            }
        } else {
            return cp.connect == 3 ? (neighbourState == null ? false : (neighbourState == ConnectedTextures.AIR_DEFAULT_STATE ? false : neighbourState.getBlock().getMaterial() == blockState.getBlock().getMaterial())) : neighbourState == blockState;
        }
    }

    private static TextureAtlasSprite getNeighbourIcon(IBlockState neighbourState, int side) {
        IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().func_175023_a().func_178125_b(neighbourState);

        if (model == null) {
            return null;
        } else {
            EnumFacing facing = ConnectedTextures.getFacing(side);
            List quads = model.func_177551_a(facing);

            if (quads.size() > 0) {
                BakedQuad var8 = (BakedQuad) quads.get(0);
                return var8.getSprite();
            } else {
                List quadsGeneral = model.func_177550_a();

                for (int i = 0; i < quadsGeneral.size(); ++i) {
                    BakedQuad quad = (BakedQuad) quadsGeneral.get(i);

                    if (quad.getFace() == facing) {
                        return quad.getSprite();
                    }
                }

                return null;
            }
        }
    }

    private static TextureAtlasSprite getConnectedTextureHorizontal(ConnectedProperties cp, IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, int vertAxis, int side, TextureAtlasSprite icon, int metadata) {
        boolean left;
        boolean right;
        left = false;
        right = false;
        label46:

        switch (vertAxis) {
            case 0:
                switch (side) {
                    case 0:
                    case 1:
                        return null;

                    case 2:
                        left = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast(), side, icon, metadata);
                        right = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest(), side, icon, metadata);
                        break label46;

                    case 3:
                        left = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest(), side, icon, metadata);
                        right = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast(), side, icon, metadata);
                        break label46;

                    case 4:
                        left = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetNorth(), side, icon, metadata);
                        right = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetSouth(), side, icon, metadata);
                        break label46;

                    case 5:
                        left = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetSouth(), side, icon, metadata);
                        right = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetNorth(), side, icon, metadata);

                    default:
                        break label46;
                }

            case 1:
                switch (side) {
                    case 0:
                        left = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest(), side, icon, metadata);
                        right = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast(), side, icon, metadata);
                        break label46;

                    case 1:
                        left = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest(), side, icon, metadata);
                        right = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast(), side, icon, metadata);
                        break label46;

                    case 2:
                    case 3:
                        return null;

                    case 4:
                        left = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetDown(), side, icon, metadata);
                        right = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp(), side, icon, metadata);
                        break label46;

                    case 5:
                        left = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp(), side, icon, metadata);
                        right = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetDown(), side, icon, metadata);

                    default:
                        break label46;
                }

            case 2:
                switch (side) {
                    case 0:
                        left = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetNorth(), side, icon, metadata);
                        right = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetSouth(), side, icon, metadata);
                        break;

                    case 1:
                        left = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetNorth(), side, icon, metadata);
                        right = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetSouth(), side, icon, metadata);
                        break;

                    case 2:
                        left = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetDown(), side, icon, metadata);
                        right = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp(), side, icon, metadata);
                        break;

                    case 3:
                        left = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp(), side, icon, metadata);
                        right = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetDown(), side, icon, metadata);
                        break;

                    case 4:
                    case 5:
                        return null;
                }
        }

        boolean index = true;
        byte index1;

        if (left) {
            if (right) {
                index1 = 1;
            } else {
                index1 = 2;
            }
        } else if (right) {
            index1 = 0;
        } else {
            index1 = 3;
        }

        return cp.tileIcons[index1];
    }

    private static TextureAtlasSprite getConnectedTextureVertical(ConnectedProperties cp, IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, int vertAxis, int side, TextureAtlasSprite icon, int metadata) {
        boolean bottom = false;
        boolean top = false;

        switch (vertAxis) {
            case 0:
                if (side == 1 || side == 0) {
                    return null;
                }

                bottom = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetDown(), side, icon, metadata);
                top = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp(), side, icon, metadata);
                break;

            case 1:
                if (side == 3 || side == 2) {
                    return null;
                }

                bottom = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetSouth(), side, icon, metadata);
                top = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetNorth(), side, icon, metadata);
                break;

            case 2:
                if (side == 5 || side == 4) {
                    return null;
                }

                bottom = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetWest(), side, icon, metadata);
                top = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast(), side, icon, metadata);
        }

        boolean index = true;
        byte index1;

        if (bottom) {
            if (top) {
                index1 = 1;
            } else {
                index1 = 2;
            }
        } else if (top) {
            index1 = 0;
        } else {
            index1 = 3;
        }

        return cp.tileIcons[index1];
    }

    private static TextureAtlasSprite getConnectedTextureHorizontalVertical(ConnectedProperties cp, IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, int vertAxis, int side, TextureAtlasSprite icon, int metadata) {
        TextureAtlasSprite[] tileIcons = cp.tileIcons;
        TextureAtlasSprite iconH = ConnectedTextures.getConnectedTextureHorizontal(cp, blockAccess, blockState, blockPos, vertAxis, side, icon, metadata);

        if (iconH != null && iconH != icon && iconH != tileIcons[3]) {
            return iconH;
        } else {
            TextureAtlasSprite iconV = ConnectedTextures.getConnectedTextureVertical(cp, blockAccess, blockState, blockPos, vertAxis, side, icon, metadata);
            return iconV == tileIcons[0] ? tileIcons[4] : (iconV == tileIcons[1] ? tileIcons[5] : (iconV == tileIcons[2] ? tileIcons[6] : iconV));
        }
    }

    private static TextureAtlasSprite getConnectedTextureVerticalHorizontal(ConnectedProperties cp, IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, int vertAxis, int side, TextureAtlasSprite icon, int metadata) {
        TextureAtlasSprite[] tileIcons = cp.tileIcons;
        TextureAtlasSprite iconV = ConnectedTextures.getConnectedTextureVertical(cp, blockAccess, blockState, blockPos, vertAxis, side, icon, metadata);

        if (iconV != null && iconV != icon && iconV != tileIcons[3]) {
            return iconV;
        } else {
            TextureAtlasSprite iconH = ConnectedTextures.getConnectedTextureHorizontal(cp, blockAccess, blockState, blockPos, vertAxis, side, icon, metadata);
            return iconH == tileIcons[0] ? tileIcons[4] : (iconH == tileIcons[1] ? tileIcons[5] : (iconH == tileIcons[2] ? tileIcons[6] : iconH));
        }
    }

    private static TextureAtlasSprite getConnectedTextureTop(ConnectedProperties cp, IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, int vertAxis, int side, TextureAtlasSprite icon, int metadata) {
        boolean top = false;

        switch (vertAxis) {
            case 0:
                if (side == 1 || side == 0) {
                    return null;
                }

                top = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetUp(), side, icon, metadata);
                break;

            case 1:
                if (side == 3 || side == 2) {
                    return null;
                }

                top = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetSouth(), side, icon, metadata);
                break;

            case 2:
                if (side == 5 || side == 4) {
                    return null;
                }

                top = ConnectedTextures.isNeighbour(cp, blockAccess, blockState, blockPos.offsetEast(), side, icon, metadata);
        }

        return top ? cp.tileIcons[0] : null;
    }

    public static void updateIcons(TextureMap textureMap) {
        ConnectedTextures.blockProperties = null;
        ConnectedTextures.tileProperties = null;

        if (Config.isConnectedTextures()) {
            IResourcePack[] rps = Config.getResourcePacks();

            for (int locEmpty = rps.length - 1; locEmpty >= 0; --locEmpty) {
                IResourcePack rp = rps[locEmpty];
                ConnectedTextures.updateIcons(textureMap, rp);
            }

            ConnectedTextures.updateIcons(textureMap, Config.getDefaultResourcePack());
            ResourceLocation var4 = new ResourceLocation("mcpatcher/ctm/default/empty");
            ConnectedTextures.emptySprite = textureMap.func_174942_a(var4);
            ConnectedTextures.spriteQuadMaps = new Map[textureMap.getCountRegisteredSprites() + 1];
        }
    }

    private static void updateIconEmpty(TextureMap textureMap) {
    }

    public static void updateIcons(TextureMap textureMap, IResourcePack rp) {
        String[] names = ConnectedTextures.collectFiles(rp, "mcpatcher/ctm/", ".properties");
        Arrays.sort(names);
        List tileList = ConnectedTextures.makePropertyList(ConnectedTextures.tileProperties);
        List blockList = ConnectedTextures.makePropertyList(ConnectedTextures.blockProperties);

        for (String name : names) {
            Config.dbg("ConnectedTextures: " + name);

            try {
                ResourceLocation e = new ResourceLocation(name);
                InputStream in = rp.getInputStream(e);

                if (in == null) {
                    Config.warn("ConnectedTextures file not found: " + name);
                } else {
                    Properties props = new Properties();
                    props.load(in);
                    ConnectedProperties cp = new ConnectedProperties(props, name);

                    if (cp.isValid(name)) {
                        cp.updateIcons(textureMap);
                        ConnectedTextures.addToTileList(cp, tileList);
                        ConnectedTextures.addToBlockList(cp, blockList);
                    }
                }
            } catch (FileNotFoundException var11) {
                Config.warn("ConnectedTextures file not found: " + name);
            } catch (IOException var12) {
                var12.printStackTrace();
            }
        }

        ConnectedTextures.blockProperties = ConnectedTextures.propertyListToArray(blockList);
        ConnectedTextures.tileProperties = ConnectedTextures.propertyListToArray(tileList);
        ConnectedTextures.multipass = ConnectedTextures.detectMultipass();
        Config.dbg("Multipass connected textures: " + ConnectedTextures.multipass);
    }

    private static List makePropertyList(ConnectedProperties[][] propsArr) {
        ArrayList list = new ArrayList();

        if (propsArr != null) {
            for (ConnectedProperties[] props : propsArr) {
                ArrayList propList = null;

                if (props != null) {
                    propList = new ArrayList(Arrays.asList(props));
                }

                list.add(propList);
            }
        }

        return list;
    }

    private static boolean detectMultipass() {
        ArrayList propList = new ArrayList();
        int props;
        ConnectedProperties[] matchIconSet;

        for (props = 0; props < ConnectedTextures.tileProperties.length; ++props) {
            matchIconSet = ConnectedTextures.tileProperties[props];

            if (matchIconSet != null) {
                propList.addAll(Arrays.asList(matchIconSet));
            }
        }

        for (props = 0; props < ConnectedTextures.blockProperties.length; ++props) {
            matchIconSet = ConnectedTextures.blockProperties[props];

            if (matchIconSet != null) {
                propList.addAll(Arrays.asList(matchIconSet));
            }
        }

        ConnectedProperties[] var6 = ((ConnectedProperties[]) propList.toArray(new ConnectedProperties[propList.size()]));
        HashSet var7 = new HashSet();
        HashSet tileIconSet = new HashSet();

        for (ConnectedProperties cp : var6) {
            if (cp.matchTileIcons != null) {
                var7.addAll(Arrays.asList(cp.matchTileIcons));
            }

            if (cp.tileIcons != null) {
                tileIconSet.addAll(Arrays.asList(cp.tileIcons));
            }
        }

        var7.retainAll(tileIconSet);
        return !var7.isEmpty();
    }

    private static ConnectedProperties[][] propertyListToArray(List list) {
        ConnectedProperties[][] propArr = new ConnectedProperties[list.size()][];

        for (int i = 0; i < list.size(); ++i) {
            List subList = (List) list.get(i);

            if (subList != null) {
                ConnectedProperties[] subArr = ((ConnectedProperties[]) subList.toArray(new ConnectedProperties[subList.size()]));
                propArr[i] = subArr;
            }
        }

        return propArr;
    }

    private static void addToTileList(ConnectedProperties cp, List tileList) {
        if (cp.matchTileIcons != null) {
            for (TextureAtlasSprite matchTileIcon : cp.matchTileIcons) {
                TextureAtlasSprite icon = matchTileIcon;

                if (!(icon instanceof TextureAtlasSprite)) {
                    Config.warn("TextureAtlasSprite is not TextureAtlasSprite: " + icon + ", name: " + icon.getIconName());
                } else {
                    int tileId = icon.getIndexInMap();

                    if (tileId < 0) {
                        Config.warn("Invalid tile ID: " + tileId + ", icon: " + icon.getIconName());
                    } else {
                        ConnectedTextures.addToList(cp, tileList, tileId);
                    }
                }
            }
        }
    }

    private static void addToBlockList(ConnectedProperties cp, List blockList) {
        if (cp.matchBlocks != null) {
            for (int blockId : cp.matchBlocks) {
                if (blockId < 0) {
                    Config.warn("Invalid block ID: " + blockId);
                } else {
                    ConnectedTextures.addToList(cp, blockList, blockId);
                }
            }
        }
    }

    private static void addToList(ConnectedProperties cp, List list, int id) {
        while (id >= list.size()) {
            list.add((Object) null);
        }

        Object subList = list.get(id);

        if (subList == null) {
            subList = new ArrayList();
            list.set(id, subList);
        }

        ((List) subList).add(cp);
    }

    private static String[] collectFiles(IResourcePack rp, String prefix, String suffix) {
        if (rp instanceof DefaultResourcePack) {
            return ConnectedTextures.collectFilesDefault(rp);
        } else if (!(rp instanceof AbstractResourcePack)) {
            return new String[0];
        } else {
            AbstractResourcePack arp = (AbstractResourcePack) rp;
            File tpFile = ResourceUtils.getResourcePackFile(arp);
            return tpFile == null ? new String[0] : (tpFile.isDirectory() ? ConnectedTextures.collectFilesFolder(tpFile, "", prefix, suffix) : (tpFile.isFile() ? ConnectedTextures.collectFilesZIP(tpFile, prefix, suffix) : new String[0]));
        }
    }

    private static String[] collectFilesDefault(IResourcePack rp) {
        ArrayList list = new ArrayList();
        String[] names = ConnectedTextures.getDefaultCtmPaths();

        for (String name : names) {
            ResourceLocation loc = new ResourceLocation(name);

            if (rp.resourceExists(loc)) {
                list.add(name);
            }
        }

        String[] var6 = ((String[]) list.toArray(new String[list.size()]));
        return var6;
    }

    private static String[] getDefaultCtmPaths() {
        ArrayList list = new ArrayList();
        String defPath = "mcpatcher/ctm/default/";

        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass.png"))) {
            list.add(defPath + "glass.properties");
            list.add(defPath + "glasspane.properties");
        }

        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/bookshelf.png"))) {
            list.add(defPath + "bookshelf.properties");
        }

        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/sandstone_normal.png"))) {
            list.add(defPath + "sandstone.properties");
        }

        String[] colors = new String[]{"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black"};

        for (int paths = 0; paths < colors.length; ++paths) {
            String color = colors[paths];

            if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass_" + color + ".png"))) {
                list.add(defPath + paths + "_glass_" + color + "/glass_" + color + ".properties");
                list.add(defPath + paths + "_glass_" + color + "/glass_pane_" + color + ".properties");
            }
        }

        String[] var5 = ((String[]) list.toArray(new String[list.size()]));
        return var5;
    }

    private static String[] collectFilesFolder(File tpFile, String basePath, String prefix, String suffix) {
        ArrayList list = new ArrayList();
        String prefixAssets = "assets/minecraft/";
        File[] files = tpFile.listFiles();

        if (files == null) {
            return new String[0];
        } else {
            for (File file : files) {
                String dirPath;

                if (file.isFile()) {
                    dirPath = basePath + file.getName();

                    if (dirPath.startsWith(prefixAssets)) {
                        dirPath = dirPath.substring(prefixAssets.length());

                        if (dirPath.startsWith(prefix) && dirPath.endsWith(suffix)) {
                            list.add(dirPath);
                        }
                    }
                } else if (file.isDirectory()) {
                    dirPath = basePath + file.getName() + "/";
                    String[] names1 = ConnectedTextures.collectFilesFolder(file, dirPath, prefix, suffix);

                    for (String name : names1) {
                        list.add(name);
                    }
                }
            }

            String[] var13 = ((String[]) list.toArray(new String[list.size()]));
            return var13;
        }
    }

    private static String[] collectFilesZIP(File tpFile, String prefix, String suffix) {
        ArrayList list = new ArrayList();
        String prefixAssets = "assets/minecraft/";

        try {
            ZipFile e = new ZipFile(tpFile);
            Enumeration en = e.entries();

            while (en.hasMoreElements()) {
                ZipEntry names = (ZipEntry) en.nextElement();
                String name = names.getName();

                if (name.startsWith(prefixAssets)) {
                    name = name.substring(prefixAssets.length());

                    if (name.startsWith(prefix) && name.endsWith(suffix)) {
                        list.add(name);
                    }
                }
            }

            e.close();
            String[] names1 = ((String[]) list.toArray(new String[list.size()]));
            return names1;
        } catch (IOException var9) {
            var9.printStackTrace();
            return new String[0];
        }
    }

    public static int getPaneTextureIndex(boolean linkP, boolean linkN, boolean linkYp, boolean linkYn) {
        return linkN && linkP ? (linkYp ? (linkYn ? 34 : 50) : (linkYn ? 18 : 2)) : (linkN && !linkP ? (linkYp ? (linkYn ? 35 : 51) : (linkYn ? 19 : 3)) : (!linkN && linkP ? (linkYp ? (linkYn ? 33 : 49) : (linkYn ? 17 : 1)) : (linkYp ? (linkYn ? 32 : 48) : (linkYn ? 16 : 0))));
    }

    public static int getReversePaneTextureIndex(int texNum) {
        int col = texNum % 16;
        return col == 1 ? texNum + 2 : (col == 3 ? texNum - 2 : texNum);
    }

    public static TextureAtlasSprite getCtmTexture(ConnectedProperties cp, int ctmIndex, TextureAtlasSprite icon) {
        if (cp.method != 1) {
            return icon;
        } else if (ctmIndex >= 0 && ctmIndex < ConnectedTextures.ctmIndexes.length) {
            int index = ConnectedTextures.ctmIndexes[ctmIndex];
            TextureAtlasSprite[] ctmIcons = cp.tileIcons;
            return index >= 0 && index < ctmIcons.length ? ctmIcons[index] : icon;
        } else {
            return icon;
        }
    }

    static class NamelessClass719841125 {
        static final int[] $SwitchMap$net$minecraft$util$EnumFacing = new int[EnumFacing.values().length];

        static {
            try {
                NamelessClass719841125.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.DOWN.ordinal()] = 1;
            } catch (NoSuchFieldError var6) {
                ;
            }

            try {
                NamelessClass719841125.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.UP.ordinal()] = 2;
            } catch (NoSuchFieldError var5) {
                ;
            }

            try {
                NamelessClass719841125.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.EAST.ordinal()] = 3;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                NamelessClass719841125.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.WEST.ordinal()] = 4;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                NamelessClass719841125.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.NORTH.ordinal()] = 5;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                NamelessClass719841125.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.SOUTH.ordinal()] = 6;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
