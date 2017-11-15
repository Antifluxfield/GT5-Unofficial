package gregtech.api.gui;

import gregtech.api.enums.ItemList;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GT_Slot_DataAccess extends Slot {

	public GT_Slot_DataAccess(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

    @Override
    public boolean isItemValid(ItemStack aStack) {
        return ItemList.Tool_DataOrb.isStackEqual(aStack, false, true) || ItemList.Tool_DataStick.isStackEqual(aStack, false, true) || ItemList.Circuit_Integrated.isStackEqual(aStack, true, true);
    }

}
