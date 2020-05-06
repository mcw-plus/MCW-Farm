package com.league.lugas.farm.entities;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.UUID;

public class Farm {

    private String farmId;
    private UUID ownerUid;
    private Location firstLocation;
    private Location secondLocation;
    private Status status;

    public Farm(YamlConfiguration yaml) {
        this.farmId = yaml.getString("farmId");
        this.ownerUid = (yaml.getString("ownerUid") == null)?null:UUID.fromString(yaml.getString("ownerUid"));
        this.firstLocation = yaml.getLocation("firstLocation");
        this.secondLocation = yaml.getLocation("secondLocation");
        this.status = Status.valueOf(yaml.getString("status"));
    }

    public String getFarmId() {
        return farmId;
    }

    public UUID getOwnerUid() {
        return ownerUid;
    }

    public Location getFirstLocation() {
        return firstLocation;
    }

    public Location getSecondLocation() {
        return secondLocation;
    }

    public Status getStatus() {
        return status;
    }

    public void setOwnerUid(UUID ownerUid) {
        this.ownerUid = ownerUid;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
