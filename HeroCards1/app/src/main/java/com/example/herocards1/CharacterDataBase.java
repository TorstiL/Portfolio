package com.example.herocards1;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {CharacterModel.class, FactionModel.class}, version = 1, exportSchema = false)
public abstract class CharacterDataBase extends RoomDatabase {
    private static CharacterDataBase INSTANCE;
    public abstract CharacterDao characterDao();
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CharacterDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CharacterDataBase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CharacterDataBase.class, "character_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onCreate (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            //Log.i("AUTO ", "ON_CREATE");
            DataArmyWithCharacters testArmy = new DataArmyWithCharacters(
                    new FactionModel("Skaven"),
                    Arrays.asList(
                            new CharacterModel(0,
                                    "Scritch Spiteclaw",
                                    "Leader",
                                    4,
                                    2,
                                    2,
                                    5,
                                    2,
                                    2),
                            new CharacterModel(0,
                                    "Krrk the Almost-Trusted",
                                    "Champion",
                                    3,
                                    2,
                                    1,
                                    5,
                                    2,
                                    2),
                            new CharacterModel(0,
                                    "Hungering Skaven",
                                    "Grunt",
                                    2,
                                    1,
                                    1,
                                    5,
                                    2,
                                    2)
                    )
            );

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                CharacterDao mCharacterDao = INSTANCE.characterDao();
                mCharacterDao.deleteAll();

                long insertIdentifier = mCharacterDao.insertFaction(testArmy.getFaction());

                for(CharacterModel character : testArmy.getFactionCharacters()){
                    character.setHomeFactionId(insertIdentifier);
                }
                mCharacterDao.insertCharacters(testArmy.getFactionCharacters());

            });
        }
    };
}

