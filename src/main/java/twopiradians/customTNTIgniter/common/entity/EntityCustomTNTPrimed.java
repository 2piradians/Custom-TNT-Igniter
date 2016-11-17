package twopiradians.customTNTIgniter.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import twopiradians.customTNTIgniter.common.CustomExplosion;
import twopiradians.customTNTIgniter.common.item.ItemTNTIgniter;


public class EntityCustomTNTPrimed extends Entity
{
	public EntityLivingBase tntPlacedBy;
	public ItemTNTIgniter igniter;
	public int fuse;
	public float power;
    public static final DataParameter<Integer> FUSE = EntityDataManager.<Integer>createKey(Entity.class, DataSerializers.VARINT);
    public static final DataParameter<Float> POWER = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);


	public EntityCustomTNTPrimed(World worldIn)
	{
		super(worldIn);
		this.preventEntitySpawning = true;
		this.setSize(0.98F, 0.98F);
	}

	public EntityCustomTNTPrimed(World worldIn, double p_i1730_2_, double p_i1730_4_, double p_i1730_6_, EntityLivingBase p_i1730_8_, ItemTNTIgniter igniter)
	{
		this(worldIn);
		this.setPosition(p_i1730_2_, p_i1730_4_, p_i1730_6_);
		float f = (float)(Math.random() * Math.PI * 2.0D);
		this.motionX = (double)(-((float)Math.sin((double)f)) * 0.02F);
		this.motionY = 0.20000000298023224D;
		this.motionZ = (double)(-((float)Math.cos((double)f)) * 0.02F);
		this.prevPosX = p_i1730_2_;
		this.prevPosY = p_i1730_4_;
		this.prevPosZ = p_i1730_6_;
		this.tntPlacedBy = p_i1730_8_;
		this.igniter = igniter;

		this.fuse = (int) igniter.fuse;
		this.power = igniter.power;
		if(!worldIn.isRemote)
		{
			this.dataManager.set(FUSE, this.igniter.fuse);
			this.dataManager.set(POWER, this.igniter.power);
		}
	}

	protected void entityInit() 
	{
		this.dataManager.register(FUSE, Integer.valueOf(0));
		this.dataManager.register(POWER, Float.valueOf(0));
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	protected boolean canTriggerWalking()
	{
		return false;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		if (this.worldObj.isRemote && this.ticksExisted == 1 && this.fuse == 0 && this.power == 0)
		{
			this.fuse = this.dataManager.get(FUSE);
			this.power = this.dataManager.get(POWER);
		}
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.motionY -= 0.03999999910593033D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9800000190734863D;
		this.motionY *= 0.9800000190734863D;
		this.motionZ *= 0.9800000190734863D;

		if (this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
			this.motionY *= -0.5D;
		}

		if (this.fuse-- <= 0)
		{
			this.setDead();

			this.explode();
		}
		else
		{
			this.handleWaterMovement();
			this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}

	private void explode()	
	{
		//TODO Explosion power
		CustomExplosion explosion = new CustomExplosion(this.worldObj, this.tntPlacedBy, this.posX, this.posY + 0.49D, this.posZ, this.power, false, true, this.igniter);
		if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.worldObj, explosion)) return;
			explosion.doExplosionA();
		explosion.doExplosionB(true); 
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	protected void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		tagCompound.setByte("Fuse", (byte)this.fuse);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	protected void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		this.fuse = tagCompund.getByte("Fuse");
	}

	/**
	 * returns null or the entityliving it was placed or ignited by
	 */
	public EntityLivingBase getTntPlacedBy()
	{
		return this.tntPlacedBy;
	}

	public float getEyeHeight()
	{
		return 0.0F;
	}
}