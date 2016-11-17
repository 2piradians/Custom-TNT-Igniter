package twopiradians.customTNTIgniter.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import twopiradians.customTNTIgniter.client.gui.inventory.ContainerItem;
import twopiradians.customTNTIgniter.client.gui.inventory.InventoryTNTIgniter;
import twopiradians.customTNTIgniter.common.CustomTNTIgniter;

public class GuiItemInventory extends GuiContainer
{
	public final InventoryTNTIgniter inventory;
	private GuiButtonExt filter;
	private GuiButtonExt killItems;
	private GuiButtonExt reset;
	private GuiButtonExt powerAdd;
	private GuiButtonExt powerSubtract;
	private GuiButtonExt fuseAdd;
	private GuiButtonExt fuseSubtract;
	private GuiButtonExt dropItems;
	private GuiButtonExt silkTouch;
	private GuiButtonExt damageEntities;
	public int fuseSec;


	public GuiItemInventory(ContainerItem containerItem) 
	{
		super(containerItem);
		this.inventory = containerItem.inventory;

		this.xSize = 256;
		this.ySize = 256;
		this.fuseSec = this.inventory.igniter.fuse / 20;
	}

	@Override
	public void initGui() 
	{
		//TODO buttons
		super.initGui();
		if (this.inventory.igniter.enchanted)
		{
			this.buttonList.add(this.dropItems = new GuiButtonExt(7, this.width / 2 + 71, this.height / 2 - 74, 40, 14, this.inventory.igniter.dropAll == 0 ? "None" : this.inventory.igniter.dropAll == 1 ? "Normal" : "All"));
			this.dropItems.enabled = true;
		}
		else
		{
			this.buttonList.add(this.dropItems = new GuiButtonExt(7, this.width / 2 + 71, this.height / 2 - 74, 40, 14, this.inventory.igniter.dropAll == 0 ? "None" : this.inventory.igniter.dropAll == 1 ? "Normal" : "All"));
			this.dropItems.enabled = false;
		}
		if (this.inventory.igniter.enchanted)
		{
			this.buttonList.add(this.silkTouch = new GuiButtonExt(8, this.width / 2 + 71, this.height / 2 - 106, 40, 14, this.inventory.igniter.silk ? "Yes" : "No"));
			this.silkTouch.enabled = true;
		}
		else
		{
			this.buttonList.add(this.silkTouch = new GuiButtonExt(8, this.width / 2 + 71, this.height / 2 - 106, 40, 14, this.inventory.igniter.silk ? "Yes" : "No"));
			this.silkTouch.enabled = false;
		}
		this.buttonList.add(this.killItems = new GuiButtonExt(1, this.width / 2 + 70, this.height / 2 + 26, 40, 14, this.inventory.igniter.killItems ? "Yes" : "No"));
		this.buttonList.add(this.damageEntities = new GuiButtonExt(9, this.width / 2 - 110, this.height / 2 + 26, 40, 14, this.inventory.igniter.damageEntities ? "Yes" : "No"));
		this.buttonList.add(this.filter = new GuiButtonExt(0, this.width / 2 - 119, this.height / 2 - 66, 50, 14, this.inventory.igniter.filter == 0 ? "No Filter" : this.inventory.igniter.filter == 1 ? "Whitelist" : "Blacklist"));
		this.buttonList.add(this.reset = new GuiButtonExt(2, this.width / 2 -20, this.height / 2 + 26, 40, 14, "Reset"));
		this.buttonList.add(this.fuseAdd = new GuiButtonExt(3, this.width / 2 - 26, this.height / 2 - 12, 14, 14, "+"));
		this.buttonList.add(this.fuseSubtract = new GuiButtonExt(4, this.width / 2 - 60, this.height / 2 - 12, 14, 14, "-"));
		this.buttonList.add(this.powerAdd = new GuiButtonExt(5, this.width / 2 + 36, this.height / 2 - 74, 14, 14, "+"));
		this.buttonList.add(this.powerSubtract = new GuiButtonExt(6, this.width / 2-2, this.height / 2 - 74, 14, 14, "-"));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException 
	{
		if (button == this.dropItems)
		{
			if(!this.inventory.igniter.enchanted)
			{

			}
			else
			{

				if (this.inventory.igniter.dropAll == 1)
				{
					this.inventory.igniter.dropAll = 2;
					button.displayString = "All";
				}
				else if (this.inventory.igniter.dropAll == 2)
				{
					this.inventory.igniter.dropAll = 0;
					button.displayString = "None";
				}
				else
				{
					this.inventory.igniter.dropAll = 1;
					button.displayString = "Normal";
				}
			}
		}
		else if (button == this.silkTouch)
		{
			if(this.inventory.igniter.enchanted)
			{
				if (this.inventory.igniter.silk)
				{
					this.inventory.igniter.silk = false;
					button.displayString = "No";
				}
				else
				{
					this.inventory.igniter.silk = true;
					button.displayString = "Yes";
				}
			}
		}
		else if (button == this.killItems)
		{
			if (this.inventory.igniter.killItems)
			{
				this.inventory.igniter.killItems = false;
				button.displayString = "No";
			}
			else
			{
				this.inventory.igniter.killItems = true;
				button.displayString = "Yes";
			}
		}
		else if (button == this.damageEntities)
		{
			if (this.inventory.igniter.damageEntities)
			{
				this.inventory.igniter.damageEntities = false;
				button.displayString = "No";
			}
			else
			{
				this.inventory.igniter.damageEntities = true;
				button.displayString = "Yes";
			}
		}
		else if (button == this.fuseAdd)
		{
			if (fuseSec > 19)
			{}
			else if (fuseSec + 5 <= 20 && GuiScreen.isShiftKeyDown())
			{
				fuseSec += 5;
				this.inventory.igniter.fuse = fuseSec * 20;
			}
			else if (fuseSec + 5 > 20 && GuiScreen.isShiftKeyDown())
			{
				fuseSec = 20;
				this.inventory.igniter.fuse = fuseSec * 20;
			}
			else
			{
				++fuseSec;
				this.inventory.igniter.fuse = fuseSec * 20;
			}
		}
		else if (button == this.fuseSubtract)
		{
			if (fuseSec < 1)
			{}
			else if (fuseSec - 5 >= 0 && GuiScreen.isShiftKeyDown())
			{
				fuseSec -= 5;
				this.inventory.igniter.fuse = fuseSec * 20;
			}
			else if (fuseSec - 5 < 0 && GuiScreen.isShiftKeyDown())
			{
				fuseSec = 0;
				this.inventory.igniter.fuse = fuseSec * 20;
			}
			else
			{
				--fuseSec;
				this.inventory.igniter.fuse = fuseSec * 20;
			}
		}
		else if (button == this.powerAdd)
		{
			if (this.inventory.igniter.power > 63)
			{}
			else if (this.inventory.igniter.power + 5 <= 64 && GuiScreen.isShiftKeyDown())
				this.inventory.igniter.power += 5;
			else if (this.inventory.igniter.power + 5 > 64 && GuiScreen.isShiftKeyDown())
				this.inventory.igniter.power = 64;
			else
				++this.inventory.igniter.power;
		}
		else if (button == this.powerSubtract)
		{
			if (this.inventory.igniter.power < 1)
			{}
			else if (this.inventory.igniter.power - 5 >= 0 && GuiScreen.isShiftKeyDown())
				this.inventory.igniter.power -= 5;
			else if (this.inventory.igniter.power - 5 < 0 && GuiScreen.isShiftKeyDown())
				this.inventory.igniter.power = 0;
			else
				--this.inventory.igniter.power;
		}
		else if (button == this.filter)
		{
			if (this.inventory.igniter.filter == 0)
			{
				this.inventory.igniter.filter = 1;
				button.displayString = "Whitelist";
			}
			else if (this.inventory.igniter.filter == 1)
			{
				this.inventory.igniter.filter = 2;
				button.displayString = "Blacklist";
			}
			else
			{
				this.inventory.igniter.filter = 0;
				button.displayString = "No Filter";
			}
		}
		else if (button == this.reset)
		{
			//TODO reset
			this.inventory.igniter.filter = 0;
			this.filter.displayString = "No Filter";
			this.inventory.igniter.silk = false;
			this.silkTouch.displayString = "No";
			this.inventory.igniter.dropAll = 1;
			this.dropItems.displayString = "Normal";
			this.fuseSec = 4;
			this.inventory.igniter.fuse = 80;
			this.inventory.igniter.power = 4.0F;
			this.inventory.igniter.damageEntities = true;
			this.damageEntities.displayString = "Yes";
			this.inventory.igniter.killItems = true;
			this.killItems.displayString = "Yes";
		}
	}



	private static final ResourceLocation guiTextures = new ResourceLocation(CustomTNTIgniter.MODID+":textures/gui/item_inventory.png");

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		this.fontRendererObj.drawString("Power", 138, 42, 4210752);
		this.fontRendererObj.drawString("Damage Entities", 8, 142, 4210752);      
		this.fontRendererObj.drawString("Kill Items", 197, 142, 4210752);      
		this.fontRendererObj.drawString("Silk Touch", 194, 10, 4210752);      
		this.fontRendererObj.drawString("Drop Items", 193, 42, 4210752);  
		this.fontRendererObj.drawString("Fuse", 80, 104, 4210752);        
		this.fontRendererObj.drawString("Igniter Inventory", 86, 6, 4210752);
		if (fuseSec >= 10)
			this.fontRendererObj.drawString(Integer.toString(fuseSec), 86, 120, 4210752);
		else
			this.fontRendererObj.drawString(Integer.toString(fuseSec), 89, 120, 4210752);
		if (this.inventory.igniter.power >= 10)
			this.fontRendererObj.drawString(Integer.toString((int)this.inventory.igniter.power), 147, 58, 4210752);
		else
			this.fontRendererObj.drawString(Integer.toString((int)this.inventory.igniter.power), 150, 58, 4210752);

		//TODO hover over text
		int k = (this.width - this.xSize) / 2; //X axis on GUI
		int l = (this.height - this.ySize) / 2; //Y axis on GUI
		if (mouseX > this.silkTouch.xPosition && mouseX < this.silkTouch.xPosition + this.silkTouch.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.silkTouch.yPosition && mouseY < this.silkTouch.yPosition + this.silkTouch.height)
			{
				if (this.inventory.igniter.enchanted)
				{
					if(this.inventory.igniter.silk)
					{
						List<String> list = new ArrayList<String>();
						list.add("Will silk touch exploded blocks.");
						this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
					}
					else
					{
						List<String> list = new ArrayList<String>();
						list.add("Will not silk touch exploded blocks.");
						this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
					}
				}
				else
				{
					List<String> list = new ArrayList<String>();
					list.add("This requires the Silk Touch enchantment.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
				}
			}
		}
		if (mouseX > this.dropItems.xPosition && mouseX < this.dropItems.xPosition + this.dropItems.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.dropItems.yPosition && mouseY < this.dropItems.yPosition + this.dropItems.height)
			{
				if (this.inventory.igniter.enchanted)
				{
					if(this.inventory.igniter.dropAll == 0)
					{
						List<String> list = new ArrayList<String>();
						list.add("Will not drop items.");
						this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
					}
					else if (this.inventory.igniter.dropAll == 1)
					{
						List<String> list = new ArrayList<String>();
						list.add("Will drop items normally.");
						this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
					}
					else
					{
						List<String> list = new ArrayList<String>();
						list.add("Will drop all items created.");
						this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
					}
				}
				else
				{
					List<String> list = new ArrayList<String>();
					list.add("This requires the Silk Touch enchantment.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
				}
			}
		}
		if (mouseX > this.filter.xPosition && mouseX < this.filter.xPosition + this.filter.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.filter.yPosition && mouseY < this.filter.yPosition + this.filter.height)
			{
				if(this.inventory.igniter.filter == 0)
				{
					List<String> list = new ArrayList<String>();
					list.add("Blocks will be destroyed normally.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
				}
				else if (this.inventory.igniter.filter == 1)
				{
					List<String> list = new ArrayList<String>();
					list.add("Only blocks in whitelist will be destroyed.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
				}
				else
				{
					List<String> list = new ArrayList<String>();
					list.add("Blocks in blacklist will not be destroyed.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
				}
			}
		}
		if (mouseX > this.damageEntities.xPosition && mouseX < this.damageEntities.xPosition + this.damageEntities.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.damageEntities.yPosition && mouseY < this.damageEntities.yPosition + this.damageEntities.height)
			{
				if(this.inventory.igniter.damageEntities)
				{
					List<String> list = new ArrayList<String>();
					list.add("Explosion will damage entities.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
				}
				else
				{
					List<String> list = new ArrayList<String>();
					list.add("Explosion will not damage entities.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
				}
			}
		}
		if (mouseX > this.killItems.xPosition && mouseX < this.killItems.xPosition + this.killItems.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.killItems.yPosition && mouseY < this.killItems.yPosition + this.killItems.height)
			{
				if(this.inventory.igniter.killItems)
				{
					List<String> list = new ArrayList<String>();
					list.add("Explosion will kill items on the ground.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
				}
				else
				{
					List<String> list = new ArrayList<String>();
					list.add("Explosion will not kill items on the ground.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);
				}
			}
		}
		if (mouseX > this.powerSubtract.xPosition && mouseX < this.powerSubtract.xPosition + this.powerSubtract.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.powerSubtract.yPosition && mouseY < this.powerSubtract.yPosition + this.powerSubtract.height)
			{
					List<String> list = new ArrayList<String>();
					list.add("Decreases the power of the explosion by 1 level.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);				
			}
		}
		if (mouseX > this.powerAdd.xPosition && mouseX < this.powerAdd.xPosition + this.powerAdd.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.powerAdd.yPosition && mouseY < this.powerAdd.yPosition + this.powerAdd.height)
			{
					List<String> list = new ArrayList<String>();
					list.add("Increases the power of the explosion by 1 level.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);				
			}
		}
		if (mouseX > this.fuseAdd.xPosition && mouseX < this.fuseAdd.xPosition + this.fuseAdd.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.fuseAdd.yPosition && mouseY < this.fuseAdd.yPosition + this.fuseAdd.height)
			{
					List<String> list = new ArrayList<String>();
					list.add("Increases the fuse delay by 1 second.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);				
			}
		}
		if (mouseX > this.fuseSubtract.xPosition && mouseX < this.fuseSubtract.xPosition + this.fuseSubtract.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.fuseSubtract.yPosition && mouseY < this.fuseSubtract.yPosition + this.fuseSubtract.height)
			{
					List<String> list = new ArrayList<String>();
					list.add("Decreases the fuse delay by 1 second.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);				
			}
		}
		if (mouseX > this.reset.xPosition && mouseX < this.reset.xPosition + this.reset.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.reset.yPosition && mouseY < this.reset.yPosition + this.reset.height)
			{
					List<String> list = new ArrayList<String>();
					list.add("Resets settings to explode TNT normally.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);				
			}
		}
		if (mouseX > this.fuseAdd.xPosition - 25 && mouseX < this.fuseAdd.xPosition - 10 + this.fuseAdd.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.fuseAdd.yPosition - 15 && mouseY < this.fuseAdd.yPosition - 15 + this.fuseAdd.height)
			{
					List<String> list = new ArrayList<String>();
					list.add("Fuse time of the TNT (seconds).");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);				
			}
		}
		if (mouseX > this.fuseAdd.xPosition - 20 && mouseX < this.fuseAdd.xPosition - 14 + this.fuseAdd.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.fuseAdd.yPosition -10 && mouseY < this.fuseAdd.yPosition + this.fuseAdd.height)
			{
					List<String> list = new ArrayList<String>();
					list.add("Fuse time of the TNT (seconds).");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);				
			}
		}
		if (mouseX > this.powerAdd.xPosition - 25 && mouseX < this.powerAdd.xPosition - 10+ this.powerAdd.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.powerAdd.yPosition -15 && mouseY < this.powerAdd.yPosition - 15 + this.powerAdd.height)
			{
					int powderNeeded;
					if (this.inventory.igniter.power > 4)
						powderNeeded = (int) this.inventory.igniter.power;
					else
						powderNeeded = 0;
					List<String> list = new ArrayList<String>();
					list.add("Power level of the TNT.");
					list.add("Requires " + powderNeeded + " gunpowder for the current power level.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);				
			}
		}
		if (mouseX > this.powerAdd.xPosition - 23 && mouseX < this.powerAdd.xPosition - 15 + this.powerAdd.width) //Basically checking if mouse is in the correct area
		{
			if (mouseY > this.powerAdd.yPosition - 10 && mouseY < this.powerAdd.yPosition + this.powerAdd.height)
			{
					int powderNeeded;
					if (this.inventory.igniter.power > 4)
						powderNeeded = (int) this.inventory.igniter.power;
					else
						powderNeeded = 0;
					List<String> list = new ArrayList<String>();
					list.add("Power level of the TNT.");
					list.add("Requires " + powderNeeded + " gunpowder for the current power level.");
					this.drawHoveringText(list, (int)mouseX - k - 8, (int)mouseY - l, fontRendererObj);				
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		this.mc.getTextureManager().bindTexture(guiTextures);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
}