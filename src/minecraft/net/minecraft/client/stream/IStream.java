package net.minecraft.client.stream;

import tv.twitch.ErrorCode;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.chat.ChatUserInfo;

public interface IStream {
    /**
     * Shuts down a steam
     */
    void shutdownStream();

    void func_152935_j();

    void func_152922_k();

    boolean func_152936_l();

    boolean func_152924_m();

    boolean func_152934_n();

    void func_152911_a(Metadata p_152911_1_, long p_152911_2_);

    void func_176026_a(Metadata p_176026_1_, long p_176026_2_, long p_176026_4_);

    boolean isPaused();

    void func_152931_p();

    void func_152916_q();

    void func_152933_r();

    void func_152915_s();

    void func_152930_t();

    void func_152914_u();

    IngestServer[] func_152925_v();

    void func_152909_x();

    IngestServerTester func_152932_y();

    boolean func_152908_z();

    int func_152920_A();

    boolean func_152927_B();

    String func_152921_C();

    ChatUserInfo func_152926_a(String p_152926_1_);

    void func_152917_b(String p_152917_1_);

    boolean func_152928_D();

    ErrorCode func_152912_E();

    boolean func_152913_F();

    void func_152910_a(boolean p_152910_1_);

    boolean func_152929_G();

    IStream.AuthFailureReason func_152918_H();

    public static enum AuthFailureReason {
        ERROR("ERROR", 0),
        INVALID_TOKEN("INVALID_TOKEN", 1);

        private static final IStream.AuthFailureReason[] $VALUES = new IStream.AuthFailureReason[]{ERROR, INVALID_TOKEN};
        private static final String __OBFID = "CL_00001813";

        private AuthFailureReason(String p_i1014_1_, int p_i1014_2_) {
        }
    }
}
