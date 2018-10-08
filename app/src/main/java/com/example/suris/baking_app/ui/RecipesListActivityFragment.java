package com.example.suris.baking_app.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.suris.baking_app.R;
import com.example.suris.baking_app.adapter.RecipeListAdapter;
import com.example.suris.baking_app.data.models.Recipe;
import com.example.suris.baking_app.utils.BakingIdlingResource;
import com.example.suris.baking_app.utils.PreferenceUtility;
import com.example.suris.baking_app.viewModel.RecipeListViewModel;
import com.example.suris.baking_app.widget.BakingWidgetRemoteViewsService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipesListActivityFragment extends Fragment implements RecipeListAdapter.RecipeAdapterOnClickHandler{

    public static final String TAG = RecipesListActivityFragment.class.getSimpleName();
    public static final String ID_EXTRA_INTENT = "recipe_id";

    @BindView(R.id.rv_recipe_list)
    RecyclerView mRecyclerView;

    /*@BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeRefreshLayout;*/

    private RecipeListViewModel mViewModel;

    private RecipeListAdapter mAdapter;

    BakingIdlingResource idlingResource;


    public RecipesListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        ButterKnife.bind(this, view);
        idlingResource = (BakingIdlingResource) ((RecipesListActivity) getActivity()).getIdlingResource();
        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }
        intialiseRecyclerView();
        initialiseViewModel();
        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle != null) {
            Recipe recipe = bundle.getParcelable(RecipeStepDetailActivity.RECIPE_KEY);
            if(recipe != null) {
                Intent recipeStepListIntent = new Intent(getContext(), RecipeStepListActivity.class);
                recipeStepListIntent.putExtra(ID_EXTRA_INTENT, recipe);
                startActivity(recipeStepListIntent);
            }
            Timber.d("List fragment: "+recipe);
        } else {
            Timber.d("List fragment: bundle null");
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isOnline()) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_network), Toast.LENGTH_SHORT).show();
        }
    }

    private void intialiseRecyclerView() {
        RecyclerView.LayoutManager layoutManager;
        if(!RecipesListActivity.ismTwoPane()) {
            layoutManager =
                    new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(getContext(), 3);
        }
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RecipeListAdapter(getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initialiseViewModel() {

        mViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        mViewModel.getmRecipeList().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipeList) {
                Timber.d( "onChanged: "+recipeList);
                mAdapter.swapRecipeList(recipeList);
                if(recipeList != null && recipeList.size() > 0) {
                    idlingResource.setIdleState(true);
                    PreferenceUtility.saveRecipe(getContext(), recipeList.get(0));
                }
            }
        });

        /*mViewModel.getAppointmentDetails().observe(this, appointmentDetailsEntities -> {
            if(DEBUG) Log.d(TAG,"change detected !!! "+appointmentDetailsEntities);
            if(appointmentDetailsEntities != null) {
                if(DEBUG) Log.d(TAG,"change detected size !!! "+appointmentDetailsEntities.size());
            }
            if (appointmentDetailsEntities != null) mAppointmentsAdapter.swapAppointmentDetails(appointmentDetailsEntities);
        });
        mViewModel.setDate(Utility.getTodayDateInString());
        tvDateSelected.setText("Today, "+Utility.getTodayDateInString());*/
    }

    @Override
    public void onClick(Recipe recipe) {
        Timber.d( "onClick: "+recipe);
        PreferenceUtility.saveRecipe(getContext(), recipe);
        BakingWidgetRemoteViewsService.updateWidget(getContext());
        Intent recipeStepListIntent = new Intent(getContext(), RecipeStepListActivity.class);
        recipeStepListIntent.putExtra(ID_EXTRA_INTENT, recipe);
        startActivity(recipeStepListIntent);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
