package twopiradians.customTNTIgniter.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import twopiradians.customTNTIgniter.common.CustomTNTIgniter;


public class ModItems 
{
	public static Item tnt_igniter;

	public static void init() 
	{
		tnt_igniter = registerItemWithTab(new ItemTNTIgniter(), "tnt_igniter");
	}

	public static void registerRenders()
	{
		registerRender(tnt_igniter);
	}
	
    public static Item registerItemWithTab(final Item item, final String unlocalizedName) 
    {
        item.setUnlocalizedName(unlocalizedName);
        item.setRegistryName(CustomTNTIgniter.MODID, unlocalizedName);
        item.setCreativeTab(CustomTNTIgniter.tab);
        GameRegistry.register(item);
        return item;
    }
    
    public static Item registerItemWithoutTab(final Item item, final String unlocalizedName) 
    {
        item.setUnlocalizedName(unlocalizedName);
        GameRegistry.register(item);
        return item;
    }

	public static void registerRender(Item item)
	{	
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(CustomTNTIgniter.MODID+":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}

