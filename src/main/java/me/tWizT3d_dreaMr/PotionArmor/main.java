package me.tWizT3d_dreaMr.PotionArmor;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;


public class main extends org.bukkit.plugin.java.JavaPlugin{
public static Plugin plugin;
public static FileConfiguration config;
public static ArrayList<String> Effect=new ArrayList<String>();
public void onEnable() {
	for(PotionEffectType pet:PotionEffectType.values()) {
		Effect.add(pet.getName().toUpperCase());
	}
	config=getConfig();
	Configify();

	plugin=this;
	Bukkit.getPluginManager().registerEvents(new ArmorThings(), this);
			
}
public void onDisable() {
	for(Player p:Bukkit.getOnlinePlayers())
		ArmorThings.removeEffects(p);
}
private void Configify() {
	if(getConfig().contains("Config")) {
		if(getConfig().getInt("Config")==1) {
			return;
		}else {
			UpdateConfig();
			return;
		}
	}else {
		CreateConfig();
		return;
	}
}
private void CreateConfig() {
	getConfig().set("Config",1);
	for(String s:Effect) {
		getConfig().set("Effect."+s+".Enable", true);
		getConfig().set("Effect."+s+".List", Arrays.asList(s+"*"));
		getConfig().set("Effect."+s+".List."+s+"*.Level", 1);
	}
	getConfig().set("Trail.MyFirstTrail*.Enable", true);
	getConfig().set("Trail.MyFirstTrail*.ID", 1);
	getConfig().set("Trail.MyFirstTrail*.Weight", 1);
	getConfig().set("Trail.MyFirstTrail*.Mode", 0);
	
	saveConfig();
	config=getConfig();
}
private void UpdateConfig() {
	// not needed yet
	
}
}
