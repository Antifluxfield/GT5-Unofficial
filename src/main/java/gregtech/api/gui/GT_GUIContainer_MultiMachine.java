package gregtech.api.gui;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_MetaGenerated_Tool_01;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine;
import ic2.core.block.machine.BlockMiningPipe;
import ic2.core.ref.BlockName;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The GUI-Container I use for all my Basic Machines
 * <p/>
 * As the NEI-RecipeTransferRect Handler can't handle one GUI-Class for all GUIs I needed to produce some dummy-classes which extend this class
 */
public class GT_GUIContainer_MultiMachine extends GT_GUIContainerMetaTile_Machine {

    String mName = "";

    public GT_GUIContainer_MultiMachine(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aTextureFile) {
        super(new GT_Container_MultiMachine(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "multimachines/" + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
        mName = aName;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString(mName, 10, 8, 16448255);

        if (mContainer != null) {
            if ((((GT_Container_MultiMachine) mContainer).mDisplayErrorCode & 1) != 0)
                fontRendererObj.drawString(trans(0, "Pipe is loose."), 10, 16, 16448255);
            if ((((GT_Container_MultiMachine) mContainer).mDisplayErrorCode & 2) != 0)
                fontRendererObj.drawString(trans(1,"Screws are missing."), 10, 24, 16448255);
            if ((((GT_Container_MultiMachine) mContainer).mDisplayErrorCode & 4) != 0)
                fontRendererObj.drawString(trans(2,"Something is stuck."), 10, 32, 16448255);
            if ((((GT_Container_MultiMachine) mContainer).mDisplayErrorCode & 8) != 0)
                fontRendererObj.drawString(trans(3, "Platings are dented."), 10, 40, 16448255);
            if ((((GT_Container_MultiMachine) mContainer).mDisplayErrorCode & 16) != 0)
                fontRendererObj.drawString(trans(4, "Circuitry burned out."), 10, 48, 16448255);
            if ((((GT_Container_MultiMachine) mContainer).mDisplayErrorCode & 32) != 0)
                fontRendererObj.drawString(trans(5, "That doesn't belong there."), 10, 56, 16448255);
            if ((((GT_Container_MultiMachine) mContainer).mDisplayErrorCode & 64) != 0)
                fontRendererObj.drawString(trans(6, "Incomplete Structure."), 10, 64, 16448255);

            if (((GT_Container_MultiMachine) mContainer).mDisplayErrorCode == 0) {
                if (((GT_Container_MultiMachine) mContainer).mActive == 0) {
                    fontRendererObj.drawString(trans(7, "Hit with Soft Hammer"), 10, 16, 16448255);
                    fontRendererObj.drawString(trans(8, "to (re-)start the Machine"), 10, 24, 16448255);
                    fontRendererObj.drawString(trans(9, "if it doesn't start."), 10, 32, 16448255);
                } else {
                    fontRendererObj.drawString(trans(10, "Running perfectly."), 10, 16, 16448255);
                }
                	int id = mContainer.mTileEntity.getMetaTileID();
                   if(id == 1157 || id == 1158){
                    	ItemStack tItem = mContainer.mTileEntity.getMetaTileEntity().getStackInSlot(1);
                    	if(tItem==null || !GT_Utility.areStacksEqual(tItem, GT_ModHandler.getIC2Item(BlockName.mining_pipe, BlockMiningPipe.MiningPipeType.pipe, 1))){
                    		fontRendererObj.drawString(trans(11, "Missing Mining Pipe"), 10,((GT_Container_MultiMachine) mContainer).mActive == 0 ? 40 : 24, 16448255);
                    	}
                    }else if(mContainer.mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_LargeTurbine){
                    	ItemStack tItem = mContainer.mTileEntity.getMetaTileEntity().getStackInSlot(1);
                    	if(tItem==null || !(tItem.getItem()==GT_MetaGenerated_Tool_01.INSTANCE&&tItem.getItemDamage()>=170&&tItem.getItemDamage()<=177)){
                    		fontRendererObj.drawString(trans(12, "Missing Turbine Rotor"), 10, ((GT_Container_MultiMachine) mContainer).mActive == 0 ? 40 : 24, 16448255);
                    	}
                    }                
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
