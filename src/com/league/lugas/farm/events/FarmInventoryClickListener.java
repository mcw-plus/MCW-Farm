package com.league.lugas.farm.events;

import com.league.lugas.farm.Main;
import com.league.lugas.farm.entities.Farm;
import com.league.lugas.farm.system.FarmSystem;
import com.league.lugas.farm.utils.Util;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FarmInventoryClickListener implements Listener {

    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String inventoryName = event.getView().getTitle();
        if (inventoryName.equals("§f[§6MCW§f] §2농장")) {
            event.setCancelled(true);

            Inventory inventory = event.getInventory();
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();
            int slot = event.getSlot();
            int currentPage;
            if (inventory.getItem(51) != null) currentPage = inventory.getItem(51).getAmount()-1;
            else if (inventory.getItem(47) != null) currentPage = inventory.getItem(47).getAmount()+1;
            else currentPage = 1;

            if (slot < 45) { // 농장 대여 시스템
                if (clickedItem == null) return;
                if (clickedItem.getType().equals(Material.BARRIER)) {
                    player.sendMessage(Util.color("&f[&6MCW&f] &c해당 농장은 이미 대여 중입니다"));
                    player.closeInventory();
                    return;
                }

                Farm farm = FarmSystem.getInstance().getFarm(clickedItem.getItemMeta().getLore().get(0).replace("§6농장 번호: §c", ""));
                if (farm.getOwnerUid() != null) {
                    player.sendMessage(Util.color("&f[&6MCW&f] &c예약 도중 다른 사람이 먼저 예약 했습니다"));
                    player.closeInventory();
                    return;
                }
                farm.setOwnerUid(player.getUniqueId());
                FarmSystem.getInstance().setFarm(farm);
                player.closeInventory();
                player.teleport(farm.getFirstLocation());
                player.sendMessage(Util.color("&f[&6MCW&f] &f"+farm.getFarmId()+" &a농장을 대여 했습니다"));
            } else {
                List<Farm> farms = FarmSystem.getInstance().getFarms();
                int startIndex;
                int endIndex;
                switch (slot) {
                    case 47: // 이번 페이지 버튼
                        if (clickedItem == null) return;

                        clickedItem.setAmount(currentPage-2);
                        ItemStack next = new ItemStack(Material.PAPER, currentPage);
                        setItemTitle(next, "&6다음 페이지");
                        inventory.setItem(51, next);

                        // 이전 페이지 로딩
                        startIndex = (currentPage-2)*45;
                        endIndex = startIndex+45;
                        for (int i = 0; i < 45; i++) inventory.clear(i);
                        for (int i = startIndex; i < endIndex; i++) {
                            Farm farm = farms.get(i);
                            addFarmIcon(inventory, farm);
                        }
                        break;
                    case 51: // 다음 페이지 버튼
                        if (clickedItem == null) return;

                        if (farms.size() > (currentPage+1)*45) clickedItem.setAmount(currentPage + 2);
                        else clickedItem.setAmount(0);
                        ItemStack pre = new ItemStack(Material.PAPER, currentPage);
                        setItemTitle(pre, "&6이전 페이지");
                        inventory.setItem(47, pre);

                        // 다음 페이지 로딩
                        startIndex = currentPage*45;
                        endIndex = Math.min(farms.size(), (currentPage+1)*45);
                        for (int i = 0; i < 45; i++) inventory.clear(i);
                        for (int i = startIndex; i < endIndex; i++) {
                            Farm farm = farms.get(i);
                            addFarmIcon(inventory, farm);
                        }
                        break;
                    case 53:
                        // 보관함 인벤토리
                        break;
                }
            }
        }
    }

    private void setItemTitle(ItemStack item, String title) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title.replaceAll("&", "§"));
        item.setItemMeta(meta);
    }

    private void setLore(ItemStack item, Integer line, String lore) {
        ItemMeta meta = item.getItemMeta();
        List<String> itemLores;
        if (item.getItemMeta().hasLore()) {
            itemLores = item.getItemMeta().getLore();
        } else {
            itemLores = new ArrayList<String>();
        }
        itemLores.set(line, lore.replaceAll("&", "§"));
        meta.setLore(itemLores);
        item.setItemMeta(meta);
    }

    private void addLore(ItemStack item, String lore) {
        ItemMeta meta = item.getItemMeta();
        List<String> itemLores;
        if (item.getItemMeta().hasLore()) {
            itemLores = item.getItemMeta().getLore();
        } else {
            itemLores = new ArrayList<String>();
        }
        itemLores.add(lore.replaceAll("&", "§"));
        meta.setLore(itemLores);
        item.setItemMeta(meta);
    }

    private void addFarmIcon(Inventory inventory, Farm farm) {

        ItemStack farmIcon;
        if (farm.getOwnerUid() == null) {
            farmIcon = new ItemStack(Material.GRASS_BLOCK);
            setItemTitle(farmIcon, "&a이용 가능");
        } else {
            farmIcon = new ItemStack(Material.BARRIER);
            setItemTitle(farmIcon, "&c사용중");
            setLore(farmIcon, 1, "&6이용자: " + plugin.getServer().getPlayer(farm.getOwnerUid()).getName());
        }
        addLore(farmIcon, "&6농장 번호: &c" + farm.getFarmId());

        for (int i = 0; i < 45; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, farmIcon);
                break;
            }
        }

    }

}
