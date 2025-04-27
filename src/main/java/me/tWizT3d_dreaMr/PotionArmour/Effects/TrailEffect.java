package me.tWizT3d_dreaMr.PotionArmour.Effects;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
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
	EquipmentEffect.EffectType type = EquipmentEffect.EffectType.TRAIL;

	public TrailEffect() {
		this(EquipmentSlotGroup.ANY, "small_flame", "overhead", null);
	}

	public TrailEffect(EquipmentSlotGroup _slot, String _effect, String _style, ConfigurationSection _data) {
		api = PlayerParticlesAPI.getInstance();
		this.slot = _slot;
		this.effect = ParticleEffect.fromName(_effect);
		this.style = ParticleStyle.fromName(_style);
		this.data = TrailData.fromConfig(_data);
		this.str = "TrailEffect: {" + _effect + " " + _style + " " + this.data.toString() + "}";
	}

	@Override
	public int compareTo(EquipmentEffect o) {
		// in order, rank by: equipment effect type, particle, style, data obj
		if (!(o instanceof TrailEffect)) {
			return this.type.compareTo(o.type);
		}
		TrailEffect cast = (TrailEffect) o;
		return this.str.compareTo(cast.str);
	}

	@Override
	public boolean applyTo(LivingEntity p) {
		if (!(p instanceof Player)) {
			return false;
		}
		ParticlePair applied = null;

		Player _p = (Player) p;

		switch (this.data.type) {
			case COLOR_TRANSITION:
				applied = api.addActivePlayerParticle(
						_p, this.effect, this.style, (ColorTransition) this.data.dataObject);
				break;
			case NOTE_COLOR:
				applied = api.addActivePlayerParticle(
						_p, this.effect, this.style, (NoteColor) this.data.dataObject);
				break;
			case ORDINARY_COLOR:
				applied = api.addActivePlayerParticle(
						_p, this.effect, this.style, (OrdinaryColor) this.data.dataObject);
				break;
			case VIBRATION:
				applied = api.addActivePlayerParticle(
						_p, this.effect, this.style, (Vibration) this.data.dataObject);
				break;
			case MATERIAL:
				applied = api.addActivePlayerParticle(
						_p, this.effect, this.style, (Material) this.data.dataObject);
				break;
			default: // no data
				applied = api.addActivePlayerParticle(_p, this.effect, this.style);
		}
		return applied != null;
	}

	@Override
	public boolean removeFrom(LivingEntity p) {
		if (!(p instanceof Player))
			return false;
		// TODO: remove specific particle effect, rather than all effects of the same
		// particle type
		api.removeActivePlayerParticles((Player) p, this.effect);
		return true;
	}

	@Override
	public String toString() {
		return "TrailEffect: {" + this.effect.toString() + " " + this.style.toString() + " " + this.data.toString() + "}";
	}

	public static TrailEffect fromConfig(EquipmentSlotGroup slot, ConfigurationSection s) {
		return new TrailEffect(slot,
				s.getString("effect"),
				s.getString("style"),
				s.getConfigurationSection("data"));
	}

}