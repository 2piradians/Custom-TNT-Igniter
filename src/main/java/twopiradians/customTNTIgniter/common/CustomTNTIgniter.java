package twopiradians.customTNTIgniter.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import twopiradians.customTNTIgniter.client.gui.GuiHandler;
import twopiradians.customTNTIgniter.common.entity.EntityCustomTNTPrimed;
import twopiradians.customTNTIgniter.common.item.ModItems;
import twopiradians.customTNTIgniter.creativetab.CustomTNTIgniterCreativeTab;

@Mod(modid = CustomTNTIgniter.MODID, version = CustomTNTIgniter.VERSION, name = CustomTNTIgniter.MODNAME)
public class CustomTNTIgniter
{
	public static final String MODID = "customTNTIgniter";
	public static final String MODNAME = "Custom TNT Igniter";
	public static final String VERSION = "1.0";
	public static final CustomTNTIgniterCreativeTab tab = new CustomTNTIgniterCreativeTab("tabCustomTNTIgniter");
	@SidedProxy(clientSide = "twopiradians.customTNTIgniter.client.ClientProxy", serverSide = "twopiradians.customTNTIgniter.common.CommonProxy")
	public static CommonProxy proxy;

	/** This is used to keep track of GUIs that we make*/
	private static int modGuiIndex = 0;

	/** Set our custom inventory Gui index to the next available Gui index */
	public static final int GUI_ITEM_INV = modGuiIndex++;
	
	@Instance("customTNTIgniter")
	public static CustomTNTIgniter instance;
	
	public GuiHandler handler = new GuiHandler();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModItems.init();
        EntityRegistry.registerModEntity(EntityCustomTNTPrimed.class, "entityCustomTntPrimed", 0, this, 256, 2, true);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.registerRenders();
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.tnt_igniter),"AAA","BCB","BBB", 'A', Items.IRON_INGOT, 'B', Blocks.PLANKS, 'C', Blocks.REDSTONE_BLOCK);
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, handler);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}
}