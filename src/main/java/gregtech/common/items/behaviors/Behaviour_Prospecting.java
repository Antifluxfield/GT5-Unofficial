package gregtech.common.items.behaviors;

import gregtech.api.GregTech_API;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.List;

public class Behaviour_Prospecting extends Behaviour_None {

    private final int mVanillaCosts;
    private final int mEUCosts;
    private final String mTooltip = GT_LanguageManager.addStringLocalization("gt.behaviour.prospecting", "Usable for Prospecting");

    public Behaviour_Prospecting(int aVanillaCosts, int aEUCosts) {
        this.mVanillaCosts = aVanillaCosts;
        this.mEUCosts = aEUCosts;
    }

    @Override
    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, BlockPos blockPos, EnumFacing aSide, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (aWorld.isRemote || !aWorld.isAirBlock(blockPos)) {
            return false;
        }
        IBlockState blockState = aWorld.getBlockState(blockPos);
        Block block = blockState.getBlock();
        int blockMeta = block.getMetaFromState(blockState);

        ItemData tAssotiation = GT_OreDictUnificator.getAssociation(new ItemStack(block, 1, blockMeta));
        if ((tAssotiation != null) && (tAssotiation.mPrefix.toString().startsWith("ore"))) {
            GT_Utility.sendChatToPlayer(aPlayer, String.format(trans(97, "This is %s Ore."), tAssotiation.mMaterial.mMaterial.mLocalizedName));
            GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(1), 1.0F, -1.0F, blockPos);
            return true;
        }
        if ((block.isReplaceableOreGen(blockState, aWorld, blockPos, state ->
                state.getBlock() == Blocks.STONE ||
                state.getBlock() == Blocks.END_STONE ||
                state.getBlock() == Blocks.NETHERRACK))) {
            if (GT_ModHandler.damageOrDechargeItem(aStack, this.mVanillaCosts, this.mEUCosts, aPlayer)) {
                GT_Utility.sendSoundToPlayers(aWorld, GregTech_API.sSoundList.get(1), 1.0F, -1.0F, blockPos);
                int tMetaID = 0;
                int tQuality = (aItem instanceof GT_MetaGenerated_Tool) ? aItem.getHarvestLevel(aStack, "") : 0;
                int scanRadius = 6 + tQuality;
                for(int x = -scanRadius; x < scanRadius; x++) {
                    boolean breakIt = false;
                    if(breakIt) break;
                    for(int z = -scanRadius; z < scanRadius; z++) {
                        if(breakIt) break;
                        for(int y = 0; y < scanRadius; y++) {
                            if(breakIt) break;
                            BlockPos scanPos = blockPos.add(x, -(y + 1), z);
                            IBlockState scanState = aWorld.getBlockState(scanPos);
                            if(scanState.getBlock() == Blocks.LAVA || scanState.getBlock() == Blocks.FLOWING_LAVA) {
                                GT_Utility.sendChatToPlayer(aPlayer, trans(98, "There is Lava behind this Rock"));
                                breakIt = true;
                            } else if(scanState.getBlock() == Blocks.WATER || scanState.getBlock() == Blocks.FLOWING_WATER) {
                                GT_Utility.sendChatToPlayer(aPlayer, trans(99, "There is Water behind this Rock"));
                                breakIt = true;
                            } else if(scanState.getBlock() instanceof IFluidBlock) {
                                GT_Utility.sendChatToPlayer(aPlayer, trans(100, "There is Fluid behind this Rock"));
                                breakIt = true;
                            } else if(scanState.getBlock() == Blocks.AIR ||
                                    scanState.getBlock() == Blocks.MONSTER_EGG ||
                                    !GT_Utility.hasBlockHitBox(aWorld, scanPos)) {
                                GT_Utility.sendChatToPlayer(aPlayer, trans(101, "There is an Air Pocket behind this Rock."));
                                breakIt = true;
                            } else if(scanState.getBlock() != block) {
                                GT_Utility.sendChatToPlayer(aPlayer, trans(102, "Material is changing behind this Rock."));
                                breakIt = true;
                            }
                        }
                    }
                }

                GT_Utility.sendChatToPlayer(aPlayer, trans(103, "No Ores found."));
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        return aList;
    }

}
