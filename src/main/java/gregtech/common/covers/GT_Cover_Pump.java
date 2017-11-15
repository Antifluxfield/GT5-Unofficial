package gregtech.common.covers;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class GT_Cover_Pump extends GT_CoverBehavior {

    public final int mTransferRate;

    public GT_Cover_Pump(int aTransferRate) {
        this.mTransferRate = aTransferRate;
    }

    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        EnumFacing aSideDirection = EnumFacing.VALUES[aSide];
        if ((aCoverVariable % 6 > 1) && ((aTileEntity instanceof IMachineProgress))) {
            if (((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4) {
                return aCoverVariable;
            }
        }
        if ((aTileEntity instanceof IFluidHandler)) {
            IFluidHandler tTank2 = aTileEntity.getITankContainerAtSide(aSide);
            if (tTank2 != null) {
                //aTileEntity.decreaseStoredEnergyUnits(GT_Utility.getTier(this.mTransferRate), true);
                IFluidHandler tTank1 = (IFluidHandler) aTileEntity;
                if (aCoverVariable % 2 == 0) {
                    FluidStack tLiquid = tTank1.drain(aSideDirection, this.mTransferRate, false);
                    if (tLiquid != null) {
                        tLiquid = tLiquid.copy();
                        tLiquid.amount = tTank2.fill(aSideDirection.getOpposite(), tLiquid, false);
                        if (tLiquid.amount > 0) {
                            if (((aCoverVariable % 2 == 0) || (aSide != 1)) && ((aCoverVariable % 2 != 0) || (aSide != 0)) && (aTileEntity.getUniversalEnergyCapacity() >= Math.min(1, tLiquid.amount / 10))) {
                                if (aTileEntity.isUniversalEnergyStored(Math.min(1, tLiquid.amount / 10))) {
                                    aTileEntity.decreaseStoredEnergyUnits(Math.min(1, tLiquid.amount / 10), true);
                                    tTank2.fill(aSideDirection.getOpposite(), tTank1.drain(aSideDirection, tLiquid.amount, true), true);
                                }
                            } else {
                                tTank2.fill(aSideDirection.getOpposite(), tTank1.drain(aSideDirection, tLiquid.amount, true), true);
                            }
                        }
                    }
                } else {
                    FluidStack tLiquid = tTank2.drain(aSideDirection.getOpposite(), this.mTransferRate, false);
                    if (tLiquid != null) {
                        tLiquid = tLiquid.copy();
                        tLiquid.amount = tTank1.fill(aSideDirection, tLiquid, false);
                        if (tLiquid.amount > 0) {
                            if (((aCoverVariable % 2 == 0) || (aSide != 1)) && ((aCoverVariable % 2 != 0) || (aSide != 0)) && (aTileEntity.getUniversalEnergyCapacity() >= Math.min(1, tLiquid.amount / 10))) {
                                if (aTileEntity.isUniversalEnergyStored(Math.min(1, tLiquid.amount / 10))) {
                                    aTileEntity.decreaseStoredEnergyUnits(Math.min(1, tLiquid.amount / 10), true);
                                    tTank1.fill(aSideDirection, tTank2.drain(aSideDirection.getOpposite(), tLiquid.amount, true), true);
                                }
                            } else {
                                tTank1.fill(aSideDirection, tTank2.drain(aSideDirection.getOpposite(), tLiquid.amount, true), true);
                            }
                        }
                    }
                }
            }
        }
        return aCoverVariable;
    }

    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 12;
        if (aCoverVariable < 0) {
            aCoverVariable = 11;
        }
        switch (aCoverVariable) {
        case 0: GT_Utility.sendChatToPlayer(aPlayer, trans(57, "Export")); break;
        case 1: GT_Utility.sendChatToPlayer(aPlayer, trans(58, "Import")); break;
        case 2: GT_Utility.sendChatToPlayer(aPlayer, trans(57, "Export") + trans(59, " (conditional)")); break;
        case 3: GT_Utility.sendChatToPlayer(aPlayer, trans(58, "Import") + trans(59, " (conditional)")); break;
        case 4: GT_Utility.sendChatToPlayer(aPlayer, trans(57, "Export") + trans(60, " (invert cond)")); break;
        case 5: GT_Utility.sendChatToPlayer(aPlayer, trans(58, "Import") + trans(60, " (invert cond)")); break;
        case 6: GT_Utility.sendChatToPlayer(aPlayer, trans(61, "Export allow Input")); break;
        case 7: GT_Utility.sendChatToPlayer(aPlayer, trans(62, "Import allow Output")); break;
        case 8: GT_Utility.sendChatToPlayer(aPlayer, trans(61, "Export allow Input") + trans(59, " (conditional)")); break;
        case 9: GT_Utility.sendChatToPlayer(aPlayer, trans(62, "Import allow Output") + trans(59, " (conditional)")); break;
        case 10: GT_Utility.sendChatToPlayer(aPlayer, trans(61, "Export allow Input") + trans(60, " (invert cond)")); break;
        case 11: GT_Utility.sendChatToPlayer(aPlayer, trans(62, "Import allow Output") + trans(60, " (invert cond)")); break;
        }
        return aCoverVariable;
    }

    public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        if ((aCoverVariable > 1) && ((aTileEntity instanceof IMachineProgress))) {
            if (((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4) {
                return false;
            }
        }
        return (aCoverVariable >= 6) || (aCoverVariable % 2 != 0);
    }

    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        if ((aCoverVariable > 1) && ((aTileEntity instanceof IMachineProgress))) {
            if (((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4) {
                return false;
            }
        }
        return (aCoverVariable >= 6) || (aCoverVariable % 2 == 0);
    }

    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

}
