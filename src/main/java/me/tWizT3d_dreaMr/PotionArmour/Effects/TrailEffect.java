package me.tWizT3d_dreaMr.PotionArmour.Effects;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.Vibration;

public class TrailEffect extends EquipmentEffect {

	ParticleEffect effect = null; // particle used
	ParticleStyle style = null; // pattern of appearance
	TrailData data = null; // modifier, optional (e.g. color, material, item, transition)
	PlayerParticlesAPI api = null;
	String str = "";
	EquipmentEffect.EffectOrder order = EquipmentEffect.EffectOrder.TRAIL;

	public TrailEffect(PlayerParticlesAPI pp) {
		this(pp, null, "none", "none", null);
	}

	public TrailEffect(PlayerParticlesAPI pp, EquipmentSlotGroup slot, String effect, String style, ConfigurationSection data) {
		api = pp;
		this.slot = slot;
		this.effect = ParticleEffect.fromName(effect);
		this.style = ParticleStyle.fromName(style);
		this.data = TrailData.fromConfig(data);
		this.str = "TrailEffect: {" + effect + " " + style + " " + this.data.toString() + "}";
	}

	@Override
	public int compareTo(EquipmentEffect o) {
		// in order, rank by: equipment effect type, particle, style, data obj
		if (!(o instanceof TrailEffect)) {
			return this.order.compareTo(o.order);
		}
		TrailEffect cast = (TrailEffect) o;
		return this.str.compareTo(cast.str);
	}

	@Override
	public boolean applyTo(LivingEntity p) {
		if (!(p instanceof Player)) {
			return false;
		}

		switch (this.data.type) {
			case COLOR_TRANSITION:
				api.addActivePlayerParticle(
						(Player) p, this.effect, this.style, (ColorTransition) this.data.dataObject);
				break;
			case NOTE_COLOR:
				api.addActivePlayerParticle(
						(Player) p, this.effect, this.style, (NoteColor) this.data.dataObject);
				break;
			case ORDINARY_COLOR:
				api.addActivePlayerParticle(
						(Player) p, this.effect, this.style, (OrdinaryColor) this.data.dataObject);
				break;
			case VIBRATION:
				api.addActivePlayerParticle(
						(Player) p, this.effect, this.style, (Vibration) this.data.dataObject);
				break;
			case MATERIAL:
				api.addActivePlayerParticle(
						(Player) p, this.effect, this.style, (Material) this.data.dataObject);
				break;
			default: //no data
				api.addActivePlayerParticle((Player) p, this.effect, this.style);
		}
		return true;

	}

	@Override
	public boolean removeFrom(LivingEntity p) {
		if(!(p instanceof Player))
			return false;
		//TODO: remove specific particle effect, rather than all effects of the same particle type
		api.removeActivePlayerParticles((Player) p, this.effect);
		return true;
	}

	@Override
	public String toString() {
		return this.str;
	}

	public static TrailEffect fromConfig(PlayerParticlesAPI pp, EquipmentSlotGroup slot,
			ConfigurationSection s) {
		return new TrailEffect(pp, slot,
				s.getString("effect"),
				s.getString("style"),
				s.getConfigurationSection("data"));
	}

}