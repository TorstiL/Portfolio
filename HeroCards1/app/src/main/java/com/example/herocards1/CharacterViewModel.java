package com.example.herocards1;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class CharacterViewModel extends AndroidViewModel {

    private CharacterModel selectedCharacter;
    private FactionModel selectedFaction;
    private RepositoryFactions mRepository;
    private LiveData<List<DataArmyWithCharacters>> armiesWithCharacters;
    private int factionIndex;

    public CharacterViewModel(@NonNull Application application) {
        super(application);
        mRepository = new RepositoryFactions(application);
        armiesWithCharacters = mRepository.getAllFactionsWithCharacters();
    }

    LiveData<List<DataArmyWithCharacters>> getAllFactionsWithCharacters() {
        return armiesWithCharacters;
    }

    public void setSelectedCharacter(CharacterModel selectedCharacter) {
        this.selectedCharacter = selectedCharacter;
    }

    public CharacterModel getSelectedCharacter(){
        return this.selectedCharacter;
    }

    public int getFactionIndex() {
        return factionIndex;
    }

    public void setFactionIndex(int factionIndex) {
        this.factionIndex = factionIndex;
    }

    public FactionModel getSelectedFaction() {
        return selectedFaction;
    }

    public void setSelectedFaction(FactionModel selectedFaction) {
        this.selectedFaction = selectedFaction;
    }

    public void insertCharacter (CharacterModel characterModel) {
        mRepository.insertCharacter(characterModel);
    }

    public void insertFaction(FactionModel factionModel){
        mRepository.insertFaction(factionModel);
    }

    public void updateCharacter(CharacterModel characterModel){
        mRepository.updateCharacter(characterModel);
    }

    public void updateFaction(FactionModel factionModel){
        mRepository.updateFaction(factionModel);
    }

    public void deleteCharacter(CharacterModel characterModel){
        mRepository.deleteCharacter(characterModel);
    }

    public void deleteFaction(FactionModel factionModel){
        mRepository.deleteFaction(factionModel);
    }
}
