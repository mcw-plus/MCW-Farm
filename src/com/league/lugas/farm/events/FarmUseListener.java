package com.league.lugas.farm.events;

import com.league.lugas.farm.Main;
import com.league.lugas.farm.entities.Farm;
import com.league.lugas.farm.entities.Status;
import com.league.lugas.farm.system.FarmSystem;
import com.league.lugas.farm.utils.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class FarmUseListener implements Listener {

    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String worldName = player.getWorld().getName();
        if (!worldName.equals(plugin.getConfig().getString("farmWorld"))) return;
        if (event.getClickedBlock() == null) return;
        if (event.getHand() == null) return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;
        Location location = event.getClickedBlock().getLocation();

        Block clickedBlock = event.getClickedBlock();
        Farm farm = FarmSystem.getInstance().getCurrentFarm(location);
        if (farm == null) {
            player.sendMessage(Util.color("&f[&6MCW&f] &c자신의 농장이 아닙니다"));
            event.setCancelled(true);
        } else if (!player.getUniqueId().equals(farm.getOwnerUid())) {
            player.sendMessage(Util.color("&f[&6MCW&f] &c자신의 농장이 아닙니다"));
            event.setCancelled(true);
        } else {
            if (farm.getStatus().equals(Status.START)) {
                player.sendMessage(Util.color("&f[&6MCW&f] &c이미 농작물을 다 심었습니다"));
                event.setCancelled(true);
            } else if (clickedBlock.getType().equals(Material.FARMLAND) && event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                player.sendMessage(Util.color("&f[&6MCW&f] &c흙 블럭은 설치 및 파괴가 불가 합니다"));
                event.setCancelled(true);
            }
        }
    }
}
