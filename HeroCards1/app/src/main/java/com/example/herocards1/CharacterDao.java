package com.example.herocards1;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface CharacterDao {

    @Query("SELECT * FROM characters")
    LiveData<List<CharacterModel>> getAll();

    @Query("SELECT * FROM characters WHERE id IN (:heroIds)")
    List<CharacterModel> loadAllByIds(int[] heroIds);

    @Query("SELECT * FROM characters WHERE character_name LIKE :characterName LIMIT 1")
    CharacterModel findByName(String characterName);

    @Transaction
    @Query("SELECT * FROM factions")
    LiveData<List<DataArmyWithCharacters>> getFactionsWithCharacters();

    @Query("SELECT * FROM factions")
    LiveData<List<FactionModel>> getAllFactions();

    @Query("SELECT * FROM characters WHERE home_faction_id=:faction_id")
    LiveData<List<CharacterModel>> getAllCharactersInFaction(int faction_id);

    @Insert
    void insert(CharacterModel hero);

    @Insert
    void insertAll(CharacterModel...characters);

    @Delete
    void deleteCharacter(CharacterModel characterModel);

    @Delete
    void deleteFaction(FactionModel factionModel);

    @Query("DELETE FROM characters")
    void deleteAll();

    @Transaction
    @Insert
    long insertFaction(FactionModel faction);

    @Insert
    void insertCharacters(List<CharacterModel> characterModels);

    @Update
    void updateCharacter(CharacterModel characterModel);

    @Update
    void updateFaction(FactionModel factionModel);
}
