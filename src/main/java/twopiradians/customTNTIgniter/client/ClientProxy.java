package twopiradians.customTNTIgniter.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import twopiradians.customTNTIgniter.common.CommonProxy;
import twopiradians.customTNTIgniter.common.entity.EntityCustomTNTPrimed;
import twopiradians.customTNTIgniter.common.item.ModItems;

public class ClientProxy extends CommonProxy
{	
	@SuppressWarnings("deprecation")
	@Override
	public void registerRenders()
	{
		ModItems.registerRenders();
		RenderingRegistry.registerEntityRenderingHandler(EntityCustomTNTPrimed.class, new RenderCustomTNTPrimed(Minecraft.getMinecraft().getRenderManager()));
	}
}
