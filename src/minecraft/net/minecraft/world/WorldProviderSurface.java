package net.minecraft.world;

public class WorldProviderSurface extends WorldProvider {
    private static final String __OBFID = "CL_00000388";

    /**
     * Returns the dimension's name, e.g. "The End", "Nether", or "Overworld".
     */
    @Override
    public String getDimensionName() {
        return "Overworld";
    }

    @Override
    public String getInternalNameSuffix() {
        return "";
    }
}
