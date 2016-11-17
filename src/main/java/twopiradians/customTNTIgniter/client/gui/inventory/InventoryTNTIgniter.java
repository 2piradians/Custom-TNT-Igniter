package twopiradians.customTNTIgniter.client.gui.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import twopiradians.customTNTIgniter.common.item.ItemTNTIgniter;


public class InventoryTNTIgniter implements IInventory
{
	public ItemStack[] inventory;
	public String name;
	public ItemTNTIgniter igniter;
	public static final int INV_SIZE = 9;
	public boolean def;

	public InventoryTNTIgniter(ItemStack stack) 
	{
		if(stack != null)
		{
			this.igniter = (ItemTNTIgniter) stack.getItem();
			this.inventory = new ItemStack[this.getSizeInventory()];
			if (!stack.hasTagCompound()) 
				stack.setTagCompound(new NBTTagCompound());
			readFromNBT(stack.getTagCompound());
		}
	}

	public String getCustomName() 
	{
		return this.name;
	}

	public void setCustomName(String igniter) 
	{
		this.name = igniter;
	}


	@Override
	public String getName() 
	{
		return this.hasCustomName() ? this.name : "container.igniter_inventory";
	}

	@Override
	public boolean hasCustomName() 
	{
		return this.name != null && !this.name.equals("");
	}

	@Override
	public ITextComponent getDisplayName() 
	{
		return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
	}

	@Override
	public int getSizeInventory() 
	{
		return 9;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		if (index < 0 || index >= this.getSizeInventory() || this.inventory == null)
			return null;
		return this.inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) 
	{
		if (this.getStackInSlot(index) != null) {
			ItemStack itemstack;

			if (this.getStackInSlot(index).stackSize <= count) {
				itemstack = this.getStackInSlot(index);
				this.setInventorySlotContents(index, null);
				this.markDirty();
				return itemstack;
			} else {
				itemstack = this.getStackInSlot(index).splitStack(count);

				if (this.getStackInSlot(index).stackSize <= 0) {
					this.setInventorySlotContents(index, null);
				} else {
					//Just to show that changes happened
					this.setInventorySlotContents(index, this.getStackInSlot(index));
				}

				this.markDirty();
				return itemstack;
			}
		} else {
			return null;
		}
	}

	public ItemStack getStackInSlotOnClosing(int index) 
	{
		ItemStack stack = this.getStackInSlot(index);
		this.setInventorySlotContents(index, stack);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) 
	{
		if (index < 0 || index >= this.getSizeInventory())
			return;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();

		if (stack != null && stack.stackSize == 0)
			stack = null;

		this.inventory[index] = stack;
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}

	@Override
	public void markDirty() 
	{

	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) 
	{

	}

	@Override
	public void closeInventory(EntityPlayer player) 
	{

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) 
	{
		return true;
	}

	@Override
	public int getField(int id) 
	{
		return 0;
	}

	@Override
	public void setField(int id, int value) 
	{

	}

	@Override
	public int getFieldCount() 
	{
		return 0;
	}

	@Override
	public void clear() 
	{
		for (int i = 0; i < this.getSizeInventory(); i++)
			this.setInventorySlotContents(i, null);
	}

	public void writeToNBT(NBTTagCompound nbt) 
	{

		nbt.setBoolean("Default", def);
		if(this.igniter.enchanted)
		{
			nbt.setBoolean("Silk", igniter.silk);
			nbt.setInteger("Drop All", igniter.dropAll);
		}
		nbt.setFloat("Power", igniter.power);
		nbt.setInteger("Filter", igniter.filter);
		nbt.setBoolean("Damage Entities", igniter.damageEntities);
		nbt.setBoolean("Kill Items", igniter.killItems);
		nbt.setFloat("Fuse", igniter.fuse);

		NBTTagList list = new NBTTagList();
		this.igniter.whitelist.clear();

		for (int i = 0; i < this.getSizeInventory(); ++i) 
		{

			if (this.getStackInSlot(i) != null) 
			{
				//TODO add to whitelist
				NBTTagCompound stackTag = new NBTTagCompound();
				stackTag.setByte("Slot", (byte) i);
				this.getStackInSlot(i).writeToNBT(stackTag);
				ItemStack stack = this.getStackInSlot(i);
				this.igniter.whitelist.add(stack);
				list.appendTag(stackTag);
			}
		}

		nbt.setTag("Items", list);

		if (this.hasCustomName()) 
		{
			nbt.setString("CustomName", this.getCustomName());
		}
	}


	public void readFromNBT(NBTTagCompound nbt)
	{
		this.def = nbt.getBoolean("Default");
		if(this.igniter.enchanted)
		{
			igniter.silk = nbt.getBoolean("Silk");
			igniter.dropAll = nbt.getInteger("Drop All");
		}
		igniter.power = nbt.getFloat("Power");
		igniter.filter = nbt.getInteger("Filter");
		igniter.damageEntities = nbt.getBoolean("Damage Entities");
		igniter.killItems = nbt.getBoolean("Kill Items");
		igniter.fuse = nbt.getInteger("Fuse");
		if (!def)
		{
			this.igniter.silk = false;
			this.igniter.dropAll = 1;
			this.igniter.power = 4.0F;
			this.igniter.filter = 0;
			this.igniter.damageEntities = true;
			this.igniter.killItems = true;
			this.igniter.fuse = 80;		
			def = true;
		}
		NBTTagList list = nbt.getTagList("Items", 10);
		for (int i = 0; i < list.tagCount(); ++i) 
		{
			NBTTagCompound stackTag = list.getCompoundTagAt(i);
			int slot = stackTag.getByte("Slot") & 255;
			this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
		}

		if (nbt.hasKey("CustomName", 8)) 
		{
			this.setCustomName(nbt.getString("CustomName"));
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		return null;
	}
}
