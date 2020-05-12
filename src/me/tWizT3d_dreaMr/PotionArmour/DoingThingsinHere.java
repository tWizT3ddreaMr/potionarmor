package me.tWizT3d_dreaMr.PotionArmour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DoingThingsinHere implements Listener {
public static World world;

public static Plugin plugin= me.tWizT3d_dreaMr.PotionArmour.main.plugin;
public static FileConfiguration conf= plugin.getConfig();
@EventHandler
public void useArmor(PlayerInteractEvent  e) {
	List<Material> Boots=Arrays.asList(Material.LEATHER_BOOTS,Material.IRON_BOOTS,Material.DIAMOND_BOOTS,Material.GOLDEN_BOOTS,Material.CHAINMAIL_BOOTS);
	List<Material> Leggings=Arrays.asList(Material.LEATHER_LEGGINGS,Material.IRON_LEGGINGS,Material.DIAMOND_LEGGINGS,Material.GOLDEN_LEGGINGS,Material.CHAINMAIL_LEGGINGS);
	List<Material> ChestPlate=Arrays.asList(Material.LEATHER_CHESTPLATE,Material.IRON_CHESTPLATE,Material.DIAMOND_CHESTPLATE,Material.GOLDEN_CHESTPLATE,Material.CHAINMAIL_CHESTPLATE);
	List<Material> Helmet=Arrays.asList(Material.LEATHER_HELMET,Material.IRON_HELMET,Material.DIAMOND_HELMET,Material.GOLDEN_HELMET,Material.CHAINMAIL_HELMET);
	Player p=(Player) e.getPlayer();
	ItemStack i=e.getItem();
	if(!conf.getBoolean("Armour")) return;
	if(i==null) return;
	if(Boots.contains(e.getItem().getType())){
		if(e.getPlayer().getInventory().getBoots()==null){
			addEffect(i,p,"ar");
		}
			
	}
	if(Leggings.contains(e.getItem().getType())){
		if(e.getPlayer().getInventory().getLeggings()==null){
			addEffect(i,p,"ar");}
			
	}
	if(ChestPlate.contains(e.getItem().getType())){
		if(e.getPlayer().getInventory().getChestplate()==null){
			addEffect(i,p,"ar");
		}
			
	}
	if(Helmet.contains(e.getItem().getType())){
		if(e.getPlayer().getInventory().getHelmet()==null){
			addEffect(i,p,"ar");
		}
			
	}
}
public boolean isbanner(Material m){
	if(m==Material.BLACK_BANNER){
		return true;
	}else if(m==Material.BLUE_BANNER){
		return true;
	}else
		if(m==Material.BROWN_BANNER){
	return true;
    }else
		if(m==Material.CYAN_BANNER){
	return true;
    }else
		if(m==Material.GRAY_BANNER){
	return true;
    }else
		if(m==Material.GREEN_BANNER){
	return true;
    }else
		if(m==Material.LIGHT_BLUE_BANNER){
	return true;
    }else
		if(m==Material.LIME_BANNER){
	return true;
    }else
		if(m==Material.MAGENTA_BANNER){
	return true;
    }else
		if(m==Material.ORANGE_BANNER){
	return true;
    }else
		if(m==Material.PINK_BANNER){
	return true;
    }else
		if(m==Material.PURPLE_BANNER){
	return true;
    }else
		if(m==Material.RED_BANNER){
	return true;
    }else
		if(m==Material.WHITE_BANNER){
	return true;
    }else
		if(m==Material.YELLOW_BANNER){
	return true;
    }else
	return false;
}
@EventHandler
public void craft(CraftItemEvent e){
	if(conf.getBoolean("BannerBlock")) return;
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
public void invClose(InventoryCloseEvent e) {
	if(e.getPlayer() instanceof Player&&conf.getBoolean("Armor")){
		Player p=(Player) e.getPlayer();
		for(ItemStack i : p.getInventory().getArmorContents()) {
			addEffect(i,p,"ar");
			}
		RemoveEffect(p);
		}
	}
@EventHandler
public void hand(PlayerItemHeldEvent e) {
	if(!conf.getBoolean("MainHand")) return;
	RemoveEffectHand(e.getPlayer(),e.getPlayer().getInventory().getItem(e.getNewSlot()));
		addEffect(e.getPlayer().getInventory().getItem(e.getNewSlot()),e.getPlayer(),"mh");
	if(conf.getBoolean("OffHand"))
		addEffect(e.getPlayer().getInventory().getItemInOffHand(),e.getPlayer(),"oh");
		
	}@EventHandler
public void drop(PlayerDropItemEvent e) {
		if(!conf.getBoolean("MainHand")) return;
		RemoveEffect(e.getPlayer());
		if(e.getItemDrop().getItemStack()==e.getPlayer().getInventory().getItemInMainHand())
			addEffect(e.getPlayer().getInventory().getItemInMainHand(),e.getPlayer(),"mh");
		if(conf.getBoolean("OffHand"))
			addEffect(e.getPlayer().getInventory().getItemInOffHand(),e.getPlayer(),"oh");
			
		}
@EventHandler
public void offhand(PlayerSwapHandItemsEvent e) {
	RemoveEffect(e.getPlayer());
	//pretty sure it checks before the swap's items
	if(conf.getBoolean("OffHand"))
	addEffect(e.getMainHandItem(),e.getPlayer(),"mh");
	if(conf.getBoolean("MainHand"))
	addEffect(e.getOffHandItem(),e.getPlayer(),"oh");
}
public static void addEffect(ItemStack i, Player p, String loc){
	
	if(i!=null){
	if(i.hasItemMeta()){
	if(i.getItemMeta().hasLore()){
	String Lore=ChatColor.stripColor(i.getItemMeta().getLore().toString());
	for(String eff : me.tWizT3d_dreaMr.PotionArmour.main.Effect){
		if(me.tWizT3d_dreaMr.PotionArmour.main.config.getBoolean(eff+".Enable")){
			List<String> efflist=new ArrayList<String>(conf.getConfigurationSection(eff+".List").getKeys(false));
			
			for(int in=0; in<efflist.size(); in++){
				String str=efflist.get(in).toLowerCase();
				int lo=conf.getInt(eff+".List."+efflist.get(in)+".Level");
				boolean oh=true;boolean ar=true;boolean mh=true;;
				while(str.endsWith(" ")){
					str=str.substring(0, str.length()-1);
				}
				@SuppressWarnings("unchecked")
				List<String> li=(List<String>)conf.getList(eff+".List."+efflist.get(in)+".Slot");
					for(int on=0; on<li.size(); on++){
						String stri=li.get(on).toLowerCase();
						if(stri.equalsIgnoreCase("MainHand"))
							mh=true;
						if(stri.equalsIgnoreCase("offHand"))
							oh=true;
						if(stri.equalsIgnoreCase("armor"))
							ar=true;
					}
				
				
				if(Lore.toLowerCase().contains(str)){
				boolean nailedit=false;
					if(ar){if(loc.equals("ar")){nailedit=true;}}
					if(oh){if(loc.equals("oh")){nailedit=true;}}
					if(mh){if(loc.equals("mh")){nailedit=true;}}
					if(nailedit){
						String E2=eff;
						if(eff.equalsIgnoreCase("Instant_Health")){
							E2="HEAL";
						}
						if(eff.equalsIgnoreCase("Slowness")){
							E2="SLOW";
						}
						p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(E2),2147000,lo,true));
							me.tWizT3d_dreaMr.PotionArmour.configHandler.addEffect(p, eff);
					}
				}
				}
				
		}
	}
}
	}}
}
public static void RemoveEffect(Player p){
	for(String eff : me.tWizT3d_dreaMr.PotionArmour.main.Effect){
	if(me.tWizT3d_dreaMr.PotionArmour.configHandler.hasEffect(p, eff)){
	boolean loseeff=true;
	for(ItemStack i : p.getInventory().getArmorContents()) {
		if(i!=null){
		if(i.hasItemMeta()){
		if(i.getItemMeta().hasLore()){
		String Lore=ChatColor.stripColor(i.getItemMeta().getLore().toString());
		List<String> efflist=new ArrayList<String>(conf.getConfigurationSection(eff+".List").getKeys(false));
		for(int in=0; in<efflist.size(); in++){
			String str=efflist.get(in).toLowerCase();
			if(str.endsWith(" ")){
				str=str.substring(0, str.length()-1);
			}
				if(Lore.toLowerCase().contains(str)){
					loseeff=false;
				}
			}
		}
	}
}	 
}if(loseeff && p.getInventory().getItemInMainHand() != null){
	ItemStack i=p.getInventory().getItemInMainHand();
	if(i.hasItemMeta()){
	String Lore=ChatColor.stripColor(i.getItemMeta().getLore().toString());
	List<String> efflist=new ArrayList<String>(conf.getConfigurationSection(eff+".List").getKeys(false));
	for(int in=0; in<efflist.size(); in++){
		String str=efflist.get(in).toLowerCase();
		if(str.endsWith(" ")){
			str=str.substring(0, str.length()-1);
		}
			if(Lore.toLowerCase().contains(str)){;
				loseeff=false;
			}
		}
	}
}
if(loseeff && p.getInventory().getItemInOffHand() != null){
	ItemStack i=p.getInventory().getItemInOffHand();
	if(i.hasItemMeta()){
	if(i.getItemMeta().hasLore()){
	String Lore=ChatColor.stripColor(i.getItemMeta().getLore().toString());
	List<String> efflist=new ArrayList<String>(conf.getConfigurationSection(eff+".List").getKeys(false));
	for(int in=0; in<efflist.size(); in++){
		String str=efflist.get(in).toLowerCase();
		if(str.endsWith(" ")){
			str=str.substring(0, str.length()-1);
		}
			if(Lore.toLowerCase().contains(str)){

				loseeff=false;
			}
		}
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
public static void RemoveEffectHand(Player p, ItemStack item){
	for(String eff : me.tWizT3d_dreaMr.PotionArmour.main.Effect){
	if(me.tWizT3d_dreaMr.PotionArmour.configHandler.hasEffect(p, eff)){
	boolean loseeff=true;
	for(ItemStack i : p.getInventory().getArmorContents()) {
		if(i!=null){
		if(i.hasItemMeta()){
		if(i.getItemMeta().hasLore()){
		String Lore=ChatColor.stripColor(i.getItemMeta().getLore().toString());
		List<String> efflist=new ArrayList<String>(conf.getConfigurationSection(eff+".List").getKeys(false));
		for(int in=0; in<efflist.size(); in++){
			String str=efflist.get(in).toLowerCase();
			if(str.endsWith(" ")){
				str=str.substring(0, str.length()-1);
			}
				if(Lore.toLowerCase().contains(str)){
					loseeff=false;
				}
			}
		}
	}
}	 
}if(loseeff && item != null){
	ItemStack i=item;
	if(i.hasItemMeta()){
	String Lore=ChatColor.stripColor(i.getItemMeta().getLore().toString());
	List<String> efflist=new ArrayList<String>(conf.getConfigurationSection(eff+".List").getKeys(false));
	for(int in=0; in<efflist.size(); in++){
		String str=efflist.get(in).toLowerCase();
		if(str.endsWith(" ")){
			str=str.substring(0, str.length()-1);
		}
			if(Lore.toLowerCase().contains(str)){
				
				loseeff=false;
			}
		}
	}
}
if(loseeff && p.getInventory().getItemInOffHand() != null){
	ItemStack i=p.getInventory().getItemInOffHand();
	if(i.hasItemMeta()){
	if(i.getItemMeta().hasLore()){
	String Lore=ChatColor.stripColor(i.getItemMeta().getLore().toString());
	List<String> efflist=new ArrayList<String>(conf.getConfigurationSection(eff+".List").getKeys(false));
	for(int in=0; in<efflist.size(); in++){
		String str=efflist.get(in).toLowerCase();
		
		if(!str.equals("")) {
		if(str.endsWith(" ")){
			str=str.substring(0, str.length()-1);
		}
			if(Lore.toLowerCase().contains(str)){

				loseeff=false;
			}
		}
	}
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
}

