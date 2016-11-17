package twopiradians.customTNTIgniter.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import twopiradians.customTNTIgniter.client.gui.inventory.ContainerItem;
import twopiradians.customTNTIgniter.client.gui.inventory.InventoryTNTIgniter;
import twopiradians.customTNTIgniter.common.CustomTNTIgniter;

public class GuiHandler implements IGuiHandler
{
	public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
	{
		if (guiId == CustomTNTIgniter.GUI_ITEM_INV)
			return new ContainerItem(player, player.inventory, new InventoryTNTIgniter(player.getHeldItemMainhand()));
		return null;
	}

	public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z)
	{
		if (guiId == CustomTNTIgniter.GUI_ITEM_INV)
			return new GuiItemInventory((ContainerItem) new ContainerItem(player, player.inventory, new InventoryTNTIgniter(player.getHeldItemMainhand())));
		return null;
	}
}