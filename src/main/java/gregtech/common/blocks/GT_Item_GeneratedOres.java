package gregtech.common.blocks;

import gregtech.api.enums.Materials;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class GT_Item_GeneratedOres extends ItemBlock {


    public GT_Item_GeneratedOres(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return ((GT_Block_GeneratedOres) block).mUnlocalizedName + "." + stack.getItemDamage();
    }

    @Override
    public int getMetadata(int damage) {
        return Math.max(0, Math.min(16, damage)); //checks to prevent outofbounds
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
    	return Materials.getLocalizedNameForItem(super.getItemStackDisplayName(aStack), getDamage(aStack) % 1000);
    }

}
