package com.example.suris.baking_app.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suris.baking_app.R;
import com.example.suris.baking_app.data.models.Ingredient;
import com.example.suris.baking_app.data.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by sahil on 10/6/2018.
 */
public class RecipeStepsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int INGREDIENTS = 0, STEPS = 1;
    private Recipe recipe;

    final private RecipeStepsListAdapterOnClickHandler mClickHandler;

    public RecipeStepsListAdapter(Recipe recipe, RecipeStepsListAdapterOnClickHandler clickHandler) {
        this.recipe = recipe;
        mClickHandler = clickHandler;
    }

    public interface RecipeStepsListAdapterOnClickHandler {
        void onClick(int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case INGREDIENTS:
                view = inflater.inflate(R.layout.layout_ingredients_list_item, parent, false);
                viewHolder = new IngredientsViewHolder(view);
                break;

            case STEPS:
                view = inflater.inflate(R.layout.layout_steps_list_item, parent, false);
                viewHolder = new StepsViewHolder(view);
                break;

            default:
                view = inflater.inflate(R.layout.layout_steps_list_item, parent, false);
                viewHolder = new StepsViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case INGREDIENTS:
                IngredientsViewHolder ingredientsViewHolder = (IngredientsViewHolder) holder;
                ingredientsViewHolder.configureViewHolder(position);
                break;
            case STEPS:
                StepsViewHolder stepsViewHolder = (StepsViewHolder) holder;
                stepsViewHolder.configureViewHolder(position);
                //prescriptionViewHolder.tvPrescription.setText(mEncounters.get(position).toString());
                break;
            default:
                Timber.d( "onBindViewHolder: should not come here !!!");

        }
    }

    @Override
    public int getItemCount() {
        if(recipe == null) return 0;
        else return recipe.getSteps().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return INGREDIENTS;
        else return STEPS;
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredents)
        TextView tvIngredients;
        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void configureViewHolder(int position) {
            Timber.d( "configureViewHolder: ingredients position:"+position);
            List<Ingredient> ingredientList = recipe.getIngredients();
            int i = 0;
            StringBuilder ingredients = new StringBuilder();
            Timber.d("size: "+ingredientList.size());
            for (Ingredient ingredient:ingredientList) {
                ingredients.append(++i + ". "+ ingredient.getIngredient() + "\n"+
                        "Quantity:"+ingredient.getQuantity()+ " "+ingredient.getMeasure());
                if(i <= ingredientList.size() -1) {
                    ingredients.append("\n\n");
                }
            }
            tvIngredients.setText(ingredients.toString());
        }
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_step_number)
        TextView tvStepNumber;

        @BindView(R.id.tv_step_description)
        TextView tvStepDescription;

        @BindView(R.id.iv_play)
        ImageView ivPlay;

        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void configureViewHolder(int position) {
            int id = recipe.getSteps().get(position-1).get_id();
            Timber.d( "configureViewHolder: step position:"+position+ " id:"+id
            +" step:"+recipe.getSteps().get(position-1));
            tvStepNumber.setText(String.valueOf(id));
            tvStepDescription.setText(recipe.getSteps().get(position-1).getShortDescription());
        }

        @OnClick(R.id.iv_play)
        public void onClick(View view) {
            Timber.d( "onClick: RecipeStepsListAdapter position "+getAdapterPosition());
            mClickHandler.onClick(getAdapterPosition() - 1);
        }
    }
}
