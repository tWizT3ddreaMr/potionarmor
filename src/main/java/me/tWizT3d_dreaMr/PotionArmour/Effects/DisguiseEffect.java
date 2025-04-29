package me.tWizT3d_dreaMr.PotionArmour.Effects;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlotGroup;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.utilities.parser.DisguiseParser;
import me.tWizT3d_dreaMr.PotionArmour.PotionArmorPlugin;

public class DisguiseEffect extends EquipmentEffect {

	Disguise disguise;
	EquipmentSlotGroup slot;
	EquipmentEffect.EffectType type = EquipmentEffect.EffectType.DISGUISE;

	public final static String DEFAULT_DISGUISE_STRING = PotionArmorPlugin.plugin.config.getString(
			"Defaults.disguise", "cow");

	public DisguiseEffect() {
		this(null, DEFAULT_DISGUISE_STRING);
	}

	public DisguiseEffect(EquipmentSlotGroup slot, String disguiseDesc) {
		this.slot = slot;

		try {
			disguise = DisguiseParser.parseDisguise(disguiseDesc);
		} catch (Throwable e) {
			PotionArmorPlugin.plugin.logger.severe("Error creating disguise: "
					+ e.toString() + " " + e.getMessage()
					+ "\n with parameter: " + disguiseDesc);
		}

	}

	@Override
	public int compareTo(EquipmentEffect o) {
		if (!(o instanceof DisguiseEffect)) {
			return this.type.compareTo(o.type);
		}
		DisguiseEffect cast = (DisguiseEffect) o;
		return this.disguise.toString().compareTo(cast.toString());
	}

	@Override
	public boolean applyTo(LivingEntity p) {
		DisguiseAPI.disguiseEntity((Entity) p, disguise);
		return true;
	}

	@Override
	public String toString() {
		return this.disguise.getType().toReadable();
	}

	public static DisguiseEffect fromConfig(EquipmentSlotGroup slot, ConfigurationSection s) {
		return new DisguiseEffect(slot, s.getString("entity", DEFAULT_DISGUISE_STRING));
	}

	@Override
	public boolean removeFrom(LivingEntity p) {
		DisguiseAPI.undisguiseToAll((Entity) p);
		return true;
	}

}