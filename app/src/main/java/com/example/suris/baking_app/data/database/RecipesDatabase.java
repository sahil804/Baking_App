package com.example.suris.baking_app.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.suris.baking_app.data.models.Ingredient;
import com.example.suris.baking_app.data.models.Recipe;
import com.example.suris.baking_app.data.models.Step;

import timber.log.Timber;

import static com.example.suris.baking_app.BuildConfig.DEBUG;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1, exportSchema = false)

public abstract class RecipesDatabase extends RoomDatabase {
    private static final String LOG_TAG = RecipesDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "sehatCentral";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static RecipesDatabase sInstance;

    public static RecipesDatabase getInstance(Context context) {
        if(DEBUG) Timber.d( "Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        RecipesDatabase.class, RecipesDatabase.DATABASE_NAME).build();
                if(DEBUG) Timber.d( "Made new database");
            }
        }
        return sInstance;
    }

    public abstract RecipeDao recipeDao();
}