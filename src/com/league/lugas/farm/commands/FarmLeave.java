package com.league.lugas.farm.commands;

import com.league.lugas.farm.Main;
import com.league.lugas.farm.system.CommandName;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@CommandName("farmleave")
public class FarmLeave implements CommandExecutor {

    private Main plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }
}
