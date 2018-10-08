package com.example.suris.baking_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.suris.baking_app.R;
import com.example.suris.baking_app.data.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListAdapterViewHolder>{

    public static final String TAG = RecipeListAdapter.class.getSimpleName();
    private List<Recipe> mRecipeList;
    private final Context mContext;

    final private RecipeAdapterOnClickHandler mClickHandler;


    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipeListAdapter(Context mContext, RecipeAdapterOnClickHandler mClickHandler) {
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }


    @NonNull
    @Override
    public RecipeListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.layout_recipe_list_item, parent, false);
        view.setFocusable(true);
        return new RecipeListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeListAdapterViewHolder holder, int position) {
        Timber.d( "onBindViewHolder: ");
        String recipeName = mRecipeList.get(position).getName();
        String recipeServing = "SERVINGS: "+String.valueOf(mRecipeList.get(position).getServings());
        Timber.d( "onBindViewHolder: recipeName:"+recipeName + " recipeServing"+recipeServing);
        holder.tvRecipeName.setText(recipeName);
        holder.tvReipeServings.setText(recipeServing);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClickHandler != null) {
                    Timber.d("onCick holder.getAdapterPosition()"+holder.getAdapterPosition());
                    mClickHandler.onClick(mRecipeList.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    public void swapRecipeList(final List<Recipe> recipeList) {
        mRecipeList = recipeList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == mRecipeList) return 0;
        return mRecipeList.size();
    }

    public class RecipeListAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recipe_name)
        public TextView tvRecipeName;

        @BindView(R.id.tv_recipe_servings)
        public TextView tvReipeServings;

        public RecipeListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}