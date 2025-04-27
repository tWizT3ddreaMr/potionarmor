package me.tWizT3d_dreaMr.PotionArmour.Effects;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.potion.PotionEffectType;

import me.libraryaddict.disguises.disguisetypes.Disguise;

public class DisguiseEffect extends EquipmentEffect {

	public static final int MAX_DURATION = 2147000;

	Disguise disguise;
	int level = 0;
	String str = "";
	EquipmentEffect.EffectOrder order = EffectOrder.POTION;

	public DisguiseEffect() {
		this(null, null, 0);
	}

	public DisguiseEffect(EquipmentSlotGroup slot, PotionEffectType eff, int _level) {
		this.slot = slot;
		effect = eff.createEffect(MAX_DURATION, _level);
		level = _level;
		str = "CE " + eff.toString();
	}

	@Override
	public int compareTo(EquipmentEffect o) {
		// in order, rank by: equipment effect type, potion effect type, level
		if (!(o instanceof PotionEffect)) {
			return this.order.compareTo(o.order);
		}
		PotionEffect cast = (PotionEffect) o;
		int strcmp = this.effect.getType().toString().compareTo(cast.effect.getType().toString());
		if (strcmp != 0) {
			return strcmp;
		}
		return Integer.compare(this.level, cast.level);
	}

	@Override
	public boolean applyTo(LivingEntity p) {
		return p.addPotionEffect(effect);
	}

	@Override
	public String toString() {
		return this.str;
	}

	public static DisguiseEffect fromConfig(EquipmentSlotGroup slot, ConfigurationSection s) {
		NamespacedKey key = NamespacedKey.fromString(s.getString("effect"));
		return new PotionEffect(slot, Registry.EFFECT.get(key), s.getInt("level", 0));
	}

	@Override
	public boolean removeFrom(LivingEntity p) {
		// org.bukkit.potion.PotionEffect active =
		// p.getPotionEffect(this.effect.getType());
		// leave stronger or longer effects alone

		// TODO: fix behavior that drinking stronger potion will leave persistent long
		// duration potion effect
		
		// if (compareEffectsIgnoreDuration(active, this.effect)) {
		// p.removePotionEffect(this.effect.getType());
		// }

		// TODO: remove only specific potion effect, rather than all effects of same
		// class
		p.removePotionEffect(this.effect.getType());
		return true;
	}

}