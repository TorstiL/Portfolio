package com.example.herocards1;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "characters", foreignKeys = {
        @ForeignKey(
                onDelete = CASCADE,
                entity = FactionModel.class,
                parentColumns = "faction_id",
                childColumns = "home_faction_id"
        )
})
public class CharacterModel {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name="home_faction_id")
    private long homeFactionId;
    @ColumnInfo(name="character_name")
    private String characterName;
    @ColumnInfo(name = "character_rank")
    private String characterRank;
    @ColumnInfo(name="character_img_path")
    private String characterImgPath;
    @ColumnInfo(name="character_wounds")
    private int wounds;
    @ColumnInfo(name="attack_damage")
    private int attackDamage;
    @ColumnInfo(name="attack_range")
    private int attackRange;
    @ColumnInfo(name="character_speed")
    private int speed;
    @ColumnInfo(name="character_defence")
    private int defence;
    @ColumnInfo(name="character_attacks")
    private int attacks;

    public CharacterModel(@NonNull long homeFactionId, @NonNull String characterName, @NonNull String characterRank, @NonNull int wounds, @NonNull int attackDamage, @NonNull int attackRange, @NonNull int speed, @NonNull int defence, @NonNull int attacks){
        this.homeFactionId = homeFactionId;
        this.characterName = characterName;
        this.characterRank = characterRank;
        this.wounds = wounds;
        this.attackDamage = attackDamage;
        this.attackRange = attackRange;
        this.speed = speed;
        this.defence = defence;
        this.attacks = attacks;
    }

    public long getId() {
        return id;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getCharacterRank() {
        return characterRank;
    }

    public int getWounds() {
        return wounds;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public long getHomeFactionId() {
        return homeFactionId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setHomeFactionId(long homeFactionId) {
        this.homeFactionId = homeFactionId;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public void setCharacterRank(String characterRank) {
        this.characterRank = characterRank;
    }

    public void setWounds(int wounds) {
        this.wounds = wounds;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getAttacks() {
        return attacks;
    }

    public void setAttacks(int attacks) {
        this.attacks = attacks;
    }

    public String getCharacterImgPath() {
        return characterImgPath;
    }

    public void setCharacterImgPath(String characterImgPath) {
        this.characterImgPath = characterImgPath;
    }

    @Override
    public String toString(){
        return characterName;
    }
}
