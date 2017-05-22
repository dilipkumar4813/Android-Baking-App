package iamdilipkumar.com.udacitybaking.ui.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.udacitybaking.R;
import iamdilipkumar.com.udacitybaking.data.BakingProvider;
import iamdilipkumar.com.udacitybaking.data.IngredientsColumns;
import iamdilipkumar.com.udacitybaking.data.StepsColumns;
import iamdilipkumar.com.udacitybaking.ui.activities.RecipeInstructionDetailActivity;
import iamdilipkumar.com.udacitybaking.ui.activities.RecipeInstructionsListActivity;
import iamdilipkumar.com.udacitybaking.ui.activities.RecipesActivity;

/**
 * A fragment representing a single RecipeItem detail screen.
 * This fragment is either contained in a {@link RecipeInstructionsListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeInstructionDetailActivity}
 * on handsets.
 */
public class RecipeInstructionDetailFragment extends Fragment {

    @BindView(R.id.instruction_player)
    SimpleExoPlayerView simpleExoPlayer;

    @BindView(R.id.tv_description)
    TextView descriptionText;

    @BindView(R.id.tv_title)
    TextView titleText;

    private String mShortDescription, mDescription = "", mVideoUrl;

    public RecipeInstructionDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(RecipesActivity.RECIPE_ID)) {
            int recipeId = getArguments().getInt(RecipesActivity.RECIPE_ID);

            if (getArguments().containsKey(RecipeInstructionsListActivity.INSTRUCTION_STEP)) {
                int instructionStep =
                        getArguments().getInt(RecipeInstructionsListActivity.INSTRUCTION_STEP) - 1;

                if (instructionStep >= 0) {
                    Cursor cursor = getActivity().getContentResolver()
                            .query(BakingProvider.StepsTable.CONTENT_URI
                                    , null
                                    , StepsColumns.RECIPE_ID + "=" + recipeId
                                            + " AND " + StepsColumns.STEPS + "=" + instructionStep
                                    , null
                                    , null);

                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            mShortDescription = cursor.getString(
                                    cursor.getColumnIndex(StepsColumns.SHORT_DESCRIPTION));
                            mDescription = cursor.getString(
                                    cursor.getColumnIndex(StepsColumns.LONG_DESCRIPTION));
                            mVideoUrl = cursor.getString(
                                    cursor.getColumnIndex(StepsColumns.VIDEO_URL));
                        }
                    }

                    if (cursor != null) {
                        cursor.close();
                    }
                } else {

                    Cursor cursor = getActivity().getContentResolver()
                            .query(BakingProvider.IngredientsTable.CONTENT_URI
                                    , null
                                    , IngredientsColumns.RECIPE_ID + "=" + recipeId
                                    , null
                                    , null);

                    if (cursor != null) {
                        int i = 1;
                        if (cursor.moveToFirst()) {
                            do {
                                String ingredient = cursor.getString(
                                        cursor.getColumnIndex(IngredientsColumns.INGREDIENT));
                                String measure = cursor.getString(
                                        cursor.getColumnIndex(IngredientsColumns.MEASURE));
                                Float quantity = cursor.getFloat(
                                        cursor.getColumnIndex(IngredientsColumns.QUANTITY));

                                mDescription += i + "." + ingredient + "\n"
                                        + quantity + " "
                                        + measure + "\n\n";
                                i++;
                            } while (cursor.moveToNext());
                        }
                    }

                    if (cursor != null) {
                        cursor.close();
                    }

                    mShortDescription = getActivity().getString(R.string.ingredients);
                }
            }
        }

        if (getArguments().containsKey(RecipesActivity.RECIPE_NAME)) {
            String name = getArguments().getString(RecipesActivity.RECIPE_NAME);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                    activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_instruction_detail, container, false);

        ButterKnife.bind(this, rootView);

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext()
                , trackSelector, loadControl);
        simpleExoPlayer.setPlayer(mExoPlayer);

        String userAgent = Util.getUserAgent(getContext(), "Baking");

        if (mVideoUrl != null) {
            if (!mVideoUrl.isEmpty()) {
                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mVideoUrl)
                        , new DefaultDataSourceFactory(
                        getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);
            } else {
                simpleExoPlayer.setVisibility(View.GONE);
            }
        } else {
            simpleExoPlayer.setVisibility(View.GONE);
        }

        if (mDescription != null) {
            descriptionText.setText(mDescription);
        }

        if (mShortDescription != null) {
            titleText.setText(mShortDescription);
        }

        return rootView;
    }
}
