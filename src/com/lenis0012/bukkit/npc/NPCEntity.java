package com.lenis0012.bukkit.npc;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R3.CraftServer;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R3.event.CraftEventFactory;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.minecraft.server.v1_7_R3.DamageSource;
import net.minecraft.server.v1_7_R3.Entity;
import net.minecraft.server.v1_7_R3.EntityDamageSource;
import net.minecraft.server.v1_7_R3.EntityDamageSourceIndirect;
import net.minecraft.server.v1_7_R3.EntityHuman;
import net.minecraft.server.v1_7_R3.EntityLiving;
import net.minecraft.server.v1_7_R3.EntityPlayer;
import net.minecraft.server.v1_7_R3.EnumGamemode;
import net.minecraft.server.v1_7_R3.Material;
import net.minecraft.server.v1_7_R3.MathHelper;
import net.minecraft.server.v1_7_R3.PlayerInteractManager;

public class NPCEntity extends EntityPlayer {
	private final NPC npc;
	private boolean invulnerable = true;
	
	public NPCEntity(World world, NPCProfile profile, NPCNetworkManager networkManager) {
		super(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) world).getHandle(), profile, new PlayerInteractManager(((CraftWorld) world).getHandle()));
		playerInteractManager.b(EnumGamemode.SURVIVAL);
		this.playerConnection = new NPCPlayerConnection(networkManager, this);
		this.fauxSleeping = true;
		this.bukkitEntity = new CraftPlayer((CraftServer) Bukkit.getServer(), this);
		this.npc = new NPC(this);
	}
	
	@Override
	public CraftPlayer getBukkitEntity() {
		return (CraftPlayer) bukkitEntity;
	}
	
	public NPC getNPC() {
		return npc;
	}
	
	public boolean isInvulnerable() {
		return invulnerable;
	}

	public void setInvulnerable(boolean invulnerable) {
		this.invulnerable = invulnerable;
	}
	
	public void kill(){
		this.getBukkitEntity().remove();
	}
	
	@Override
	public void e(float sideMot, float forMot) {
	    if (this.passenger == null || !(this.passenger instanceof EntityHuman)) {
	        super.e(sideMot, forMot);
	        this.W = 0.5F;    // Make sure the entity can walk over half slabs, instead of jumping
	        return;
	    }
	 
	    this.lastYaw = this.yaw = this.passenger.yaw;
	    this.pitch = this.passenger.pitch * 0.5F;
	 
	    // Set the entity's pitch, yaw, head rotation etc.
	    this.b(this.yaw, this.pitch); //[url]https://github.com/Bukkit/mc-dev/blob/master/net/minecraft/server/Entity.java#L163-L166[/url]
	    this.aO = this.aM = this.yaw;
	 
	    this.W = 1.0F;    // The custom entity will now automatically climb up 1 high blocks
	 
	    sideMot = ((EntityLiving) this.passenger).bd * 0.5F;
	    forMot = ((EntityLiving) this.passenger).be;
	 
	    if (forMot <= 0.0F) {
	        forMot *= 0.25F;    // Make backwards slower
	    }
	    sideMot *= 0.75F;    // Also make sideways slower
	 
	    float speed = 0.35F;    // 0.2 is the default entity speed. I made it slightly faster so that riding is better than walking
	    this.i(speed);    // Apply the speed
	    super.e(sideMot, forMot);    // Apply the motion to the entity
	    
	    Field jump = null;
	    try {
			jump = EntityLiving.class.getDeclaredField("bc");
		} catch (NoSuchFieldException | SecurityException e1) {
			e1.printStackTrace();
		}
	    
	    jump.setAccessible(true);
	 
	    if (jump != null && this.onGround) {    // Wouldn't want it jumping while on the ground would we?
	        try {
	            if (jump.getBoolean(this.passenger)) {
	                double jumpHeight = 0.5D;
	                this.motY = jumpHeight;    // Used all the time in NMS for entity jumping
	            }
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	@Override
	public void h() {
		super.h();
		this.B();
		
		npc.onTick();
		if(world.getType(MathHelper.floor(locX), MathHelper.floor(locY), MathHelper.floor(locZ)).getMaterial() == Material.FIRE) {
			setOnFire(15);
		}
	}

	@Override
	public boolean a(EntityHuman entity) {
		NPCInteractEvent event = new NPCInteractEvent(npc, entity.getBukkitEntity());
		Bukkit.getPluginManager().callEvent(event);
		if(event.isCancelled()) {
			return false;
		}
		
		return super.a(entity);
	}

	@Override
	public boolean damageEntity(DamageSource source, float damage) {
		if(invulnerable || noDamageTicks > 0) {
			return false;
		}
		
		DamageCause cause = null;
		org.bukkit.entity.Entity bEntity = null;
		if(source instanceof EntityDamageSource) {
			Entity damager = source.getEntity();
			cause = DamageCause.ENTITY_ATTACK;
			if(source instanceof EntityDamageSourceIndirect) {
				damager = ((EntityDamageSourceIndirect) source).getProximateDamageSource();
				if(damager.getBukkitEntity() instanceof ThrownPotion) {
					cause = DamageCause.MAGIC;
				} else if(damager.getBukkitEntity() instanceof Projectile) {
					cause = DamageCause.PROJECTILE;
				}
			}
			
			bEntity = damager.getBukkitEntity();
		} else if (source == DamageSource.FIRE)
			cause = DamageCause.FIRE;
		else if (source == DamageSource.STARVE)
			cause = DamageCause.STARVATION;
		else if (source == DamageSource.WITHER)
			cause = DamageCause.WITHER;
		else if (source == DamageSource.STUCK)
			cause = DamageCause.SUFFOCATION;
		else if (source == DamageSource.DROWN)
			cause = DamageCause.DROWNING;
		else if (source == DamageSource.BURN)
			cause = DamageCause.FIRE_TICK;
		else if (source == CraftEventFactory.MELTING)
			cause = DamageCause.MELTING;
		else if (source == CraftEventFactory.POISON)
			cause = DamageCause.POISON;
		else if (source == DamageSource.MAGIC) {
			cause = DamageCause.MAGIC;
		} else if(source == DamageSource.OUT_OF_WORLD) {
			cause = DamageCause.VOID;
		}
		
		if(cause != null) {
			NPCDamageEvent event = new NPCDamageEvent(npc, bEntity, cause, (double) damage);
			Bukkit.getPluginManager().callEvent(event);
			if(!event.isCancelled()) {
				return super.damageEntity(source, (float) event.getDamage());
			} else {
				return false;
			}
		}
		
		if(super.damageEntity(source, damage)) {
			if (bEntity != null) {
				Entity e = ((CraftEntity) bEntity).getHandle();
				double d0 = e.locX - this.locX;

				double d1;
				for (d1 = e.locZ - this.locZ; d0 * d0 + d1 * d1 < 0.0001D; d1 = (Math.random() - Math.random()) * 0.01D) { 
					d0 = (Math.random() - Math.random()) * 0.01D;
				}

				a(e, damage, d0, d1);
			}
			
			return true;
		} else {
			return false;
		}
	}
}