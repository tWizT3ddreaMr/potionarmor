/* (C)2024 */
package me.tWizT3d_dreaMr.PotionArmour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Registry;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;

public class PotionArmorPlugin extends org.bukkit.plugin.java.JavaPlugin {
    public Logger logger = getLogger();
    public static Plugin plugin;
    public static FileConfiguration config;
    public static FileConfiguration lang;
    // public static List<FileConfiguration> moreEffectsConfig; // to add for
    // 'effects/' dir
    private static File config_dir;
    public static EventListener listener;
    public static EffectManager manager;
    public static List<NamespacedKey> supportedEffects = new ArrayList<NamespacedKey>();

    // check out java.util.concurrent.Executors#newFixedThreadPool
    // probably a ThreadPoolExecutor with corePoolSize=4 ish, maximumPoolSize=20ish,
    // keepAliveTime=30 timeunit=second, blockingqueue =
    // ArrayBlockingQueue<Runnable> capacity = 200 ish?, fair=true,
    // likely put this in the plugin

    // need to periodically purge cancelled tasks?

    private ThreadPoolExecutor pool;
    private ArrayBlockingQueue<Runnable> workQueue;
    private AsyncOptions opt;
    private boolean workAsync;
    private boolean acceptNewJobs = false;

    @Override
    public void onEnable() {
        plugin = this;
        setSupportedEffects(); // TODO: add config option to disable certain effects
        config_dir = getDataFolder();
        reloadConfigs();

        manager = new EffectManager(this);
        listener = new EventListener(manager);
        manager.loadEffects(config);
        // manager.loadEffects(moreEffectsConfig); // to add for 'effects/' dir

        workAsync = config.getBoolean("meta.async");
        if (workAsync) {
            opt = AsyncOptions.fromConfig(config);
            workQueue = new ArrayBlockingQueue<Runnable>(opt.queueCapacity);
            pool = new ThreadPoolExecutor(
                    opt.nThreads, opt.maxThreads, opt.timeout, opt.timeUnit, workQueue);
            acceptNewJobs = true;
        }
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        saveConfig(); // in case loaded default configs
    }

    public void setSupportedEffects() {
        setSupportedEffects(new ArrayList<NamespacedKey>());
    }

    public void setSupportedEffects(List<NamespacedKey> exclude) {
        NamespacedKey tag;
        for (PotionEffectType pe : Registry.EFFECT) {
            tag = pe.getKey();
            if (!exclude.contains(tag)) {
                supportedEffects.add(tag);
            }
        }
    }

    @Override
    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public void saveConfig() {
        List<String> comments = config.getComments("");

        // ensure effects list in comments
        if (!comments.contains(supportedEffects.get(0).toString())) {
            comments.add(lang.getString("supported_effects"));
            for (NamespacedKey k : supportedEffects) {
                comments.add(k.toString());
            }
            config.setComments("", comments);
        }
        super.saveConfig();

        String lang_path = config.getString("language_loc");
        try {
            lang.save(lang_path);
        } catch (IOException e) {
            logger.log(Level.SEVERE, lang.getString("failed_save") + lang_path);
        }
    }

    /**
     * {@code checkPerms} defaults to
     * {@link PotionArmorPlugin#checkPerms(CommandSender, String, boolean)}
     * 
     * @see PotionArmorPlugin#checkPerms(CommandSender, String, boolean)
     */
    public boolean checkPerms(CommandSender p, String perm) {
        return checkPerms(p, perm, true); // default to check only players
    }

    /**
     * Check permissions
     *
     * @param p           who to check permission for
     * @param perm        the permission string
     * @param onlyPlayers whether to ignore (return true for) nonplayers
     * @return whether 'p' has permission 'perm'
     */
    public boolean checkPerms(CommandSender p, String perm, boolean onlyPlayers) {
        if (onlyPlayers && (!(p instanceof Player))) {
            return true;
        }
        if (p.hasPermission(perm)) {
            return true;
        } else {
            p.sendMessage(lang.getString("no_perms"));
            return false;
        }
    }

    public boolean reloadConfigs() {
        // note: void reloadConfig() (no 's') is a superclass method, don't get confused
        return reloadConfigs((CommandSender) Bukkit.getConsoleSender());
    }

    public boolean reloadConfigs(CommandSender sender) {
        acceptNewJobs = false;
        boolean saveNeeded = false;
        if (!checkPerms(sender, "Potionarmor.reload"))
            return true;

        if (!(config_dir.exists() && config_dir.isDirectory())) {
            config_dir.delete();
            config_dir.mkdir();
            saveNeeded = true;
        }

        config = getConfig();
        // TODO: check version and convert to new format
        String lang_path = config.getString("meta.language_file");

        List<String> config_files = Arrays.asList(config_dir.list());
        if (!config_files.contains(lang_path)) {
            lang = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(getResource(lang_path)));
            saveNeeded = true;
        } else {
            lang = YamlConfiguration.loadConfiguration(new File(lang_path));
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            manager.resetPlayerEffects(p);
        }
        if (saveNeeded)
            saveConfig();
        manager.resetLoreCache();
        acceptNewJobs = workAsync;
        if (workAsync != config.getBoolean("meta.async")){
            logger.log(Level.SEVERE, "To change async option, must restart.");
            sender.sendMessage(lang.getString("change_async"));
        }
        sender.sendMessage(lang.getString("config_reload"));
        return true;
    }

    @SuppressWarnings("deprecation")
    public boolean resetPlayer(CommandSender sender, String[] args) {
        if (!checkPerms(sender, "Potionarmor.pareset"))
            return true;

        if (args.length != 1) {
            sender.sendMessage(lang.getString("invalid_command"));
            return true;
        }

        // TODO: does this take too long based on our offline player list...?
        // alternatively loop through online players, or try-catch creating online
        // player
        // TODO: fuzzy searching functionality was removed, consider re-adding
        OfflinePlayer p = Bukkit.getOfflinePlayer(
                Bukkit.getServer().createPlayerProfile(args[0]).getUniqueId());

        if (p.isOnline()) {
            manager.resetPlayerEffects(p.getPlayer());
            sender.sendMessage(lang.getString("reset_notice") + args[0]); // not sure if want to keep arg passing...
            return true;
        }

        sender.sendMessage(lang.getString("player_not_found") + args[0]); // not sure if want to keep arg passing...
        return true;
    }

    public boolean printEffects(CommandSender sender) {
        if (!checkPerms(sender, "Potionarmor.effect"))
            return true;

        String msg = lang.getString("supported_effects");
        for (NamespacedKey k : supportedEffects) {
            msg += k.toString() + ", ";
        }
        msg = msg.substring(0, msg.length() - 2); // clip final comma
        sender.sendMessage(msg);
        return true;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String commandName = command.getName().toLowerCase();
        switch (commandName) {
            case "Pareset":
                return resetPlayer(sender, args);
            case "reload":
                return reloadConfigs(sender);
            case "effects":
                return printEffects(sender);
            default:
                break;
        }
        sender.sendMessage(lang.getString("invalid_command"));
        return false;
    }

    private static final class AsyncOptions {
        int nThreads;
        int maxThreads;
        long timeout;
        TimeUnit timeUnit;
        int queueCapacity;

        public AsyncOptions(int nThreads, int maxThreads,
                long timeout, TimeUnit timeUnit, int queueCapacity) {
            this.nThreads = nThreads;
            this.maxThreads = maxThreads;
            this.timeout = timeout;
            this.timeUnit = timeUnit;
            this.queueCapacity = queueCapacity;
        }

        private static AsyncOptions fromConfig(ConfigurationSection s) {
            ConfigurationSection meta = s.getConfigurationSection("meta");
            int nThreads = meta.getInt("threads", 4);
            int maxThreads = meta.getInt("maxThreads", 10);
            long timeout = meta.getLong("timeout", 60);
            TimeUnit timeUnit = TimeUnit.SECONDS;
            String timeUnitString = meta.getString("unit", "SECONDS").toUpperCase();
            try {
                timeUnit = TimeUnit.valueOf(timeUnitString);
            } catch (IllegalArgumentException e) {
                ((PotionArmorPlugin) PotionArmorPlugin.plugin).logger.log(
                        Level.SEVERE, "Invalid timeout units in meta: " + timeUnitString);
            }
            int queueCapacity = meta.getInt("capacity", 200);
            return new AsyncOptions(nThreads, maxThreads, timeout, timeUnit, queueCapacity);
        }
    }

    public void submitAsyncTask(Runnable job) {
        if (!workAsync) {
            job.run(); //blocks
        }
        if (acceptNewJobs) {
            try {
                pool.execute(job);
            } catch (RejectedExecutionException e) {
                logger.log(Level.SEVERE, 
                    "Job queue is full, rejecting event...Try increasing capcaity");
            }
        }
    }

    public void cancelAllTasks() {
        if (!workAsync)
            return;
        for (Runnable job : pool.getQueue()) {
            pool.remove(job);
        }
        pool.purge();
    }
}
