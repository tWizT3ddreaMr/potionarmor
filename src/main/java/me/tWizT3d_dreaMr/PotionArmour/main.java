package me.tWizT3d_dreaMr.PotionArmour;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import net.md_5.bungee.api.ChatColor;


public class main extends org.bukkit.plugin.java.JavaPlugin{
public static Plugin plugin;
public static FileConfiguration config;
public static List<String> Effect= new ArrayList<String>();
public void onEnable() {
	
	for(PotionEffectType pe: PotionEffectType.values()) {
		Effect.add(pe.getName());
	}
	
	me.tWizT3d_dreaMr.PotionArmour.configHandler.enable();

	config();
	
	plugin=this;
	Bukkit.getPluginManager().registerEvents(new DoingThingsinHere(), this);plugin=this;

}
 
  public void onDisable() {}
  public void config(){
	  if(configHandler.mconfig.contains("Version") && config.getInt("Version")==1)
		 return;
	  
	  configHandler.mconfig.set("Version", 1);
	  
	  configHandler.mconfig.addDefault("BannerBlock", true);
	  configHandler.mconfig.addDefault("OffHand", true);
	  configHandler.mconfig.addDefault("MainHand", true);
	  configHandler.mconfig.addDefault("Armor", true);
	  
	  for(String eff : Effect) {
		  String eff2=eff;
		  if(eff.equalsIgnoreCase("HEAL"))
			  eff2="INSTANT_HEALTH";
		  if(eff.equalsIgnoreCase("SLOW"))
			  eff2="SLOWNESS";
		  if(configHandler.mconfig.contains(eff))
			  continue;
		  
		  List<String> configList = Arrays.asList("Kolbi", eff2);

		  configHandler.mconfig.addDefault(eff+".Enable", true);
		  configHandler.mconfig.addDefault(eff+".List", configList);
		  
		  configHandler.mconfig.addDefault(eff+".List.Kolbi.Level",100);
		 
		  configHandler.mconfig.addDefault(eff+".List."+eff2+".Level",1);;
		  
	  }
	  
	  configHandler.mconfig.options().copyDefaults(true);
	  configHandler.savemConfig();  
	  
  }
public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
	if (command.getName().equalsIgnoreCase("Pareset")) {
		if(args.length!=1) {
			sender.sendMessage(ChatColor.RED+"Please check your command");
			return true;
		}
		if(sender instanceof Player) {
			Player p= (Player) sender;
			if(!p.hasPermission("Potionarmor.pareset")) {
				sender.sendMessage(ChatColor.RED+"No permission.");
				return true;
			}
		}
		for(Player p: Bukkit.getOnlinePlayers()) {
			if(p.getName().equalsIgnoreCase(args[0])||p.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
				sender.sendMessage(ChatColor.AQUA+"You have reset "+ChatColor.DARK_AQUA+p.getName()+ChatColor.AQUA+" potioneffects");
				DoingThingsinHere.resetPlayer(p);
				return true;
			}
		}
		sender.sendMessage(ChatColor.RED+"Player"+ChatColor.DARK_RED+args[0]+ChatColor.RED+" not found");
		return true;
	}
	return false;
}
  
}



