package gregtech.common.blocks;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GT_Item_Storage extends ItemBlock {
    public GT_Item_Storage(Block par1) {
        super(par1);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTech_API.TAB_GREGTECH_MATERIALS);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
    	String aName = super.getItemStackDisplayName(aStack);
    	if (this.block instanceof GT_Block_Metal) {
    		int aDamage = getDamage(aStack);
    		if (aDamage >= 0 && aDamage < ((GT_Block_Metal) this.block).mMats.length){
    			Materials aMaterial = ((GT_Block_Metal) this.block).mMats[aDamage];
    			if (aMaterial != null)
    				return aMaterial.getLocalizedNameForItem(aName);
    		}
    	}
    	return aName;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return this.block.getUnlocalizedName() + "." + getDamage(aStack);
    }

    @Override
    public int getMetadata(int aMeta) {
        return aMeta;
    }

}