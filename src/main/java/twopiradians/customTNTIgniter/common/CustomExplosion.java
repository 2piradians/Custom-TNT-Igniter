package twopiradians.customTNTIgniter.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twopiradians.customTNTIgniter.client.gui.inventory.InventoryTNTIgniter;
import twopiradians.customTNTIgniter.common.item.ItemTNTIgniter;



public class CustomExplosion extends Explosion
{
	//used for debugging
	@SuppressWarnings("unused")
	private ArrayList<BlockPos> posToDestroy = new ArrayList<BlockPos>();

	private final boolean isSmoking;
	private final World worldObj;
	private final double explosionX;
	private final double explosionY;
	private final double explosionZ;
	private final Entity exploder;
	private final float explosionSize;
	private final List affectedBlockPositions;
	private final Map field_77288_k;
	private final Vec3d position;
	public ItemTNTIgniter igniter;
	public InventoryTNTIgniter inventory;

	@SideOnly(Side.CLIENT)
	public CustomExplosion(World worldIn, Entity placer, double posX, double posY, double posZ, float power, List p_i45752_10_)
	{
		this(worldIn, placer, posX, posY, posZ, power, false, true, p_i45752_10_);
	}

	@SideOnly(Side.CLIENT)
	public CustomExplosion(World worldIn, Entity placer, double posX, double posY, double posZ, float power, boolean p_i45753_10_, boolean p_i45753_11_, List p_i45753_12_)
	{
		this(worldIn, placer, posX, posY, posZ, power, p_i45753_10_, p_i45753_11_, (ItemTNTIgniter)null);
		this.affectedBlockPositions.addAll(p_i45753_12_);
	}

	public CustomExplosion(World worldIn, Entity placer, double posX, double posY, double posZ, float power, boolean p_i45754_10_, boolean p_i45754_11_, ItemTNTIgniter igniter)
	{
		super(worldIn, placer, posX, posY, posZ, power, p_i45754_10_, p_i45754_11_);
		new Random();
		this.affectedBlockPositions = Lists.newArrayList();
		this.field_77288_k = Maps.newHashMap();
		this.worldObj = worldIn;
		this.exploder = placer;
		this.explosionSize = power;
		this.explosionX = posX;
		this.explosionY = posY;
		this.explosionZ = posZ;
		this.isSmoking = p_i45754_11_;
		this.position = new Vec3d(explosionX, explosionY, explosionZ);
		this.igniter = igniter;
	}

	/**
	 * Does the first part of the explosion (destroy blocks)
	 */
	public void doExplosionA()
	{
		HashSet hashset = Sets.newHashSet();
		int j;
		int k;

		for (int i = 0; i < 16; ++i)
		{
			for (j = 0; j < 16; ++j)
			{
				for (k = 0; k < 16; ++k)
				{
					if (i == 0 || i == 15 || j == 0 || j == 15 || k == 0 || k == 15)
					{
						double d0 = (double)((float)i / 15.0F * 2.0F - 1.0F);
						double d1 = (double)((float)j / 15.0F * 2.0F - 1.0F);
						double d2 = (double)((float)k / 15.0F * 2.0F - 1.0F);
						double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
						d0 /= d3;
						d1 /= d3;
						d2 /= d3;
						float f = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
						double d4 = this.explosionX;
						double d6 = this.explosionY;
						double d8 = this.explosionZ;

						for (; f > 0.0F; f -= 0.22500001F)
						{
							BlockPos blockpos = new BlockPos(d4, d6, d8);
							IBlockState iblockstate = this.worldObj.getBlockState(blockpos);

							if (iblockstate.getMaterial() != Material.AIR)
							{
								float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.worldObj, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(worldObj, blockpos, (Entity)null, this);
								f -= (f2 + 0.3F) * 0.3F;
							}

							if (f > 0.0F && (this.exploder == null || this.exploder.verifyExplosion(this, this.worldObj, blockpos, iblockstate, f)))
							{
								//TODO filter ; hashset = list of blockpos to destroy
								if (!this.worldObj.isRemote)
								{
									if (this.igniter.whitelist.isEmpty() && this.igniter.filter != 1)
										hashset.add(blockpos);
									for (int p=0; p<this.igniter.whitelist.size(); p++)
									{
										if(!(this.worldObj.getBlockState(blockpos).getBlock() instanceof BlockAir))
										{
											Block block = ((ItemBlock) this.igniter.whitelist.get(p).getItem()).getBlock();
											//whitelist: if block and meta match, add to hashset
											//blacklist: if block and meta don't match, add to hashset
											if (       (igniter.filter == 1 &&   block.equals(this.worldObj.getBlockState(blockpos).getBlock()) && this.igniter.whitelist.get(p).getMetadata() == this.worldObj.getBlockState(blockpos).getBlock().damageDropped(iblockstate))
													|| (igniter.filter == 2 && !(block.equals(this.worldObj.getBlockState(blockpos).getBlock()) && this.igniter.whitelist.get(p).getMetadata() == this.worldObj.getBlockState(blockpos).getBlock().damageDropped(iblockstate))) 
													||  igniter.filter == 0)
												hashset.add(blockpos);	
										}
									}
									if(igniter.filter == 2)//so why did we need this? (we should have commented here)
									{
										ArrayList<BlockPos> posToRemove = new ArrayList<BlockPos>();
										for (Object pos : hashset)
										{
											ItemStack item = new ItemStack(Item.getItemFromBlock(this.worldObj.getBlockState((BlockPos) pos).getBlock()), 1, this.worldObj.getBlockState((BlockPos) pos).getBlock().getMetaFromState(this.worldObj.getBlockState((BlockPos) pos)));
											for(ItemStack stack : this.igniter.whitelist)
											{	
												if(item.getItem() == stack.getItem() && item.getMetadata() == stack.getMetadata())
													posToRemove.add((BlockPos) pos);
											}
										}
										for (BlockPos pos : posToRemove)
											hashset.remove(pos);
									}
									//DEBUG
									/*for (Object pos : hashset)//print hashset contents for debugging
										if (!this.posToDestroy.contains(pos))
											this.posToDestroy.add((BlockPos) pos);
									for (BlockPos pos : this.posToDestroy)
										System.out.println("AFTER " + new ItemStack(Item.getItemFromBlock(this.worldObj.getBlockState((BlockPos) pos).getBlock()), 1, this.worldObj.getBlockState(pos).getBlock().getMetaFromState(this.worldObj.getBlockState(pos))).getDisplayName() + " : " + ((BlockPos)pos));
									System.out.println("---------------------------------");
									hashset.clear();//clear so nothing is destroyed for debugging
									 */									//END DEBUG
								}
								else
									hashset.add(blockpos);
							}

							d4 += d0 * 0.30000001192092896D;
							d6 += d1 * 0.30000001192092896D;
							d8 += d2 * 0.30000001192092896D;
						}
					}
				}
			}
		}

		this.affectedBlockPositions.addAll(hashset);
		float f3 = this.explosionSize * 2.0F;
		j = MathHelper.floor_double(this.explosionX - (double)f3 - 1.0D);
		k = MathHelper.floor_double(this.explosionX + (double)f3 + 1.0D);
		int j1 = MathHelper.floor_double(this.explosionY - (double)f3 - 1.0D);
		int l = MathHelper.floor_double(this.explosionY + (double)f3 + 1.0D);
		int k1 = MathHelper.floor_double(this.explosionZ - (double)f3 - 1.0D);
		int i1 = MathHelper.floor_double(this.explosionZ + (double)f3 + 1.0D);
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double)j, (double)j1, (double)k1, (double)k, (double)l, (double)i1));
		net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.worldObj, this, list, f3);
		Vec3d vec3 = new Vec3d(this.explosionX, this.explosionY, this.explosionZ);

		for (int l1 = 0; l1 < list.size(); ++l1) 
		{
			Entity entity = (Entity)list.get(l1);

			if (!entity.isImmuneToExplosions())
			{
				double d12 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double)f3;

				if (d12 <= 1.0D)
				{
					double d5 = entity.posX - this.explosionX;
					double d7 = entity.posY + (double)entity.getEyeHeight() - this.explosionY;
					double d9 = entity.posZ - this.explosionZ;
					double d13 = (double)MathHelper.sqrt_double(d5 * d5 + d7 * d7 + d9 * d9);

					if (d13 != 0.0D)
					{
						d5 /= d13;
						d7 /= d13;
						d9 /= d13;
						double d14 = (double)this.worldObj.getBlockDensity(vec3, entity.getEntityBoundingBox());
						double d10 = (1.0D - d12) * d14;
						double d11 = 1.0D;
						//TODO damage entities and kill items
						if (!this.worldObj.isRemote)
						{
							if (igniter.damageEntities && !(entity instanceof EntityItem))
								entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float)((int)((d10 * d10 + d10) / 2.0D * 8.0D * (double)f3 + 1.0D)));
							else if (igniter.killItems && (entity instanceof EntityItem))
								entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float)((int)((d10 * d10 + d10) / 2.0D * 8.0D * (double)f3 + 1.0D)));
						}
						else
							entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float)((int)((d10 * d10 + d10) / 2.0D * 8.0D * (double)f3 + 1.0D)));
						if (entity instanceof EntityLivingBase)
							d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, d10);				
						entity.motionX += d5 * d11;
						entity.motionY += d7 * d11;
						entity.motionZ += d9 * d11;

						if (entity instanceof EntityPlayer)
							this.field_77288_k.put((EntityPlayer)entity, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
					}
				}
			}
		}
	}

	/**
	 * Does the second part of the explosion (sound, particles, drop spawn)
	 */
	public void doExplosionB(boolean p_77279_1_)
	{
		this.worldObj.playSound((EntityPlayer)null, this.explosionX, this.explosionY, this.explosionZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

		if (this.explosionSize >= 2.0F && this.isSmoking)
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);
		else
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.explosionX, this.explosionY, this.explosionZ, 1.0D, 0.0D, 0.0D, new int[0]);

		Iterator iterator;
		BlockPos blockpos;

		if (this.isSmoking)
		{
			iterator = this.affectedBlockPositions.iterator();

			while (iterator.hasNext())
			{
				blockpos = (BlockPos)iterator.next();
				IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
				Block block = iblockstate.getBlock();

				if (this.explosionSize < 7 || this.worldObj.rand.nextDouble() <= 3/(double)this.explosionSize)
				{
					double d0 = (double)((float)blockpos.getX() + this.worldObj.rand.nextFloat());
					double d1 = (double)((float)blockpos.getY() + this.worldObj.rand.nextFloat());
					double d2 = (double)((float)blockpos.getZ() + this.worldObj.rand.nextFloat());
					double d3 = d0 - this.explosionX;
					double d4 = d1 - this.explosionY;
					double d5 = d2 - this.explosionZ;
					double d6 = (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
					d3 /= d6;
					d4 /= d6;
					d5 /= d6;
					double d7 = 0.5D / (d6 / (double)this.explosionSize + 0.1D);
					d7 *= (double)(this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
					d3 *= d7;
					d4 *= d7;
					d5 *= d7;
					this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + this.explosionX * 1.0D) / 2.0D, (d1 + this.explosionY * 1.0D) / 2.0D, (d2 + this.explosionZ * 1.0D) / 2.0D, d3, d4, d5, new int[0]);
					this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
				}

				if (iblockstate.getMaterial() != Material.AIR)
				{
					//TODO drop items and silk touch
					if (block.canDropFromExplosion(this))
					{
						if (!this.worldObj.isRemote)
						{
							if (igniter.dropAll == 2 && igniter.silk)
							{
								ItemStack itemStack = new ItemStack(Item.getItemFromBlock(block));
								if(block instanceof BlockTallGrass)
								{
									itemStack.setItemDamage(block.getMetaFromState(iblockstate));
									this.spawnEntityItem(worldObj, blockpos, itemStack);
								}
								else if (block instanceof BlockDoublePlant)
								{
									if(this.worldObj.getBlockState(blockpos.down()).getBlock() instanceof BlockDoublePlant)
									{
										iblockstate = this.worldObj.getBlockState(blockpos.down());
										itemStack.setItemDamage(block.getMetaFromState(iblockstate));
										this.spawnEntityItem(worldObj, blockpos, itemStack);
									}
									else
									{
										iblockstate = this.worldObj.getBlockState(blockpos);
										itemStack.setItemDamage(block.getMetaFromState(iblockstate));
										this.spawnEntityItem(worldObj, blockpos, itemStack);
									}
								}
								else
								{
									itemStack.setItemDamage(block.damageDropped(iblockstate));
									this.spawnEntityItem(worldObj, blockpos, itemStack);
								}
							}
							else if (igniter.dropAll == 1 && igniter.silk)
							{
								if (worldObj.rand.nextFloat() <= (1.0F / this.explosionSize))
								{
									ItemStack itemStack = new ItemStack(Item.getItemFromBlock(block));
									itemStack.setItemDamage(block.damageDropped(iblockstate));
									this.spawnEntityItem(worldObj, blockpos, itemStack);
								}
							}
							else if (igniter.dropAll == 2 && !igniter.silk)
								block.dropBlockAsItem(this.worldObj, blockpos, this.worldObj.getBlockState(blockpos), 0);
							else if (igniter.dropAll == 0)
							{}	
							else
								block.dropBlockAsItemWithChance(this.worldObj, blockpos, this.worldObj.getBlockState(blockpos), 1.0F / this.explosionSize, 0);
						}
						else
							block.dropBlockAsItemWithChance(this.worldObj, blockpos, this.worldObj.getBlockState(blockpos), 1.0F / this.explosionSize, 0);
					}
					if (!this.worldObj.isRemote)
						block.onBlockExploded(this.worldObj, blockpos, this);
				}
			}
		}
	}

	private void spawnEntityItem(World world, BlockPos pos, ItemStack itemStack)
	{
		if (itemStack != null)
		{
			float f = 0.5F;
			double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			EntityItem entityitem = new EntityItem(world, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemStack);
			entityitem.setDefaultPickupDelay();
			world.spawnEntityInWorld(entityitem);	
		}
	}

	public Map func_77277_b()
	{
		return this.field_77288_k;
	}

	/**
	 * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
	 */
	public EntityLivingBase getExplosivePlacedBy()
	{
		return this.exploder == null ? null : (this.exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed)this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase)this.exploder : null));
	}

	public void func_180342_d()
	{
		this.affectedBlockPositions.clear();
	}

	public List func_180343_e()
	{
		return this.affectedBlockPositions;
	}

	public Vec3d getPosition(){ return this.position; }
}