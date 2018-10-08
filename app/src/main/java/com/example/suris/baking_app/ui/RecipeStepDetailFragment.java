package com.example.suris.baking_app.ui;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.suris.baking_app.R;
import com.example.suris.baking_app.data.models.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A fragment representing a single RecipeStep detail screen.
 * This fragment is either contained in a {@link RecipeStepListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String STEP_KEY = "step_key";

    public static final String PLAY_WHEN_READY_KEY = "play_when_ready_key";

    public static final String CURRENT_POSITION_KEY = "current_position_key";

    public static final String WINDOW_INDEX_KEY = "current_window_index_key";

    /**
     * The dummy content this fragment is presenting.
     */
    private Step mStep;

    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mExoPlayerView;

    @BindView(R.id.iv_thumbnail)
    ImageView mThumbnail;

    @BindView(R.id.tv_description)
    TextView tvDescription;

    private SimpleExoPlayer mExoPlayer;

    boolean mPlayWhenReady = true;

    private long mCurrentPosition = 0;
    private int mWindowIndex = 0;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    public static RecipeStepDetailFragment newInstance(Step step) {
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(STEP_KEY, step);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Timber.d("onSaveInstanceState is not null StepDetail");
            mStep = savedInstanceState.getParcelable(STEP_KEY);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
            mCurrentPosition = savedInstanceState.getLong(CURRENT_POSITION_KEY);
            mWindowIndex = savedInstanceState.getInt(WINDOW_INDEX_KEY);
        }
        if (getArguments().containsKey(STEP_KEY)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mStep = getArguments().getParcelable(STEP_KEY);

            /*Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Step: "+mStep.get_id());
            }*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        // Show the dummy content as text in a TextView.
        if (mStep != null) {
            tvDescription.setText(mStep.getDescription());
            if(mStep.getThumbnailURL() != null && !mStep.getThumbnailURL().isEmpty()) {
                /*Picasso.with(getActivity()).load(mStep.getThumbnailURL()).placeholder(R.drawable.ic_launcher_foreground)
                        .into(mThumbnail);*/
                Glide.with(getContext())
                        .load(mStep.getThumbnailURL())
                        .into(mThumbnail);
                mThumbnail.setVisibility(View.VISIBLE);
            }
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("OnResume of StepDetail");
        if (!TextUtils.isEmpty(mStep.getVideoURL())) {
            hideSystemUi();
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("OnPause of StepDetail");
        releasePlayer();
    }

    private void initializePlayer(Uri mediaUri) {
        Timber.d("initializePlayer");
        if (mExoPlayer == null) {
            // Create a default TrackSelector
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            // Bind the player to the view.
            mExoPlayerView.setPlayer(mExoPlayer);
            // Measures bandwidth during playback. Can be null if not required.
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            // Prepare the player with the source.
            mExoPlayer.prepare(videoSource);

            // onRestore
            if (mCurrentPosition != 0)
            mExoPlayer.seekTo(mWindowIndex, mCurrentPosition);

            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            mExoPlayerView.setVisibility(View.VISIBLE);
        }
    }

        private void releasePlayer() {
            Timber.d("releasePlayer of StepDetail");
            if (mExoPlayer != null) {
                mPlayWhenReady = mExoPlayer.getPlayWhenReady();
                mCurrentPosition = mExoPlayer.getCurrentPosition();
                mWindowIndex = mExoPlayer.getCurrentWindowIndex();
                mExoPlayer.stop();
                mExoPlayer.release();
                mExoPlayer = null;
            }
        }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Timber.d("onSaveInstanceState StepDetail");
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_KEY, mStep);
        outState.putLong(CURRENT_POSITION_KEY, mCurrentPosition);
        outState.putInt(WINDOW_INDEX_KEY, mWindowIndex);
        outState.putBoolean(PLAY_WHEN_READY_KEY, mPlayWhenReady);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
