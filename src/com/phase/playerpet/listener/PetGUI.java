package com.phase.playerpet.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.phase.playerpet.main.MessageManager;

public class PetGUI implements Listener {
	
	public static ItemStack createItem(Material m, int amount, byte data, String name, String... lore){
		ItemStack item = new ItemStack(m, amount, data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		List<String> list = new ArrayList<String>();
		for(String l : lore)
			list.add(l);
		meta.setLore(list);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createPlayerHead(Material m, byte data, String name, String skin){
		return  createItem(m, 1, data, "§eName: " + name, "§bSkin: " + skin);
	}
	public static ItemStack createPlayerHead(Material m, byte data, String name){
		return createPlayerHead(m, data, name, name);
	}
	
	private String invName = "§b§lPlayerPet!§e   Custom Pets!";
	
	public Inventory openInventory(Player p, boolean b){
		Inventory i = Bukkit.createInventory(null, 36, invName);
		if(b){
			i.setItem(0, createPlayerHead(Material.STAINED_GLASS_PANE, (byte)14,"mbaxter", "dinnerbone"));
			i.setItem(1, createPlayerHead(Material.STAINED_GLASS_PANE, (byte)13,"&b@&lJames", "kingbdogz"));
			i.setItem(2, createPlayerHead(Material.STAINED_GLASS_PANE, (byte)12,"Notch", "Notch"));
			i.setItem(3, createPlayerHead(Material.STAINED_GLASS_PANE, (byte)11,"jeb_", "jeb_"));
			i.setItem(4, createPlayerHead(Material.STAINED_GLASS_PANE, (byte)10,"Searge", "Searge"));
			i.setItem(5, createPlayerHead(Material.STAINED_GLASS_PANE, (byte)9, "C418", "C418"));
			i.setItem(6, createPlayerHead(Material.STAINED_GLASS_PANE, (byte)8, "inthelittlewood", "inthelittlewood"));
			i.setItem(7, createPlayerHead(Material.STAINED_GLASS_PANE, (byte)7, "Guude", "Guude"));
			i.setItem(8, createPlayerHead(Material.STAINED_GLASS_PANE, (byte)6, "EvilSeph", "EvilSeph"));
			i.setItem(9, createPlayerHead(Material.STAINED_GLASS_PANE, (byte)5, "BdoubleO100", "BdoubleO100"));
			i.setItem(10,createPlayerHead(Material.STAINED_GLASS_PANE, (byte)4, "SethBling", "SethBling"));
			i.setItem(11,createPlayerHead(Material.STAINED_GLASS_PANE, (byte)3, "&6It's-Phase!", "Phasesaber"));
			i.setItem(12,createPlayerHead(Material.STAINED_GLASS_PANE, (byte)2, "_DSH105_", "_DSH105_"));
			i.setItem(13,createPlayerHead(Material.STAINED_GLASS_PANE, (byte)1, "Ash-Ketchum", "Ad"));
			i.setItem(14,createPlayerHead(Material.STAINED_GLASS_PANE, (byte)0, "Mario!", "SethBling"));
			i.setItem(15,createPlayerHead(Material.STAINED_GLASS_PANE, (byte)15,"oHaiiChun", "oHaiiChun"));
			i.setItem(16,createPlayerHead(Material.WOOL, (byte)0, "codename_B", "codename_B"));
			i.setItem(17,createPlayerHead(Material.WOOL, (byte)1, "AntVenom", "AntVenom"));
			i.setItem(18,createPlayerHead(Material.WOOL, (byte)2, "Hypixel", "Hypixel"));
			i.setItem(19,createPlayerHead(Material.WOOL, (byte)3, "&bRobo-Cop", "felipepcjr"));
			i.setItem(20,createPlayerHead(Material.WOOL, (byte)4, "&bRobo-Cop", "EvilSeph"));
			i.setItem(21,createPlayerHead(Material.WOOL, (byte)5, "SkyDoesMinecraft", "SkyTheKidRS"));
			i.setItem(22,createPlayerHead(Material.WOOL, (byte)6, "Glis", "Glis6"));
			i.setItem(23,createPlayerHead(Material.WOOL, (byte)7, "Vechs", "Vechs_"));
			i.setItem(24,createPlayerHead(Material.WOOL, (byte)8, "&7Docm", "Docm77"));
			i.setItem(25,createPlayerHead(Material.WOOL, (byte)9, "UnpausePause", "PauseUnpause"));
			i.setItem(26,createPlayerHead(Material.WOOL, (byte)10,"null", "null"));
			i.setItem(27,createPlayerHead(Material.WOOL, (byte)11,"null", "null"));
			i.setItem(28,createPlayerHead(Material.WOOL, (byte)12,"null", "null"));
			i.setItem(29,createPlayerHead(Material.WOOL, (byte)13,"null", "null"));
			i.setItem(30,createPlayerHead(Material.WOOL, (byte)14,"null", "null"));
			i.setItem(31,createPlayerHead(Material.WOOL, (byte)15,"null", "null"));
			i.setItem(32,createPlayerHead(Material.STAINED_CLAY, (byte)15, "null", "null"));
			i.setItem(33,createPlayerHead(Material.STAINED_CLAY, (byte)14, "null", "null"));
			i.setItem(34,createPlayerHead(Material.STAINED_CLAY, (byte)13, "null", "null"));
			i.setItem(35,createPlayerHead(Material.STAINED_CLAY, (byte)12, "null", "null"));
			
			p.openInventory(i);
		}
		return i;
	}
	
	@EventHandler
	public void ClickInventory(InventoryClickEvent e){
		if(e.getWhoClicked() instanceof Player){
			Player p = (Player)e.getWhoClicked();
			if(e.getInventory().getName().equalsIgnoreCase(invName)){
				if(p.hasPermission("playerpet") || p.isOp()){
					e.setCancelled(true);
					ItemStack i = e.getCurrentItem();
					if(i == null || i.getType() == Material.AIR) return;
					p.closeInventory();
					String name = i.getItemMeta().getDisplayName().split(":")[1];
					String skin = i.getItemMeta().getLore().get(0).split(":")[1];
					p.getServer().dispatchCommand(p, "playerpet spawn" + name + skin);
				}else{
					e.setCancelled(true);
					p.closeInventory();
					MessageManager.sendMessage(p, "You aren't allowed to do that!");
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void OpenInventory(PlayerInteractEvent e){
		Player p = e.getPlayer();
		ItemStack i = p.getItemInHand();
		if(i.getType() == Material.SKULL_ITEM && i.getData().getData() == (byte)3){
			openInventory(p,true);
			e.setCancelled(true);
		}
		
	}
	
	
	
	
	
}
