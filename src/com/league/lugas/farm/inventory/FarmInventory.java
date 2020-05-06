package com.league.lugas.farm.inventory;

import com.league.lugas.farm.Main;
import com.league.lugas.farm.entities.Farm;
import com.league.lugas.farm.system.FarmSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FarmInventory {

    private Main plugin = Main.getPlugin(Main.class);

    private Player player;

    public FarmInventory(Player player) {
        this.player = player;
    }

    public void open() {
        Inventory inventory = Bukkit.createInventory(null, 54, "§f[§6MCW§f] §2농장");
        List<Farm> farms = FarmSystem.getInstance().getFarms();

        for (int i = 0; i < (Math.min(farms.size(), 45)); i++) {
            Farm farm = farms.get(i);
            addFarmIcon(inventory, farm);
        }

        ItemStack book = new ItemStack(Material.BOOK);
        setItemTitle(book, "&f[&b사용법&f]");
        addLore(book, "&7입장 가능한 농장을 클릭하면 농장을 발급 받고 이동합니다.");
        addLore(book, "&7농장에서 작물을 심은뒤 농장 안 종료 표지판을 누르면");
        addLore(book, "&7일정 시간이 지난 후 &f[&a농작물 보관함&f]&7에서 찾아가실 수 있습니다.");
        inventory.setItem(45, book);

        ItemStack playerHead = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        meta.setOwner(player.getName());
        playerHead.setItemMeta(meta);
        setItemTitle(playerHead, "&6환영합니다, &c"+player.getName()+"&6님");
        addLore(playerHead, "&6소유 농장: ");
        inventory.setItem(49, playerHead);

        if (farms.size() > 45) {
            ItemStack next = new ItemStack(Material.PAPER, 2);
            setItemTitle(next, "&6다음 페이지");
            inventory.setItem(51, next);
        }

        ItemStack chest = new ItemStack(Material.CHEST);
        setItemTitle(chest, "&f[&a농작물 보관함&f]");
        inventory.setItem(53, chest);

        player.openInventory(inventory);
    }

    private void setItemTitle(ItemStack item, String title) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title.replaceAll("&", "§"));
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
            addLore(farmIcon, "&6농장 번호: &c" + farm.getFarmId());
        } else {
            farmIcon = new ItemStack(Material.BARRIER);
            setItemTitle(farmIcon, "&c사용중");
            addLore(farmIcon, "&6농장 번호: &c" + farm.getFarmId());
            addLore(farmIcon, "&6이용자: " + plugin.getServer().getOfflinePlayer(farm.getOwnerUid()).getName());
        }

        for (int i = 0; i < 45; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, farmIcon);
                break;
            }
        }

    }
}
