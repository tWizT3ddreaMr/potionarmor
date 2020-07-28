package me.tWizT3d_dreaMr.PotionArmor;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import me.kvq.supertrailspro.API.SuperTrailsAPI;

public class PotionArmorPlayer {

private Player player;
private ArrayList<Integer> trails=new ArrayList<Integer>();
private ArrayList<Integer> trailsWeight=new ArrayList<Integer>();
private ArrayList<String> effects=new ArrayList<String>();
private ArrayList<Integer> effectsLevel=new ArrayList<Integer>();
private ArrayList<Integer> trailsn=new ArrayList<Integer>();
private ArrayList<Integer> trailsWeightn=new ArrayList<Integer>();
private ArrayList<String> effectsn=new ArrayList<String>();
private ArrayList<Integer> effectsLeveln=new ArrayList<Integer>();

public PotionArmorPlayer(Player p) {
	this.player=p;
}
public void AddTrail(int trail, int weight) {
	if(!trails.contains(trail)) {
		trails.add(trail);
		trailsWeight.add(weight);
		return;
	}
	else {
		int loc=trails.indexOf(trail);
		if(weight>trailsWeight.get(loc))
			trailsWeight.set(loc, weight);
		return;
	}
}
public void AddPotion(String pot, int level) {
	if(!effects.contains(pot)) {
		effects.add(pot);
		effectsLevel.add(level);
		return;
	}
	else {
		int loc=effects.indexOf(pot);
		if(level>effectsLevel.get(loc))
			effectsLevel.set(loc, level);
		return;
	}
}
public void addToNew(String s, Integer level, Integer trail, Integer weight) {
	if(s!=null) {
		if(!effectsn.contains(s)) {
			effectsn.add(s);
			effectsLeveln.add(level);
		}
		else {
			if(level>effectsLeveln.get(effectsn.indexOf(s)))
				effectsLeveln.set(effectsn.indexOf(s),level);
		}
	}
	if(trail!=null) {
		if(!trailsn.contains(trail)) {
			trailsn.add(trail);
			trailsWeightn.add(weight);
		}
		else {
			if(weight>trailsWeightn.get(trailsn.indexOf(trail)))
				trailsWeightn.set(trailsn.indexOf(trail),weight);
		}
	}
}
public void emptyNew() {
	trailsn.clear();
	trailsWeightn.clear();
	effectsLeveln.clear();
	effectsn.clear();
}
public ArrayList<String> getAllPotionNames(){
	return effects;
}
public ArrayList<Integer> getAllPotionLevels(){
	return effectsLevel;
}
public ArrayList<Integer> getAllTrails(){
	return trails;
}
public ArrayList<Integer> getAllTrailsWeight(){
	return trailsWeight;
}
public void removeAllPotions() {
	if(effects.isEmpty()) return;
	for(String s:effects)
		player.removePotionEffect(PotionEffectType.getByName(s));
	effects.clear();
	effectsLevel.clear();
}
public void removeTrails() {
	if(!trails.isEmpty()) {
		SuperTrailsAPI.setTrail(player, 0);
		trails.clear();
		trailsWeight.clear();
		}
}
public Player getPlayer() {
	return player;
}
public void setNewOld() {
	effects=effectsn;
	effectsLevel=effectsLeveln;
	trails=trailsn;
	trailsWeight=trailsWeightn;
}
public ArrayList<Integer> getNewTrails(){
	return trailsn;
}
public ArrayList<PotionEffectType> potionsToRemove(){
	ArrayList<PotionEffectType> ptr=new ArrayList<PotionEffectType>();
	for(String s:effects)
		if(!effectsn.contains(s)) {
			int l=effects.indexOf(s);
			ptr.add(PotionEffectType.getByName(s));
			effects.remove(l);
			effectsLevel.remove(l);
		}
	return ptr;
}
}
