package twopiradians.customTNTIgniter.common.item;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import twopiradians.customTNTIgniter.common.CustomTNTIgniter;
import twopiradians.customTNTIgniter.common.entity.EntityCustomTNTPrimed;


public class ItemTNTIgniter extends Item
{
	public boolean silk;
	public int dropAll;
	public float power;
	/**0 = none, 1 = whitelist, 2 = blacklist*/
	public int filter;
	public boolean damageEntities;
	public boolean killItems;
	public int fuse;
	public boolean enchanted;
	public ArrayList<ItemStack> whitelist;
	public boolean shouldExplode;
	public int gunpowder;

	public ItemTNTIgniter()
	{
		super();
		silk = false;
		dropAll = 1;
		power = 4.0F;
		filter = 0;
		damageEntities = true;
		killItems = true;
		fuse = 80;		
		setMaxStackSize(1);
		whitelist = new ArrayList<ItemStack>();

	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (!worldIn.isRemote)
		{
			Block blockLookingAt = null;
			Vec3d posVec = playerIn.getPositionVector().addVector(0, playerIn.getEyeHeight(), 0);
			Vec3d lookVec = playerIn.getLookVec();
			RayTraceResult mop = worldIn.rayTraceBlocks(posVec, lookVec);
			if (mop != null && mop.getBlockPos() != null)
			{
				blockLookingAt = worldIn.getBlockState(mop.getBlockPos()).getBlock() ; 
			}
			if (!playerIn.isSneaking() && blockLookingAt != Blocks.TNT) 
			{
				if (EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(33), stack) > 0)
					enchanted = true;
				else
					enchanted = false;
				playerIn.openGui(CustomTNTIgniter.instance, CustomTNTIgniter.GUI_ITEM_INV, worldIn, 0, 0, 0);
			}
			else if (!playerIn.isSneaking() && blockLookingAt == Blocks.TNT && this.shouldExplode)
			{
				worldIn.setBlockToAir(mop.getBlockPos());
			}
		}
		return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));	
	}

	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (playerIn.getHeldItem(hand) == null || !(playerIn.getHeldItem(hand).getItem() instanceof ItemTNTIgniter))
			return EnumActionResult.FAIL;
		
		final Block block = worldIn.getBlockState(pos).getBlock();

		if (block == Blocks.TNT && !worldIn.isRemote && !playerIn.isSneaking()) 
		{
			int gunpowder = (int) (this.power < 5 ? 0 : this.power);
			if (playerIn.capabilities.isCreativeMode)
				gunpowder = 0;
			int needed = gunpowder;
			if (gunpowder > 0)
			{
				for (int i = 0; i < playerIn.inventory.getSizeInventory(); ++i)
				{
					if(playerIn.inventory.getStackInSlot(i) != null && Items.GUNPOWDER == playerIn.inventory.getStackInSlot(i).getItem())
						needed -= playerIn.inventory.getStackInSlot(i).stackSize;
				}
			}
			if(needed <= 0)
			{
				if (gunpowder > 0)
				{
					for (int i = 0; i < playerIn.inventory.getSizeInventory(); i++)
					{
						if(playerIn.inventory.getStackInSlot(i) != null && Items.GUNPOWDER == playerIn.inventory.getStackInSlot(i).getItem() && gunpowder > 0)
						{
							if(gunpowder > playerIn.inventory.getStackInSlot(i).stackSize)
							{
								gunpowder -= playerIn.inventory.getStackInSlot(i).stackSize;
								playerIn.inventory.decrStackSize(i, playerIn.inventory.getStackInSlot(i).stackSize);	
							}
							else
							{
								playerIn.inventory.decrStackSize(i, gunpowder);
								gunpowder = 0;
							}
						}
					}
				}
				EntityCustomTNTPrimed entitytntprimed = new EntityCustomTNTPrimed(worldIn, (double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), playerIn, (ItemTNTIgniter) playerIn.getHeldItem(hand).getItem());
				worldIn.spawnEntityInWorld(entitytntprimed);
				worldIn.playSound(playerIn, pos, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
				this.shouldExplode = true;
				return EnumActionResult.SUCCESS;
			}
			else
			{
				playerIn.addChatMessage(new TextComponentString("You need " + gunpowder + " gunpowder for this explosion.").setStyle(new Style().setBold(true).setColor(TextFormatting.RED).setItalic(true)));
				this.shouldExplode = false;
			}
		}
		return EnumActionResult.FAIL;
	}  
}
