package com.example.suris.baking_app;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.suris.baking_app.ui.RecipesListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by sahil on 10/8/2018.
 */
@RunWith(AndroidJUnit4.class)
public class RecipesListActivityTest {

    private IdlingResource mIdlingResource;


    @Rule
    public ActivityTestRule<RecipesListActivity> recipesListActivityActivityTestRule =
            new ActivityTestRule<>(RecipesListActivity.class);

    @Before
    public void registerIdlingResources() {
        mIdlingResource = recipesListActivityActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void checkRecipeItem() {

        onView(withId(R.id.rv_recipe_list)).check(matches(hasDescendant(withText("Nutella Pie"))));
        onView(withId(R.id.rv_recipe_list)).check(matches(hasDescendant(withText("Brownies"))));
        onView(withId(R.id.rv_recipe_list)).check(matches(hasDescendant(withText("Yellow Cake"))));
        onView(withId(R.id.rv_recipe_list)).check(matches(hasDescendant(withText("Cheesecake"))));
        onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withText("Ingredients")).check(matches(isDisplayed()));

    }

   /* @Test
    public void checkRecipeItemClick() {

    }*/

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
