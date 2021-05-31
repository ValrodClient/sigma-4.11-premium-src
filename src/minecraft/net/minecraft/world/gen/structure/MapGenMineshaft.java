package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.util.MathHelper;

public class MapGenMineshaft extends MapGenStructure {
    private double field_82673_e = 0.004D;
    private static final String __OBFID = "CL_00000443";

    public MapGenMineshaft() {
    }

    public String getStructureName() {
        return "Mineshaft";
    }

    public MapGenMineshaft(Map p_i2034_1_) {
        Iterator var2 = p_i2034_1_.entrySet().iterator();

        while (var2.hasNext()) {
            Entry var3 = (Entry) var2.next();

            if (((String) var3.getKey()).equals("chance")) {
                this.field_82673_e = MathHelper.parseDoubleWithDefault((String) var3.getValue(), this.field_82673_e);
            }
        }
    }

    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
        return this.rand.nextDouble() < this.field_82673_e && this.rand.nextInt(80) < Math.max(Math.abs(p_75047_1_), Math.abs(p_75047_2_));
    }

    protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_) {
        return new StructureMineshaftStart(this.worldObj, this.rand, p_75049_1_, p_75049_2_);
    }
}
