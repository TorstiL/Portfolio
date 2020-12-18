package com.example.herocards1;

import android.util.Log;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DataArmyWithCharacters {

    @Embedded
    private FactionModel faction;
    @Relation(
            parentColumn = "faction_id",
            entityColumn = "home_faction_id"
    )
    public List<CharacterModel> factionCharacters;

    public DataArmyWithCharacters(FactionModel faction, List<CharacterModel> factionCharacters){
        this.faction = faction;
        this.factionCharacters = factionCharacters;
    }

    public FactionModel getFaction() {
        return faction;
    }

    public List<CharacterModel> getFactionCharacters() {
        return factionCharacters;
    }

    public void setFaction(FactionModel faction) {
        this.faction = faction;
    }

    public void setFactionCharacters(List<CharacterModel> factionCharacters) {
        this.factionCharacters = factionCharacters;
    }
}
