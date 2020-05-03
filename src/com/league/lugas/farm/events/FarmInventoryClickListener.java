package com.league.lugas.farm.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class FarmInventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String inventoryName = event.getView().getTitle();
        if (inventoryName.equals("§f[§6MCW§f] §b농장")) {
            event.setCancelled(true);

            Inventory inventory = event.getInventory();
            Player player = (Player) event.getWhoClicked();
            Integer slot = event.getSlot();

            if (slot < 45) {

            } else {
                switch (slot) {
                    case 47:
                        // 이전 페이지
                        break;
                    case 51:
                        // 다음 페이지
                        break;
                    case 53:
                        // 보관함 인벤토리
                        break;
                }
            }
        }
    }

}
