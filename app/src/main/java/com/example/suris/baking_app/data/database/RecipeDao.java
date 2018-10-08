package com.example.suris.baking_app.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.suris.baking_app.data.models.Ingredient;
import com.example.suris.baking_app.data.models.Recipe;
import com.example.suris.baking_app.data.models.Step;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipes(List<Recipe> recipeList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(Recipe recipe);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredients(List<Ingredient> ingredients);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSteps(List<Step> steps);

    @Query("SELECT * FROM receipe WHERE id=:id")
    Recipe getRecipe(int id);

    @Query("SELECT * FROM receipe")
    LiveData<List<Recipe>> getRecipes();

    @Query("DELETE FROM receipe")
    void deleteRecipes();

    @Query("SELECT * FROM ingredient WHERE recipeId=:recipeId")
    List<Ingredient> getIngredients(int recipeId);

    @Query("SELECT * FROM step WHERE recipeId=:recipeId")
    List<Step> getSteps(int recipeId);
}