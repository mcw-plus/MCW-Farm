package com.league.lugas.farm.system;

import com.league.lugas.farm.Main;

import java.io.File;

public class PluginStart {

	public PluginStart() {
		Main plugin = Main.getPlugin(Main.class);
		File configFile = new File(plugin.getDataFolder(), "config.yml");
		File farmFolder = new File(plugin.getDataFolder(), "farm/");
		File userHarvest = new File(plugin.getDataFolder(), "harvest/");

		if(!configFile.exists()) {
			String pluginName = plugin.getDescription().getName();
			plugin.getServer().getConsoleSender().sendMessage("[" + pluginName + "] 플러그인이 설정파일을 생성중입니다");
			plugin.saveResource("config.yml", false);
			plugin.getServer().getConsoleSender().sendMessage("[" + pluginName + "] 플러그인이 설정파일을 생성하였습니다");
		}
		if (!farmFolder.exists()) farmFolder.mkdir();
		if (!userHarvest.exists()) userHarvest.mkdir();
		
	}
	
}
