package net.minecraft.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;

public class ItemCoal extends Item {
    private static final String __OBFID = "CL_00000002";

    public ItemCoal() {
        setHasSubtypes(true);
        setMaxDamage(0);
        setCreativeTab(CreativeTabs.tabMaterials);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an
     * ItemStack so different stacks can have different names based on their
     * damage or NBT.
     */
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return stack.getMetadata() == 1 ? "item.charcoal" : "item.coal";
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye
     * returns 16 items)
     *
     * @param subItems The List of sub-items. This is a List of ItemStacks.
     */
    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        subItems.add(new ItemStack(itemIn, 1, 0));
        subItems.add(new ItemStack(itemIn, 1, 1));
    }
}
