package net.minecraft.potion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PotionHelper {
    public static final String field_77924_a = null;
    public static final String sugarEffect;
    public static String ghastTearEffect = "+0-1-2-3&4-4+13";
    public static final String spiderEyeEffect;
    public static final String fermentedSpiderEyeEffect;
    public static final String speckledMelonEffect;
    public static final String blazePowderEffect;
    public static final String magmaCreamEffect;
    public static final String redstoneEffect;
    public static final String glowstoneEffect;
    public static final String gunpowderEffect;
    public static final String goldenCarrotEffect;
    public static final String field_151423_m;
    public static final String field_179538_n;
    private static final Map field_179539_o = Maps.newHashMap();
    private static final Map field_179540_p = Maps.newHashMap();
    private static final Map field_77925_n;

    /**
     * An array of possible potion prefix names, as translation IDs.
     */
    private static final String[] potionPrefixes;
    private static final String __OBFID = "CL_00000078";

    /**
     * Checks if the bit at 1 << j is on in i.
     */
    public static boolean checkFlag(int p_77914_0_, int p_77914_1_) {
        return (p_77914_0_ & 1 << p_77914_1_) != 0;
    }

    /**
     * Returns 1 if the flag is set, 0 if it is not set.
     */
    private static int isFlagSet(int p_77910_0_, int p_77910_1_) {
        return PotionHelper.checkFlag(p_77910_0_, p_77910_1_) ? 1 : 0;
    }

    /**
     * Returns 0 if the flag is set, 1 if it is not set.
     */
    private static int isFlagUnset(int p_77916_0_, int p_77916_1_) {
        return PotionHelper.checkFlag(p_77916_0_, p_77916_1_) ? 0 : 1;
    }

    public static int func_77909_a(int p_77909_0_) {
        return PotionHelper.func_77908_a(p_77909_0_, 5, 4, 3, 2, 1);
    }

    /**
     * Given a {@link Collection}<{@link PotionEffect}> will return an Integer
     * color.
     */
    public static int calcPotionLiquidColor(Collection p_77911_0_) {
        int var1 = 3694022;

        if (p_77911_0_ != null && !p_77911_0_.isEmpty()) {
            float var2 = 0.0F;
            float var3 = 0.0F;
            float var4 = 0.0F;
            float var5 = 0.0F;
            Iterator var6 = p_77911_0_.iterator();

            while (var6.hasNext()) {
                PotionEffect var7 = (PotionEffect) var6.next();

                if (var7.func_180154_f()) {
                    int var8 = Potion.potionTypes[var7.getPotionID()].getLiquidColor();

                    for (int var9 = 0; var9 <= var7.getAmplifier(); ++var9) {
                        var2 += (var8 >> 16 & 255) / 255.0F;
                        var3 += (var8 >> 8 & 255) / 255.0F;
                        var4 += (var8 >> 0 & 255) / 255.0F;
                        ++var5;
                    }
                }
            }

            if (var5 == 0.0F) {
                return 0;
            } else {
                var2 = var2 / var5 * 255.0F;
                var3 = var3 / var5 * 255.0F;
                var4 = var4 / var5 * 255.0F;
                return (int) var2 << 16 | (int) var3 << 8 | (int) var4;
            }
        } else {
            return var1;
        }
    }

    public static boolean func_82817_b(Collection p_82817_0_) {
        Iterator var1 = p_82817_0_.iterator();
        PotionEffect var2;

        do {
            if (!var1.hasNext()) {
                return true;
            }

            var2 = (PotionEffect) var1.next();
        } while (var2.getIsAmbient());

        return false;
    }

    public static int func_77915_a(int p_77915_0_, boolean p_77915_1_) {
        if (!p_77915_1_) {
            if (PotionHelper.field_77925_n.containsKey(Integer.valueOf(p_77915_0_))) {
                return ((Integer) PotionHelper.field_77925_n.get(Integer.valueOf(p_77915_0_))).intValue();
            } else {
                int var2 = PotionHelper.calcPotionLiquidColor(PotionHelper.getPotionEffects(p_77915_0_, false));
                PotionHelper.field_77925_n.put(Integer.valueOf(p_77915_0_), Integer.valueOf(var2));
                return var2;
            }
        } else {
            return PotionHelper.calcPotionLiquidColor(PotionHelper.getPotionEffects(p_77915_0_, true));
        }
    }

    public static String func_77905_c(int p_77905_0_) {
        int var1 = PotionHelper.func_77909_a(p_77905_0_);
        return PotionHelper.potionPrefixes[var1];
    }

    private static int func_77904_a(boolean p_77904_0_, boolean p_77904_1_, boolean p_77904_2_, int p_77904_3_, int p_77904_4_, int p_77904_5_, int p_77904_6_) {
        int var7 = 0;

        if (p_77904_0_) {
            var7 = PotionHelper.isFlagUnset(p_77904_6_, p_77904_4_);
        } else if (p_77904_3_ != -1) {
            if (p_77904_3_ == 0 && PotionHelper.countSetFlags(p_77904_6_) == p_77904_4_) {
                var7 = 1;
            } else if (p_77904_3_ == 1 && PotionHelper.countSetFlags(p_77904_6_) > p_77904_4_) {
                var7 = 1;
            } else if (p_77904_3_ == 2 && PotionHelper.countSetFlags(p_77904_6_) < p_77904_4_) {
                var7 = 1;
            }
        } else {
            var7 = PotionHelper.isFlagSet(p_77904_6_, p_77904_4_);
        }

        if (p_77904_1_) {
            var7 *= p_77904_5_;
        }

        if (p_77904_2_) {
            var7 *= -1;
        }

        return var7;
    }

    /**
     * Returns the number of 1 bits in the given integer.
     */
    private static int countSetFlags(int p_77907_0_) {
        int var1;

        for (var1 = 0; p_77907_0_ > 0; ++var1) {
            p_77907_0_ &= p_77907_0_ - 1;
        }

        return var1;
    }

    private static int parsePotionEffects(String p_77912_0_, int p_77912_1_, int p_77912_2_, int p_77912_3_) {
        if (p_77912_1_ < p_77912_0_.length() && p_77912_2_ >= 0 && p_77912_1_ < p_77912_2_) {
            int var4 = p_77912_0_.indexOf(124, p_77912_1_);
            int var5;
            int var17;

            if (var4 >= 0 && var4 < p_77912_2_) {
                var5 = PotionHelper.parsePotionEffects(p_77912_0_, p_77912_1_, var4 - 1, p_77912_3_);

                if (var5 > 0) {
                    return var5;
                } else {
                    var17 = PotionHelper.parsePotionEffects(p_77912_0_, var4 + 1, p_77912_2_, p_77912_3_);
                    return var17 > 0 ? var17 : 0;
                }
            } else {
                var5 = p_77912_0_.indexOf(38, p_77912_1_);

                if (var5 >= 0 && var5 < p_77912_2_) {
                    var17 = PotionHelper.parsePotionEffects(p_77912_0_, p_77912_1_, var5 - 1, p_77912_3_);

                    if (var17 <= 0) {
                        return 0;
                    } else {
                        int var18 = PotionHelper.parsePotionEffects(p_77912_0_, var5 + 1, p_77912_2_, p_77912_3_);
                        return var18 <= 0 ? 0 : (var17 > var18 ? var17 : var18);
                    }
                } else {
                    boolean var6 = false;
                    boolean var7 = false;
                    boolean var8 = false;
                    boolean var9 = false;
                    boolean var10 = false;
                    byte var11 = -1;
                    int var12 = 0;
                    int var13 = 0;
                    int var14 = 0;

                    for (int var15 = p_77912_1_; var15 < p_77912_2_; ++var15) {
                        char var16 = p_77912_0_.charAt(var15);

                        if (var16 >= 48 && var16 <= 57) {
                            if (var6) {
                                var13 = var16 - 48;
                                var7 = true;
                            } else {
                                var12 *= 10;
                                var12 += var16 - 48;
                                var8 = true;
                            }
                        } else if (var16 == 42) {
                            var6 = true;
                        } else if (var16 == 33) {
                            if (var8) {
                                var14 += PotionHelper.func_77904_a(var9, var7, var10, var11, var12, var13, p_77912_3_);
                                var9 = false;
                                var10 = false;
                                var6 = false;
                                var7 = false;
                                var8 = false;
                                var13 = 0;
                                var12 = 0;
                                var11 = -1;
                            }

                            var9 = true;
                        } else if (var16 == 45) {
                            if (var8) {
                                var14 += PotionHelper.func_77904_a(var9, var7, var10, var11, var12, var13, p_77912_3_);
                                var9 = false;
                                var10 = false;
                                var6 = false;
                                var7 = false;
                                var8 = false;
                                var13 = 0;
                                var12 = 0;
                                var11 = -1;
                            }

                            var10 = true;
                        } else if (var16 != 61 && var16 != 60 && var16 != 62) {
                            if (var16 == 43 && var8) {
                                var14 += PotionHelper.func_77904_a(var9, var7, var10, var11, var12, var13, p_77912_3_);
                                var9 = false;
                                var10 = false;
                                var6 = false;
                                var7 = false;
                                var8 = false;
                                var13 = 0;
                                var12 = 0;
                                var11 = -1;
                            }
                        } else {
                            if (var8) {
                                var14 += PotionHelper.func_77904_a(var9, var7, var10, var11, var12, var13, p_77912_3_);
                                var9 = false;
                                var10 = false;
                                var6 = false;
                                var7 = false;
                                var8 = false;
                                var13 = 0;
                                var12 = 0;
                                var11 = -1;
                            }

                            if (var16 == 61) {
                                var11 = 0;
                            } else if (var16 == 60) {
                                var11 = 2;
                            } else if (var16 == 62) {
                                var11 = 1;
                            }
                        }
                    }

                    if (var8) {
                        var14 += PotionHelper.func_77904_a(var9, var7, var10, var11, var12, var13, p_77912_3_);
                    }

                    return var14;
                }
            }
        } else {
            return 0;
        }
    }

    /**
     * Returns a list of effects for the specified potion damage value.
     */
    public static List getPotionEffects(int p_77917_0_, boolean p_77917_1_) {
        ArrayList var2 = null;
        Potion[] var3 = Potion.potionTypes;
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Potion var6 = var3[var5];

            if (var6 != null && (!var6.isUsable() || p_77917_1_)) {
                String var7 = (String) PotionHelper.field_179539_o.get(Integer.valueOf(var6.getId()));

                if (var7 != null) {
                    int var8 = PotionHelper.parsePotionEffects(var7, 0, var7.length(), p_77917_0_);

                    if (var8 > 0) {
                        int var9 = 0;
                        String var10 = (String) PotionHelper.field_179540_p.get(Integer.valueOf(var6.getId()));

                        if (var10 != null) {
                            var9 = PotionHelper.parsePotionEffects(var10, 0, var10.length(), p_77917_0_);

                            if (var9 < 0) {
                                var9 = 0;
                            }
                        }

                        if (var6.isInstant()) {
                            var8 = 1;
                        } else {
                            var8 = 1200 * (var8 * 3 + (var8 - 1) * 2);
                            var8 >>= var9;
                            var8 = (int) Math.round(var8 * var6.getEffectiveness());

                            if ((p_77917_0_ & 16384) != 0) {
                                var8 = (int) Math.round(var8 * 0.75D + 0.5D);
                            }
                        }

                        if (var2 == null) {
                            var2 = Lists.newArrayList();
                        }

                        PotionEffect var11 = new PotionEffect(var6.getId(), var8, var9);

                        if ((p_77917_0_ & 16384) != 0) {
                            var11.setSplashPotion(true);
                        }

                        var2.add(var11);
                    }
                }
            }
        }

        return var2;
    }

    /**
     * Manipulates the specified bit of the potion damage value according to the
     * rules passed from applyIngredient.
     */
    private static int brewBitOperations(int p_77906_0_, int p_77906_1_, boolean p_77906_2_, boolean p_77906_3_, boolean p_77906_4_) {
        if (p_77906_4_) {
            if (!PotionHelper.checkFlag(p_77906_0_, p_77906_1_)) {
                return 0;
            }
        } else if (p_77906_2_) {
            p_77906_0_ &= ~(1 << p_77906_1_);
        } else if (p_77906_3_) {
            if ((p_77906_0_ & 1 << p_77906_1_) == 0) {
                p_77906_0_ |= 1 << p_77906_1_;
            } else {
                p_77906_0_ &= ~(1 << p_77906_1_);
            }
        } else {
            p_77906_0_ |= 1 << p_77906_1_;
        }

        return p_77906_0_;
    }

    /**
     * Returns the new potion damage value after the specified ingredient info
     * is applied to the specified potion.
     */
    public static int applyIngredient(int p_77913_0_, String p_77913_1_) {
        byte var2 = 0;
        int var3 = p_77913_1_.length();
        boolean var4 = false;
        boolean var5 = false;
        boolean var6 = false;
        boolean var7 = false;
        int var8 = 0;

        for (int var9 = var2; var9 < var3; ++var9) {
            char var10 = p_77913_1_.charAt(var9);

            if (var10 >= 48 && var10 <= 57) {
                var8 *= 10;
                var8 += var10 - 48;
                var4 = true;
            } else if (var10 == 33) {
                if (var4) {
                    p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, var8, var6, var5, var7);
                    var7 = false;
                    var5 = false;
                    var6 = false;
                    var4 = false;
                    var8 = 0;
                }

                var5 = true;
            } else if (var10 == 45) {
                if (var4) {
                    p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, var8, var6, var5, var7);
                    var7 = false;
                    var5 = false;
                    var6 = false;
                    var4 = false;
                    var8 = 0;
                }

                var6 = true;
            } else if (var10 == 43) {
                if (var4) {
                    p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, var8, var6, var5, var7);
                    var7 = false;
                    var5 = false;
                    var6 = false;
                    var4 = false;
                    var8 = 0;
                }
            } else if (var10 == 38) {
                if (var4) {
                    p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, var8, var6, var5, var7);
                    var7 = false;
                    var5 = false;
                    var6 = false;
                    var4 = false;
                    var8 = 0;
                }

                var7 = true;
            }
        }

        if (var4) {
            p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, var8, var6, var5, var7);
        }

        return p_77913_0_ & 32767;
    }

    public static int func_77908_a(int p_77908_0_, int p_77908_1_, int p_77908_2_, int p_77908_3_, int p_77908_4_, int p_77908_5_) {
        return (PotionHelper.checkFlag(p_77908_0_, p_77908_1_) ? 16 : 0) | (PotionHelper.checkFlag(p_77908_0_, p_77908_2_) ? 8 : 0) | (PotionHelper.checkFlag(p_77908_0_, p_77908_3_) ? 4 : 0) | (PotionHelper.checkFlag(p_77908_0_, p_77908_4_) ? 2 : 0) | (PotionHelper.checkFlag(p_77908_0_, p_77908_5_) ? 1 : 0);
    }

    static {
        PotionHelper.field_179539_o.put(Integer.valueOf(Potion.regeneration.getId()), "0 & !1 & !2 & !3 & 0+6");
        sugarEffect = "-0+1-2-3&4-4+13";
        PotionHelper.field_179539_o.put(Integer.valueOf(Potion.moveSpeed.getId()), "!0 & 1 & !2 & !3 & 1+6");
        magmaCreamEffect = "+0+1-2-3&4-4+13";
        PotionHelper.field_179539_o.put(Integer.valueOf(Potion.fireResistance.getId()), "0 & 1 & !2 & !3 & 0+6");
        speckledMelonEffect = "+0-1+2-3&4-4+13";
        PotionHelper.field_179539_o.put(Integer.valueOf(Potion.heal.getId()), "0 & !1 & 2 & !3");
        spiderEyeEffect = "-0-1+2-3&4-4+13";
        PotionHelper.field_179539_o.put(Integer.valueOf(Potion.poison.getId()), "!0 & !1 & 2 & !3 & 2+6");
        fermentedSpiderEyeEffect = "-0+3-4+13";
        PotionHelper.field_179539_o.put(Integer.valueOf(Potion.weakness.getId()), "!0 & !1 & !2 & 3 & 3+6");
        PotionHelper.field_179539_o.put(Integer.valueOf(Potion.harm.getId()), "!0 & !1 & 2 & 3");
        PotionHelper.field_179539_o.put(Integer.valueOf(Potion.moveSlowdown.getId()), "!0 & 1 & !2 & 3 & 3+6");
        blazePowderEffect = "+0-1-2+3&4-4+13";
        PotionHelper.field_179539_o.put(Integer.valueOf(Potion.damageBoost.getId()), "0 & !1 & !2 & 3 & 3+6");
        goldenCarrotEffect = "-0+1+2-3+13&4-4";
        PotionHelper.field_179539_o.put(Integer.valueOf(Potion.nightVision.getId()), "!0 & 1 & 2 & !3 & 2+6");
        PotionHelper.field_179539_o.put(Integer.valueOf(Potion.invisibility.getId()), "!0 & 1 & 2 & 3 & 2+6");
        field_151423_m = "+0-1+2+3+13&4-4";
        PotionHelper.field_179539_o.put(Integer.valueOf(Potion.waterBreathing.getId()), "0 & !1 & 2 & 3 & 2+6");
        field_179538_n = "+0+1-2+3&4-4+13";
        PotionHelper.field_179539_o.put(Integer.valueOf(Potion.jump.getId()), "0 & 1 & !2 & 3");
        glowstoneEffect = "+5-6-7";
        PotionHelper.field_179540_p.put(Integer.valueOf(Potion.moveSpeed.getId()), "5");
        PotionHelper.field_179540_p.put(Integer.valueOf(Potion.digSpeed.getId()), "5");
        PotionHelper.field_179540_p.put(Integer.valueOf(Potion.damageBoost.getId()), "5");
        PotionHelper.field_179540_p.put(Integer.valueOf(Potion.regeneration.getId()), "5");
        PotionHelper.field_179540_p.put(Integer.valueOf(Potion.harm.getId()), "5");
        PotionHelper.field_179540_p.put(Integer.valueOf(Potion.heal.getId()), "5");
        PotionHelper.field_179540_p.put(Integer.valueOf(Potion.resistance.getId()), "5");
        PotionHelper.field_179540_p.put(Integer.valueOf(Potion.poison.getId()), "5");
        PotionHelper.field_179540_p.put(Integer.valueOf(Potion.jump.getId()), "5");
        redstoneEffect = "-5+6-7";
        gunpowderEffect = "+14&13-13";
        field_77925_n = Maps.newHashMap();
        potionPrefixes = new String[]{"potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick", "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky"};
    }
}
