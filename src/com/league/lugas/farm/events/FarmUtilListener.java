package com.league.lugas.farm.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class FarmUtilListener implements Listener {

    @EventHandler
    public void onDirtChange(EntityChangeBlockEvent event) {
        Block block = event.getBlock();
        if (block.getType().equals(Material.FARMLAND)) {
            event.setCancelled(true);
        }
    }

}
