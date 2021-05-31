package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;

public class CompiledChunk {
    public static final CompiledChunk field_178502_a = new CompiledChunk() {
        private static final String __OBFID = "CL_00002455";

        protected void func_178486_a(EnumWorldBlockLayer p_178486_1_) {
            throw new UnsupportedOperationException();
        }

        public void func_178493_c(EnumWorldBlockLayer p_178493_1_) {
            throw new UnsupportedOperationException();
        }

        public boolean func_178495_a(EnumFacing p_178495_1_, EnumFacing p_178495_2_) {
            return false;
        }
    };
    private final boolean[] field_178500_b = new boolean[EnumWorldBlockLayer.values().length];
    private final boolean[] field_178501_c = new boolean[EnumWorldBlockLayer.values().length];
    private boolean field_178498_d = true;
    private final List field_178499_e = Lists.newArrayList();
    private SetVisibility field_178496_f = new SetVisibility();
    private WorldRenderer.State field_178497_g;
    private static final String __OBFID = "CL_00002456";

    public boolean func_178489_a() {
        return this.field_178498_d;
    }

    protected void func_178486_a(EnumWorldBlockLayer p_178486_1_) {
        this.field_178498_d = false;
        this.field_178500_b[p_178486_1_.ordinal()] = true;
    }

    public boolean func_178491_b(EnumWorldBlockLayer p_178491_1_) {
        return !this.field_178500_b[p_178491_1_.ordinal()];
    }

    public void func_178493_c(EnumWorldBlockLayer p_178493_1_) {
        this.field_178501_c[p_178493_1_.ordinal()] = true;
    }

    public boolean func_178492_d(EnumWorldBlockLayer p_178492_1_) {
        return this.field_178501_c[p_178492_1_.ordinal()];
    }

    public List func_178485_b() {
        return this.field_178499_e;
    }

    public void func_178490_a(TileEntity p_178490_1_) {
        this.field_178499_e.add(p_178490_1_);
    }

    public boolean func_178495_a(EnumFacing p_178495_1_, EnumFacing p_178495_2_) {
        return this.field_178496_f.func_178621_a(p_178495_1_, p_178495_2_);
    }

    public void func_178488_a(SetVisibility p_178488_1_) {
        this.field_178496_f = p_178488_1_;
    }

    public WorldRenderer.State func_178487_c() {
        return this.field_178497_g;
    }

    public void func_178494_a(WorldRenderer.State p_178494_1_) {
        this.field_178497_g = p_178494_1_;
    }
}
