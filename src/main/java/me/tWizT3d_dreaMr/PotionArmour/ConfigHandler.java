// /* (C)2024 */
// package me.tWizT3d_dreaMr.PotionArmour;

// import java.io.File;
// import java.io.IOException;
// import java.util.Arrays;
// import java.util.List;
// import org.bukkit.configuration.file.FileConfiguration;
// import org.bukkit.configuration.file.YamlConfiguration;
// import org.bukkit.entity.Player;

// // TODO: probably can drop this whole class

// public class ConfigHandler {
//     public static FileConfiguration config;
//     public static File configFile;
//     public static FileConfiguration mConfig;
//     public static File mConfigFile;

//     public static void enable() {
//         if (!(new File("plugins/PotionArmour/UserData/Players.yml").exists())) {
//             try {
//                 new File("plugins/PotionArmour/UserData/Players.yml").createNewFile();
//             } catch (IOException e) {
//                 // TODO Auto-generated catch block
//                 e.printStackTrace();
//             }
//         }
//         configFile = new File("plugins/PotionArmour/UserData/Players.yml");
//         config = YamlConfiguration.loadConfiguration(configFile);
//         try {
//             config.save(configFile);
//         } catch (IOException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
//         if (!(new File("plugins/PotionArmour/config.yml").exists())) {
// 	        try {
// 	            new File("plugins/PotionArmour/config.yml").createNewFile();
// 	        } catch (IOException e) {
// 	            // TODO Auto-generated catch block
// 	            e.printStackTrace();
// 	        }
//     	}
//         mConfigFile = new File("plugins/PotionArmour/config.yml");
//         mConfig = YamlConfiguration.loadConfiguration(mConfigFile);
//         try {
//             mConfig.save(mConfigFile);
//         } catch (IOException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
//     }

//     public static void savemConfig() {
//         try {
//             mConfig.save(mConfigFile);
//         } catch (IOException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
//     }

//     public static void addEffect(Player player, String effect) {
//         if (!has(player)) {
//             config.addDefault(
//                     player.getUniqueId().toString(),
//                     Arrays.asList("default", effect));
//             try {
//                 config.options().copyDefaults(true);
//                 config.save(configFile);

//                 config = YamlConfiguration.loadConfiguration(configFile);

//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
//         } else {
//             @SuppressWarnings("unchecked")
//             List<String> conf = (List<String>) config.getList(player.getUniqueId().toString());
//             if (!conf.contains(effect)) conf.add(effect);
//             config.set(player.getUniqueId().toString(), conf);
//             try {
//                 config.save(configFile);
//             } catch (IOException e) {
//                 e.printStackTrace();
//             }
//         }
//     }

//     public static void removeEffect(Player player, String effect) {
//         if (!has(player)) return;

//         @SuppressWarnings("unchecked")
//         List<String> conf = (List<String>) config.getList(player.getUniqueId().toString());
//         conf.remove(effect);
//         config.set(player.getUniqueId().toString(), conf);
//         try {
//             config.save(configFile);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     public static boolean hasEffect(Player p, String effect) {

//         if (has(p)) {
//             @SuppressWarnings("unchecked")
//             List<String> conf = (List<String>) config.getList(p.getUniqueId().toString());
//             if (conf.contains(effect)) {
//                 return true;
//             } else return false;
//         } else return false;
//     }

//     public static boolean has(Player p) {

//         return config.contains(p.getUniqueId().toString());
//     }
// }
