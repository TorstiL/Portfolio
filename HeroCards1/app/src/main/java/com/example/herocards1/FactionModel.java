package com.example.herocards1;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "factions")
public class FactionModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "faction_id")
    public long factionId;
    @ColumnInfo(name = "faction_name")
    private String factionName;

    public FactionModel(@NonNull String factionName){
        this.factionName = factionName;
    }

    public String getFactionName() {
        return factionName;
    }

    public void setFactionName(String factionName) {
        this.factionName = factionName;
    }

    public long getFactionId() {
        return factionId;
    }

    public void setFactionId(long factionId) {
        this.factionId = factionId;
    }

}
