package net.minecraft.util;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CombatTracker {
    /**
     * The CombatEntry objects that we've tracked so far.
     */
    private final List combatEntries = Lists.newArrayList();

    /**
     * The entity tracked.
     */
    private final EntityLivingBase fighter;
    private int field_94555_c;
    private int field_152775_d;
    private int field_152776_e;
    private boolean field_94552_d;
    private boolean field_94553_e;
    private String field_94551_f;
    private static final String __OBFID = "CL_00001520";

    public CombatTracker(EntityLivingBase p_i1565_1_) {
        fighter = p_i1565_1_;
    }

    public void func_94545_a() {
        func_94542_g();

        if (fighter.isOnLadder()) {
            Block var1 = fighter.worldObj.getBlockState(new BlockPos(fighter.posX, fighter.getEntityBoundingBox().minY, fighter.posZ)).getBlock();

            if (var1 == Blocks.ladder) {
                field_94551_f = "ladder";
            } else if (var1 == Blocks.vine) {
                field_94551_f = "vines";
            }
        } else if (fighter.isInWater()) {
            field_94551_f = "water";
        }
    }

    public void func_94547_a(DamageSource p_94547_1_, float p_94547_2_, float p_94547_3_) {
        func_94549_h();
        func_94545_a();
        CombatEntry var4 = new CombatEntry(p_94547_1_, fighter.ticksExisted, p_94547_2_, p_94547_3_, field_94551_f, fighter.fallDistance);
        combatEntries.add(var4);
        field_94555_c = fighter.ticksExisted;
        field_94553_e = true;

        if (var4.func_94559_f() && !field_94552_d && fighter.isEntityAlive()) {
            field_94552_d = true;
            field_152775_d = fighter.ticksExisted;
            field_152776_e = field_152775_d;
            fighter.func_152111_bt();
        }
    }

    public IChatComponent func_151521_b() {
        if (combatEntries.size() == 0) {
            return new ChatComponentTranslation("death.attack.generic", new Object[]{fighter.getDisplayName()});
        } else {
            CombatEntry var1 = func_94544_f();
            CombatEntry var2 = (CombatEntry) combatEntries.get(combatEntries.size() - 1);
            IChatComponent var4 = var2.func_151522_h();
            Entity var5 = var2.getDamageSrc().getEntity();
            Object var3;

            if (var1 != null && var2.getDamageSrc() == DamageSource.fall) {
                IChatComponent var6 = var1.func_151522_h();

                if (var1.getDamageSrc() != DamageSource.fall && var1.getDamageSrc() != DamageSource.outOfWorld) {
                    if (var6 != null && (var4 == null || !var6.equals(var4))) {
                        Entity var9 = var1.getDamageSrc().getEntity();
                        ItemStack var8 = var9 instanceof EntityLivingBase ? ((EntityLivingBase) var9).getHeldItem() : null;

                        if (var8 != null && var8.hasDisplayName()) {
                            var3 = new ChatComponentTranslation("death.fell.assist.item", new Object[]{fighter.getDisplayName(), var6, var8.getChatComponent()});
                        } else {
                            var3 = new ChatComponentTranslation("death.fell.assist", new Object[]{fighter.getDisplayName(), var6});
                        }
                    } else if (var4 != null) {
                        ItemStack var7 = var5 instanceof EntityLivingBase ? ((EntityLivingBase) var5).getHeldItem() : null;

                        if (var7 != null && var7.hasDisplayName()) {
                            var3 = new ChatComponentTranslation("death.fell.finish.item", new Object[]{fighter.getDisplayName(), var4, var7.getChatComponent()});
                        } else {
                            var3 = new ChatComponentTranslation("death.fell.finish", new Object[]{fighter.getDisplayName(), var4});
                        }
                    } else {
                        var3 = new ChatComponentTranslation("death.fell.killer", new Object[]{fighter.getDisplayName()});
                    }
                } else {
                    var3 = new ChatComponentTranslation("death.fell.accident." + func_94548_b(var1), new Object[]{fighter.getDisplayName()});
                }
            } else {
                var3 = var2.getDamageSrc().getDeathMessage(fighter);
            }

            return (IChatComponent) var3;
        }
    }

    public EntityLivingBase func_94550_c() {
        EntityLivingBase var1 = null;
        EntityPlayer var2 = null;
        float var3 = 0.0F;
        float var4 = 0.0F;
        Iterator var5 = combatEntries.iterator();

        while (var5.hasNext()) {
            CombatEntry var6 = (CombatEntry) var5.next();

            if (var6.getDamageSrc().getEntity() instanceof EntityPlayer && (var2 == null || var6.func_94563_c() > var4)) {
                var4 = var6.func_94563_c();
                var2 = (EntityPlayer) var6.getDamageSrc().getEntity();
            }

            if (var6.getDamageSrc().getEntity() instanceof EntityLivingBase && (var1 == null || var6.func_94563_c() > var3)) {
                var3 = var6.func_94563_c();
                var1 = (EntityLivingBase) var6.getDamageSrc().getEntity();
            }
        }

        if (var2 != null && var4 >= var3 / 3.0F) {
            return var2;
        } else {
            return var1;
        }
    }

    private CombatEntry func_94544_f() {
        CombatEntry var1 = null;
        CombatEntry var2 = null;
        byte var3 = 0;
        float var4 = 0.0F;

        for (int var5 = 0; var5 < combatEntries.size(); ++var5) {
            CombatEntry var6 = (CombatEntry) combatEntries.get(var5);
            CombatEntry var7 = var5 > 0 ? (CombatEntry) combatEntries.get(var5 - 1) : null;

            if ((var6.getDamageSrc() == DamageSource.fall || var6.getDamageSrc() == DamageSource.outOfWorld) && var6.func_94561_i() > 0.0F && (var1 == null || var6.func_94561_i() > var4)) {
                if (var5 > 0) {
                    var1 = var7;
                } else {
                    var1 = var6;
                }

                var4 = var6.func_94561_i();
            }

            if (var6.func_94562_g() != null && (var2 == null || var6.func_94563_c() > var3)) {
                var2 = var6;
            }
        }

        if (var4 > 5.0F && var1 != null) {
            return var1;
        } else if (var3 > 5 && var2 != null) {
            return var2;
        } else {
            return null;
        }
    }

    private String func_94548_b(CombatEntry p_94548_1_) {
        return p_94548_1_.func_94562_g() == null ? "generic" : p_94548_1_.func_94562_g();
    }

    public int func_180134_f() {
        return field_94552_d ? fighter.ticksExisted - field_152775_d : field_152776_e - field_152775_d;
    }

    private void func_94542_g() {
        field_94551_f = null;
    }

    public void func_94549_h() {
        int var1 = field_94552_d ? 300 : 100;

        if (field_94553_e && (!fighter.isEntityAlive() || fighter.ticksExisted - field_94555_c > var1)) {
            boolean var2 = field_94552_d;
            field_94553_e = false;
            field_94552_d = false;
            field_152776_e = fighter.ticksExisted;

            if (var2) {
                fighter.func_152112_bu();
            }

            combatEntries.clear();
        }
    }

    public EntityLivingBase func_180135_h() {
        return fighter;
    }
}
