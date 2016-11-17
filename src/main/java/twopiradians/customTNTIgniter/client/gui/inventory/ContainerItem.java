package twopiradians.customTNTIgniter.client.gui.inventory;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerItem extends Container
{
	public InventoryTNTIgniter inventory;
	private static final int INV_START = InventoryTNTIgniter.INV_SIZE, INV_END = INV_START+26,
			HOTBAR_START = INV_END+1, HOTBAR_END = HOTBAR_START+8;
	
	public ContainerItem(EntityPlayer player, IInventory playerInv, InventoryTNTIgniter inventoryTNTIgniter)
	{
		this.inventory = inventoryTNTIgniter;
		
		// Tile Entity, Slot 0-8, Slot IDs 0-8
	    for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 3; ++x) {
	            this.addSlotToContainer(new CustomSlot(this.inventory, x + y * 3, 8 + x * 18, 80 + y * 18));
	        }
	    }

	    // Player Inventory, Slot 9-35, Slot IDs 9-35
	    for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 9; ++x) {
	            this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 48 + x * 18, 176 + y * 18));
	        }
	    }

	    // Player Inventory, Slot 0-8, Slot IDs 36-44
	    for (int x = 0; x < 9; ++x) {
	        this.addSlotToContainer(new Slot(playerInv, x, 48 + x * 18, 234));
	    }
	}
	
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return true;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		World world = player.worldObj;

		if (!world.isRemote)
		{
			ItemStack stack = player.getHeldItemMainhand();
			inventory.writeToNBT(stack.getTagCompound());
		}

		super.onContainerClosed(player);
	}
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			// If item is in our custom Inventory or armor slot
			if (index < INV_START)
			{
				// try to place in player inventory / action bar
				if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END+1, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			// Item is in inventory / hotbar, try to place in custom inventory or armor slots
			else
			{
				/**
				 * Implementation number 1: Shift-click into your custom inventory
				 */
				if (index >= INV_START)
				{
					// place in custom inventory
					if (!this.mergeItemStack(itemstack1, 0, INV_START, false))
					{
						return null;
					}
				}
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}

		return itemstack;
	}

	@Override
	public ItemStack slotClick(int slot, int button, ClickType click, EntityPlayer player) 
	{
		if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getActiveItemStack()) 
		{
			return null;
		}
		return super.slotClick(slot, button, click, player);
	}
}

