package gregtech.api.gui;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class GT_Container_4by4_DataAccess extends GT_ContainerMetaTile_Machine {

    public GT_Container_4by4_DataAccess(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
        super(aInventoryPlayer, aTileEntity);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 0, 53, 8));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 1, 71, 8));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 2, 89, 8));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 3, 107, 8));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 4, 53, 26));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 5, 71, 26));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 6, 89, 26));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 7, 107, 26));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 8, 53, 44));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 9, 71, 44));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 10, 89, 44));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 11, 107, 44));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 12, 53, 62));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 13, 71, 62));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 14, 89, 62));
        addSlotToContainer(new GT_Slot_DataAccess(mTileEntity, 15, 107, 62));
    }

    @Override
    public int getSlotCount() {
        return 16;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 16;
    }
}
