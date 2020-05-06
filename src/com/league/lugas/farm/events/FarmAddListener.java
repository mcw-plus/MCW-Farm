package com.league.lugas.farm.events;

import com.league.lugas.farm.Main;
import com.league.lugas.farm.entities.Farm;
import com.league.lugas.farm.entities.Status;
import com.league.lugas.farm.system.FarmSystem;
import com.league.lugas.farm.utils.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FarmAddListener implements Listener {

    private Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) throws IOException {
        Player player = event.getPlayer();
        if (player.getPlayerListHeader() == null) return;
        if (player.getPlayerListHeader().equals("§c농장 추가중 입니다")) {
            event.setCancelled(true);
            if (!event.getHand().equals(EquipmentSlot.HAND)) return;
            if (!player.getWorld().getName().equals(plugin.getConfig().getString("farmWorld"))) {
                player.sendMessage(Util.color(String.format("&f[&6MCW&f] &c설정된 농장 월드로 가세요. [%s]",
                        plugin.getConfig().getString("farmWorld"))));
                return;
            }
            if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
            if (player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD)) {
                Location location = event.getClickedBlock().getLocation();
                String worldName = location.getWorld().getName();
                int x = location.getBlockX();
                int y = location.getBlockY();
                int z = location.getBlockZ();

                for (Farm farm : FarmSystem.getInstance().getFarms()) {
                    int x1 = farm.getFirstLocation().getBlockX();
                    int x2 = farm.getSecondLocation().getBlockX();
                    int z1 = farm.getFirstLocation().getBlockZ();
                    int z2 = farm.getSecondLocation().getBlockZ();

                    if ((Math.min(x1, x2) <= x && x <= Math.max(x1, x2)) && (Math.min(z1, z2) <= z && z <= Math.max(z1, z2))) {
                        player.sendMessage(Util.color("&f[&6MCW&f] &c해당 좌표가 다른 농장의 좌표와 중복됩니다"));
                        return;
                    }
                }

                if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    player.setCompassTarget(location);

                    player.sendMessage(Util.color(String.format("&f[&6MCW&f] &a첫번째 좌표가 설정 되었습니다 [%s@%d,%d,%d]",
                            worldName, x, y, z)));
                } else {
                    if (player.getCompassTarget().equals(player.getCompassTarget().getWorld().getSpawnLocation())) {
                        player.sendMessage(Util.color("&f[&6MCW&f] &c첫번째 좌표를 먼저 설정해 주세요"));
                        return;
                    }

                    List<String> newFarmArea = new ArrayList<>();
                    int x1 = player.getCompassTarget().getBlockX();
                    int x2 = location.getBlockX();
                    int z1 = player.getCompassTarget().getBlockZ();
                    int z2 = location.getBlockZ();
                    for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
                        for (int j = Math.min(z1, z2); j <= Math.max(z1, z2); j++) {
                            newFarmArea.add(i+","+z);
                        }
                    }
                    for (Farm farm : FarmSystem.getInstance().getFarms()) {
                        x1 = farm.getFirstLocation().getBlockX();
                        x2 = farm.getSecondLocation().getBlockX();
                        z1 = farm.getFirstLocation().getBlockZ();
                        z2 = farm.getSecondLocation().getBlockZ();
                        for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
                            for (int j = Math.min(z1, z2); j <= Math.max(z1, z2); j++) {
                                if (newFarmArea.contains(i+","+j)) {
                                    player.sendMessage(Util.color("&f[&6MCW&f] &c설정 구역이 &f"+farm.getFarmId()+" &c농장과 겹칩니다"));
                                    return;
                                }
                            }
                        }
                    }

                    player.sendMessage(Util.color(String.format("&f[&6MCW&f] &a두번째 좌표가 설정 되었습니다 [%s@%d,%d,%d]",
                            worldName, x, y, z)));

                    String farmId = randomCode();
                    File file = new File(plugin.getDataFolder(), "farm/"+farmId+".yml");
                    YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                    yaml.set("farmId", farmId);
                    yaml.set("ownerUid", null);
                    yaml.set("firstLocation", player.getCompassTarget());
                    yaml.set("secondLocation", location);
                    yaml.set("status", Status.READY.toString());
                    yaml.save(file);

                    FarmSystem.getInstance().addFarm(new Farm(yaml));
                    player.setPlayerListHeader(null);
                    player.setCompassTarget(player.getCompassTarget().getWorld().getSpawnLocation());
                    player.sendMessage(Util.color("&f[&6MCW&f] &a농장 &f" + farmId + "&a(이)가 생성되었습니다"));
                }
            } else {
                player.sendMessage(Util.color("&f[&6MCW&f] &c농장을 추가 하려면 블레이즈 막대를 들고 클릭 하세요"));
                player.sendMessage(Util.color("&f[&6MCW&f] &c농장 추가를 취소하려면 &f/farm cancel &c를 입력하세요"));
            }
        }
    }

    private String randomCode() {
        StringBuilder result = new StringBuilder();
        String[] codeList = "0123456789QWERTYUIOPASDFGHJKLZXCVBNM".split("");
        for (int i = 0; i < 5; i++) {
            result.append(codeList[(int) (Math.random() * codeList.length)]);
        }
        File file = new File(plugin.getDataFolder(), "farm/"+result.toString()+".yml");
        if (file.exists()) return randomCode();
        return result.toString();
    }
}
