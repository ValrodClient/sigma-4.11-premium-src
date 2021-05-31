package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.world.Explosion;

public class DamageSource {
    public static DamageSource inFire = (new DamageSource("inFire")).setFireDamage();
    public static DamageSource field_180137_b = new DamageSource("lightningBolt");
    public static DamageSource onFire = (new DamageSource("onFire")).setDamageBypassesArmor().setFireDamage();
    public static DamageSource lava = (new DamageSource("lava")).setFireDamage();
    public static DamageSource inWall = (new DamageSource("inWall")).setDamageBypassesArmor();
    public static DamageSource drown = (new DamageSource("drown")).setDamageBypassesArmor();
    public static DamageSource starve = (new DamageSource("starve")).setDamageBypassesArmor().setDamageIsAbsolute();
    public static DamageSource cactus = new DamageSource("cactus");
    public static DamageSource fall = (new DamageSource("fall")).setDamageBypassesArmor();
    public static DamageSource outOfWorld = (new DamageSource("outOfWorld")).setDamageBypassesArmor().setDamageAllowedInCreativeMode();
    public static DamageSource generic = (new DamageSource("generic")).setDamageBypassesArmor();
    public static DamageSource magic = (new DamageSource("magic")).setDamageBypassesArmor().setMagicDamage();
    public static DamageSource wither = (new DamageSource("wither")).setDamageBypassesArmor();
    public static DamageSource anvil = new DamageSource("anvil");
    public static DamageSource fallingBlock = new DamageSource("fallingBlock");

    /**
     * This kind of damage can be blocked or not.
     */
    private boolean isUnblockable;
    private boolean isDamageAllowedInCreativeMode;

    /**
     * Whether or not the damage ignores modification by potion effects or
     * enchantments.
     */
    private boolean damageIsAbsolute;
    private float hungerDamage = 0.3F;

    /**
     * This kind of damage is based on fire or not.
     */
    private boolean fireDamage;

    /**
     * This kind of damage is based on a projectile or not.
     */
    private boolean projectile;

    /**
     * Whether this damage source will have its damage amount scaled based on
     * the current difficulty.
     */
    private boolean difficultyScaled;

    /**
     * Whether the damage is magic based.
     */
    private boolean magicDamage;
    private boolean explosion;
    public String damageType;
    private static final String __OBFID = "CL_00001521";

    public static DamageSource causeMobDamage(EntityLivingBase p_76358_0_) {
        return new EntityDamageSource("mob", p_76358_0_);
    }

    /**
     * returns an EntityDamageSource of type player
     */
    public static DamageSource causePlayerDamage(EntityPlayer p_76365_0_) {
        return new EntityDamageSource("player", p_76365_0_);
    }

    /**
     * returns EntityDamageSourceIndirect of an arrow
     */
    public static DamageSource causeArrowDamage(EntityArrow p_76353_0_, Entity p_76353_1_) {
        return (new EntityDamageSourceIndirect("arrow", p_76353_0_, p_76353_1_)).setProjectile();
    }

    /**
     * returns EntityDamageSourceIndirect of a fireball
     */
    public static DamageSource causeFireballDamage(EntityFireball p_76362_0_, Entity p_76362_1_) {
        return p_76362_1_ == null ? (new EntityDamageSourceIndirect("onFire", p_76362_0_, p_76362_0_)).setFireDamage().setProjectile() : (new EntityDamageSourceIndirect("fireball", p_76362_0_, p_76362_1_)).setFireDamage().setProjectile();
    }

    public static DamageSource causeThrownDamage(Entity p_76356_0_, Entity p_76356_1_) {
        return (new EntityDamageSourceIndirect("thrown", p_76356_0_, p_76356_1_)).setProjectile();
    }

    public static DamageSource causeIndirectMagicDamage(Entity p_76354_0_, Entity p_76354_1_) {
        return (new EntityDamageSourceIndirect("indirectMagic", p_76354_0_, p_76354_1_)).setDamageBypassesArmor().setMagicDamage();
    }

    /**
     * Returns the EntityDamageSource of the Thorns enchantment
     */
    public static DamageSource causeThornsDamage(Entity p_92087_0_) {
        return (new EntityDamageSource("thorns", p_92087_0_)).func_180138_v().setMagicDamage();
    }

    public static DamageSource setExplosionSource(Explosion p_94539_0_) {
        return p_94539_0_ != null && p_94539_0_.getExplosivePlacedBy() != null ? (new EntityDamageSource("explosion.player", p_94539_0_.getExplosivePlacedBy())).setDifficultyScaled().setExplosion() : (new DamageSource("explosion")).setDifficultyScaled().setExplosion();
    }

    /**
     * Returns true if the damage is projectile based.
     */
    public boolean isProjectile() {
        return projectile;
    }

    /**
     * Define the damage type as projectile based.
     */
    public DamageSource setProjectile() {
        projectile = true;
        return this;
    }

    public boolean isExplosion() {
        return explosion;
    }

    public DamageSource setExplosion() {
        explosion = true;
        return this;
    }

    public boolean isUnblockable() {
        return isUnblockable;
    }

    /**
     * How much satiate(food) is consumed by this DamageSource
     */
    public float getHungerDamage() {
        return hungerDamage;
    }

    public boolean canHarmInCreative() {
        return isDamageAllowedInCreativeMode;
    }

    /**
     * Whether or not the damage ignores modification by potion effects or
     * enchantments.
     */
    public boolean isDamageAbsolute() {
        return damageIsAbsolute;
    }

    protected DamageSource(String p_i1566_1_) {
        damageType = p_i1566_1_;
    }

    public Entity getSourceOfDamage() {
        return getEntity();
    }

    public Entity getEntity() {
        return null;
    }

    protected DamageSource setDamageBypassesArmor() {
        isUnblockable = true;
        hungerDamage = 0.0F;
        return this;
    }

    protected DamageSource setDamageAllowedInCreativeMode() {
        isDamageAllowedInCreativeMode = true;
        return this;
    }

    /**
     * Sets a value indicating whether the damage is absolute (ignores
     * modification by potion effects or enchantments), and also clears out
     * hunger damage.
     */
    protected DamageSource setDamageIsAbsolute() {
        damageIsAbsolute = true;
        hungerDamage = 0.0F;
        return this;
    }

    /**
     * Define the damage type as fire based.
     */
    protected DamageSource setFireDamage() {
        fireDamage = true;
        return this;
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    public IChatComponent getDeathMessage(EntityLivingBase p_151519_1_) {
        EntityLivingBase var2 = p_151519_1_.func_94060_bK();
        String var3 = "death.attack." + damageType;
        String var4 = var3 + ".player";
        return var2 != null && StatCollector.canTranslate(var4) ? new ChatComponentTranslation(var4, new Object[]{p_151519_1_.getDisplayName(), var2.getDisplayName()}) : new ChatComponentTranslation(var3, new Object[]{p_151519_1_.getDisplayName()});
    }

    /**
     * Returns true if the damage is fire based.
     */
    public boolean isFireDamage() {
        return fireDamage;
    }

    /**
     * Return the name of damage type.
     */
    public String getDamageType() {
        return damageType;
    }

    /**
     * Set whether this damage source will have its damage amount scaled based
     * on the current difficulty.
     */
    public DamageSource setDifficultyScaled() {
        difficultyScaled = true;
        return this;
    }

    /**
     * Return whether this damage source will have its damage amount scaled
     * based on the current difficulty.
     */
    public boolean isDifficultyScaled() {
        return difficultyScaled;
    }

    /**
     * Returns true if the damage is magic based.
     */
    public boolean isMagicDamage() {
        return magicDamage;
    }

    /**
     * Define the damage type as magic based.
     */
    public DamageSource setMagicDamage() {
        magicDamage = true;
        return this;
    }

    public boolean func_180136_u() {
        Entity var1 = getEntity();
        return var1 instanceof EntityPlayer && ((EntityPlayer) var1).capabilities.isCreativeMode;
    }
}
