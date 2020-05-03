package com.league.lugas.farm.inventory;

import com.league.lugas.farm.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class FarmInventory implements InventoryHolder {

    private Main plugin = Main.getPlugin(Main.class);

    private Player player;
    private Inventory inventory;

    public FarmInventory(Player player) {
        this.player = player;
    }

    public void open() {
        inventory = Bukkit.createInventory(this, 54, "§f[§6MCW§f] §b농장");

        ItemStack book = new ItemStack(Material.BOOK);
        setItemTitle(book, "&f[&b사용법&f]");
        addLore(book, "&7입장 가능한 농장을 클릭하면 농장을 발급 받고 이동합니다.");
        addLore(book, "&7농장에서 작물을 심은뒤 농장 안 종료 표지판을 누르면");
        addLore(book, "&7일정 시간이 지난 후 &f[&a농작물 보관함&f]&7에서 찾아가실 수 있습니다.");
        inventory.setItem(45, book);

        ItemStack pre = new ItemStack(Material.PAPER, 0);
        setItemTitle(pre, "&6이전 페이지");
        inventory.setItem(47, pre);

        ItemStack playerHead = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        meta.setOwner(player.getName());
        playerHead.setItemMeta(meta);
        setItemTitle(playerHead, "&6환영합니다, &c"+player.getName()+"&6님");
        addLore(playerHead, "&6소유 농장: ");
        inventory.setItem(49, playerHead);

        ItemStack next = new ItemStack(Material.PAPER, 0);
        setItemTitle(next, "&6이전 페이지");
        inventory.setItem(51, next);

        ItemStack chest = new ItemStack(Material.CHEST);
        setItemTitle(chest, "&f[&a농작물 보관함&f]");
        inventory.setItem(53, chest);

        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
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
}
