package com.example.suris.baking_app;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.suris.baking_app.ui.RecipeStepDetailActivity;
import com.example.suris.baking_app.ui.RecipeStepListActivity;
import com.example.suris.baking_app.ui.RecipesListActivity;

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
public class RecipeStepListActivityTest {

    @Rule
    public ActivityTestRule<RecipeStepListActivity> recipeStepListActivityActivityTestRule =
            new ActivityTestRule<>(RecipeStepListActivity.class);

    @Test
    public void checkRecipeStepDetails() {

        onView(withId(R.id.recipestep_list)).check(matches(isDisplayed()));
        //onView(withText("Recipe Steps")).check(matches(isDisplayed()));
    }

}
