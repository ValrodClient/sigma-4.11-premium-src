package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.PriorityQueue;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.util.QuadComparator;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;

import info.sigmaclient.Client;
import info.sigmaclient.module.impl.render.Xray;
import info.sigmaclient.util.render.Colors;

public class WorldRenderer {
    private ByteBuffer byteBuffer;
    private IntBuffer rawIntBuffer;
    private FloatBuffer rawFloatBuffer;
    private int vertexCount;
    private double field_178998_e;
    private double field_178995_f;
    private int field_178996_g;
    private int field_179007_h;
    private int rawBufferIndex;

    /**
     * Boolean for whether this renderer needs to be updated or not
     */
    private boolean needsUpdate;
    private int drawMode;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private int field_179003_o;
    private int field_179012_p;
    private VertexFormat vertexFormat;
    private boolean isDrawing;
    private int bufferSize;
    private static final String __OBFID = "CL_00000942";

    public WorldRenderer(int p_i46275_1_) {
        this.bufferSize = p_i46275_1_;
        this.byteBuffer = GLAllocation.createDirectByteBuffer(p_i46275_1_ * 4);
        this.rawIntBuffer = this.byteBuffer.asIntBuffer();
        this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
        this.vertexFormat = new VertexFormat();
        this.vertexFormat.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.POSITION, 3));
    }

    private void growBuffer(int p_178983_1_) {
        LogManager.getLogger().warn("Needed to grow BufferBuilder buffer: Old size " + this.bufferSize * 4 + " bytes, new size " + (this.bufferSize * 4 + p_178983_1_) + " bytes.");
        this.bufferSize += p_178983_1_ / 4;
        ByteBuffer var2 = GLAllocation.createDirectByteBuffer(this.bufferSize * 4);
        this.rawIntBuffer.position(0);
        var2.asIntBuffer().put(this.rawIntBuffer);
        this.byteBuffer = var2;
        this.rawIntBuffer = this.byteBuffer.asIntBuffer();
        this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
    }

    public WorldRenderer.State getVertexState(float p_178971_1_, float p_178971_2_, float p_178971_3_) {
        int[] var4 = new int[this.rawBufferIndex];
        PriorityQueue var5 = new PriorityQueue(this.rawBufferIndex, new QuadComparator(this.rawFloatBuffer, (float) ((double) p_178971_1_ + this.xOffset), (float) ((double) p_178971_2_ + this.yOffset), (float) ((double) p_178971_3_ + this.zOffset), this.vertexFormat.func_177338_f() / 4));
        int var6 = this.vertexFormat.func_177338_f();
        int var7;

        for (var7 = 0; var7 < this.rawBufferIndex; var7 += var6) {
            var5.add(Integer.valueOf(var7));
        }

        for (var7 = 0; !var5.isEmpty(); var7 += var6) {
            int var8 = ((Integer) var5.remove()).intValue();

            for (int var9 = 0; var9 < var6; ++var9) {
                var4[var7 + var9] = this.rawIntBuffer.get(var8 + var9);
            }
        }

        this.rawIntBuffer.clear();
        this.rawIntBuffer.put(var4);
        return new WorldRenderer.State(var4, this.rawBufferIndex, this.vertexCount, new VertexFormat(this.vertexFormat));
    }

    public void setVertexState(WorldRenderer.State p_178993_1_) {
        if (p_178993_1_.func_179013_a().length > this.rawIntBuffer.capacity()) {
            this.growBuffer(2097152);
        }

        this.rawIntBuffer.clear();
        this.rawIntBuffer.put(p_178993_1_.func_179013_a());
        this.rawBufferIndex = p_178993_1_.getRawBufferIndex();
        this.vertexCount = p_178993_1_.getVertexCount();
        this.vertexFormat = new VertexFormat(p_178993_1_.func_179016_d());
    }

    public void reset() {
        this.vertexCount = 0;
        this.rawBufferIndex = 0;
        this.vertexFormat.clear();
        this.vertexFormat.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.POSITION, 3));
    }

    public void startDrawingQuads() {
        this.startDrawing(7);
    }

    public void startDrawing(int mode) {
        if (this.isDrawing) {
            throw new IllegalStateException("Already building!");
        } else {
            this.isDrawing = true;
            this.reset();
            this.drawMode = mode;
            this.needsUpdate = false;
        }
    }

    public void setTextureUV(double p_178992_1_, double p_178992_3_) {
        if (!this.vertexFormat.func_177347_a(0) && !this.vertexFormat.func_177347_a(1)) {
            VertexFormatElement var5 = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.UV, 2);
            this.vertexFormat.func_177349_a(var5);
        }

        this.field_178998_e = p_178992_1_;
        this.field_178995_f = p_178992_3_;
    }

    public void setColorRGBA_I(final int p_178974_1_, final int p_178974_2_) {
        final int var3 = p_178974_1_ >> 16 & 0xFF;
        final int var4 = p_178974_1_ >> 8 & 0xFF;
        final int var5 = p_178974_1_ & 0xFF;
        this.func_178961_b(var3, var4, var5, p_178974_2_);
    }

    public void func_178963_b(int p_178963_1_) {
        if (!this.vertexFormat.func_177347_a(1)) {
            if (!this.vertexFormat.func_177347_a(0)) {
                this.vertexFormat.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUseage.UV, 2));
            }

            VertexFormatElement var2 = new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUseage.UV, 2);
            this.vertexFormat.func_177349_a(var2);
        }

        this.field_178996_g = p_178963_1_;
    }

    public void func_178986_b(float p_178986_1_, float p_178986_2_, float p_178986_3_) {
        this.setPosition((int) (p_178986_1_ * 255.0F), (int) (p_178986_2_ * 255.0F), (int) (p_178986_3_ * 255.0F));
    }

    public void setColorRGBA(float p_178960_1_, float p_178960_2_, float p_178960_3_, float p_178960_4_) {
        this.func_178961_b((int) (p_178960_1_ * 255.0F), (int) (p_178960_2_ * 255.0F), (int) (p_178960_3_ * 255.0F), (int) (p_178960_4_ * 255.0F));
    }

    /**
     * Sets a new position for the renderer and setting it up so it can be
     * reloaded with the new data for that position
     */
    public void setPosition(int p_78913_1_, int p_78913_2_, int p_78913_3_) {
        this.func_178961_b(p_78913_1_, p_78913_2_, p_78913_3_, 255);
    }

    public void func_178962_a(int p_178962_1_, int p_178962_2_, int p_178962_3_, int p_178962_4_) {
        int var5 = (this.vertexCount - 4) * (this.vertexFormat.func_177338_f() / 4) + this.vertexFormat.func_177344_b(1) / 4;
        int var6 = this.vertexFormat.func_177338_f() >> 2;
        this.rawIntBuffer.put(var5, p_178962_1_);
        this.rawIntBuffer.put(var5 + var6, p_178962_2_);
        this.rawIntBuffer.put(var5 + var6 * 2, p_178962_3_);
        this.rawIntBuffer.put(var5 + var6 * 3, p_178962_4_);
    }

    public void func_178987_a(double p_178987_1_, double p_178987_3_, double p_178987_5_) {
        if (this.rawBufferIndex >= this.bufferSize - this.vertexFormat.func_177338_f()) {
            this.growBuffer(2097152);
        }

        int var7 = this.vertexFormat.func_177338_f() / 4;
        int var8 = (this.vertexCount - 4) * var7;

        for (int var9 = 0; var9 < 4; ++var9) {
            int var10 = var8 + var9 * var7;
            int var11 = var10 + 1;
            int var12 = var11 + 1;
            this.rawIntBuffer.put(var10, Float.floatToRawIntBits((float) (p_178987_1_ + this.xOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(var10))));
            this.rawIntBuffer.put(var11, Float.floatToRawIntBits((float) (p_178987_3_ + this.yOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(var11))));
            this.rawIntBuffer.put(var12, Float.floatToRawIntBits((float) (p_178987_5_ + this.zOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(var12))));
        }
    }

    /**
     * Takes in the pass the call list is being requested for. Args: renderPass
     */
    private int getGLCallListForPass(int p_78909_1_) {
        return ((this.vertexCount - p_78909_1_) * this.vertexFormat.func_177338_f() + this.vertexFormat.func_177340_e()) / 4;
    }

    public void func_178978_a(float r, float g, float b, int indx) {
        int index = this.getGLCallListForPass(indx);
        int color = this.rawIntBuffer.get(index);
        int iR;
        int iG;
        int iB;

        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            iR = (int) ((float) (color & 255) * r);
            iG = (int) ((float) (color >> 8 & 255) * g);
            iB = (int) ((float) (color >> 16 & 255) * b);
            color &= -16777216;
            color |= iB << 16 | iG << 8 | iR;
        } else {
            iR = (int) ((float) (this.field_179007_h >> 24 & 255) * r);
            iG = (int) ((float) (this.field_179007_h >> 16 & 255) * g);
            iB = (int) ((float) (this.field_179007_h >> 8 & 255) * b);
            color &= 255;
            color |= iR << 24 | iG << 16 | iB << 8;
        }
        if (this.needsUpdate) {
            color = -1;
        }
        Xray x = (Xray) Client.getModuleManager().get(Xray.class);
        if (x.isEnabled()) {
            color = Colors.getColor(iR, iG, iB, x.getOpacity());
        }

        this.rawIntBuffer.put(index, color);
    }

    private void func_178988_b(int p_178988_1_, int p_178988_2_) {
        int var3 = this.getGLCallListForPass(p_178988_2_);
        int var4 = p_178988_1_ >> 16 & 255;
        int var5 = p_178988_1_ >> 8 & 255;
        int var6 = p_178988_1_ & 255;
        int var7 = p_178988_1_ >> 24 & 255;
        this.func_178972_a(var3, var4, var5, var6, var7);
    }

    public void func_178994_b(float p_178994_1_, float p_178994_2_, float p_178994_3_, int p_178994_4_) {
        int var5 = this.getGLCallListForPass(p_178994_4_);
        int var6 = MathHelper.clamp_int((int) (p_178994_1_ * 255.0F), 0, 255);
        int var7 = MathHelper.clamp_int((int) (p_178994_2_ * 255.0F), 0, 255);
        int var8 = MathHelper.clamp_int((int) (p_178994_3_ * 255.0F), 0, 255);
        this.func_178972_a(var5, var6, var7, var8, 255);
    }

    private void func_178972_a(int p_178972_1_, int p_178972_2_, int p_178972_3_, int p_178972_4_, int p_178972_5_) {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            this.rawIntBuffer.put(p_178972_1_, p_178972_5_ << 24 | p_178972_4_ << 16 | p_178972_3_ << 8 | p_178972_2_);
        } else {
            this.rawIntBuffer.put(p_178972_1_, p_178972_2_ << 24 | p_178972_3_ << 16 | p_178972_4_ << 8 | p_178972_5_);
        }
    }

    public void func_178961_b(int p_178961_1_, int p_178961_2_, int p_178961_3_, int p_178961_4_) {
        if (!this.needsUpdate) {
            if (p_178961_1_ > 255) {
                p_178961_1_ = 255;
            }

            if (p_178961_2_ > 255) {
                p_178961_2_ = 255;
            }

            if (p_178961_3_ > 255) {
                p_178961_3_ = 255;
            }

            if (p_178961_4_ > 255) {
                p_178961_4_ = 255;
            }

            if (p_178961_1_ < 0) {
                p_178961_1_ = 0;
            }

            if (p_178961_2_ < 0) {
                p_178961_2_ = 0;
            }

            if (p_178961_3_ < 0) {
                p_178961_3_ = 0;
            }

            if (p_178961_4_ < 0) {
                p_178961_4_ = 0;
            }

            if (!this.vertexFormat.func_177346_d()) {
                VertexFormatElement var5 = new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUseage.COLOR, 4);
                this.vertexFormat.func_177349_a(var5);
            }

            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                this.field_179007_h = p_178961_4_ << 24 | p_178961_3_ << 16 | p_178961_2_ << 8 | p_178961_1_;
            } else {
                this.field_179007_h = p_178961_1_ << 24 | p_178961_2_ << 16 | p_178961_3_ << 8 | p_178961_4_;
            }
        }
    }

    public void func_178982_a(byte p_178982_1_, byte p_178982_2_, byte p_178982_3_) {
        this.setPosition(p_178982_1_ & 255, p_178982_2_ & 255, p_178982_3_ & 255);
    }

    public void addVertexWithUV(double p_178985_1_, double p_178985_3_, double p_178985_5_, double p_178985_7_, double p_178985_9_) {
        this.setTextureUV(p_178985_7_, p_178985_9_);
        this.addVertex(p_178985_1_, p_178985_3_, p_178985_5_);
    }

    public void setVertexFormat(VertexFormat p_178967_1_) {
        this.vertexFormat = new VertexFormat(p_178967_1_);
    }

    public void func_178981_a(int[] p_178981_1_) {
        int var2 = this.vertexFormat.func_177338_f() / 4;
        this.vertexCount += p_178981_1_.length / var2;
        this.rawIntBuffer.position(this.rawBufferIndex);
        this.rawIntBuffer.put(p_178981_1_);
        this.rawBufferIndex += p_178981_1_.length;
    }

    public void addVertex(double p_178984_1_, double p_178984_3_, double p_178984_5_) {
        if (this.rawBufferIndex >= this.bufferSize - this.vertexFormat.func_177338_f()) {
            this.growBuffer(2097152);
        }

        List var7 = this.vertexFormat.func_177343_g();
        int listSize = var7.size();

        for (int i = 0; i < listSize; ++i) {
            VertexFormatElement var9 = (VertexFormatElement) var7.get(i);
            int var10 = var9.func_177373_a() >> 2;
            int var11 = this.rawBufferIndex + var10;

            switch (WorldRenderer.SwitchEnumUseage.field_178959_a[var9.func_177375_c().ordinal()]) {
                case 1:
                    this.rawIntBuffer.put(var11, Float.floatToRawIntBits((float) (p_178984_1_ + this.xOffset)));
                    this.rawIntBuffer.put(var11 + 1, Float.floatToRawIntBits((float) (p_178984_3_ + this.yOffset)));
                    this.rawIntBuffer.put(var11 + 2, Float.floatToRawIntBits((float) (p_178984_5_ + this.zOffset)));
                    break;

                case 2:
                    this.rawIntBuffer.put(var11, this.field_179007_h);
                    break;

                case 3:
                    if (var9.func_177369_e() == 0) {
                        this.rawIntBuffer.put(var11, Float.floatToRawIntBits((float) this.field_178998_e));
                        this.rawIntBuffer.put(var11 + 1, Float.floatToRawIntBits((float) this.field_178995_f));
                    } else {
                        this.rawIntBuffer.put(var11, this.field_178996_g);
                    }

                    break;

                case 4:
                    this.rawIntBuffer.put(var11, this.field_179003_o);
            }
        }

        this.rawBufferIndex += this.vertexFormat.func_177338_f() >> 2;
        ++this.vertexCount;
    }

    public void func_178991_c(int p_178991_1_) {
        int var2 = p_178991_1_ >> 16 & 255;
        int var3 = p_178991_1_ >> 8 & 255;
        int var4 = p_178991_1_ & 255;
        this.setPosition(var2, var3, var4);
    }

    public void drawColor(int p_178974_1_, int p_178974_2_) {
        int var3 = p_178974_1_ >> 16 & 255;
        int var4 = p_178974_1_ >> 8 & 255;
        int var5 = p_178974_1_ & 255;
        this.func_178961_b(var3, var4, var5, p_178974_2_);
    }

    /**
     * Marks the current renderer data as dirty and needing to be updated.
     */
    public void markDirty() {
        this.needsUpdate = true;
    }

    public void func_178980_d(float p_178980_1_, float p_178980_2_, float p_178980_3_) {
        if (!this.vertexFormat.func_177350_b()) {
            VertexFormatElement var7 = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUseage.NORMAL, 3);
            this.vertexFormat.func_177349_a(var7);
            this.vertexFormat.func_177349_a(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUseage.PADDING, 1));
        }

        byte var71 = (byte) ((int) (p_178980_1_ * 127.0F));
        byte var5 = (byte) ((int) (p_178980_2_ * 127.0F));
        byte var6 = (byte) ((int) (p_178980_3_ * 127.0F));
        this.field_179003_o = var71 & 255 | (var5 & 255) << 8 | (var6 & 255) << 16;
    }

    public void func_178975_e(float p_178975_1_, float p_178975_2_, float p_178975_3_) {
        byte var4 = (byte) ((int) (p_178975_1_ * 127.0F));
        byte var5 = (byte) ((int) (p_178975_2_ * 127.0F));
        byte var6 = (byte) ((int) (p_178975_3_ * 127.0F));
        int var7 = this.vertexFormat.func_177338_f() >> 2;
        int var8 = (this.vertexCount - 4) * var7 + this.vertexFormat.func_177342_c() / 4;
        this.field_179003_o = var4 & 255 | (var5 & 255) << 8 | (var6 & 255) << 16;
        this.rawIntBuffer.put(var8, this.field_179003_o);
        this.rawIntBuffer.put(var8 + var7, this.field_179003_o);
        this.rawIntBuffer.put(var8 + var7 * 2, this.field_179003_o);
        this.rawIntBuffer.put(var8 + var7 * 3, this.field_179003_o);
    }

    public void setTranslation(double p_178969_1_, double p_178969_3_, double p_178969_5_) {
        this.xOffset = p_178969_1_;
        this.yOffset = p_178969_3_;
        this.zOffset = p_178969_5_;
    }

    public int draw() {
        if (!this.isDrawing) {
            throw new IllegalStateException("Not building!");
        } else {
            this.isDrawing = false;

            if (this.vertexCount > 0) {
                this.byteBuffer.position(0);
                this.byteBuffer.limit(this.rawBufferIndex * 4);
            }

            this.field_179012_p = this.rawBufferIndex * 4;
            return this.field_179012_p;
        }
    }

    public int func_178976_e() {
        return this.field_179012_p;
    }

    public ByteBuffer func_178966_f() {
        return this.byteBuffer;
    }

    public VertexFormat func_178973_g() {
        return this.vertexFormat;
    }

    public int func_178989_h() {
        return this.vertexCount;
    }

    public int getDrawMode() {
        return this.drawMode;
    }

    public void func_178968_d(int p_178968_1_) {
        for (int var2 = 0; var2 < 4; ++var2) {
            this.func_178988_b(p_178968_1_, var2 + 1);
        }
    }

    public void func_178990_f(float p_178990_1_, float p_178990_2_, float p_178990_3_) {
        for (int var4 = 0; var4 < 4; ++var4) {
            this.func_178994_b(p_178990_1_, p_178990_2_, p_178990_3_, var4 + 1);
        }
    }

    public class State {
        private final int[] field_179019_b;
        private final int field_179020_c;
        private final int field_179017_d;
        private final VertexFormat field_179018_e;
        private static final String __OBFID = "CL_00002568";

        public State(int[] p_i46274_2_, int p_i46274_3_, int p_i46274_4_, VertexFormat p_i46274_5_) {
            this.field_179019_b = p_i46274_2_;
            this.field_179020_c = p_i46274_3_;
            this.field_179017_d = p_i46274_4_;
            this.field_179018_e = p_i46274_5_;
        }

        public int[] func_179013_a() {
            return this.field_179019_b;
        }

        public int getRawBufferIndex() {
            return this.field_179020_c;
        }

        public int getVertexCount() {
            return this.field_179017_d;
        }

        public VertexFormat func_179016_d() {
            return this.field_179018_e;
        }
    }

    static final class SwitchEnumUseage {
        static final int[] field_178959_a = new int[VertexFormatElement.EnumUseage.values().length];
        private static final String __OBFID = "CL_00002569";

        static {
            try {
                field_178959_a[VertexFormatElement.EnumUseage.POSITION.ordinal()] = 1;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                field_178959_a[VertexFormatElement.EnumUseage.COLOR.ordinal()] = 2;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                field_178959_a[VertexFormatElement.EnumUseage.UV.ordinal()] = 3;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_178959_a[VertexFormatElement.EnumUseage.NORMAL.ordinal()] = 4;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
