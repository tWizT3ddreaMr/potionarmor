package me.tWizT3d_dreaMr.PotionArmor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.kvq.supertrailspro.API.SuperTrailsAPI;
import me.kvq.supertrailspro.data.PlayerData;
import net.md_5.bungee.api.ChatColor;

public class ArmorThings implements Listener {

	public ArrayList<Player> playerss=new ArrayList<Player>();
	public ArrayList<PotionArmorPlayer> pap=new ArrayList<PotionArmorPlayer>();
	@EventHandler
	public void pickup(EntityPickupItemEvent e) {
		if(e.getEntity() instanceof Player)
			Bukkit.getServer().getScheduler().runTask(main.plugin, new Runnable() {
	            @Override
	            public void run() {UpdateEffects((Player)e.getEntity());
	            }
			}
			);
			
	}
	@EventHandler
	public void offhand(PlayerSwapHandItemsEvent e) {
		UpdateEffects(e.getPlayer());
	}
	@EventHandler
	public void login(PlayerLoginEvent e) {
		UpdateEffects(e.getPlayer());
	}
	@EventHandler
	public void logout(PlayerQuitEvent e) {
		removeEffects(e.getPlayer());
	}
	public static void removeEffects(Player p) {
		SuperTrailsAPI.setTrail(p, 0);
		ArrayList<ItemStack> check=new ArrayList<ItemStack>();
		check.addAll(Arrays.asList(p.getInventory().getArmorContents()));
		check.add(p.getInventory().getItemInMainHand());
		check.add(p.getInventory().getItemInOffHand());
		ArrayList<String> lostEffects=new ArrayList<String>();
		for(String s:main.Effect) {
			if(main.config.getBoolean("Effect."+s+".Enable"))
			for(String ef:main.config.getConfigurationSection("Effect."+s+".List").getKeys(false)) {
				for(ItemStack is:check) {
					if(is!=null&&is.getType()!=Material.AIR){
						if(is.hasItemMeta()&&is.getItemMeta().hasLore()&&ChatColor.stripColor(is.getItemMeta().getLore().toString().toLowerCase()).contains(ef.toLowerCase())) {
							if(!lostEffects.contains(s)) {
								lostEffects.add(s);
							}
						}
					}
				}
			}
		}
		if(!lostEffects.isEmpty()) {
			for(String s:lostEffects) {
				p.removePotionEffect(PotionEffectType.getByName(s));
			}
		}
		
	}
	@EventHandler
	public void drop(PlayerDropItemEvent e) {
		Bukkit.getServer().getScheduler().runTask(main.plugin, new Runnable() {
	        @Override
	        public void run() {UpdateEffects(e.getPlayer());
	        }
	    }
		);
	}
	@EventHandler
	public void hand(PlayerItemHeldEvent e) {
		Bukkit.getServer().getScheduler().runTask(main.plugin, new Runnable() {
            @Override
            public void run() {UpdateEffects(e.getPlayer());
            }
        }
		);
	}
	@EventHandler
	public void gamemode(PlayerGameModeChangeEvent e) {
		Bukkit.getServer().getScheduler().runTask(main.plugin, new Runnable() {
            @Override
            public void run() {UpdateEffects(e.getPlayer());
            }
        }
		);
	}
	@EventHandler
	public void invClick(InventoryClickEvent e) {
		if(e.getSlotType()==SlotType.ARMOR||e.getSlotType()==SlotType.QUICKBAR||e.getSlot()==40) { 
			Bukkit.getServer().getScheduler().runTask(main.plugin, new Runnable() {
            @Override
            public void run() {UpdateEffects(Bukkit.getPlayer(e.getWhoClicked().getName()));
            }
        });
			
		}
	}
	@EventHandler
	public void useArmor(PlayerInteractEvent  e) {
		List<Material> Armor=Arrays.asList(Material.LEATHER_BOOTS,Material.IRON_BOOTS,Material.DIAMOND_BOOTS,Material.GOLDEN_BOOTS,Material.CHAINMAIL_BOOTS,
				Material.LEATHER_LEGGINGS,Material.IRON_LEGGINGS,Material.DIAMOND_LEGGINGS,Material.GOLDEN_LEGGINGS,Material.CHAINMAIL_LEGGINGS,
				Material.LEATHER_CHESTPLATE,Material.IRON_CHESTPLATE,Material.DIAMOND_CHESTPLATE,Material.GOLDEN_CHESTPLATE,Material.CHAINMAIL_CHESTPLATE,
				Material.LEATHER_HELMET,Material.IRON_HELMET,Material.DIAMOND_HELMET,Material.GOLDEN_HELMET,Material.CHAINMAIL_HELMET);
		if((e.getPlayer().getInventory().getItemInMainHand()!=null&&Armor.contains(e.getPlayer().getInventory().getItemInMainHand().getType()))||
				(e.getPlayer().getInventory().getItemInOffHand()!=null&&Armor.contains(e.getPlayer().getInventory().getItemInOffHand().getType()))) 
		{
			UpdateEffects(e.getPlayer());
		}
	}
	public void UpdateEffects(Player p) {
		
		PotionArmorPlayer pop=new PotionArmorPlayer(p);
		if(playerss.contains(p)) {
			pop=pap.get(playerss.indexOf(p));
		}else {
			pap.add(pop);
			playerss.add(p);
		}

		if(!pop.getAllPotionNames().isEmpty()) {
			for(String s: pop.getAllPotionNames()) {
				p.removePotionEffect(PotionEffectType.getByName(s));
			}pop.removeAllPotions();
		}
		if(!pop.getAllTrails().isEmpty()) {
			SuperTrailsAPI.setTrail(p, 0);
			pop.removeTrails();
		}
		ArrayList<ItemStack> check=new ArrayList<ItemStack>();
		check.addAll(Arrays.asList(p.getInventory().getArmorContents()));
		check.add(p.getInventory().getItemInMainHand());
		check.add(p.getInventory().getItemInOffHand());
		ArrayList<String> keptEffects=new ArrayList<String>();
		ArrayList<Integer> keptEffectsLevels=new ArrayList<Integer>();
		ArrayList<String> keptEffectsMinor=new ArrayList<String>();
		ArrayList<String> keptTrails=new ArrayList<String>();
		ArrayList<Integer> keptTrailsID=new ArrayList<Integer>();
		ArrayList<Integer> keptTrailsWeight=new ArrayList<Integer>();
		ArrayList<Integer> keptTrailsMode=new ArrayList<Integer>();
		for(String s:main.Effect) {
			if(main.config.getBoolean("Effect."+s+".Enable"))
			for(String ef:main.config.getConfigurationSection("Effect."+s+".List").getKeys(false)) {
				for(ItemStack is:check) {
					if(is!=null&&is.getType()!=Material.AIR){
						if(is.hasItemMeta()&&is.getItemMeta().hasLore()&&ChatColor.stripColor(is.getItemMeta().getLore().toString().toLowerCase()).contains(ef.toLowerCase())) {

							pop.addToNew(s, main.config.getInt("Effect."+s+".List."+ef+".Level"), null, null);
							if(!keptEffects.contains(s)) {
								keptEffects.add(s);
								keptEffectsLevels.add(main.config.getInt("Effect."+s+".List."+ef+".Level"));
								}
							else{
								int l=keptEffects.indexOf(s);
								int i=keptEffectsLevels.get(l);
								if(main.config.getInt("Effect."+s+".List."+ef+".Level")>i){	
									pop.addToNew(s, main.config.getInt("Effect."+s+".Level"), null, null);
									keptEffectsLevels.set(l, main.config.getInt("Effect."+s+".List."+ef+".Level"));
								}
								}
							if(!keptEffectsMinor.contains(ef))
								keptEffectsMinor.add(ef);
						}
					}
				}
			}
		}
		for(String s:main.config.getConfigurationSection("Trail").getKeys(false)){
			if(main.config.getBoolean("Trail."+s+".Enable")) {
				for(ItemStack is:check) {
					if(is!=null&&is.getType()!=Material.AIR){
						if(is.hasItemMeta()&&is.getItemMeta().hasLore()&&ChatColor.stripColor(is.getItemMeta().getLore().toString().toLowerCase()).contains(s.toLowerCase())) {
							keptTrails.add(s);
							pop.addToNew(null, null, main.config.getInt("Trail."+s+".ID"), main.config.getInt("Trail."+s+".Weight"));
							if(!keptTrailsID.contains(main.config.getInt("Trail."+s+".ID"))) {
								keptTrailsID.add(main.config.getInt("Trail."+s+".ID"));
								keptTrailsWeight.add(main.config.getInt("Trail."+s+".Weight"));
								keptTrailsMode.add(main.config.getInt("Trail."+s+".Mode"));
								}
							else {
								int l=keptTrailsID.indexOf(main.config.getInt("Trail."+s+".ID"));
								if(main.config.getInt("Trail."+s+".Weight")>keptTrailsWeight.get(l)) {
									keptTrailsWeight.set(l, main.config.getInt("Trail."+s+".Weight"));
									keptTrailsMode.set(l, main.config.getInt("Trail."+s+".Mode"));
								}
							}
						}
					}
				}
			}
		}
		if(!keptEffects.isEmpty()) {
			for(String s:keptEffects) {
				if(keptEffectsLevels.get(keptEffects.indexOf(s))!=-1)
					p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(s),2147000,keptEffectsLevels.get(keptEffects.indexOf(s))-1,true));
				else
					p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(s),5,1,true));
			}
		}
		if(!keptTrails.isEmpty()) {
			int biggest=0;
			for(int w:keptTrailsWeight) {
				if(w>biggest) {
					biggest=w;
				}
			}
			PlayerData data=SuperTrailsAPI.getPlayerData(p);
			data.setTrail(keptTrailsID.get(keptTrailsWeight.indexOf(biggest)));
			data.setMode(keptTrailsMode.get(keptTrailsWeight.indexOf(biggest)));
			data.save();
		}
		if(pop.getNewTrails().isEmpty()&&!(pop.getAllTrails().isEmpty())) {
	
			SuperTrailsAPI.setTrail(p, 0);
			pop.removeTrails();
		}
		for(PotionEffectType pet:pop.potionsToRemove()) {
		
			p.removePotionEffect(pet);
		}
		pop.setNewOld();
		pap.set(playerss.indexOf(p), pop);
	}
	
	
}
