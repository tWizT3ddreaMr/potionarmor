package me.tWizT3d_dreaMr.PotionArmour;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class configHandler {
	public static FileConfiguration config;
	public static File Config;
	public static FileConfiguration mconfig;
	public static File mConfig;
public static void enable(){
if(!(new File("plugins/PotionArmour/UserData/Players.yml").exists())){
	try {
		new File("plugins/PotionArmour/UserData/Players.yml").createNewFile();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
Config =new File("plugins/PotionArmour/UserData/Players.yml");
config= YamlConfiguration.loadConfiguration(Config);
try {
	config.save(Config);
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
try {
	new File("plugins/PotionArmour/config.yml").createNewFile();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
mConfig =new File("plugins/PotionArmour/config.yml");
mconfig= YamlConfiguration.loadConfiguration(mConfig);
try {
mconfig.save(mConfig);
} catch (IOException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
	
}
public static void savemConfig() {
	try {
		mconfig.save(mConfig);
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
}
public static void addEffect(Player player, String effect){
if(!has(player)){
config.addDefault(player.getUniqueId().toString(), Arrays.asList("default", effect.toUpperCase()));
try {
	config.options().copyDefaults(true);
	config.save(Config);

	config= YamlConfiguration.loadConfiguration(Config);
	
} catch (IOException e) {
	e.printStackTrace();
}
}
else{
@SuppressWarnings("unchecked")
List<String> conf=(List<String>) config.getList(player.getUniqueId().toString());
if(!conf.contains(effect))
	conf.add(effect);
config.set(player.getUniqueId().toString(), conf);
try {
	config.save(Config);
} catch (IOException e) {
	e.printStackTrace();
}
}
}
public static void removeEffect(Player player, String effect){
if(!has(player))
return;

@SuppressWarnings("unchecked")
List<String> conf=(List<String>) config.getList(player.getUniqueId().toString());
conf.remove(effect);
config.set(player.getUniqueId().toString(), conf);
try {
	config.save(Config);
} catch (IOException e) {
	e.printStackTrace();
}
}
public static boolean hasEffect(Player p, String effect){
	
	if(has(p)){@SuppressWarnings("unchecked")
	List<String> conf=(List<String>) config.getList(p.getUniqueId().toString());
	if (conf.contains(effect.toUpperCase())) {
		return true;
	}else return false;}
	else return false;
}
public static boolean has(Player p){

return config.contains(p.getUniqueId().toString());

}
}
