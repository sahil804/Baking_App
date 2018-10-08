package com.example.suris.baking_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.suris.baking_app.R;
import com.example.suris.baking_app.data.models.Recipe;
import com.example.suris.baking_app.data.models.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * An activity representing a single RecipeStep detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeStepListActivity}.
 */
public class RecipeStepDetailActivity extends AppCompatActivity {

    private StepsViewPagerAdapter mStepsViewPagerAdapter;

    Recipe recipe;
    Step step;

    public static final String RECIPE_KEY = "recipe_key";

    @BindView(R.id.container)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipestep_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Timber.d("RecipeStepDetail");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // Show the Up button in the action bar.
        final ActionBar actionBar = getSupportActionBar();

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //

        // Create the detail fragment and add it to the activity
        // using a fragment transaction.
        if(savedInstanceState != null) {
            Timber.d("savedInstanceState is not null " );
            recipe = savedInstanceState.getParcelable(RECIPE_KEY);
            step = savedInstanceState.getParcelable(RecipeStepDetailFragment.STEP_KEY);
        } else {
            recipe = getIntent().getExtras().getParcelable(RECIPE_KEY);
            Timber.d("step Detail Acitivity recipe: " + recipe);
            step = getIntent().getExtras().getParcelable(RecipeStepDetailFragment.STEP_KEY);
            Timber.d("step Detail Acitivity step: " + step);
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(recipe.getName());
        }

        mStepsViewPagerAdapter = new StepsViewPagerAdapter(recipe.getSteps(), getSupportFragmentManager());
        mViewPager.setAdapter(mStepsViewPagerAdapter);
            /*RecipeStepDetailFragment fragment = RecipeStepDetailFragment.newInstance(step);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipestep_detail_container, fragment)
                    .commit();*/
        mViewPager.setCurrentItem(step.get_id());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(actionBar != null) actionBar.setTitle(recipe.getSteps().get(position).getShortDescription());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, RecipeStepListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1,false);
        }else{
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.d("saving the instance state");
        outState.putParcelable(RECIPE_KEY, recipe);
        outState.putParcelable(RecipeStepDetailFragment.STEP_KEY, step);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
