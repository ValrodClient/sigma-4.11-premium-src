package net.minecraft.client.gui;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;

public class ServerSelectionList extends GuiListExtended {
    private final GuiMultiplayer owner;
    private final List field_148198_l = Lists.newArrayList();
    private final List field_148199_m = Lists.newArrayList();
    private final GuiListExtended.IGuiListEntry lanScanEntry = new ServerListEntryLanScan();
    private int field_148197_o = -1;
    private static final String __OBFID = "CL_00000819";

    public ServerSelectionList(GuiMultiplayer p_i45049_1_, Minecraft mcIn, int p_i45049_3_, int p_i45049_4_, int p_i45049_5_, int p_i45049_6_, int p_i45049_7_) {

        super(mcIn, p_i45049_3_, p_i45049_4_, p_i45049_5_, p_i45049_6_, p_i45049_7_);
        this.owner = p_i45049_1_;
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_) {
        if (p_148180_1_ < this.field_148198_l.size()) {
            return (GuiListExtended.IGuiListEntry) this.field_148198_l.get(p_148180_1_);
        } else {
            p_148180_1_ -= this.field_148198_l.size();

            if (p_148180_1_ == 0) {
                return this.lanScanEntry;
            } else {
                --p_148180_1_;
                return (GuiListExtended.IGuiListEntry) this.field_148199_m.get(p_148180_1_);
            }
        }
    }

    protected int getSize() {
        return this.field_148198_l.size() + 1 + this.field_148199_m.size();
    }

    public void func_148192_c(int p_148192_1_) {
        this.field_148197_o = p_148192_1_;
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int slotIndex) {
        return slotIndex == this.field_148197_o;
    }

    public int func_148193_k() {
        return this.field_148197_o;
    }

    public void func_148195_a(ServerList p_148195_1_) {
        this.field_148198_l.clear();

        for (int var2 = 0; var2 < p_148195_1_.countServers(); ++var2) {
            this.field_148198_l.add(new ServerListEntryNormal(this.owner, p_148195_1_.getServerData(var2)));
        }
    }

    public void func_148194_a(List p_148194_1_) {
        this.field_148199_m.clear();
        Iterator var2 = p_148194_1_.iterator();

        while (var2.hasNext()) {
            LanServerDetector.LanServer var3 = (LanServerDetector.LanServer) var2.next();
            this.field_148199_m.add(new ServerListEntryLanDetected(this.owner, var3));
        }
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 30;
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth() {
        return super.getListWidth() + 85;
    }
}
