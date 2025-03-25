package me.tWizT3d_dreaMr.PotionArmour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import me.tWizT3d_dreaMr.PotionArmour.Effects.EquipmentEffect;
import net.md_5.bungee.api.ChatColor;

public class EffectManager {

	// currently only async is effect application
	// TODO: consider adding item lookup/lore processing too

	// TODO: Handle slot checking...

	PotionArmorPlugin p;
	private PlayerParticlesAPI ppAPI;
	private final String LORE_DELIM = "|";

	// loreline --> effects list
	private static Map<String, List<EquipmentEffect>> effectsTable = new HashMap<String, List<EquipmentEffect>>();

	// full item lore --> relevant lore lines
	// (cache built up as items processed)
	// TODO: schedule cache clears?
	private static Map<String, List<String>> loreCache = new HashMap<String, List<String>>();

	public EffectManager(PotionArmorPlugin _p) {
		p = _p;
		if (Bukkit.getPluginManager().isPluginEnabled("PlayerParticles")) {
			ppAPI = PlayerParticlesAPI.getInstance();
		} else {
			p.logger.log(Level.SEVERE, "PlayerParticles is not loaded, trail support will not be active.");
		}
	}

	public EffectManager(PotionArmorPlugin _p, PlayerParticlesAPI _ppAPI) {
		p = _p;
		ppAPI = _ppAPI;
	}

	public void loadEffects(List<FileConfiguration> list) {
		for (FileConfiguration c : list) {
			loadEffects(c);
		}
	}

	public void loadEffects(FileConfiguration cfg) {
		effectsTable.putAll(EquipmentEffect.fromConfig(cfg, ppAPI, p.logger));
	}

	EquipmentSlot[] slots = {
		EquipmentSlot.FEET, 
		EquipmentSlot.LEGS,
		EquipmentSlot.CHEST,
		EquipmentSlot.HEAD,
		EquipmentSlot.HAND,
		EquipmentSlot.OFF_HAND
	};

	public void resetPlayerEffects(Player _p) {
		FutureTask<Void> job = new FutureTask<Void>(new Callable<Void>() {
			@Override
			public Void call() {
				PlayerInventory inv = _p.getInventory();
				List<ItemStack> equipment = new ArrayList<ItemStack>();
				equipment.addAll(Arrays.asList(inv.getArmorContents())); //in order, boots, legs, chest, helmet
				equipment.add(inv.getItemInMainHand());
				equipment.add(inv.getItemInOffHand());

				_p.clearActivePotionEffects();
				ppAPI.resetActivePlayerParticles(_p);
				for (int i = 0; i < equipment.size(); i++) {
					addEquipment(_p, equipment.get(i), slots[i]);
				}
				return null;
			}
		});
		p.submitAsyncTask(job);
	}

	private void removeEffects(Player _p, List<String> lines) {
		FutureTask<Void> job = new FutureTask<Void>(new Callable<Void>() {
			@Override
			public Void call() {
				for (String loreLine : lines) {
					for (EquipmentEffect eff : effectsTable.get(loreLine)) {
						eff.removeFrom(_p);
					}
				}
				return null;
			}
		});
		p.submitAsyncTask(job);
	}

	public void resetLoreCache() {
		loreCache.clear();
	}

	public void addEquipment(Player _p, ItemStack i, EquipmentSlot slot) {
		addEquipment(_p, i, slot, true);
	}

	private void addEquipment(Player _p, ItemStack i, EquipmentSlot slot, boolean apply) {
		List<String> lore = getLore(i);
		if (lore == null || _p == null)
			return;
		FutureTask<Void> job = new FutureTask<Void>(new Callable<Void>() {
			@Override
			public Void call() {
				for (String line : getCached(lore)) {
					for (EquipmentEffect eff : effectsTable.get(line)) {
						if (!eff.slot.test(slot)) //could probably move to outer loop
							continue;
						eff.applyTo(_p);
					}
				}
				return null;
			}
		});
		p.submitAsyncTask(job);
	}

	public List<String> getCached(List<String> lore) {
		List<String> linesWithEffects = new ArrayList<String>();
		String loreKey = loreKey(lore);
		if (loreCache.containsKey(loreKey)) {
			linesWithEffects = loreCache.get(loreKey);
		} else {
			for (String line : lore) {
				if (!effectsTable.containsKey(line)) 
					continue;
				linesWithEffects.add(line);
			}
			loreCache.put(loreKey, linesWithEffects);
		}
		return linesWithEffects;
	}

	private String loreKey(List<String> lore) {
		// hashing done by cache object
		return String.join(LORE_DELIM, lore);
	}

	public void removeEquipment(Player _p, ItemStack i) {
		removeEquipment(_p, i, true);
	}

	private void removeEquipment(Player _p, ItemStack i, boolean apply) {
		List<String> lore = getLore(i);
		if (lore == null || p == null)
			return;
		String key = loreKey(lore);
		if (!loreCache.containsKey(key))
			return;
		removeEffects(_p, loreCache.get(key));
	}

	public void replaceEquipment(Player _p, ItemStack _new, ItemStack _old, EquipmentSlot slot) {
		// TODO: figure out if bugs when new and old have overlapping effects
		if (_new == null) {
			removeEquipment(_p, _old);
			return;
		}
		if (_old == null) {
			addEquipment(_p, _new, slot);
			return;
		}
		// addEquipment(_p, _new, false);
		// removeEquipment(_p, _old, false);
		resetPlayerEffects(_p);
	}

	@SuppressWarnings("deprecation")
	private static List<String> getLore(ItemStack i) {
		if (i == null)
			return null;
		if (!i.hasItemMeta())
			return null;
		ItemMeta meta = i.getItemMeta();
		if (!meta.hasLore())
			return null;
		return meta.getLore().stream()
				.map(line -> ChatColor.stripColor(line))
				.collect(Collectors.toList());
	}

}