package com.phase.playerpet.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.lenis0012.bukkit.npc.NPC;
import com.phase.playerpet.cmd.PetCommand;
import com.phase.playerpet.listener.LogoutListener;
import com.phase.playerpet.listener.PetGUI;
import com.phase.playerpet.listener.RideEntity;
import com.phase.playerpet.listener.TeleportListener;

public class PlayerPet extends JavaPlugin {
	
	static PlayerPet instance;
	
	public static HashMap<UUID, NPC> NpcList = new HashMap<UUID, NPC>();
	public static List<String> TeamNpcNameList = new ArrayList<String>();
	
	public void onEnable(){
		instance = this;
		getCommand("playerpet").setExecutor(new PetCommand());
		register(new RideEntity());
		register(new PetGUI());
		register(new LogoutListener());
		register(new TeleportListener());
		for(String s : TeamNpcNameList)
			if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(s) != null){
				Bukkit.getScoreboardManager().getMainScoreboard().getTeam(s).setPrefix("");
				Bukkit.getScoreboardManager().getMainScoreboard().getTeam(s).setSuffix("");
			}
		/*Bukkit.getScheduler().scheduleSyncRepeatingTask(getInstance(), new Runnable(){
			public void run(){
				for(Player p : Bukkit.getOnlinePlayers()){
					if(hasNPC(p)){
						Entity e = getNPC(p).getBukkitEntity();
						if(e.getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR){
							e.teleport(e.getLocation().add(0, -1, 0));
						}
					}
				}
			}
		}, 0l, 0l);*/
	}
	
	public void onDisable(){
			for(String s : TeamNpcNameList)
				if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(s) != null){
					Bukkit.getScoreboardManager().getMainScoreboard().getTeam(s).setPrefix("");
					Bukkit.getScoreboardManager().getMainScoreboard().getTeam(s).setSuffix("");
				}
	}
	
	public static void register(Listener l){
		Bukkit.getPluginManager().registerEvents(l, getInstance());
	}
	
	public static PlayerPet getInstance(){
		return instance;
	}
	
	public static boolean hasNPC(Player p){
		return NpcList.containsKey(p.getUniqueId());
	}
	
	public static void addNPC(Player p, NPC n){
		if(NpcList.get(p.getUniqueId()) != null){
			if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(PlayerPet.getNPC(p).getBukkitEntity().getName()) != null){
				Bukkit.getScoreboardManager().getMainScoreboard().getTeam(PlayerPet.getNPC(p).getBukkitEntity().getName()).setPrefix("");
				Bukkit.getScoreboardManager().getMainScoreboard().getTeam(PlayerPet.getNPC(p).getBukkitEntity().getName()).setSuffix("");
			}
			NpcList.get(p.getUniqueId()).getBukkitEntity().remove();
		}
		NpcList.put(p.getUniqueId(), n);
	}
	
	public static void removeNPC(Player p){
		if(NpcList.get(p.getUniqueId()) != null)
			NpcList.get(p.getUniqueId()).getBukkitEntity().remove();
		NpcList.put(p.getUniqueId(), null);
	}
	
	public static NPC getNPC(Player p){
		return NpcList.get(p.getUniqueId());
	}
}
