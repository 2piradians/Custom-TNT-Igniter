package twopiradians.customTNTIgniter.client.gui.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class CustomSlot extends Slot
{

	public CustomSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) 
	{
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	 public boolean isItemValid(ItemStack stack)
	    {
	        if (stack.getItem() instanceof ItemBlock)
	        	return true;
	        return false;
	    }
}
