package com.example.herocards1;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RepositoryFactions {
    private CharacterDao mCharacterDao;
    private LiveData<List<DataArmyWithCharacters>> factionsWithCharacters;

    RepositoryFactions(Application application){
        CharacterDataBase db = CharacterDataBase.getDatabase(application);
        mCharacterDao = db.characterDao();
        factionsWithCharacters = mCharacterDao.getFactionsWithCharacters();
    }

    LiveData<List<DataArmyWithCharacters>> getAllFactionsWithCharacters(){
        return factionsWithCharacters;
    }

    void insertFaction(FactionModel faction){
        CharacterDataBase.databaseWriteExecutor.execute(() -> {
            mCharacterDao.insertFaction(faction);
        });
    }

    void insertFactionWithCharacters(DataArmyWithCharacters factionWithCharacters){
        CharacterDataBase.databaseWriteExecutor.execute(()->{
            long insertIdentifier = mCharacterDao.insertFaction(factionWithCharacters.getFaction());

            for(CharacterModel character : factionWithCharacters.getFactionCharacters()){
                character.setHomeFactionId(insertIdentifier);
            }
            mCharacterDao.insertCharacters(factionWithCharacters.getFactionCharacters());
        });
    }

    void insertCharacter(CharacterModel characterModel){
        CharacterDataBase.databaseWriteExecutor.execute(()->{
            mCharacterDao.insert(characterModel);
        });
    }

    void updateCharacter(CharacterModel characterModel){
        CharacterDataBase.databaseWriteExecutor.execute(()->{
            mCharacterDao.updateCharacter(characterModel);
        });
    }

    void updateFaction(FactionModel factionModel){
        CharacterDataBase.databaseWriteExecutor.execute(()->{
            mCharacterDao.updateFaction(factionModel);
        });
    }

    void deleteCharacter(CharacterModel characterModel){
        CharacterDataBase.databaseWriteExecutor.execute(()->{
            mCharacterDao.deleteCharacter(characterModel);
        });
    }

    void deleteFaction(FactionModel factionModel){
        CharacterDataBase.databaseWriteExecutor.execute(()->{
            mCharacterDao.deleteFaction(factionModel);
        });
    }
}
