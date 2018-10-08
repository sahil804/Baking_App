package com.example.suris.baking_app.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.suris.baking_app.R;
import com.example.suris.baking_app.utils.BakingIdlingResource;

import timber.log.Timber;

public class RecipesListActivity extends AppCompatActivity {

    public static final String TAG = RecipesListActivity.class.getSimpleName();

    public static boolean ismTwoPane() {
        return mTwoPane;
    }

    private BakingIdlingResource mIdlingResource;

    public static void setmTwoPane(boolean mTwoPane) {
        RecipesListActivity.mTwoPane = mTwoPane;
    }

    private static boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Timber.plant(new Timber.DebugTree());

        if(findViewById(R.id.tab_fragment) != null) {
            mTwoPane = true;
        }

        Timber.d( "onCreate: "+mTwoPane);

        /*Recipe recipe = getIntent().getExtras().getParcelable(RecipeStepDetailActivity.RECIPE_KEY);
        Timber.d("step Detail Acitivity recipe: " + recipe);*/
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new BakingIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_recipes_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
