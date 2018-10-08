package com.example.suris.baking_app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.suris.baking_app.data.models.Recipe;
import com.google.gson.Gson;

import timber.log.Timber;

/**
 * Created by sahil on 10/8/2018.
 */
public class PreferenceUtility {

    public static final String KEY_OBJECT = "key_object";

    public static void saveRecipe(Context context, Recipe recipe) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recipe);
        SharedPreferences.Editor myObject = prefsEditor.putString(KEY_OBJECT, json);
        Timber.d("json: "+json);
        prefsEditor.commit();
    }

    public static Recipe retrieveRecipe(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = preferences.getString(KEY_OBJECT, "");
        Recipe recipe = gson.fromJson(json, Recipe.class);
        Timber.d("recipe: "+recipe);
        return recipe;
    }
}
