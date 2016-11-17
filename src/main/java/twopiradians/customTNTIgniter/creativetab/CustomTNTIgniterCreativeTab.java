package twopiradians.customTNTIgniter.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import twopiradians.customTNTIgniter.common.item.ModItems;

public class CustomTNTIgniterCreativeTab extends CreativeTabs 
{
	public CustomTNTIgniterCreativeTab(String label) 
	{
		super(label);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.tnt_igniter;
	}
	
}

