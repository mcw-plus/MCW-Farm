package com.league.lugas.farm.commands;

import com.league.lugas.farm.Main;
import com.league.lugas.farm.entities.Farm;
import com.league.lugas.farm.inventory.FarmInventory;
import com.league.lugas.farm.system.CommandName;
import com.league.lugas.farm.system.FarmSystem;
import com.league.lugas.farm.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;


@CommandName("farm")
public class FarmCommand implements CommandExecutor {

    private Main plugin = Main.getPlugin(Main.class);
    private String pluginName = plugin.getDescription().getName();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Util.color("&c[Server] 콘솔에서는 해당 명령어를 실행할 수 없습니다"));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("mcw.farm.open")) {
            player.sendMessage(Util.color("&f[&a" + pluginName + "&f] &c명령어 사용 권한이 없습니다"));
            return true;
        }

        if (args.length == 0) { // 농장 GUI 오픈
            if (Bukkit.getWorld(plugin.getConfig().getString("farmWorld")) == null) {
                player.sendMessage(Util.color("&f[&a" + pluginName + "&f] &c/farm setworld 로 농장 월드를 먼저 설정하세요"));
                return true;
            }

            for (Farm farm : FarmSystem.getInstance().getFarms()) {
                if (farm.getOwnerUid() == null) continue;
                if (farm.getOwnerUid().equals(player.getUniqueId())) {
                    Location farmLocation = farm.getFirstLocation();
                    Location newLocation = farmLocation.clone();
                    newLocation.setX(farmLocation.getX()+0.5);
                    newLocation.setZ(farmLocation.getZ()+0.5);
                    newLocation.setY(farmLocation.getBlockY()+1);
                    player.teleport(newLocation);
                    player.sendMessage(Util.color("&f[&6MCW&f] &a농장으로 이동 하였습니다"));
                    return true;
                }
            }

            FarmInventory inventory = new FarmInventory(player);
            inventory.open();
        } else if (args.length == 1) { // 농장 유틸
            if (!player.hasPermission("mcw.farm.admin")) {
                player.sendMessage(Util.color("&f[&a" + pluginName + "&f] &c명령어 사용 권한이 없습니다"));
                return true;
            }
            switch (args[0]) {
                case "help":
                    player.sendMessage(Util.color("&2====================&f[&6MCW&f]&2===================="));
                    player.sendMessage(Util.color("&a/farm &8- &7농장 GUI를 오픈 합니다"));
                    player.sendMessage(Util.color("&a/farm add &8- &7농장을 등록 합니다"));
                    player.sendMessage(Util.color("&a/farm delete <농장 아이디> &8- &7해당 농장을 등록 해제 합니다"));
                    player.sendMessage(Util.color("&a/farm cancel &8- &7농장 추가 작업을 취소 합니다"));
                    player.sendMessage(Util.color("&a/farm setworld &8- &7현재 월드를 농장 월드로 지정합니다"));
                    player.sendMessage(Util.color("&a/farm reload &8- &7농장 설정 파일을 리로드 합니다"));
                    break;
                case "add":
                    player.setPlayerListHeader("§c농장 추가중 입니다");
                    player.sendMessage(Util.color("&f[&6MCW&f] &a농장 크기 설정 시스템이 시작 되었습니다"));
                    player.sendMessage(Util.color("&f[&6MCW&f] &a블레이즈 막대기로 농장 범위를 설정 하세요"));
                    break;
                case "cancel":
                    player.setPlayerListHeader(null);
                    player.setCompassTarget(player.getCompassTarget().getWorld().getSpawnLocation());
                    player.sendMessage(Util.color("&f[&6MCW&f] &a농장 크기 설정 시스템이 취소 되었습니다"));
                    break;
                case "setworld":
                    String worldName = player.getWorld().getName();
                    plugin.getConfig().set("farmWorld", worldName);
                    player.sendMessage(Util.color("&f[&6MCW&f] &f" + worldName + " &a월드를 농장 월드로 지정 하였습니다"));
                    break;
                case "reload":
                    FarmSystem.getInstance().reload();
                    player.sendMessage(Util.color("&f[&6MCW&f] &aMCW-Farm이 리로드 되었습니다"));
                    break;
                default:
                    player.sendMessage(Util.color("&f[6MCW&f] &c잘못된 명령어 사용법 입니다. &f/farm help &c로 도움말을 참조하세요"));
                    break;
            }
        } else if (args.length == 2) { // 농장 등록 해제
            if (!player.hasPermission("mcw.farm.admin")) {
                player.sendMessage(Util.color("&f[&a" + pluginName + "&f] &c명령어 사용 권한이 없습니다"));
                return true;
            }
            if (args[0].equals("delete")) {
                Farm farm = FarmSystem.getInstance().getFarm(args[1]);
                if (farm == null) {
                    player.sendMessage(Util.color("&f[&6MCW&f] &f"+args[1]+" &c농장은 존재하지 않습니다"));
                } else {
                    new File(plugin.getDataFolder(), "farm/"+farm.getFarmId()+".yml").delete();
                    FarmSystem.getInstance().reload();
                    player.sendMessage(Util.color("&f[&6MCW&f] &f"+farm.getFarmId()+" &a농장을 삭제 하였습니다"));
                }
            } else {
                player.sendMessage(Util.color("&f[6MCW&f] &c잘못된 명령어 사용법 입니다. &f/farm help &c로 도움말을 참조하세요"));
            }
        } else {
            player.sendMessage(Util.color("&f[6MCW&f] &c잘못된 명령어 사용법 입니다. &f/farm help &c로 도움말을 참조하세요"));
        }

        return false;
    }

}
