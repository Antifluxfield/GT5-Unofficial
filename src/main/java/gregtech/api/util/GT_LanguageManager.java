package gregtech.api.util;


import gregtech.api.GregTech_API;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Locale;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static gregtech.api.enums.GT_Values.E;

public class GT_LanguageManager {

    private GT_LanguageManager() {}

    public static final HashMap<String, String>
            LOCALIZATION = new HashMap<>(),
            BUFFERMAP = new HashMap<>();

    public static Configuration sEnglishFile;
    public static boolean sUseEnglishFile = false;
    //TODO implement 03f6c9e

    public static String addStringLocalization(String aKey, String aEnglish) {
        return addStringLocalization(aKey, aEnglish, true);
    }

    public static String addStringLocalization(String aKey, String aEnglish, boolean aWriteIntoLangFile) {
        if (aKey == null)
            return E;
        if (aWriteIntoLangFile)
            aEnglish = writeToLangFile(aKey, aEnglish);
        LOCALIZATION.putIfAbsent(aKey, aEnglish);
        if(sUseEnglishFile && !aWriteIntoLangFile && LOCALIZATION.containsKey(aKey)){
        	aEnglish = LOCALIZATION.get(aKey);
        }
        return aEnglish;
    }

    private static synchronized String writeToLangFile(String aKey, String aEnglish) {
        if (aKey == null) return E;
        if (sEnglishFile == null) {
            BUFFERMAP.put(aKey.trim(), aEnglish);
        } else {
            if (!BUFFERMAP.isEmpty()) {
                for (Entry<String, String> tEntry : BUFFERMAP.entrySet()) {
                    Property tProperty = sEnglishFile.get("LanguageFile", tEntry.getKey(), tEntry.getValue());
                    if (!tProperty.wasRead() && GregTech_API.sPostloadFinished) sEnglishFile.save();
                }
                BUFFERMAP.clear();
            }
            Property tProperty = sEnglishFile.get("LanguageFile", aKey.trim(), aEnglish);
            if (!tProperty.wasRead() && GregTech_API.sPostloadFinished) sEnglishFile.save();
            if (sEnglishFile.get("EnableLangFile", "UseThisFileAsLanguageFile", false).getBoolean(false)){
                aEnglish = tProperty.getString();
                sUseEnglishFile = true;
            }
        }
        return aEnglish;
    }

    public static String getTranslation(String aKey) {
        if (aKey == null) {
            return E;
        }
        if(LOCALIZATION.containsKey(aKey) &&
                !LOCALIZATION.get(aKey).equals(
                net.minecraft.util.text.translation.I18n.translateToLocal(aKey))) {
            injectAllLocales();
        }
        return net.minecraft.util.text.translation.I18n.translateToLocal(aKey);
    }

    public static String getTranslation(String aKey, String aSeperator) {
        if (aKey == null) return E;
        String rTranslation = E;
        for (String tString : aKey.split(aSeperator)) {
            rTranslation += getTranslation(tString);
        }
        return rTranslation;
    }

    public static void injectAllLocales() {
        injectCommonLocales();

        if(FMLCommonHandler.instance().getSide().isClient()) {
            injectClientLocales();
        }

        GT_Log.out.println("Localization injected.");
    }

    public static void injectCommonLocales() {
        LanguageMap languageMap = ObfuscationReflectionHelper.getPrivateValue(net.minecraft.util.text.translation.I18n.class, null, 0);
        Map<String, String> properties2 = ObfuscationReflectionHelper.getPrivateValue(LanguageMap.class, languageMap, 3);
        properties2.putAll(LOCALIZATION);
    }

    @SideOnly(Side.CLIENT)
    public static void injectClientLocales() {
        Locale i18nLocale = ObfuscationReflectionHelper.getPrivateValue(I18n.class, null, 0);
        Map<String, String> properties = ObfuscationReflectionHelper.getPrivateValue(Locale.class, i18nLocale, 2);
        properties.putAll(LOCALIZATION);
    }

    public static void writePlaceholderStrings() {
    	for (int i = 0; i < 6; i++) getFaceSideName(i, true);
    	appendInteractTrans(0,	"Pipe is loose.");
    	appendInteractTrans(1,	"Screws are missing.");
    	appendInteractTrans(2,	"Something is stuck.");
    	appendInteractTrans(3,	"Platings are dented.");
    	appendInteractTrans(4,	"Circuitry burned out.");
    	appendInteractTrans(5,	"That doesn't belong there.");
    	appendInteractTrans(6,	"Incomplete Structure.");
    	appendInteractTrans(7,	"Hit with Soft Hammer");
    	appendInteractTrans(8,	"to (re-)start the Machine");
    	appendInteractTrans(9,	"if it doesn't start.");
    	appendInteractTrans(10,	"Running perfectly.");
    	appendInteractTrans(11,	"Missing Mining Pipe");
    	appendInteractTrans(12,	"Missing Turbine Rotor");
    	appendInteractTrans(13,	"Emit Energy to Outputside");
    	appendInteractTrans(14,	"Don't emit Energy");
    	appendInteractTrans(15,	"Emit Redstone if no Slot is free");
    	appendInteractTrans(16,	"Don't emit Redstone");
    	appendInteractTrans(17,	"Invert Redstone");
    	appendInteractTrans(18,	"Don't invert Redstone");
    	appendInteractTrans(19,	"Invert Filter");
    	appendInteractTrans(20,	"Don't invert Filter");
    	appendInteractTrans(21,	"Ignore NBT");
    	appendInteractTrans(22,	"NBT has to match");
    	appendInteractTrans(23,	"Allow Items with NBT");
    	appendInteractTrans(24,	"Don't allow Items with NBT");
    	appendInteractTrans(25,	"Machine Processing: Enabled");
    	appendInteractTrans(26,	"Machine Processing: Disabled");
    	appendInteractTrans(27,	"Redstone Output at %s side set to: Strong");
    	appendInteractTrans(28,	"Redstone Output at %s side set to: Weak");
    	appendInteractTrans(29,	"Auto-Input: Disabled");
    	appendInteractTrans(30,	"Auto-Input: Enabled");
    	appendInteractTrans(31,	"Auto-Output: Disabled");
    	appendInteractTrans(32,	"Auto-Output: Enabled");
    	appendInteractTrans(33,	"Input from Output Side allowed");
    	appendInteractTrans(34,	"Input from Output Side forbidden");
    	appendInteractTrans(35,	"Do not regulate Item Stack Size");
    	appendInteractTrans(36,	"Regulate Item Stack Size to: ");
    	appendInteractTrans(37,	"Outputs Liquids, Steam and Items");
    	appendInteractTrans(38,	"Outputs Steam and Items");
    	appendInteractTrans(39,	"Outputs Steam and Liquids");
    	appendInteractTrans(40,	"Outputs Steam");
    	appendInteractTrans(41,	"Outputs Liquids and Items");
    	appendInteractTrans(42,	"Outputs only Items");
    	appendInteractTrans(43,	"Outputs only Liquids");
    	appendInteractTrans(44,	"Outputs nothing");
    	appendInteractTrans(45,	"Step Down, In: %dV@%dAmp, Out: %dV@%dAmp");
    	appendInteractTrans(46,	"Step Up, In: %dV@%dAmp, Out: %dV@%dAmp");
    	appendInteractTrans(47,	"Items per side: ");
    	appendInteractTrans(48,	"Redstone");
    	appendInteractTrans(49,	"Energy");
    	appendInteractTrans(50,	"Fluids");
    	appendInteractTrans(51,	"Items");
    	appendInteractTrans(52,	"Puts out into adjacent Slot #");
    	appendInteractTrans(53,	"Grabs in for own Slot #");
    	appendInteractTrans(54,	"Pump speed: %dL/tick(%dL/sec)");
    	appendInteractTrans(55,	"Slot #");
    	appendInteractTrans(56,	"No Work at all");
    	appendInteractTrans(57,	"Export");
    	appendInteractTrans(58,	"Import");
    	appendInteractTrans(59,	" (conditional)");
    	appendInteractTrans(60,	" (invert cond)");
    	appendInteractTrans(61,	"Export allow Input");
    	appendInteractTrans(62,	"Import allow Output");
    	appendInteractTrans(63,	"Normal");
    	appendInteractTrans(64,	"Inverted");
    	appendInteractTrans(65,	"Ready to work");
    	appendInteractTrans(66,	"Not ready to work");
    	appendInteractTrans(67,	"Keep Liquids Away");
    	appendInteractTrans(68,	"Allow");
    	appendInteractTrans(69,	"Disallow");
    	appendInteractTrans(70,	" Universal Storage");
    	appendInteractTrans(71,	" Electricity Storage");
    	appendInteractTrans(72,	" Steam Storage");
    	appendInteractTrans(73,	" Average Electric Input");
    	appendInteractTrans(74,	" Average Electric Output");
    	appendInteractTrans(75,	"(Including Batteries)");
    	appendInteractTrans(76,	"Filter Fluid: ");
    	appendInteractTrans(77,	"Emit if %d Maintenance Needed");
    	appendInteractTrans(78,	"(inverted)");
    	appendInteractTrans(79,	"Emit if rotor needs maintainance");
    	appendInteractTrans(80,	"Emit if any Player is close");
    	appendInteractTrans(81,	"Emit if you are close");
    	appendInteractTrans(82,	"Emit if other player is close");
    	appendInteractTrans(83,	"Input whitelisted, ");
    	appendInteractTrans(84,	"Input blacklisted, ");
    	appendInteractTrans(85,	"Deny Output");
    	appendInteractTrans(86,	"Allow Output");
    	appendInteractTrans(87,	"Conducts strongest Input");
    	appendInteractTrans(88,	"Conducts from %s Input");
    	appendInteractTrans(89,	"Signal = ");
    	appendInteractTrans(90,	"Conditional Signal = ");
    	appendInteractTrans(91,	"Inverted Conditional Signal = ");
    	appendInteractTrans(92,	"Frequency: ");
    	appendInteractTrans(93,	"Open if work enabled");
    	appendInteractTrans(94,	"Open if work disabled");
    	appendInteractTrans(95,	"Only Output allowed");
    	appendInteractTrans(96,	"Only Input allowed");
    	appendInteractTrans(97,	"This is %s Ore.");
    	appendInteractTrans(98,	"There is Lava behind this Rock");
    	appendInteractTrans(99,	"There is Water behind this Rock");
    	appendInteractTrans(100,"There is Fluid behind this Rock");
    	appendInteractTrans(101,"There is an Air Pocket behind this Rock.");
    	appendInteractTrans(102,"Material is changing behind this Rock.");
    	appendInteractTrans(103,"No Ores found.");
    	appendInteractTrans(104,"Name: %s  MetaData: %s");
    	appendInteractTrans(105,"Is valid Beacon Pyramid Material");
    	appendInteractTrans(106,"Tank %d: %s / %s %s");
    	appendInteractTrans(107,"Heat: %d/%d  HEM: %.4f  Base EU Output: %s");
    	appendInteractTrans(108,"Facing: %s");
    	appendInteractTrans(109,"You can remove this with a Wrench");
    	appendInteractTrans(110,"You can NOT remove this with a Wrench");
    	appendInteractTrans(111,"Demanded Energy: %.4f");
    	appendInteractTrans(112,"Max Safe Input: %d");
    	appendInteractTrans(113,"Hardness: %.2f  Blast Resistance: %.2f");
    	appendInteractTrans(114,"Conduction Loss: %.4f");
    	appendInteractTrans(115,"Contained Energy: %d of %d");
    	appendInteractTrans(116,"Teleporter Compatible at %s side");
    	appendInteractTrans(117,"Not Teleporter Compatible at %s side");
    	appendInteractTrans(118,"Has Muffler Upgrade");
    	appendInteractTrans(119,"Progress: %s / %s");
    	appendInteractTrans(120,"Max IN: %d EU");
    	appendInteractTrans(121,"Max OUT: %d EU at %d Amperes");
    	appendInteractTrans(122,"Energy: %s / %s EU");
    	appendInteractTrans(123,"Owned by: ");
    	appendInteractTrans(124,"Type -- Crop-Name: %s  Growth: %d  Gain: %s  Resistance: %d");
    	appendInteractTrans(125,"Plant -- Fertilizer: %d  Water: %d  Weed-Ex: %d  Scan-Level: %d");
    	appendInteractTrans(126,"Environment -- Nutrients: %d  Humidity: %d  Air-Quality: %d");
    	appendInteractTrans(127,"Discovered by: ");
    	appendInteractTrans(128,"Oil in Chunk: %d %s");
    	appendInteractTrans(129,"Pollution in Chunk: %s gibbl");
    	appendInteractTrans(130,"No Pollution in Chunk! HAYO!");
    	appendInteractTrans(131,"It's dangerous to go alone! Take this.");
    	appendInteractTrans(132,"Teleporter");
    	appendInteractTrans(133,"Dim: ");
    	appendInteractTrans(134,"Microwave Energy Transmitter");
    	//appendInteractTrans(135,"");
    	//appendInteractTrans(136,"");
    	//appendInteractTrans(137,"");
    	//appendInteractTrans(138,"");
    	//appendInteractTrans(139,"");
    	
    	appendInfoTrans(0,	"Progress:");
    	appendInfoTrans(1,	"secs");
    	appendInfoTrans(2,	"Efficiency:");
    	appendInfoTrans(3,	"Problems:");
    	appendInfoTrans(4,	"Stored Items:");
    	appendInfoTrans(5,	"Current Output: %d EU/t");
    	appendInfoTrans(6,	"Fuel Consumption: %d L/t");
    	appendInfoTrans(7,	"Fuel Value: %d EU/L");
    	appendInfoTrans(8,	"Fuel Remaining: %d L");
    	appendInfoTrans(9,	"Current Efficiency: %d%");
    	appendInfoTrans(10,	"No Items");
    	appendInfoTrans(11,	"EU Required: %dEU/t");
    	appendInfoTrans(12,	"Stored EU: %d / %d");
    	appendInfoTrans(13,	"Plasma Output: %dL/t");
    	appendInfoTrans(14,	"Turbine running");
    	appendInfoTrans(15,	"Turbine stopped");
    	appendInfoTrans(16,	"No Maintainance issues");
    	appendInfoTrans(17,	"Needs Maintainance");
    	appendInfoTrans(18,	"Stored Energy:");
    	appendInfoTrans(19,	"Current Output: %d EU/t");
    	appendInfoTrans(20,	"Optimal Flow: %d L/t");
    	appendInfoTrans(21,	"Current Speed: %d%");
    	appendInfoTrans(22,	"Turbine Damage: %d%");
    	appendInfoTrans(23,	"Average input:");
    	appendInfoTrans(24,	"Average output:");
    	appendInfoTrans(25,	"Coordinates:");
    	appendInfoTrans(26,	"Dimension: ");
    	appendInfoTrans(27,	"Stored Fluid:");
    	appendInfoTrans(28,	"No Fluid");
    	appendInfoTrans(29,	"L");
    	appendInfoTrans(30,	"");
    	
    	appendJEITrans(0,	"Chance: %.2f%");
    	appendJEITrans(1,	"Does not get consumed in the process");
    	appendJEITrans(2,	"Amount: %d");
    	appendJEITrans(3,	"Temperature: %d K");
    	appendJEITrans(4,	"State: Gas");
    	appendJEITrans(5,	"State: Liquid");
    	appendJEITrans(6,	"Total: %d EU");
    	appendJEITrans(7,	"Usage: %d EU/t");
    	appendJEITrans(8,	"Voltage: %d EU");
    	appendJEITrans(9,	"Amperage: %d");
    	appendJEITrans(10,	"Voltage: unspecified");
    	appendJEITrans(11,	"Amperage: unspecified");
    	appendJEITrans(12,	"Time: %.2f secs");
    	
    	appendItemTrans(0, "Stored Heat: %s");
    	appendItemTrans(1, "Durability: %s/%s");
    	appendItemTrans(2, "%s lvl %s");
    	appendItemTrans(3, "Attack Damage: %s");
    	appendItemTrans(4, "Mining Speed: %s");
    	appendItemTrans(5, "Turbine Efficiency: %s");
    	appendItemTrans(6, "Optimal Steam flow: %sL/sec");
    	appendItemTrans(7, "Optimal Gas flow(EU burnvalue per tick): %sEU/t");
    	appendItemTrans(8, "Optimal Plasma flow(Plasma energyvalue per tick): %sEU/t");
    	appendItemTrans(9, "Contains %s EU   Tier: %s");
    	appendItemTrans(10, "Empty. You should recycle it properly.");
    	appendItemTrans(11, "%s / %s EU - Voltage: %s");
    	appendItemTrans(12, "No Fluids Contained");
    	appendItemTrans(13, "%sL / %sL");
    	appendItemTrans(14, "Missing Coodinates!");
    	appendItemTrans(15, "Device at:");
    	appendItemTrans(16, "Amount: %d L");
    	appendItemTrans(17, "Temperature: %d K");
    	appendItemTrans(18, "State: Gas");
    	appendItemTrans(19, "State: Liquid");
    	appendItemTrans(20, "Heat: %dK");
    	
    	GT_LanguageManager.addStringLocalization("TileEntity_EUp_IN", "Voltage IN: ");
    	GT_LanguageManager.addStringLocalization("TileEntity_EUp_OUT", "Voltage OUT: ");
    	GT_LanguageManager.addStringLocalization("TileEntity_EUp_AMOUNT", "Amperage: ");
    	GT_LanguageManager.addStringLocalization("TileEntity_EUp_STORE", "Capacity: ");
    	GT_LanguageManager.addStringLocalization("GT_TileEntity_MUFFLER", "Has Muffler Upgrade");
    	GT_LanguageManager.addStringLocalization("GT_TileEntity_STEAMCONVERTER", "Has Steam Upgrade");
    	GT_LanguageManager.addStringLocalization("GT_TileEntity_STEAMTANKS", "Steam Tank Upgrades");
    }

    public static String getFaceSideName(int aSide) {
    	return getFaceSideName(aSide, false);
    }

    public static String getFaceSideName(int aSide, boolean aWriteIntoLangFile) {
    	if (aSide < 0 || aSide >= 6) return "";
    	return getFaceSideName(EnumFacing.VALUES[aSide], aWriteIntoLangFile);
    }

    public static String getFaceSideName(EnumFacing aSide, boolean aWriteIntoLangFile) {
    	return addStringLocalization("Side_" + aSide.name().toUpperCase(),aSide.name().toLowerCase(), aWriteIntoLangFile);
    }

    public static void appendInteractTrans(int aKey, String aEnglish) {
    	addStringLocalization(String.format("Interaction_DESCRIPTION_Index_%03d", aKey), aEnglish);
    }

    public static void appendJEITrans(int aKey, String aEnglish) {
    	addStringLocalization(String.format("JEI_DESCRIPTION_Index_%03d", aKey), aEnglish);
    }

    public static void appendItemTrans(int aKey, String aEnglish) {
    	addStringLocalization(String.format("Item_DESCRIPTION_Index_%03d", aKey), aEnglish);
    }

    public static void appendInfoTrans(int aKey, String aEnglish) {
    	addStringLocalization(String.format("Info_DESCRIPTION_Index_%03d", aKey), aEnglish);
    }

}