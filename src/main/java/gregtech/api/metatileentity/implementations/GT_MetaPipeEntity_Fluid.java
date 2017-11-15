package gregtech.api.metatileentity.implementations;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static gregtech.api.enums.GT_Values.D1;

public class GT_MetaPipeEntity_Fluid extends MetaPipeEntity {
    public final float mThickNess;
    public final Materials mMaterial;
    public final int mCapacity, mHeatResistance, mPipeAmount;
    public final boolean mGasProof;
    public final FluidStack[] mFluids;
    public byte mLastReceivedFrom = 0, oLastReceivedFrom = 0;

    public GT_MetaPipeEntity_Fluid(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial, int aCapacity, int aHeatResistance, boolean aGasProof, int aFluidTypes) {
    	super(aID, aName, aNameRegional, 0);
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mCapacity = aCapacity;
        mGasProof = aGasProof;
        mHeatResistance = aHeatResistance;
        mPipeAmount = aFluidTypes;
        mFluids = new FluidStack[aFluidTypes];
    }

    public GT_MetaPipeEntity_Fluid(int aID, String aName, String aNameRegional, float aThickNess, Materials aMaterial, int aCapacity, int aHeatResistance, boolean aGasProof) {
        this(aID, aName, aNameRegional, aThickNess, aMaterial, aCapacity, aHeatResistance, aGasProof, 1);
    }

    @Deprecated
    public GT_MetaPipeEntity_Fluid(String aName, float aThickNess, Materials aMaterial, int aCapacity, int aHeatResistance, boolean aGasProof) {
        this(aName, aThickNess, aMaterial, aCapacity, aHeatResistance, aGasProof, 1);
    }

    public GT_MetaPipeEntity_Fluid(String aName, float aThickNess, Materials aMaterial, int aCapacity, int aHeatResistance, boolean aGasProof, int aFluidTypes) {
    	super(aName, 0);
        mThickNess = aThickNess;
        mMaterial = aMaterial;
        mCapacity = aCapacity;
        mGasProof = aGasProof;
        mHeatResistance = aHeatResistance;
        mPipeAmount = aFluidTypes;
        mFluids = new FluidStack[aFluidTypes];
    }

    @Override
    public byte getTileEntityBaseType() {
        return mMaterial == null ? 4 : (byte) ((mMaterial.contains(SubTag.WOOD) ? 12 : 4) + Math.max(0, Math.min(3, mMaterial.mToolQuality)));
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaPipeEntity_Fluid(mName, mThickNess, mMaterial, mCapacity, mHeatResistance, mGasProof, mPipeAmount);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections, byte aColorIndex, boolean aConnected, boolean aRedstone) {
        if (aConnected) {
        	if (mPipeAmount >= 9)
        		return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipeNonuple.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
        	if (mPipeAmount >= 4)
        		return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipeQuadruple.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
            float tThickNess = getThickNess();
            if (tThickNess < 0.37F)
                return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipeTiny.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
            if (tThickNess < 0.49F)
                return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipeSmall.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
            if (tThickNess < 0.74F)
                return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipeMedium.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
            if (tThickNess < 0.99F)
                return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipeLarge.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
            return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipeHuge.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
        }
        return new ITexture[]{new GT_RenderedTexture(mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex], Dyes.getModulation(aColorIndex, mMaterial.mRGBa))};
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ, EnumHand hand) {
        return false;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, EnumHand hand) {}

    @Override
    public int getProgresstime() {
        return getFluidAmount();
    }

    @Override
    public int maxProgresstime() {
        return getCapacity();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
    	for (int i = 0; i < mFluids.length; i++)
    		if (mFluids[i] != null) aNBT.setTag(i==0?"mFluid":"mFluid"+i, mFluids[i].writeToNBT(new NBTTagCompound()));
        aNBT.setByte("mLastReceivedFrom", mLastReceivedFrom);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
    	for (int i = 0; i < mFluids.length; i++)
    		mFluids[i] = FluidStack.loadFluidStackFromNBT(aNBT.getCompoundTag(i==0?"mFluid":"mFluid"+i));
        mLastReceivedFrom = aNBT.getByte("mLastReceivedFrom");
    }

    @Override
    public void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity aEntity) {
        if ((((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections & -128) == 0 && aEntity instanceof EntityLivingBase) {
            for (FluidStack tFluid : mFluids) {
            	if (tFluid != null) {
                	int tTemperature = tFluid.getFluid().getTemperature(tFluid);
                    if (tTemperature > 320 && !isCoverOnSide((BaseMetaPipeEntity) getBaseMetaTileEntity(), (EntityLivingBase) aEntity)) {
                        GT_Utility.applyHeatDamage((EntityLivingBase) aEntity, (tTemperature - 300) / 50.0F);
                    } else if (tTemperature < 260 && !isCoverOnSide((BaseMetaPipeEntity) getBaseMetaTileEntity(), (EntityLivingBase) aEntity)) {
                        GT_Utility.applyFrostDamage((EntityLivingBase) aEntity, (270 - tTemperature) / 25.0F);
                    }
                }
            }
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        return new AxisAlignedBB(aX + 0.125D, aY + 0.125D, aZ + 0.125D, aX + 0.875D, aY + 0.875D, aZ + 0.875D);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && aTick % 5 == 0) {
            mLastReceivedFrom &= 63;
            if (mLastReceivedFrom == 63) {
                mLastReceivedFrom = 0;
            }

            for (FluidStack tFluid : mFluids) {
            	if (tFluid != null && tFluid.amount > 0) {
                    int tTemperature = tFluid.getFluid().getTemperature(tFluid);
                    if (tTemperature > mHeatResistance) {
                        if (aBaseMetaTileEntity.getRandomNumber(100) == 0) {
                            aBaseMetaTileEntity.setToFire();
                            return;
                        }
                        aBaseMetaTileEntity.setOnFire();
                    }
                    if (!mGasProof && tFluid.getFluid().isGaseous(tFluid)) {
                        tFluid.amount -= 5;
                        sendSound((byte) 9);
                        if (tTemperature > 320) {
                            try {
                                for (EntityLivingBase tLiving : getBaseMetaTileEntity().getWorldObj().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getBaseMetaTileEntity().getXCoord() - 2, getBaseMetaTileEntity().getYCoord() - 2, getBaseMetaTileEntity().getZCoord() - 2, getBaseMetaTileEntity().getXCoord() + 3, getBaseMetaTileEntity().getYCoord() + 3, getBaseMetaTileEntity().getZCoord() + 3))) {
                                    GT_Utility.applyHeatDamage(tLiving, (tTemperature - 300) / 25.0F);
                                }
                            } catch (Throwable e) {
                                if (D1) e.printStackTrace(GT_Log.err);
                            }
                        } else if (tTemperature < 260) {
                            try {
                                for (EntityLivingBase tLiving : getBaseMetaTileEntity().getWorldObj().getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getBaseMetaTileEntity().getXCoord() - 2, getBaseMetaTileEntity().getYCoord() - 2, getBaseMetaTileEntity().getZCoord() - 2, getBaseMetaTileEntity().getXCoord() + 3, getBaseMetaTileEntity().getYCoord() + 3, getBaseMetaTileEntity().getZCoord() + 3))) {
                                    GT_Utility.applyFrostDamage(tLiving, (270 - tTemperature) / 12.5F);
                                }
                            } catch (Throwable e) {
                                if (D1) e.printStackTrace(GT_Log.err);
                            }
                        }
                        if (tFluid.amount <= 0) tFluid = null;
                    }
                }
            }

            if (mLastReceivedFrom == oLastReceivedFrom) {
                ConcurrentHashMap<IFluidHandler, EnumFacing> tTanks = new ConcurrentHashMap<IFluidHandler, EnumFacing>();

                mConnections = 0;

                for (byte tSide = 0, i = 0, j = (byte) aBaseMetaTileEntity.getRandomNumber(6); i < 6; i++) {
                    tSide = (byte) ((j + i) % 6);

                    IFluidHandler tTileEntity = aBaseMetaTileEntity.getITankContainerAtSide(tSide);
                    if (tTileEntity != null) {
                        if (tTileEntity instanceof IGregTechTileEntity) {
                            if (aBaseMetaTileEntity.getColorization() >= 0) {
                                byte tColor = ((IGregTechTileEntity) tTileEntity).getColorization();
                                if (tColor >= 0 && (tColor & 15) != (aBaseMetaTileEntity.getColorization() & 15)) {
                                    continue;
                                }
                            }
                        }
                        FluidTankInfo[] tInfo = tTileEntity.getTankInfo(EnumFacing.VALUES[tSide].getOpposite());
                        if (tInfo != null && tInfo.length > 0) {
                            if (tTileEntity instanceof ICoverable && ((ICoverable) tTileEntity).getCoverBehaviorAtSide(GT_Utility.getOppositeSide(tSide)).alwaysLookConnected(GT_Utility.getOppositeSide(tSide), ((ICoverable) tTileEntity).getCoverIDAtSide(GT_Utility.getOppositeSide(tSide)), ((ICoverable) tTileEntity).getCoverDataAtSide(GT_Utility.getOppositeSide(tSide)), ((ICoverable) tTileEntity))) {
                                mConnections |= (1 << tSide);
                            }
                            if (aBaseMetaTileEntity.getCoverBehaviorAtSide(tSide).letsFluidIn(tSide, aBaseMetaTileEntity.getCoverIDAtSide(tSide), aBaseMetaTileEntity.getCoverDataAtSide(tSide), null, aBaseMetaTileEntity)) {
                                mConnections |= (1 << tSide);
                            }
                            if (aBaseMetaTileEntity.getCoverBehaviorAtSide(tSide).letsFluidOut(tSide, aBaseMetaTileEntity.getCoverIDAtSide(tSide), aBaseMetaTileEntity.getCoverDataAtSide(tSide), null, aBaseMetaTileEntity)) {
                                mConnections |= (1 << tSide);
                                if (((1 << tSide) & mLastReceivedFrom) == 0)
                                    tTanks.put(tTileEntity, EnumFacing.VALUES[tSide].getOpposite());
                            }
                            if (aBaseMetaTileEntity.getCoverBehaviorAtSide(tSide).alwaysLookConnected(tSide, aBaseMetaTileEntity.getCoverIDAtSide(tSide), aBaseMetaTileEntity.getCoverDataAtSide(tSide), aBaseMetaTileEntity)) {
                                mConnections |= (1 << tSide);
                            }
                        } else if (tInfo != null && tInfo.length == 0) {
                            IGregTechTileEntity tSideTile = aBaseMetaTileEntity.getIGregTechTileEntityAtSide(tSide);
                            if (tSideTile != null) {
                                ItemStack tCover = GregTech_API.getCoverItem(GregTech_API.getCoverId(tSideTile.getCoverBehaviorAtSide(GT_Utility.getOppositeSide(tSide)))).toStack();
                                if (tCover != null && (GT_Utility.areStacksEqual(tCover, ItemList.FluidRegulator_LV.get(1, new Object[]{}, true)) ||
                                        GT_Utility.areStacksEqual(tCover, ItemList.FluidRegulator_MV.get(1, new Object[]{}, true)) ||
                                        GT_Utility.areStacksEqual(tCover, ItemList.FluidRegulator_HV.get(1, new Object[]{}, true)) ||
                                        GT_Utility.areStacksEqual(tCover, ItemList.FluidRegulator_EV.get(1, new Object[]{}, true)) ||
                                        GT_Utility.areStacksEqual(tCover, ItemList.FluidRegulator_IV.get(1, new Object[]{}, true)))) {
                                    mConnections |= (1 << tSide);
                                }
                            }
                        }
                    }
                }

                for (int i = 0, j = aBaseMetaTileEntity.getRandomNumber(mPipeAmount); i < mPipeAmount; i++) {
                	int index = (i + j) % mPipeAmount;
                	if (mFluids[index] != null && mFluids[index].amount > 0) {
                        int tAmount = Math.max(1, Math.min(mCapacity * 10, mFluids[index].amount / 2)), tSuccessfulTankAmount = 0;

                        for (Map.Entry<IFluidHandler, EnumFacing> tEntry : tTanks.entrySet())
                            if (tEntry.getKey().fill(tEntry.getValue(), drainFromIndex(tAmount, false, index), false) > 0)
                                tSuccessfulTankAmount++;

                        if (tSuccessfulTankAmount > 0) {
                            if (tAmount >= tSuccessfulTankAmount) {
                                tAmount /= tSuccessfulTankAmount;
                                for (Map.Entry<IFluidHandler, EnumFacing> tTileEntity : tTanks.entrySet()) {
                                    if (mFluids[index] == null || mFluids[index].amount <= 0) break;
                                    int tFilledAmount = tTileEntity.getKey().fill(tTileEntity.getValue(), drain(tAmount, false), false);
                                    if (tFilledAmount > 0)
                                        tTileEntity.getKey().fill(tTileEntity.getValue(), drainFromIndex(tFilledAmount, true, index), true);
                                }
                            } else {
                                for (Map.Entry<IFluidHandler, EnumFacing> tTileEntity : tTanks.entrySet()) {
                                    if (mFluids[index] == null || mFluids[index].amount <= 0) break;
                                    int tFilledAmount = tTileEntity.getKey().fill(tTileEntity.getValue(), drainFromIndex(mFluids[index].amount, false, index), false);
                                    if (tFilledAmount > 0)
                                        tTileEntity.getKey().fill(tTileEntity.getValue(), drainFromIndex(tFilledAmount, true, index), true);
                                }
                            }
                        }
                    }
                }

                mLastReceivedFrom = 0;
            }

            oLastReceivedFrom = mLastReceivedFrom;
        }
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        super.doSound(aIndex, aX, aY, aZ);
        if (aIndex == 9) {
            GT_Utility.doSoundAtClient(GregTech_API.sSoundList.get(4), 5, 1.0F, aX, aY, aZ);
            for (byte i = 0; i < 6; i++) {
                EnumFacing facing = EnumFacing.VALUES[i];
                for (int l = 0; l < 2; ++l)
                    getBaseMetaTileEntity().getWorldObj().spawnParticle(EnumParticleTypes.SMOKE_LARGE, aX - 0.5 + (new XSTR()).nextFloat(), aY - 0.5 + (new XSTR()).nextFloat(), aZ - 0.5 + (new XSTR()).nextFloat(), facing.getFrontOffsetX() / 5.0, 0, facing.getFrontOffsetZ() / 5.0);
            }
        }
    }

    @Override
    public final int getCapacity() {
        return mCapacity * 20 * mPipeAmount;
    }

    @Override
    public FluidTankInfo getInfo() {
    	for (FluidStack tFluid : mFluids) {
    		if (tFluid != null)
    			return new FluidTankInfo(tFluid, mCapacity * 20);
    	}
    	return new FluidTankInfo(null, mCapacity * 20);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing aSide) {
        if (getCapacity() <= 0 && !getBaseMetaTileEntity().hasSteamEngineUpgrade()) return new FluidTankInfo[]{};
        ArrayList<FluidTankInfo> tList = new ArrayList<>();
        for (FluidStack tFluid : mFluids)
        	tList.add(new FluidTankInfo(tFluid, mCapacity * 20));
        return tList.toArray(new FluidTankInfo[mPipeAmount]);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public final FluidStack getFluid() {
    	for (FluidStack tFluid : mFluids) {
    		if (tFluid != null)
    			return tFluid;
    	}
        return null;
    }

    @Override
    public final int getFluidAmount() {
    	int rAmount = 0;
    	for (FluidStack tFluid : mFluids) {
    		if (tFluid != null)
    			rAmount += tFluid.amount;
    	}
        return rAmount;
    }

    @Override
    public final int fill_default(EnumFacing aSide, FluidStack aFluid, boolean doFill) {
    	if (aFluid == null) return 0;

        int index = -1;
        for (int i = 0; i < mPipeAmount; i++) {
        	if (mFluids[i] != null && mFluids[i].isFluidEqual(aFluid)) {
        		index = i; break;
        	}
        	else if ((mFluids[i] == null) && index < 0) {
        		index = i;
        	}
        }
        
        return fill_default_intoIndex(aSide, aFluid, doFill, index);
    }
    
    private final int fill_default_intoIndex(EnumFacing aSide, FluidStack aFluid, boolean doFill, int index) {
        if (aFluid == null) return 0;

        if (mFluids[index] == null) {
            if (aFluid.amount <= getCapacity()) {
                if (doFill) {
                    mFluids[index] = aFluid.copy();
                    mLastReceivedFrom |= (1 << aSide.ordinal());
                }
                return aFluid.amount;
            }
            if (doFill) {
                mFluids[index] = aFluid.copy();
                mLastReceivedFrom |= (1 << aSide.ordinal());
                mFluids[index].amount = getCapacity();
            }
            return getCapacity();
        }

        if (!mFluids[index].isFluidEqual(aFluid)) return 0;

        int space = getCapacity() - mFluids[index].amount;
        if (aFluid.amount <= space) {
            if (doFill) {
                mFluids[index].amount += aFluid.amount;
                mLastReceivedFrom |= (1 << aSide.ordinal());
            }
            return aFluid.amount;
        }
        if (doFill) {
            mFluids[index].amount = getCapacity();
            mLastReceivedFrom |= (1 << aSide.ordinal());
        }
        return space;
    }

    @Override
    public final FluidStack drain(int maxDrain, boolean doDrain) {
    	FluidStack drained = null;
    	for (int i = 0; i < mPipeAmount; i++) {
    		if ((drained = drainFromIndex(maxDrain, doDrain, i)) != null)
    			return drained;
    	}
    	return null;
    }

    private final FluidStack drainFromIndex(int maxDrain, boolean doDrain, int index) {
    	if (index < 0 || index >= mPipeAmount) return null;
    	if (mFluids[index] == null) return null;
        if (mFluids[index].amount <= 0) {
            mFluids[index] = null;
            return null;
        }

        int used = maxDrain;
        if (mFluids[index].amount < used)
            used = mFluids[index].amount;

        if (doDrain) {
            mFluids[index].amount -= used;
        }

        FluidStack drained = mFluids[index].copy();
        drained.amount = used;

        if (mFluids[index].amount <= 0) {
            mFluids[index] = null;
        }

        return drained;
    }

    @Override
    public int getTankPressure() {
    	return getFluidAmount() - (getCapacity() / 2);
    }

    @Override
    public String[] getDescription() {
    	if (mPipeAmount == 1) {
        	return new String[]{
                    TextFormatting.BLUE + "Fluid Capacity: %%%" + (mCapacity * 20) + "%%% L/sec" + TextFormatting.GRAY,
                    TextFormatting.RED + "Heat Limit: %%%" + mHeatResistance + "%%% K" + TextFormatting.GRAY
            };
        } else {
        	return new String[]{
                    TextFormatting.BLUE + "Fluid Capacity: %%%" + (mCapacity * 20) + "%%% L/sec" + TextFormatting.GRAY,
                    TextFormatting.RED + "Heat Limit: %%%" + mHeatResistance + "%%% K" + TextFormatting.GRAY,
                    TextFormatting.AQUA + "Pipe Amount: %%%" + mPipeAmount + TextFormatting.GRAY
            };
        }
    }

    @Override
    public float getThickNess() {
        return mThickNess;
    }
}