package com.example.suris.baking_app.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.suris.baking_app.data.database.RecipesDatabase;
import com.example.suris.baking_app.data.models.Recipe;
import com.example.suris.baking_app.data.network.BakingNetworkSource;
import com.example.suris.baking_app.data.repository.BakingCentralRepository;
import com.example.suris.baking_app.utils.AppExecutors;

import java.util.List;

import timber.log.Timber;

import static com.example.suris.baking_app.BuildConfig.DEBUG;

/**
 * Created by sahil on 10/6/2018.
 */
public class RecipeListViewModel extends AndroidViewModel {

    private static final String TAG = RecipeListViewModel.class.getSimpleName();
    BakingCentralRepository bakingCentralRepository;

    LiveData<List<Recipe>> mRecipeList;

    public LiveData<List<Recipe>> getmRecipeList() {
        mRecipeList = bakingCentralRepository.getRecipeListLiveData();
        return mRecipeList;
    }

    public RecipeListViewModel(@NonNull Application application) {
        super(application);
        Timber.d( "RecipeListViewModel: ");
        RecipesDatabase recipesDatabase = RecipesDatabase.getInstance(getApplication());
        AppExecutors executors = AppExecutors.getInstance();
        BakingNetworkSource bakingNetworkSource = BakingNetworkSource.getInstance(getApplication());
        bakingCentralRepository = BakingCentralRepository.getInstance(recipesDatabase.recipeDao(),
                bakingNetworkSource, executors);
    }


    @Override
    protected void onCleared() {
        if (DEBUG) Timber.d( "onCleared: ");
        super.onCleared();
    }
}
