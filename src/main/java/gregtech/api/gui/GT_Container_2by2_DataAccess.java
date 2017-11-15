package gregtech.api.gui;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class GT_Container_2by2_DataAccess extends GT_ContainerMetaTile_Machine {

    public GT_Container_2by2_DataAccess(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 0, 71, 26));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 1, 89, 26));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 2, 71, 44));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 3, 89, 44));
    }

    @Override
    public int getSlotCount() {
        return 4;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 4;
    }
}
