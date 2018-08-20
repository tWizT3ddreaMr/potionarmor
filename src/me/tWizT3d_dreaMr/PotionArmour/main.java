package me.tWizT3d_dreaMr.PotionArmour;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;


public class main extends org.bukkit.plugin.java.JavaPlugin{
public static Plugin plugin;
public static FileConfiguration config;
public static List<String> Effect=Arrays.asList("CONDUIT_POWER","DOLPHINS_GRACE","SLOW_FALLING","SPEED","SLOW","FAST_DIGGING","SLOW_DIGGING","INCREASE_DAMAGE","HEAL","HARM","JUMP","CONFUSION","REGENERATION","DAMAGE_RESISTANCE","FIRE_RESISTANCE","WATER_BREATHING","INVISIBILITY","BLINDNESS","NIGHT_VISION","HUNGER","WEAKNESS","POISON","WITHER","HEALTH_BOOST","ABSORPTION","SATURATION","GLOWING","LEVITATION","LUCK","UNLUCK");
public void onEnable()
{
	me.tWizT3d_dreaMr.PotionArmour.configHandler.enable();

	
	
	config();
	
	plugin=this;
	Bukkit.getPluginManager().registerEvents(new DoingThingsinHere(), this);plugin=this;

}
 
  public void onDisable() {}
  public void config(){
	  config =getConfig();
	  config.addDefault("BannerBlock", true);
	  config.addDefault("OffHand", true);
	  config.addDefault("MainHand", true);
	  config.addDefault("Armor", true);
	  for(String eff : Effect) {
		  String eff2=eff;
		  if(eff.equalsIgnoreCase("HEAL"))
			  eff2="INSTANT_HEALTH";
		  if(eff.equalsIgnoreCase("SLOW"))
			  eff2="SLOWNESS";
		  List<String> configList = Arrays.asList("Kolbi", eff2);

		  config.addDefault(eff+".Enable", true);
		  config.addDefault(eff+".List", configList);
		  config.addDefault(eff+".List.Kolbi",Arrays.asList("Level", "Slot"));
		  config.addDefault(eff+".List.Kolbi.Level",100);
		  config.addDefault(eff+".List.Kolbi.Slot",Arrays.asList("OffHand","MainHand","Armor"));
		 
		  config.addDefault(eff+".List."+eff2,Arrays.asList("Level", "Slot"));
		  config.addDefault(eff+".List."+eff2+".Level",1);
		  config.addDefault(eff+".List."+eff2+".LevelInGame",true);
		  config.addDefault(eff+".List."+eff2+".Slot",Arrays.asList("OffHand","MainHand","Armor"));
		  
	  }
	  
	  config.options().copyDefaults(true);
	  saveConfig();  
	  
  }
}



