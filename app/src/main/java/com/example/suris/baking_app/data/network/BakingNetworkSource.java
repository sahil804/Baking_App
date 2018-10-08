package com.example.suris.baking_app.data.network;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.example.suris.baking_app.data.database.RecipeDao;
import com.example.suris.baking_app.data.models.Ingredient;
import com.example.suris.baking_app.data.models.Recipe;
import com.example.suris.baking_app.data.models.Step;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by sahil on 10/6/2018.
 */
public class BakingNetworkSource {

    private static final String TAG = "BakingNetworkSource";
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static BakingNetworkSource sInstance;
    private MutableLiveData<List<Recipe>> recipeListLiveData = new MutableLiveData<>();
    private Context mContext;

    public MutableLiveData<List<Recipe>> getRecipeListLiveData() {
        return recipeListLiveData;
    }

    public void setRecipeListLiveData(MutableLiveData<List<Recipe>> recipeListLiveData) {
        this.recipeListLiveData = recipeListLiveData;
    }

    private BakingNetworkSource(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Get the singleton for this class
     */
    public static BakingNetworkSource getInstance(Context context) {
        Timber.d("Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new BakingNetworkSource(context);
                Timber.d("Made new network data source");
            }
        }
        return sInstance;
    }

    public void fetchRecipes(final RecipeDao recipeDao) {
        ApiInterface apiInterface = ServiceBuilder.buildService(ApiInterface.class);
        Call<List<Recipe>> recListCall = apiInterface.getRecipes();
        recListCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Timber.i( "onResponse: "+response.body());
                if(response.body() != null) {
                    List<Recipe> recipeList = response.body();
                    recipeListLiveData.postValue(response.body());
                    Timber.i( "recipeList: "+recipeList);
                    recipeDao.insertRecipes(recipeList);
                    for (Recipe recipe:recipeList) {
                        List<Ingredient> ingredientList = recipe.getIngredients();
                        int i=0;
                        for (Ingredient ingredient:ingredientList) {
                            ingredient.setRecipeId(recipe.getId());
                            ingredientList.set(i++, ingredient);
                        }
                        Timber.d( "ingredientList: "+ingredientList);
                        recipeDao.insertIngredients(ingredientList);
                        List<Step> stepList = recipe.getSteps();
                        i=0;
                        for (Step step:stepList) {
                            step.setRecipeId(recipe.getId());
                            stepList.set(i++, step);
                        }
                        recipeDao.insertSteps(stepList);
                        Timber.d( "stepList: "+stepList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                t.printStackTrace();
                Timber.d( "onFailure: ");
            }
        });
    }
}
