package me.tWizT3d_dreaMr.PotionArmour;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;

public class DoingThingsinHere implements Listener {
public static World world;

public static Plugin plugin= me.tWizT3d_dreaMr.PotionArmour.main.plugin;

public boolean isbanner(Material m){
	return m.toString().endsWith("BANNER");
}
//banner
@EventHandler
public void craft(CraftItemEvent e){
	if(configHandler.mconfig.getBoolean("BannerBlock")) return;
	ItemStack i=e.getCurrentItem();
	if(isbanner(i.getType())){
		if(i.hasItemMeta()){
			if((i.getItemMeta().hasLore()||i.getItemMeta().hasEnchants())){
				if(e.getWhoClicked() instanceof Player){
					if(((Player)e.getWhoClicked()).isOp()) return;
				}
				e.setCancelled(true);
			}
		}
	}
}

@EventHandler
public void Armor(PlayerArmorChangeEvent e) {
	Bukkit.getServer().getScheduler().runTask(plugin, new Runnable() {
    @Override
    public void run() {
        Player p=(Player) e.getPlayer();
	    addEffect(e.getNewItem(),p);
	    RemoveEffect(p);
	     }
    
	}
	);
	
	
}
//inventory click
@EventHandler
public void invClick(InventoryClickEvent e) {
	Bukkit.getServer().getScheduler().runTask(plugin, new Runnable() {
	    @Override
	    public void run() {
			if(!(e.getSlot()== e.getWhoClicked().getInventory().getHeldItemSlot()||e.getSlot()==45)) return;
			Player p=(Player)e.getWhoClicked();
			PlayerInventory inv=p.getInventory();
			hand(p,inv.getItemInMainHand(),inv.getItemInOffHand());
		}
	});
}
//hotbar
@EventHandler
public void heldItem(PlayerItemHeldEvent e) {
	Bukkit.getServer().getScheduler().runTask(plugin, new Runnable() {
	    @Override
	    public void run() {
	    	PlayerInventory inv=e.getPlayer().getInventory();
			hand(e.getPlayer(),inv.getItemInMainHand(),inv.getItemInOffHand());
		}
	});
}
//drop
@EventHandler
public void drop(PlayerDropItemEvent e) {
	Bukkit.getServer().getScheduler().runTask(plugin, new Runnable() {
	    @Override
	    public void run() {
			PlayerInventory inv=e.getPlayer().getInventory();
			hand(e.getPlayer(),inv.getItemInMainHand(),inv.getItemInOffHand());
	    }
	});
}

@EventHandler
public void gamemode(PlayerGameModeChangeEvent e) {
	resetPlayer(e.getPlayer());
}

public static void resetPlayer(Player p) {
	Bukkit.getServer().getScheduler().runTask(plugin, new Runnable() {
	    @Override
	    public void run() {
	    	PlayerInventory inv=p.getInventory();
	    	 p.getActivePotionEffects().clear();
	    	 RemoveEffect(p);
	    	 for(ItemStack is: inv.getArmorContents())
	    		 addEffect(is, p);
	    	 addEffect(inv.getItemInMainHand(), p);
	    	 addEffect(inv.getItemInOffHand(), p);
	     }
	}
	);
	
}
public void hand(Player p, ItemStack mainHand, ItemStack offHand) {
	RemoveEffect(p);
	addEffect(mainHand, p);
	addEffect(offHand, p);
}
public static void addEffect(ItemStack i, Player p){

if(i==null) return;
if(!i.hasItemMeta()) return;
if(!i.getItemMeta().hasLore()) return;
@SuppressWarnings("deprecation")
String Lore=ChatColor.stripColor(i.getItemMeta().getLore().toString());
for(String eff : me.tWizT3d_dreaMr.PotionArmour.main.Effect){
	if(configHandler.mconfig.getBoolean(eff+".Enable")){
		List<String> efflist=new ArrayList<String>(configHandler.mconfig.getConfigurationSection(eff+".List").getKeys(false));
			
		for(int in=0; in<efflist.size(); in++){
			String str=efflist.get(in).toLowerCase();
			int lo=configHandler.mconfig.getInt(eff+".List."+efflist.get(in)+".Level");
			while(str.endsWith(" ")){
				str=str.substring(0, str.length()-1);
			}
			if(Lore.toLowerCase().contains(str)){
				String E2=eff;
				if(eff.equalsIgnoreCase("Instant_Health")) E2="HEAL";
				if(eff.equalsIgnoreCase("Slowness")) E2="SLOW";
				p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(E2),2147000,lo,true));
				me.tWizT3d_dreaMr.PotionArmour.configHandler.addEffect(p, eff);
					
			}
		}
				
	}
}
}
public static void RemoveEffect(Player p){
	for(String eff : me.tWizT3d_dreaMr.PotionArmour.main.Effect){
		if(me.tWizT3d_dreaMr.PotionArmour.configHandler.hasEffect(p, eff)){
			List<String> efflist=new ArrayList<String>(configHandler.mconfig.getConfigurationSection(eff+".List").getKeys(false));
			boolean loseeff=true;
			for(ItemStack i : p.getInventory().getArmorContents()) {
				if(i==null) continue;
				if(!i.hasItemMeta()) continue;
				if(!i.getItemMeta().hasLore()) continue;
				@SuppressWarnings("deprecation")
				String Lore=ChatColor.stripColor(i.getItemMeta().getLore().toString());
		
				loseeff=check(Lore,efflist); 
		}if(loseeff && p.getInventory().getItemInMainHand() != null){
			ItemStack i=p.getInventory().getItemInMainHand();
			if(i.hasItemMeta()) {
				@SuppressWarnings("deprecation")
				String Lore=ChatColor.stripColor(i.getItemMeta().getLore().toString());
		
				loseeff=check(Lore,efflist);
			}
		}
		
		if(loseeff && p.getInventory().getItemInOffHand() != null){
			ItemStack i=p.getInventory().getItemInOffHand();
			if(i.hasItemMeta()){
				if(i.getItemMeta().hasLore()){
					@SuppressWarnings("deprecation")
					String Lore=ChatColor.stripColor(i.getItemMeta().getLore().toString());
					loseeff=check(Lore,efflist);
				}
			}
		}
			if(loseeff){
				p.removePotionEffect(PotionEffectType.getByName(eff));
				me.tWizT3d_dreaMr.PotionArmour.configHandler.removeEffect(p, eff);
			}
		}
	}
}
public static boolean check(String Lore,List<String> efflist) {
for(int in=0; in<efflist.size(); in++){
	String str=efflist.get(in).toLowerCase();
	if(str.endsWith(" ")){
		str=str.substring(0, str.length()-1);
	}
		if(Lore.toLowerCase().contains(str)){

			return false;
		}
	}
	return true;
}
}

