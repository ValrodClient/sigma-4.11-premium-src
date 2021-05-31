package net.minecraft.profiler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.optifine.Config;
import net.minecraft.optifine.Lagometer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profiler {
    private static final Logger logger = LogManager.getLogger();

    /**
     * List of parent sections
     */
    private final List sectionList = Lists.newArrayList();

    /**
     * List of timestamps (System.nanoTime)
     */
    private final List timestampList = Lists.newArrayList();

    /**
     * Flag profiling enabled
     */
    public boolean profilingEnabled;

    /**
     * Current profiling section
     */
    private String profilingSection = "";

    /**
     * Profiling map
     */
    private final Map profilingMap = Maps.newHashMap();
    private static final String __OBFID = "CL_00001497";
    public boolean profilerGlobalEnabled = true;
    private boolean profilerLocalEnabled;
    private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
    private static final String TICK = "tick";
    private static final String PRE_RENDER_ERRORS = "preRenderErrors";
    private static final int HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
    private static final int HASH_TICK = "tick".hashCode();
    private static final int HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();

    public Profiler() {
        profilerLocalEnabled = profilerGlobalEnabled;
    }

    /**
     * Clear profiling.
     */
    public void clearProfiling() {
        profilingMap.clear();
        profilingSection = "";
        sectionList.clear();
        profilerLocalEnabled = profilerGlobalEnabled;
    }

    /**
     * Start section
     */
    public void startSection(String name) {
        if (Lagometer.isActive()) {
            int hashName = name.hashCode();

            if (hashName == Profiler.HASH_SCHEDULED_EXECUTABLES && name.equals("scheduledExecutables")) {
                Lagometer.timerScheduledExecutables.start();
            } else if (hashName == Profiler.HASH_TICK && name.equals("tick") && Config.isMinecraftThread()) {
                Lagometer.timerScheduledExecutables.end();
                Lagometer.timerTick.start();
            } else if (hashName == Profiler.HASH_PRE_RENDER_ERRORS && name.equals("preRenderErrors")) {
                Lagometer.timerTick.end();
            }
        }

        if (profilerLocalEnabled) {
            if (profilingEnabled) {
                if (profilingSection.length() > 0) {
                    profilingSection = profilingSection + ".";
                }

                profilingSection = profilingSection + name;
                sectionList.add(profilingSection);
                timestampList.add(Long.valueOf(System.nanoTime()));
            }
        }
    }

    /**
     * End section
     */
    public void endSection() {
        if (profilerLocalEnabled) {
            if (profilingEnabled) {
                long var1 = System.nanoTime();
                long var3 = ((Long) timestampList.remove(timestampList.size() - 1)).longValue();
                sectionList.remove(sectionList.size() - 1);
                long var5 = var1 - var3;

                if (profilingMap.containsKey(profilingSection)) {
                    profilingMap.put(profilingSection, Long.valueOf(((Long) profilingMap.get(profilingSection)).longValue() + var5));
                } else {
                    profilingMap.put(profilingSection, Long.valueOf(var5));
                }

                if (var5 > 100000000L) {
                    Profiler.logger.warn("Something\'s taking too long! \'" + profilingSection + "\' took aprox " + var5 / 1000000.0D + " ms");
                }

                profilingSection = !sectionList.isEmpty() ? (String) sectionList.get(sectionList.size() - 1) : "";
            }
        }
    }

    /**
     * Get profiling data
     */
    public List getProfilingData(String p_76321_1_) {
        profilerLocalEnabled = profilerGlobalEnabled;

        if (!profilerLocalEnabled) {
            return new ArrayList(Arrays.asList(new Profiler.Result[]{new Profiler.Result("root", 0.0D, 0.0D)}));
        } else if (!profilingEnabled) {
            return null;
        } else {
            long var3 = profilingMap.containsKey("root") ? ((Long) profilingMap.get("root")).longValue() : 0L;
            long var5 = profilingMap.containsKey(p_76321_1_) ? ((Long) profilingMap.get(p_76321_1_)).longValue() : -1L;
            ArrayList var7 = Lists.newArrayList();

            if (p_76321_1_.length() > 0) {
                p_76321_1_ = p_76321_1_ + ".";
            }

            long var8 = 0L;
            Iterator var10 = profilingMap.keySet().iterator();

            while (var10.hasNext()) {
                String var20 = (String) var10.next();

                if (var20.length() > p_76321_1_.length() && var20.startsWith(p_76321_1_) && var20.indexOf(".", p_76321_1_.length() + 1) < 0) {
                    var8 += ((Long) profilingMap.get(var20)).longValue();
                }
            }

            float var201 = var8;

            if (var8 < var5) {
                var8 = var5;
            }

            if (var3 < var8) {
                var3 = var8;
            }

            Iterator var21 = profilingMap.keySet().iterator();
            String var12;

            while (var21.hasNext()) {
                var12 = (String) var21.next();

                if (var12.length() > p_76321_1_.length() && var12.startsWith(p_76321_1_) && var12.indexOf(".", p_76321_1_.length() + 1) < 0) {
                    long var13 = ((Long) profilingMap.get(var12)).longValue();
                    double var15 = var13 * 100.0D / var8;
                    double var17 = var13 * 100.0D / var3;
                    String var19 = var12.substring(p_76321_1_.length());
                    var7.add(new Profiler.Result(var19, var15, var17));
                }
            }

            var21 = profilingMap.keySet().iterator();

            while (var21.hasNext()) {
                var12 = (String) var21.next();
                profilingMap.put(var12, Long.valueOf(((Long) profilingMap.get(var12)).longValue() * 950L / 1000L));
            }

            if (var8 > var201) {
                var7.add(new Profiler.Result("unspecified", (var8 - var201) * 100.0D / var8, (var8 - var201) * 100.0D / var3));
            }

            Collections.sort(var7);
            var7.add(0, new Profiler.Result(p_76321_1_, 100.0D, var8 * 100.0D / var3));
            return var7;
        }
    }

    /**
     * End current section and start a new section
     */
    public void endStartSection(String name) {
        if (profilerLocalEnabled) {
            endSection();
            startSection(name);
        }
    }

    public String getNameOfLastSection() {
        return sectionList.size() == 0 ? "[UNKNOWN]" : (String) sectionList.get(sectionList.size() - 1);
    }

    public static final class Result implements Comparable {
        public double field_76332_a;
        public double field_76330_b;
        public String field_76331_c;
        private static final String __OBFID = "CL_00001498";

        public Result(String p_i1554_1_, double p_i1554_2_, double p_i1554_4_) {
            field_76331_c = p_i1554_1_;
            field_76332_a = p_i1554_2_;
            field_76330_b = p_i1554_4_;
        }

        public int compareTo(Profiler.Result p_compareTo_1_) {
            return p_compareTo_1_.field_76332_a < field_76332_a ? -1 : (p_compareTo_1_.field_76332_a > field_76332_a ? 1 : p_compareTo_1_.field_76331_c.compareTo(field_76331_c));
        }

        public int func_76329_a() {
            return (field_76331_c.hashCode() & 11184810) + 4473924;
        }

        @Override
        public int compareTo(Object p_compareTo_1_) {
            return this.compareTo((Profiler.Result) p_compareTo_1_);
        }
    }
}
