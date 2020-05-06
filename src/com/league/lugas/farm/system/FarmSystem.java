package com.league.lugas.farm.system;

import com.league.lugas.farm.Main;
import com.league.lugas.farm.entities.Farm;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FarmSystem {

    private Main plugin = Main.getPlugin(Main.class);
    private static FarmSystem instance = null;

    private Map<String, Farm> farmMap = new HashMap<>();

    public FarmSystem() {}

    public static FarmSystem getInstance() {
        if (instance == null) instance = new FarmSystem();

        return instance;
    }

    public void addFarm(Farm farm) {
        farmMap.put(farm.getFarmId(), farm);
    }

    public Farm getFarm(String farmId) {
        return farmMap.get(farmId);
    }

    public void setFarm(Farm farm) {
        farmMap.replace(farm.getFarmId(), farm);
        File file = new File(plugin.getDataFolder(), "farm/"+farm.getFarmId()+".yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set("ownerUid", farm.getOwnerUid().toString());
        yaml.set("status", farm.getStatus().toString());
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Farm> getFarms() {
        List<Farm> farms = new ArrayList<>();
        for (String s : farmMap.keySet()) {
            farms.add(farmMap.get(s));
        }
        return farms;
    }

    public void loadFarms() {
        File[] files = new File(plugin.getDataFolder(), "farm/").listFiles();
        for (File file : files) {
            String fileName = file.getName();
            if (!fileName.substring(fileName.lastIndexOf(".")+1).equals("yml")) continue;

            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            Farm farm = new Farm(yaml);
            farmMap.put(farm.getFarmId(), farm);
        }
    }

    public void reload() {
        farmMap.clear();
        loadFarms();
    }

    public Farm getCurrentFarm(Location location) {
        for (Farm farm : FarmSystem.getInstance().getFarms()) {
            List<String> locationList = new ArrayList<>();
            int x = location.getBlockX();
            int z = location.getBlockZ();

            int x1 = farm.getFirstLocation().getBlockX();
            int x2 = farm.getSecondLocation().getBlockX();
            int z1 = farm.getFirstLocation().getBlockZ();
            int z2 = farm.getSecondLocation().getBlockZ();
            for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
                for (int j = Math.min(z1, z2); j <= Math.max(z1, z2); j++) {
                    locationList.add(i+","+j);
                }
            }
            if (locationList.contains(x+","+z)) return farm;
        }
        return null;
    }
}
