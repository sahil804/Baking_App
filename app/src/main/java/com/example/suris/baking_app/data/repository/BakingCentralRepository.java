package com.example.suris.baking_app.data.repository;

import android.arch.lifecycle.LiveData;

import com.example.suris.baking_app.data.database.RecipeDao;
import com.example.suris.baking_app.data.models.Recipe;
import com.example.suris.baking_app.data.network.BakingNetworkSource;
import com.example.suris.baking_app.utils.AppExecutors;

import java.util.List;

import timber.log.Timber;

import static com.example.suris.baking_app.BuildConfig.DEBUG;

/**
 * Created by sahil on 10/6/2018.
 */
public class BakingCentralRepository {
    private static final Object LOCK = new Object();
    private static final String TAG = BakingCentralRepository.class.getSimpleName();
    private static BakingCentralRepository sInstance;
    BakingNetworkSource mBakingNetworkSource;
    AppExecutors mExecutors;
    RecipeDao mRecipeDao;
    LiveData<List<Recipe>> recipeListLiveData;

    public BakingCentralRepository(RecipeDao recipeDao, BakingNetworkSource bakingNetworkSource, AppExecutors mExecutors) {
        this.mBakingNetworkSource = bakingNetworkSource;
        this.mExecutors = mExecutors;
        this.mRecipeDao = recipeDao;
    }

    public synchronized static BakingCentralRepository getInstance(
            RecipeDao recipeDao, BakingNetworkSource bakingNetworkSource, AppExecutors executors) {
        if (DEBUG) Timber.d(TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new BakingCentralRepository(recipeDao,bakingNetworkSource, executors);
                if (DEBUG) Timber.d(TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    public LiveData<List<Recipe>> getRecipeListLiveData() {
        mBakingNetworkSource.fetchRecipes(mRecipeDao);
        return mBakingNetworkSource.getRecipeListLiveData();
    }

}
