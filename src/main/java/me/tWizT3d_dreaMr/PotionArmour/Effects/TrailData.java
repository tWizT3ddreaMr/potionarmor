package me.tWizT3d_dreaMr.PotionArmour.Effects;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;

import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.Vibration;

//wrapper class to unify objects
public class TrailData {
	Object dataObject;
	DataType type;
	String str;

	public TrailData(){
		this(null, null);
	}

	public TrailData(DataType _type, Object _dataObject){
		dataObject = _dataObject;
		type = _type;
	}

	public Object getData(){
		return dataObject;
	}

	public static TrailData fromConfig(ConfigurationSection cfg) {
		if(cfg == null){
			return null;
		}
		String typeName = cfg.getString("type", "");
		DataType type = null;
		Object obj = null;
		switch (typeName) {
			case "color_transition":
				type = DataType.COLOR_TRANSITION;
				OrdinaryColor start = new OrdinaryColor(
						cfg.getInt("r", 0),
						cfg.getInt("g", 0),
						cfg.getInt("b", 0));
				OrdinaryColor end = new OrdinaryColor(
						cfg.getInt("r2", 0),
						cfg.getInt("g2", 0),
						cfg.getInt("b2", 0));
				obj = new ColorTransition(start, end);
				break;
			case "note_color":
				type = DataType.NOTE_COLOR;
				obj = new NoteColor(cfg.getInt("note",0));
				break;
			case "vibration":
				type = DataType.VIBRATION;
				obj = new Vibration(cfg.getInt("duration",0));
				break;
			case "ordinary_color":
				type = DataType.ORDINARY_COLOR;
				obj = new OrdinaryColor(
						cfg.getInt("r", 0),
						cfg.getInt("g", 0),
						cfg.getInt("b", 0));
				break;
			case "material":
				type = DataType.MATERIAL;
				obj = Registry.MATERIAL.get(
					NamespacedKey.fromString(cfg.getString("material")));
				break;
			default:
				return null;

		}
		return new TrailData(type, obj);
	}

	public String toString(){
		return "TrailData: {" + this.type.toString() + " " + this.dataObject.toString() + "}";
	}

	public enum DataType {
		COLOR_TRANSITION,
		NOTE_COLOR,
		ORDINARY_COLOR,
		VIBRATION,
		MATERIAL
	}

}