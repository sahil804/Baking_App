package com.example.suris.baking_app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.suris.baking_app.R;
import com.example.suris.baking_app.data.models.Recipe;
import com.example.suris.baking_app.ui.RecipeStepDetailActivity;
import com.example.suris.baking_app.ui.RecipesListActivity;
import com.example.suris.baking_app.utils.PreferenceUtility;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Recipe recipe = PreferenceUtility.retrieveRecipe(context);

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        if(recipe != null) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_widget_ingredients);
            views.setTextViewText(R.id.tv_wd_recipe_name, recipe.getName());
            Intent recipeListActivity = new Intent(context, RecipesListActivity.class);
            recipeListActivity.putExtra(RecipeStepDetailActivity.RECIPE_KEY, recipe);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, recipeListActivity, 0);
            views.setOnClickPendingIntent(R.id.tv_wd_recipe_name, pendingIntent);
            // Initialize the list view
            Intent intent = new Intent(context, BakingWidgetRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            // Bind the remote adapter
            views.setRemoteAdapter(R.id.recipe_widget_listview, intent);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_widget_listview);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

