package com.league.lugas.farm.system;

import com.league.lugas.farm.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ListenerSystem {

	private static final String commandPackage = "com.league.lugas.farm.events";

	public ListenerSystem(File pluginFile) {
		Main plugin = Main.getPlugin(Main.class);
		ZipInputStream zis;
		ZipEntry entry;
		List<Class> classes = new ArrayList<>();
		try {
			zis = new ZipInputStream(new FileInputStream(pluginFile));
			while ((entry = zis.getNextEntry()) != null) {
				String fileName = entry.getName().replace("/", ".");
				if (!fileName.startsWith(commandPackage) || !fileName.endsWith(".class")) continue;
				fileName = fileName.replace(".class", "");

				classes.add(Class.forName(fileName));
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		boolean hasListener = false;
		for (Class c : classes) {
			for (Class interfaceClass : c.getInterfaces()) {
				if (interfaceClass.equals(Listener.class)) {
					hasListener = true;
					break;
				}
			}
			if (!hasListener) continue;
			try {
				plugin.getServer().getPluginManager().registerEvents((Listener) c.newInstance(), plugin);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
}
