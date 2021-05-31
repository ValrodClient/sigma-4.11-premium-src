package net.minecraft.client.stream;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ThreadSafeBoundList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.twitch.AuthToken;
import tv.twitch.Core;
import tv.twitch.ErrorCode;
import tv.twitch.MessageLevel;
import tv.twitch.StandardCoreAPI;
import tv.twitch.broadcast.ArchivingState;
import tv.twitch.broadcast.AudioDeviceType;
import tv.twitch.broadcast.AudioParams;
import tv.twitch.broadcast.ChannelInfo;
import tv.twitch.broadcast.DesktopStreamAPI;
import tv.twitch.broadcast.EncodingCpuUsage;
import tv.twitch.broadcast.FrameBuffer;
import tv.twitch.broadcast.GameInfo;
import tv.twitch.broadcast.GameInfoList;
import tv.twitch.broadcast.IStatCallbacks;
import tv.twitch.broadcast.IStreamCallbacks;
import tv.twitch.broadcast.IngestList;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.broadcast.PixelFormat;
import tv.twitch.broadcast.StartFlags;
import tv.twitch.broadcast.StatType;
import tv.twitch.broadcast.Stream;
import tv.twitch.broadcast.StreamInfo;
import tv.twitch.broadcast.StreamInfoForSetting;
import tv.twitch.broadcast.UserInfo;
import tv.twitch.broadcast.VideoParams;

public class BroadcastController {
    private static final Logger logger = LogManager.getLogger();
    protected final int field_152865_a = 30;
    protected final int field_152866_b = 3;
    private static final ThreadSafeBoundList field_152862_C = new ThreadSafeBoundList(String.class, 50);
    private String field_152863_D = null;
    protected BroadcastController.BroadcastListener field_152867_c = null;
    protected String field_152868_d = "";
    protected String field_152869_e = "";
    protected String field_152870_f = "";
    protected boolean field_152871_g = true;
    protected Core field_152872_h = null;
    protected Stream field_152873_i = null;
    protected List field_152874_j = Lists.newArrayList();
    protected List field_152875_k = Lists.newArrayList();
    protected boolean field_152876_l = false;
    protected boolean field_152877_m = false;
    protected boolean field_152878_n = false;
    protected BroadcastController.BroadcastState broadcastState;
    protected String field_152880_p;
    protected VideoParams field_152881_q;
    protected AudioParams field_152882_r;
    protected IngestList field_152883_s;
    protected IngestServer field_152884_t;
    protected AuthToken field_152885_u;
    protected ChannelInfo channelInfo;
    protected UserInfo field_152887_w;
    protected StreamInfo field_152888_x;
    protected ArchivingState field_152889_y;
    protected long field_152890_z;
    protected IngestServerTester field_152860_A;
    private ErrorCode field_152864_E;
    protected IStreamCallbacks field_177948_B;
    protected IStatCallbacks field_177949_C;
    private static final String __OBFID = "CL_00001822";

    public void func_152841_a(BroadcastController.BroadcastListener p_152841_1_) {
        this.field_152867_c = p_152841_1_;
    }

    public boolean func_152858_b() {
        return this.field_152876_l;
    }

    public void func_152842_a(String p_152842_1_) {
        this.field_152868_d = p_152842_1_;
    }

    public StreamInfo func_152816_j() {
        return this.field_152888_x;
    }

    public ChannelInfo func_152843_l() {
        return this.channelInfo;
    }

    public boolean isBroadcasting() {
        return this.broadcastState == BroadcastController.BroadcastState.Broadcasting || this.broadcastState == BroadcastController.BroadcastState.Paused;
    }

    public boolean func_152857_n() {
        return this.broadcastState == BroadcastController.BroadcastState.ReadyToBroadcast;
    }

    public boolean isIngestTesting() {
        return this.broadcastState == BroadcastController.BroadcastState.IngestTesting;
    }

    public boolean isBroadcastPaused() {
        return this.broadcastState == BroadcastController.BroadcastState.Paused;
    }

    public boolean func_152849_q() {
        return this.field_152877_m;
    }

    public IngestServer func_152833_s() {
        return this.field_152884_t;
    }

    public void func_152824_a(IngestServer p_152824_1_) {
        this.field_152884_t = p_152824_1_;
    }

    public IngestList func_152855_t() {
        return this.field_152883_s;
    }

    public void func_152829_a(float p_152829_1_) {
        this.field_152873_i.setVolume(AudioDeviceType.TTV_RECORDER_DEVICE, p_152829_1_);
    }

    public void func_152837_b(float p_152837_1_) {
        this.field_152873_i.setVolume(AudioDeviceType.TTV_PLAYBACK_DEVICE, p_152837_1_);
    }

    public IngestServerTester isReady() {
        return this.field_152860_A;
    }

    public long func_152844_x() {
        return this.field_152873_i.getStreamTime();
    }

    protected boolean func_152848_y() {
        return true;
    }

    public ErrorCode func_152852_P() {
        return this.field_152864_E;
    }

    public BroadcastController() {
        this.broadcastState = BroadcastController.BroadcastState.Uninitialized;
        this.field_152880_p = null;
        this.field_152881_q = null;
        this.field_152882_r = null;
        this.field_152883_s = new IngestList(new IngestServer[0]);
        this.field_152884_t = null;
        this.field_152885_u = new AuthToken();
        this.channelInfo = new ChannelInfo();
        this.field_152887_w = new UserInfo();
        this.field_152888_x = new StreamInfo();
        this.field_152889_y = new ArchivingState();
        this.field_152890_z = 0L;
        this.field_152860_A = null;
        this.field_177948_B = new IStreamCallbacks() {
            private static final String __OBFID = "CL_00002375";

            public void requestAuthTokenCallback(ErrorCode p_requestAuthTokenCallback_1_, AuthToken p_requestAuthTokenCallback_2_) {
                if (ErrorCode.succeeded(p_requestAuthTokenCallback_1_)) {
                    BroadcastController.this.field_152885_u = p_requestAuthTokenCallback_2_;
                    BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.Authenticated);
                } else {
                    BroadcastController.this.field_152885_u.data = "";
                    BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.Initialized);
                    String var3 = ErrorCode.getString(p_requestAuthTokenCallback_1_);
                    BroadcastController.this.func_152820_d(String.format("RequestAuthTokenDoneCallback got failure: %s", new Object[]{var3}));
                }

                try {
                    if (BroadcastController.this.field_152867_c != null) {
                        BroadcastController.this.field_152867_c.func_152900_a(p_requestAuthTokenCallback_1_, p_requestAuthTokenCallback_2_);
                    }
                } catch (Exception var4) {
                    BroadcastController.this.func_152820_d(var4.toString());
                }
            }

            public void loginCallback(ErrorCode p_loginCallback_1_, ChannelInfo p_loginCallback_2_) {
                if (ErrorCode.succeeded(p_loginCallback_1_)) {
                    BroadcastController.this.channelInfo = p_loginCallback_2_;
                    BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.LoggedIn);
                    BroadcastController.this.field_152877_m = true;
                } else {
                    BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.Initialized);
                    BroadcastController.this.field_152877_m = false;
                    String var3 = ErrorCode.getString(p_loginCallback_1_);
                    BroadcastController.this.func_152820_d(String.format("LoginCallback got failure: %s", new Object[]{var3}));
                }

                try {
                    if (BroadcastController.this.field_152867_c != null) {
                        BroadcastController.this.field_152867_c.func_152897_a(p_loginCallback_1_);
                    }
                } catch (Exception var4) {
                    BroadcastController.this.func_152820_d(var4.toString());
                }
            }

            public void getIngestServersCallback(ErrorCode p_getIngestServersCallback_1_, IngestList p_getIngestServersCallback_2_) {
                if (ErrorCode.succeeded(p_getIngestServersCallback_1_)) {
                    BroadcastController.this.field_152883_s = p_getIngestServersCallback_2_;
                    BroadcastController.this.field_152884_t = BroadcastController.this.field_152883_s.getDefaultServer();
                    BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.ReceivedIngestServers);

                    try {
                        if (BroadcastController.this.field_152867_c != null) {
                            BroadcastController.this.field_152867_c.func_152896_a(p_getIngestServersCallback_2_);
                        }
                    } catch (Exception var4) {
                        BroadcastController.this.func_152820_d(var4.toString());
                    }
                } else {
                    String var3 = ErrorCode.getString(p_getIngestServersCallback_1_);
                    BroadcastController.this.func_152820_d(String.format("IngestListCallback got failure: %s", new Object[]{var3}));
                    BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.LoggingIn);
                }
            }

            public void getUserInfoCallback(ErrorCode p_getUserInfoCallback_1_, UserInfo p_getUserInfoCallback_2_) {
                BroadcastController.this.field_152887_w = p_getUserInfoCallback_2_;

                if (ErrorCode.failed(p_getUserInfoCallback_1_)) {
                    String var3 = ErrorCode.getString(p_getUserInfoCallback_1_);
                    BroadcastController.this.func_152820_d(String.format("UserInfoDoneCallback got failure: %s", new Object[]{var3}));
                }
            }

            public void getStreamInfoCallback(ErrorCode p_getStreamInfoCallback_1_, StreamInfo p_getStreamInfoCallback_2_) {
                if (ErrorCode.succeeded(p_getStreamInfoCallback_1_)) {
                    BroadcastController.this.field_152888_x = p_getStreamInfoCallback_2_;

                    try {
                        if (BroadcastController.this.field_152867_c != null) {
                            BroadcastController.this.field_152867_c.func_152894_a(p_getStreamInfoCallback_2_);
                        }
                    } catch (Exception var4) {
                        BroadcastController.this.func_152820_d(var4.toString());
                    }
                } else {
                    String var3 = ErrorCode.getString(p_getStreamInfoCallback_1_);
                    BroadcastController.this.func_152832_e(String.format("StreamInfoDoneCallback got failure: %s", new Object[]{var3}));
                }
            }

            public void getArchivingStateCallback(ErrorCode p_getArchivingStateCallback_1_, ArchivingState p_getArchivingStateCallback_2_) {
                BroadcastController.this.field_152889_y = p_getArchivingStateCallback_2_;

                if (ErrorCode.failed(p_getArchivingStateCallback_1_)) {
                    ;
                }
            }

            public void runCommercialCallback(ErrorCode p_runCommercialCallback_1_) {
                if (ErrorCode.failed(p_runCommercialCallback_1_)) {
                    String var2 = ErrorCode.getString(p_runCommercialCallback_1_);
                    BroadcastController.this.func_152832_e(String.format("RunCommercialCallback got failure: %s", new Object[]{var2}));
                }
            }

            public void setStreamInfoCallback(ErrorCode p_setStreamInfoCallback_1_) {
                if (ErrorCode.failed(p_setStreamInfoCallback_1_)) {
                    String var2 = ErrorCode.getString(p_setStreamInfoCallback_1_);
                    BroadcastController.this.func_152832_e(String.format("SetStreamInfoCallback got failure: %s", new Object[]{var2}));
                }
            }

            public void getGameNameListCallback(ErrorCode p_getGameNameListCallback_1_, GameInfoList p_getGameNameListCallback_2_) {
                if (ErrorCode.failed(p_getGameNameListCallback_1_)) {
                    String var3 = ErrorCode.getString(p_getGameNameListCallback_1_);
                    BroadcastController.this.func_152820_d(String.format("GameNameListCallback got failure: %s", new Object[]{var3}));
                }

                try {
                    if (BroadcastController.this.field_152867_c != null) {
                        BroadcastController.this.field_152867_c.func_152898_a(p_getGameNameListCallback_1_, p_getGameNameListCallback_2_ == null ? new GameInfo[0] : p_getGameNameListCallback_2_.list);
                    }
                } catch (Exception var4) {
                    BroadcastController.this.func_152820_d(var4.toString());
                }
            }

            public void bufferUnlockCallback(long p_bufferUnlockCallback_1_) {
                FrameBuffer var3 = FrameBuffer.lookupBuffer(p_bufferUnlockCallback_1_);
                BroadcastController.this.field_152875_k.add(var3);
            }

            public void startCallback(ErrorCode p_startCallback_1_) {
                if (ErrorCode.succeeded(p_startCallback_1_)) {
                    try {
                        if (BroadcastController.this.field_152867_c != null) {
                            BroadcastController.this.field_152867_c.func_152899_b();
                        }
                    } catch (Exception var4) {
                        BroadcastController.this.func_152820_d(var4.toString());
                    }

                    BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.Broadcasting);
                } else {
                    BroadcastController.this.field_152881_q = null;
                    BroadcastController.this.field_152882_r = null;
                    BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.ReadyToBroadcast);

                    try {
                        if (BroadcastController.this.field_152867_c != null) {
                            BroadcastController.this.field_152867_c.func_152892_c(p_startCallback_1_);
                        }
                    } catch (Exception var3) {
                        BroadcastController.this.func_152820_d(var3.toString());
                    }

                    String var2 = ErrorCode.getString(p_startCallback_1_);
                    BroadcastController.this.func_152820_d(String.format("startCallback got failure: %s", new Object[]{var2}));
                }
            }

            public void stopCallback(ErrorCode p_stopCallback_1_) {
                if (ErrorCode.succeeded(p_stopCallback_1_)) {
                    BroadcastController.this.field_152881_q = null;
                    BroadcastController.this.field_152882_r = null;
                    BroadcastController.this.func_152831_M();

                    try {
                        if (BroadcastController.this.field_152867_c != null) {
                            BroadcastController.this.field_152867_c.func_152901_c();
                        }
                    } catch (Exception var3) {
                        BroadcastController.this.func_152820_d(var3.toString());
                    }

                    if (BroadcastController.this.field_152877_m) {
                        BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.ReadyToBroadcast);
                    } else {
                        BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.Initialized);
                    }
                } else {
                    BroadcastController.this.func_152827_a(BroadcastController.BroadcastState.ReadyToBroadcast);
                    String var2 = ErrorCode.getString(p_stopCallback_1_);
                    BroadcastController.this.func_152820_d(String.format("stopCallback got failure: %s", new Object[]{var2}));
                }
            }

            public void sendActionMetaDataCallback(ErrorCode p_sendActionMetaDataCallback_1_) {
                if (ErrorCode.failed(p_sendActionMetaDataCallback_1_)) {
                    String var2 = ErrorCode.getString(p_sendActionMetaDataCallback_1_);
                    BroadcastController.this.func_152820_d(String.format("sendActionMetaDataCallback got failure: %s", new Object[]{var2}));
                }
            }

            public void sendStartSpanMetaDataCallback(ErrorCode p_sendStartSpanMetaDataCallback_1_) {
                if (ErrorCode.failed(p_sendStartSpanMetaDataCallback_1_)) {
                    String var2 = ErrorCode.getString(p_sendStartSpanMetaDataCallback_1_);
                    BroadcastController.this.func_152820_d(String.format("sendStartSpanMetaDataCallback got failure: %s", new Object[]{var2}));
                }
            }

            public void sendEndSpanMetaDataCallback(ErrorCode p_sendEndSpanMetaDataCallback_1_) {
                if (ErrorCode.failed(p_sendEndSpanMetaDataCallback_1_)) {
                    String var2 = ErrorCode.getString(p_sendEndSpanMetaDataCallback_1_);
                    BroadcastController.this.func_152820_d(String.format("sendEndSpanMetaDataCallback got failure: %s", new Object[]{var2}));
                }
            }
        };
        this.field_177949_C = new IStatCallbacks() {
            private static final String __OBFID = "CL_00002374";

            public void statCallback(StatType p_statCallback_1_, long p_statCallback_2_) {
            }
        };
        this.field_152872_h = Core.getInstance();

        if (Core.getInstance() == null) {
            this.field_152872_h = new Core(new StandardCoreAPI());
        }

        this.field_152873_i = new Stream(new DesktopStreamAPI());
    }

    protected PixelFormat func_152826_z() {
        return PixelFormat.TTV_PF_RGBA;
    }

    public boolean func_152817_A() {
        if (this.field_152876_l) {
            return false;
        } else {
            this.field_152873_i.setStreamCallbacks(this.field_177948_B);
            ErrorCode var1 = this.field_152872_h.initialize(this.field_152868_d, System.getProperty("java.library.path"));

            if (!this.func_152853_a(var1)) {
                this.field_152873_i.setStreamCallbacks((IStreamCallbacks) null);
                this.field_152864_E = var1;
                return false;
            } else {
                var1 = this.field_152872_h.setTraceLevel(MessageLevel.TTV_ML_ERROR);

                if (!this.func_152853_a(var1)) {
                    this.field_152873_i.setStreamCallbacks((IStreamCallbacks) null);
                    this.field_152872_h.shutdown();
                    this.field_152864_E = var1;
                    return false;
                } else if (ErrorCode.succeeded(var1)) {
                    this.field_152876_l = true;
                    this.func_152827_a(BroadcastController.BroadcastState.Initialized);
                    return true;
                } else {
                    this.field_152864_E = var1;
                    this.field_152872_h.shutdown();
                    return false;
                }
            }
        }
    }

    public boolean func_152851_B() {
        if (!this.field_152876_l) {
            return true;
        } else if (this.isIngestTesting()) {
            return false;
        } else {
            this.field_152878_n = true;
            this.func_152845_C();
            this.field_152873_i.setStreamCallbacks((IStreamCallbacks) null);
            this.field_152873_i.setStatCallbacks((IStatCallbacks) null);
            ErrorCode var1 = this.field_152872_h.shutdown();
            this.func_152853_a(var1);
            this.field_152876_l = false;
            this.field_152878_n = false;
            this.func_152827_a(BroadcastController.BroadcastState.Uninitialized);
            return true;
        }
    }

    public void statCallback() {
        if (this.broadcastState != BroadcastController.BroadcastState.Uninitialized) {
            if (this.field_152860_A != null) {
                this.field_152860_A.func_153039_l();
            }

            for (; this.field_152860_A != null; this.func_152821_H()) {
                try {
                    Thread.sleep(200L);
                } catch (Exception var2) {
                    this.func_152820_d(var2.toString());
                }
            }

            this.func_152851_B();
        }
    }

    public boolean func_152818_a(String p_152818_1_, AuthToken p_152818_2_) {
        if (this.isIngestTesting()) {
            return false;
        } else {
            this.func_152845_C();

            if (p_152818_1_ != null && !p_152818_1_.isEmpty()) {
                if (p_152818_2_ != null && p_152818_2_.data != null && !p_152818_2_.data.isEmpty()) {
                    this.field_152880_p = p_152818_1_;
                    this.field_152885_u = p_152818_2_;

                    if (this.func_152858_b()) {
                        this.func_152827_a(BroadcastController.BroadcastState.Authenticated);
                    }

                    return true;
                } else {
                    this.func_152820_d("Auth token must be valid");
                    return false;
                }
            } else {
                this.func_152820_d("Username must be valid");
                return false;
            }
        }
    }

    public boolean func_152845_C() {
        if (this.isIngestTesting()) {
            return false;
        } else {
            if (this.isBroadcasting()) {
                this.field_152873_i.stop(false);
            }

            this.field_152880_p = "";
            this.field_152885_u = new AuthToken();

            if (!this.field_152877_m) {
                return false;
            } else {
                this.field_152877_m = false;

                if (!this.field_152878_n) {
                    try {
                        if (this.field_152867_c != null) {
                            this.field_152867_c.func_152895_a();
                        }
                    } catch (Exception var2) {
                        this.func_152820_d(var2.toString());
                    }
                }

                this.func_152827_a(BroadcastController.BroadcastState.Initialized);
                return true;
            }
        }
    }

    public boolean func_152828_a(String p_152828_1_, String p_152828_2_, String p_152828_3_) {
        if (!this.field_152877_m) {
            return false;
        } else {
            if (p_152828_1_ == null || p_152828_1_.equals("")) {
                p_152828_1_ = this.field_152880_p;
            }

            if (p_152828_2_ == null) {
                p_152828_2_ = "";
            }

            if (p_152828_3_ == null) {
                p_152828_3_ = "";
            }

            StreamInfoForSetting var4 = new StreamInfoForSetting();
            var4.streamTitle = p_152828_3_;
            var4.gameName = p_152828_2_;
            ErrorCode var5 = this.field_152873_i.setStreamInfo(this.field_152885_u, p_152828_1_, var4);
            this.func_152853_a(var5);
            return ErrorCode.succeeded(var5);
        }
    }

    public boolean func_152830_D() {
        if (!this.isBroadcasting()) {
            return false;
        } else {
            ErrorCode var1 = this.field_152873_i.runCommercial(this.field_152885_u);
            this.func_152853_a(var1);
            return ErrorCode.succeeded(var1);
        }
    }

    public VideoParams func_152834_a(int p_152834_1_, int p_152834_2_, float p_152834_3_, float p_152834_4_) {
        int[] var5 = this.field_152873_i.getMaxResolution(p_152834_1_, p_152834_2_, p_152834_3_, p_152834_4_);
        VideoParams var6 = new VideoParams();
        var6.maxKbps = p_152834_1_;
        var6.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
        var6.pixelFormat = this.func_152826_z();
        var6.targetFps = p_152834_2_;
        var6.outputWidth = var5[0];
        var6.outputHeight = var5[1];
        var6.disableAdaptiveBitrate = false;
        var6.verticalFlip = false;
        return var6;
    }

    public boolean func_152836_a(VideoParams p_152836_1_) {
        if (p_152836_1_ != null && this.func_152857_n()) {
            this.field_152881_q = p_152836_1_.clone();
            this.field_152882_r = new AudioParams();
            this.field_152882_r.audioEnabled = this.field_152871_g && this.func_152848_y();
            this.field_152882_r.enableMicCapture = this.field_152882_r.audioEnabled;
            this.field_152882_r.enablePlaybackCapture = this.field_152882_r.audioEnabled;
            this.field_152882_r.enablePassthroughAudio = false;

            if (!this.func_152823_L()) {
                this.field_152881_q = null;
                this.field_152882_r = null;
                return false;
            } else {
                ErrorCode var2 = this.field_152873_i.start(p_152836_1_, this.field_152882_r, this.field_152884_t, StartFlags.None, true);

                if (ErrorCode.failed(var2)) {
                    this.func_152831_M();
                    String var3 = ErrorCode.getString(var2);
                    this.func_152820_d(String.format("Error while starting to broadcast: %s", new Object[]{var3}));
                    this.field_152881_q = null;
                    this.field_152882_r = null;
                    return false;
                } else {
                    this.func_152827_a(BroadcastController.BroadcastState.Starting);
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public boolean func_152819_E() {
        if (!this.isBroadcasting()) {
            return false;
        } else {
            ErrorCode var1 = this.field_152873_i.stop(true);

            if (ErrorCode.failed(var1)) {
                String var2 = ErrorCode.getString(var1);
                this.func_152820_d(String.format("Error while stopping the broadcast: %s", new Object[]{var2}));
                return false;
            } else {
                this.func_152827_a(BroadcastController.BroadcastState.Stopping);
                return ErrorCode.succeeded(var1);
            }
        }
    }

    public boolean func_152847_F() {
        if (!this.isBroadcasting()) {
            return false;
        } else {
            ErrorCode var1 = this.field_152873_i.pauseVideo();

            if (ErrorCode.failed(var1)) {
                this.func_152819_E();
                String var2 = ErrorCode.getString(var1);
                this.func_152820_d(String.format("Error pausing stream: %s\n", new Object[]{var2}));
            } else {
                this.func_152827_a(BroadcastController.BroadcastState.Paused);
            }

            return ErrorCode.succeeded(var1);
        }
    }

    public boolean func_152854_G() {
        if (!this.isBroadcastPaused()) {
            return false;
        } else {
            this.func_152827_a(BroadcastController.BroadcastState.Broadcasting);
            return true;
        }
    }

    public boolean func_152840_a(String p_152840_1_, long p_152840_2_, String p_152840_4_, String p_152840_5_) {
        ErrorCode var6 = this.field_152873_i.sendActionMetaData(this.field_152885_u, p_152840_1_, p_152840_2_, p_152840_4_, p_152840_5_);

        if (ErrorCode.failed(var6)) {
            String var7 = ErrorCode.getString(var6);
            this.func_152820_d(String.format("Error while sending meta data: %s\n", new Object[]{var7}));
            return false;
        } else {
            return true;
        }
    }

    public long func_177946_b(String p_177946_1_, long p_177946_2_, String p_177946_4_, String p_177946_5_) {
        long var6 = this.field_152873_i.sendStartSpanMetaData(this.field_152885_u, p_177946_1_, p_177946_2_, p_177946_4_, p_177946_5_);

        if (var6 == -1L) {
            this.func_152820_d(String.format("Error in SendStartSpanMetaData\n", new Object[0]));
        }

        return var6;
    }

    public boolean func_177947_a(String p_177947_1_, long p_177947_2_, long p_177947_4_, String p_177947_6_, String p_177947_7_) {
        if (p_177947_4_ == -1L) {
            this.func_152820_d(String.format("Invalid sequence id: %d\n", new Object[]{Long.valueOf(p_177947_4_)}));
            return false;
        } else {
            ErrorCode var8 = this.field_152873_i.sendEndSpanMetaData(this.field_152885_u, p_177947_1_, p_177947_2_, p_177947_4_, p_177947_6_, p_177947_7_);

            if (ErrorCode.failed(var8)) {
                String var9 = ErrorCode.getString(var8);
                this.func_152820_d(String.format("Error in SendStopSpanMetaData: %s\n", new Object[]{var9}));
                return false;
            } else {
                return true;
            }
        }
    }

    protected void func_152827_a(BroadcastController.BroadcastState p_152827_1_) {
        if (p_152827_1_ != this.broadcastState) {
            this.broadcastState = p_152827_1_;

            try {
                if (this.field_152867_c != null) {
                    this.field_152867_c.func_152891_a(p_152827_1_);
                }
            } catch (Exception var3) {
                this.func_152820_d(var3.toString());
            }
        }
    }

    public void func_152821_H() {
        if (this.field_152873_i != null && this.field_152876_l) {
            ErrorCode var1 = this.field_152873_i.pollTasks();
            this.func_152853_a(var1);

            if (this.isIngestTesting()) {
                this.field_152860_A.func_153041_j();

                if (this.field_152860_A.func_153032_e()) {
                    this.field_152860_A = null;
                    this.func_152827_a(BroadcastController.BroadcastState.ReadyToBroadcast);
                }
            }

            String var2;

            switch (BroadcastController.SwitchBroadcastState.field_177773_a[this.broadcastState.ordinal()]) {
                case 1:
                    this.func_152827_a(BroadcastController.BroadcastState.LoggingIn);
                    var1 = this.field_152873_i.login(this.field_152885_u);

                    if (ErrorCode.failed(var1)) {
                        var2 = ErrorCode.getString(var1);
                        this.func_152820_d(String.format("Error in TTV_Login: %s\n", new Object[]{var2}));
                    }

                    break;

                case 2:
                    this.func_152827_a(BroadcastController.BroadcastState.FindingIngestServer);
                    var1 = this.field_152873_i.getIngestServers(this.field_152885_u);

                    if (ErrorCode.failed(var1)) {
                        this.func_152827_a(BroadcastController.BroadcastState.LoggedIn);
                        var2 = ErrorCode.getString(var1);
                        this.func_152820_d(String.format("Error in TTV_GetIngestServers: %s\n", new Object[]{var2}));
                    }

                    break;

                case 3:
                    this.func_152827_a(BroadcastController.BroadcastState.ReadyToBroadcast);
                    var1 = this.field_152873_i.getUserInfo(this.field_152885_u);

                    if (ErrorCode.failed(var1)) {
                        var2 = ErrorCode.getString(var1);
                        this.func_152820_d(String.format("Error in TTV_GetUserInfo: %s\n", new Object[]{var2}));
                    }

                    this.func_152835_I();
                    var1 = this.field_152873_i.getArchivingState(this.field_152885_u);

                    if (ErrorCode.failed(var1)) {
                        var2 = ErrorCode.getString(var1);
                        this.func_152820_d(String.format("Error in TTV_GetArchivingState: %s\n", new Object[]{var2}));
                    }

                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                default:
                    break;

                case 11:
                case 12:
                    this.func_152835_I();
            }
        }
    }

    protected void func_152835_I() {
        long var1 = System.nanoTime();
        long var3 = (var1 - this.field_152890_z) / 1000000000L;

        if (var3 >= 30L) {
            this.field_152890_z = var1;
            ErrorCode var5 = this.field_152873_i.getStreamInfo(this.field_152885_u, this.field_152880_p);

            if (ErrorCode.failed(var5)) {
                String var6 = ErrorCode.getString(var5);
                this.func_152820_d(String.format("Error in TTV_GetStreamInfo: %s", new Object[]{var6}));
            }
        }
    }

    public IngestServerTester func_152838_J() {
        if (this.func_152857_n() && this.field_152883_s != null) {
            if (this.isIngestTesting()) {
                return null;
            } else {
                this.field_152860_A = new IngestServerTester(this.field_152873_i, this.field_152883_s);
                this.field_152860_A.func_176004_j();
                this.func_152827_a(BroadcastController.BroadcastState.IngestTesting);
                return this.field_152860_A;
            }
        } else {
            return null;
        }
    }

    protected boolean func_152823_L() {
        for (int var1 = 0; var1 < 3; ++var1) {
            FrameBuffer var2 = this.field_152873_i.allocateFrameBuffer(this.field_152881_q.outputWidth * this.field_152881_q.outputHeight * 4);

            if (!var2.getIsValid()) {
                this.func_152820_d(String.format("Error while allocating frame buffer", new Object[0]));
                return false;
            }

            this.field_152874_j.add(var2);
            this.field_152875_k.add(var2);
        }

        return true;
    }

    protected void func_152831_M() {
        for (int var1 = 0; var1 < this.field_152874_j.size(); ++var1) {
            FrameBuffer var2 = (FrameBuffer) this.field_152874_j.get(var1);
            var2.free();
        }

        this.field_152875_k.clear();
        this.field_152874_j.clear();
    }

    public FrameBuffer func_152822_N() {
        if (this.field_152875_k.size() == 0) {
            this.func_152820_d(String.format("Out of free buffers, this should never happen", new Object[0]));
            return null;
        } else {
            FrameBuffer var1 = (FrameBuffer) this.field_152875_k.get(this.field_152875_k.size() - 1);
            this.field_152875_k.remove(this.field_152875_k.size() - 1);
            return var1;
        }
    }

    public void func_152846_a(FrameBuffer p_152846_1_) {
        try {
            this.field_152873_i.captureFrameBuffer_ReadPixels(p_152846_1_);
        } catch (Throwable var5) {
            CrashReport var3 = CrashReport.makeCrashReport(var5, "Trying to submit a frame to Twitch");
            CrashReportCategory var4 = var3.makeCategory("Broadcast State");
            var4.addCrashSection("Last reported errors", Arrays.toString(field_152862_C.func_152756_c()));
            var4.addCrashSection("Buffer", p_152846_1_);
            var4.addCrashSection("Free buffer count", Integer.valueOf(this.field_152875_k.size()));
            var4.addCrashSection("Capture buffer count", Integer.valueOf(this.field_152874_j.size()));
            throw new ReportedException(var3);
        }
    }

    public ErrorCode func_152859_b(FrameBuffer p_152859_1_) {
        if (this.isBroadcastPaused()) {
            this.func_152854_G();
        } else if (!this.isBroadcasting()) {
            return ErrorCode.TTV_EC_STREAM_NOT_STARTED;
        }

        ErrorCode var2 = this.field_152873_i.submitVideoFrame(p_152859_1_);

        if (var2 != ErrorCode.TTV_EC_SUCCESS) {
            String var3 = ErrorCode.getString(var2);

            if (ErrorCode.succeeded(var2)) {
                this.func_152832_e(String.format("Warning in SubmitTexturePointer: %s\n", new Object[]{var3}));
            } else {
                this.func_152820_d(String.format("Error in SubmitTexturePointer: %s\n", new Object[]{var3}));
                this.func_152819_E();
            }

            if (this.field_152867_c != null) {
                this.field_152867_c.func_152893_b(var2);
            }
        }

        return var2;
    }

    protected boolean func_152853_a(ErrorCode p_152853_1_) {
        if (ErrorCode.failed(p_152853_1_)) {
            this.func_152820_d(ErrorCode.getString(p_152853_1_));
            return false;
        } else {
            return true;
        }
    }

    protected void func_152820_d(String p_152820_1_) {
        this.field_152863_D = p_152820_1_;
        field_152862_C.func_152757_a("<Error> " + p_152820_1_);
        logger.error(TwitchStream.field_152949_a, "[Broadcast controller] {}", new Object[]{p_152820_1_});
    }

    protected void func_152832_e(String p_152832_1_) {
        field_152862_C.func_152757_a("<Warning> " + p_152832_1_);
        logger.warn(TwitchStream.field_152949_a, "[Broadcast controller] {}", new Object[]{p_152832_1_});
    }

    public interface BroadcastListener {
        void func_152900_a(ErrorCode p_152900_1_, AuthToken p_152900_2_);

        void func_152897_a(ErrorCode p_152897_1_);

        void func_152898_a(ErrorCode p_152898_1_, GameInfo[] p_152898_2_);

        void func_152891_a(BroadcastController.BroadcastState p_152891_1_);

        void func_152895_a();

        void func_152894_a(StreamInfo p_152894_1_);

        void func_152896_a(IngestList p_152896_1_);

        void func_152893_b(ErrorCode p_152893_1_);

        void func_152899_b();

        void func_152901_c();

        void func_152892_c(ErrorCode p_152892_1_);
    }

    public static enum BroadcastState {
        Uninitialized("Uninitialized", 0),
        Initialized("Initialized", 1),
        Authenticating("Authenticating", 2),
        Authenticated("Authenticated", 3),
        LoggingIn("LoggingIn", 4),
        LoggedIn("LoggedIn", 5),
        FindingIngestServer("FindingIngestServer", 6),
        ReceivedIngestServers("ReceivedIngestServers", 7),
        ReadyToBroadcast("ReadyToBroadcast", 8),
        Starting("Starting", 9),
        Broadcasting("Broadcasting", 10),
        Stopping("Stopping", 11),
        Paused("Paused", 12),
        IngestTesting("IngestTesting", 13);

        private static final BroadcastController.BroadcastState[] $VALUES = new BroadcastController.BroadcastState[]{Uninitialized, Initialized, Authenticating, Authenticated, LoggingIn, LoggedIn, FindingIngestServer, ReceivedIngestServers, ReadyToBroadcast, Starting, Broadcasting, Stopping, Paused, IngestTesting};
        private static final String __OBFID = "CL_00001820";

        private BroadcastState(String p_i1025_1_, int p_i1025_2_) {
        }
    }

    static final class SwitchBroadcastState {
        static final int[] field_177773_a = new int[BroadcastController.BroadcastState.values().length];
        private static final String __OBFID = "CL_00001821";

        static {
            try {
                field_177773_a[BroadcastController.BroadcastState.Authenticated.ordinal()] = 1;
            } catch (NoSuchFieldError var12) {
                ;
            }

            try {
                field_177773_a[BroadcastController.BroadcastState.LoggedIn.ordinal()] = 2;
            } catch (NoSuchFieldError var11) {
                ;
            }

            try {
                field_177773_a[BroadcastController.BroadcastState.ReceivedIngestServers.ordinal()] = 3;
            } catch (NoSuchFieldError var10) {
                ;
            }

            try {
                field_177773_a[BroadcastController.BroadcastState.Starting.ordinal()] = 4;
            } catch (NoSuchFieldError var9) {
                ;
            }

            try {
                field_177773_a[BroadcastController.BroadcastState.Stopping.ordinal()] = 5;
            } catch (NoSuchFieldError var8) {
                ;
            }

            try {
                field_177773_a[BroadcastController.BroadcastState.FindingIngestServer.ordinal()] = 6;
            } catch (NoSuchFieldError var7) {
                ;
            }

            try {
                field_177773_a[BroadcastController.BroadcastState.Authenticating.ordinal()] = 7;
            } catch (NoSuchFieldError var6) {
                ;
            }

            try {
                field_177773_a[BroadcastController.BroadcastState.Initialized.ordinal()] = 8;
            } catch (NoSuchFieldError var5) {
                ;
            }

            try {
                field_177773_a[BroadcastController.BroadcastState.Uninitialized.ordinal()] = 9;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                field_177773_a[BroadcastController.BroadcastState.IngestTesting.ordinal()] = 10;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                field_177773_a[BroadcastController.BroadcastState.Paused.ordinal()] = 11;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_177773_a[BroadcastController.BroadcastState.Broadcasting.ordinal()] = 12;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
