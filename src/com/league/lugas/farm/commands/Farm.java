package com.league.lugas.farm.commands;

import com.league.lugas.farm.Main;
import com.league.lugas.farm.inventory.FarmInventory;
import com.league.lugas.farm.system.CommandName;
import com.league.lugas.farm.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

@CommandName("farm")
public class Farm implements CommandExecutor {

    private Main plugin = Main.getPlugin(Main.class);
    private String pluginName = plugin.getDescription().getName();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Util.color("&c[Server] 콘솔에서는 해당 명령어를 실행할 수 없습니다"));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("mcw.farm.admin")) {
            player.sendMessage(Util.color("&f[&a" + pluginName + "&f] &c명령어 사용 권한이 없습니다"));
            return true;
        }

        FileConfiguration config = plugin.getConfig();

        if (args.length == 0) { // 농장 GUI 오픈
            if (Bukkit.getWorld(config.getString("farmWorld")) == null) {
                player.sendMessage(Util.color("&f[&a" + pluginName + "&f] &c/farm setworld 로 농장 월드를 먼저 설정하세요"));
                return true;
            }

            FarmInventory inventory = new FarmInventory(player);
            inventory.open();

        } else if (args.length == 1) { // 농장 추가

        } else if (args.length == 2) {

        } else {

        }

        return false;
    }

}
