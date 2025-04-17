/* (C)2024 */
package me.tWizT3d_dreaMr.PotionArmour;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent.SlotType;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EquipmentSlot;

@SuppressWarnings("deprecation")
public class EventListener implements Listener {

    public final int UNKNOWN_SLOT_NUM = 45; // TODO: explain hardcoded value from prior...?

    public EffectManager mgr;

    public EventListener(EffectManager p) {
        mgr = p;
    }

    @EventHandler
    public void changeArmor(PlayerArmorChangeEvent e) {
        ItemStack n = e.getNewItem();
        ItemStack o = e.getOldItem();
        Player p = (Player) e.getPlayer();
        SlotType slotType = e.getSlotType();
        EquipmentSlot slot;
        switch (slotType) {
            case HEAD:
                slot = EquipmentSlot.HEAD;
                break;
            case CHEST:
                slot = EquipmentSlot.CHEST;
                break;
            case LEGS:
                slot = EquipmentSlot.LEGS;
                break;
            case FEET:
                slot = EquipmentSlot.FEET;
                break;
            default:
                slot = EquipmentSlot.HAND;
        }
        this.mgr.replaceEquipment(p, n, o, slot);
    }

    // inventory click
    @EventHandler
    public void invClick(InventoryClickEvent e) {
        EquipmentSlot slot = null;
        if (e.getSlot() == e.getWhoClicked().getInventory().getHeldItemSlot()) {
            slot = EquipmentSlot.HAND;
        } else if (e.getSlot() == UNKNOWN_SLOT_NUM) { // TODO: see above, figure out magic number
            slot = EquipmentSlot.OFF_HAND;
        } else {
            return; // only act on specific slots
        }
        final Player p = (Player) e.getWhoClicked();
        ItemStack n = e.getCurrentItem();
        ItemStack o = e.getInventory().getItem(e.getSlot());
        mgr.replaceEquipment(p, n, o, slot);
    }

    // hotbar
    @EventHandler
    public void newItemHeld(PlayerItemHeldEvent e) {
        final Player p = e.getPlayer();
        final PlayerInventory inv = p.getInventory();
        ItemStack n = inv.getItem(e.getNewSlot());
        ItemStack o = inv.getItem(e.getPreviousSlot());
        mgr.replaceEquipment(p, n, o, EquipmentSlot.HAND);
    }

    // drop
    @EventHandler
    public void drop(PlayerDropItemEvent e) {
        final Player p = e.getPlayer();
        ItemStack o = e.getItemDrop().getItemStack();

        // TODO: check if dropped item is being held?
        mgr.removeEquipment(p, o);
    }

    // armor stand use
    @EventHandler
    public void armorStandInteract(PlayerArmorStandManipulateEvent e) { // PlayerInteractEntityEvent
        final Player p = e.getPlayer();
        ItemStack n = e.getArmorStandItem();
        ItemStack o = e.getPlayerItem();
        EquipmentSlot slot = e.getSlot();
        mgr.replaceEquipment(p, n, o, slot);
    }

    @EventHandler
    public void gamemode(PlayerGameModeChangeEvent e) {
        mgr.resetPlayerEffects(e.getPlayer());
    }

    // TODO: check /hat command
    // check swap hands commands

}
