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
        switch(slotType){
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
        if(e.getSlot() == e.getWhoClicked().getInventory().getHeldItemSlot()){
            slot = EquipmentSlot.HAND;
        } else if(e.getSlot() == UNKNOWN_SLOT_NUM){ // TODO: see above, figure out magic number
            slot = EquipmentSlot.OFF_HAND;
        } else{
            return; //only act on specific slots
        }
        final Player p = (Player) e.getWhoClicked();
        final PlayerInventory inv = p.getInventory();
        ItemStack n = e.getCurrentItem();
        ItemStack o = e.getInventory().getItem(e.getSlot());
        mgr.replaceEquipment(p, n, o, slot);
    }

    // hotbar
    @EventHandler
    public void newItemHeld(PlayerItemHeldEvent e) {
        final PlayerInventory inv = e.getPlayer().getInventory();
        final Player p = e.getPlayer();
        ItemStack n = inv.getItem(e.getNewSlot());
        ItemStack o = inv.getItem(e.getPreviousSlot());
        mgr.replaceEquipment(p, n, o, EquipmentSlot.HAND);
    }

    // drop
    @EventHandler
    public void drop(PlayerDropItemEvent e) {
        final Player p = e.getPlayer();
        // final PlayerInventory inv = p.getInventory();
        ItemStack o = e.getItemDrop().getItemStack();

        // TODO: check if dropped item is being held?
        mgr.removeEquipment(p, o);
    }

    // armor stand use
    @EventHandler
    public void armorStandInteract(PlayerArmorStandManipulateEvent e) { // PlayerInteractEntityEvent
        final Player p = e.getPlayer();
        // final PlayerInventory inv = p.getInventory();
        ItemStack n = e.getArmorStandItem();
        ItemStack o = e.getPlayerItem();
        EquipmentSlot slot = e.getSlot();
        mgr.replaceEquipment(p, n, o, slot);
    }

    @EventHandler
    public void gamemode(PlayerGameModeChangeEvent e) {
        mgr.resetPlayerEffects(e.getPlayer());
    }

    // private void checkHands(Player p, PlayerInventory inv){
    //     mgr.addEquipment(p, inv.getItemInMainHand(), EquipmentSlot.HAND);
    //     mgr.addEquipment(p, inv.getItemInOffHand(), EquipmentSlot.OFF_HAND);
    // }


    // ALL THIS WILL MOVE TO THE EffectManager CLASS

    // public static void resetPlayer(final Player p) {

    //     final PlayerInventory inv = p.getInventory();
    //     Bukkit.getServer()
    //             .getScheduler()
    //             .runTask(
    //                     plugin,
    //                     new Runnable() {
    //                         public void run() {
    //                             p.getActivePotionEffects().clear();
    //                             RemoveEffect(p);
    //                             for (ItemStack is : inv.getArmorContents())
    //                                 addEffect(is, p);
    //                             addEffect(inv.getItemInMainHand(), p);
    //                             addEffect(inv.getItemInOffHand(), p);
    //                         }
    //                     });
    // }

    // public void hand(Player p, ItemStack mainHand, ItemStack offHand) {
    //     RemoveEffect(p);
    //     addEffect(mainHand, p);
    //     addEffect(offHand, p);
    // }

    // public static void addEffect(ItemStack i, Player p) {
    //     if (i == null)
    //         return;
    //     if (!i.hasItemMeta())
    //         return;
    //     if (!i.getItemMeta().hasLore())
    //         return;
    //     @SuppressWarnings("deprecation")
    //     String Lore = ChatColor.stripColor(i.getItemMeta().getLore().toString());
    //     for (String eff : me.tWizT3d_dreaMr.PotionArmour.PotionArmorPlugin.Effect) {
    //         if (ConfigHandler.mConfig.getBoolean(eff + ".Enable")) {
    //             List<String> efflist = new ArrayList<String>(
    //                     ConfigHandler.mConfig
    //                             .getConfigurationSection(eff + ".List")
    //                             .getKeys(false));
    //             for (int in = 0; in < efflist.size(); in++) {
    //                 String str = efflist.get(in).toLowerCase();
    //                 int lo = ConfigHandler.mConfig.getInt(
    //                         eff + ".List." + efflist.get(in) + ".Level") - 1; // zero-indexed
    //                 while (str.endsWith(" ")) {
    //                     str = str.substring(0, str.length() - 1);
    //                 }
    //                 if (Lore.toLowerCase().contains(str)) {
    //                     String E2 = eff;
    //                     if (eff.equalsIgnoreCase("Instant_Health")) {
    //                         E2 = "HEAL";
    //                     }
    //                     if (eff.equalsIgnoreCase("Slowness")) {
    //                         E2 = "SLOW";
    //                     }
    //                     PotionEffect e = new PotionEffect(
    //                             Registry.EFFECT.get(NamespacedKey.fromString(E2)),
    //                             2147000,
    //                             lo,
    //                             true);
    //                     p.addPotionEffect(e);
    //                     me.tWizT3d_dreaMr.PotionArmour.ConfigHandler.addEffect(p, eff);
    //                 }
    //             }
    //         }
    //     }
    // }

    // public static void RemoveEffect(Player p) {
    //     // TODO: remove effects higher than intended level
    //     // e.g. regen I item will maintain a regen II effect even when regen II effect
    //     // would normally finish
    //     for (String eff : me.tWizT3d_dreaMr.PotionArmour.PotionArmorPlugin.Effect) {
    //         if (me.tWizT3d_dreaMr.PotionArmour.ConfigHandler.hasEffect(p, eff)) {
    //             List<String> efflist = new ArrayList<String>(
    //                     ConfigHandler.mConfig
    //                             .getConfigurationSection(eff + ".List")
    //                             .getKeys(false));
    //             boolean loseeff = true;
    //             for (ItemStack i : p.getInventory().getArmorContents()) {
    //                 if (i == null)
    //                     continue;
    //                 if (!i.hasItemMeta())
    //                     continue;
    //                 if (!i.getItemMeta().hasLore())
    //                     continue;
    //                 @SuppressWarnings("deprecation")
    //                 String Lore = ChatColor.stripColor(i.getItemMeta().getLore().toString());

    //                 loseeff = check(Lore, efflist);
    //             }
    //             if (loseeff && p.getInventory().getItemInMainHand() != null) {
    //                 ItemStack i = p.getInventory().getItemInMainHand();
    //                 if (i.hasItemMeta()) {
    //                     if (i.getItemMeta().hasLore()) {
    //                         @SuppressWarnings("deprecation")
    //                         String Lore = ChatColor.stripColor(i.getItemMeta().getLore().toString());

    //                         loseeff = check(Lore, efflist);
    //                     }
    //                 }
    //             }

    //             if (loseeff && p.getInventory().getItemInOffHand() != null) {
    //                 ItemStack i = p.getInventory().getItemInOffHand();
    //                 if (i.hasItemMeta()) {
    //                     if (i.getItemMeta().hasLore()) {
    //                         @SuppressWarnings("deprecation")
    //                         String Lore = ChatColor.stripColor(i.getItemMeta().getLore().toString());
    //                         loseeff = check(Lore, efflist);
    //                     }
    //                 }
    //             }
    //             if (loseeff) {
    //                 p.removePotionEffect(Registry.EFFECT.get(NamespacedKey.fromString(eff)));
    //                 me.tWizT3d_dreaMr.PotionArmour.ConfigHandler.removeEffect(p, eff);
    //             }
    //         }
    //     }
    // }

    // public static boolean check(String Lore, List<String> efflist) {
    //     for (int in = 0; in < efflist.size(); in++) {
    //         String str = efflist.get(in).toLowerCase();
    //         if (str.endsWith(" ")) {
    //             str = str.substring(0, str.length() - 1);
    //         }
    //         if (Lore.toLowerCase().contains(str)) {

    //             return false;
    //         }
    //     }
    //     return true;
    // }
}
