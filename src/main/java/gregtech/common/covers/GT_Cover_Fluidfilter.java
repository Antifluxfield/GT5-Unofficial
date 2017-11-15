package gregtech.common.covers;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.*;

public class GT_Cover_Fluidfilter extends GT_CoverBehavior {

    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return aCoverVariable;
    }

    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int aFilterMode = aCoverVariable & 7;
        aCoverVariable ^= aFilterMode;
        aFilterMode = (aFilterMode + (aPlayer.isSneaking()? -1 : 1) + 4) % 4;
        switch(aFilterMode) {
            case 0: GT_Utility.sendChatToPlayer(aPlayer, trans(83, "Input whitelisted, ") + trans(85, "Deny Output")); break;
            case 1: GT_Utility.sendChatToPlayer(aPlayer, trans(84, "Input blacklisted, ") + trans(85, "Deny Output")); break;
            case 2: GT_Utility.sendChatToPlayer(aPlayer, trans(83, "Input whitelisted, ") + trans(86, "Allow Output")); break;
            case 3: GT_Utility.sendChatToPlayer(aPlayer, trans(84, "Input blacklisted, ") + trans(86, "Allow Output")); break;
        }
        aCoverVariable|=aFilterMode;
        return aCoverVariable;
    }

    public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        //System.out.println("rightclick");
        if (((aX > 0.375D) && (aX < 0.625D)) || ((aSide > 3) && (((aY > 0.375D) && (aY < 0.625D)) || ((aSide < 2) && (((aZ > 0.375D) && (aZ < 0.625D)) || (aSide == 2) || (aSide == 3)))))) {
            ItemStack tStack = aPlayer.inventory.getCurrentItem();
            if (tStack != null) {
                FluidStack tFluid = FluidContainerRegistry.getFluidForFilledItem(tStack);
                int aFluidID;
                if (tFluid != null) {
                    //System.out.println(tFluid.getLocalizedName()+" "+tFluid.getFluidID());
                    aFluidID = FluidRegistry.getFluidID(tFluid.getFluid());
                    aCoverVariable = (aCoverVariable & 0x7) | (aFluidID << 3);
                    aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
                    FluidStack sFluid = new FluidStack(FluidRegistry.getFluid(aFluidID), 1000);
                    GT_Utility.sendChatToPlayer(aPlayer, trans(76, "Filter Fluid: ") + sFluid.getLocalizedName());
                } else if (tStack.getItem() instanceof IFluidContainerItem) {
                    IFluidContainerItem tContainer = (IFluidContainerItem) tStack.getItem();
                    if (tContainer.getFluid(tStack) != null) {
                        aFluidID = FluidRegistry.getFluidID(tContainer.getFluid(tStack).getFluid());
                        aCoverVariable = (aCoverVariable & 0x7) | (aFluidID << 3);
                        aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
                        //System.out.println("fluidcontainer " + aCoverVariable);
                        FluidStack sFluid = new FluidStack(FluidRegistry.getFluid(aFluidID), 1000);
                        GT_Utility.sendChatToPlayer(aPlayer, trans(76, "Filter Fluid: ") + sFluid.getLocalizedName());
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        if(aFluid==null){return true;}
        int aFilterMode = aCoverVariable & 7;
        int aFilterFluid = aCoverVariable >>> 3;
        return FluidRegistry.getFluidID(aFluid) == aFilterFluid ? (aFilterMode == 0 || aFilterMode == 2 ? true : false) : (aFilterMode == 1 || aFilterMode == 3 ? true : false);
    }
    
    @Override
    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        int aFilterMode = aCoverVariable & 7;
        return  aFilterMode == 0 || aFilterMode == 1 ? false : true;
    }
    
    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 0;
    }
}
