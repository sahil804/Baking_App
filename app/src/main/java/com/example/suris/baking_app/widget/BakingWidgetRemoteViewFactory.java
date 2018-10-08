package com.example.suris.baking_app.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.suris.baking_app.R;
import com.example.suris.baking_app.data.models.Recipe;
import com.example.suris.baking_app.utils.PreferenceUtility;

import timber.log.Timber;

/**
 * Created by sahil on 10/8/2018.
 */
public class BakingWidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Recipe recipe;

    public BakingWidgetRemoteViewFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        recipe = PreferenceUtility.retrieveRecipe(mContext);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Timber.d("getCount"+recipe.getIngredients().size());
        return recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_list_item);
        rv.setTextViewText(R.id.tv_ingredent_item, recipe.getIngredients().get(i).getIngredient());
        Timber.d("getViewAt "+recipe.getIngredients().get(i).getIngredient());
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
