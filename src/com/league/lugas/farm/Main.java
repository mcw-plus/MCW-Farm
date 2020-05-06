package com.league.lugas.farm;

import com.league.lugas.farm.system.CommandSystem;
import com.league.lugas.farm.system.FarmSystem;
import com.league.lugas.farm.system.ListenerSystem;
import com.league.lugas.farm.system.PluginStart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private String pluginName = getDescription().getName();

    @Override
    public void onEnable() {
        new PluginStart();
        new CommandSystem(getFile());
        new ListenerSystem(getFile());
        FarmSystem.getInstance().loadFarms();
        Bukkit.getConsoleSender().sendMessage("§a[" + pluginName + "] 플러그인이 활성화 되었습니다");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§c[" + pluginName + "] 플러그인이 비활성화 되었습니다");
    }

}
