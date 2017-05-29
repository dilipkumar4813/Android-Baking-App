package iamdilipkumar.com.udacitybaking.ui.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import butterknife.OnClick;
import iamdilipkumar.com.udacitybaking.R;
import iamdilipkumar.com.udacitybaking.data.ApplicationPreferences;
import iamdilipkumar.com.udacitybaking.data.BakingProvider;
import iamdilipkumar.com.udacitybaking.data.IngredientsColumns;
import iamdilipkumar.com.udacitybaking.models.Step;
import iamdilipkumar.com.udacitybaking.ui.activities.RecipeInstructionDetailActivity;
import iamdilipkumar.com.udacitybaking.ui.activities.RecipeInstructionsListActivity;

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

    @BindView(R.id.tv_instruction_previous)
    TextView previousInstruction;

    @OnClick(R.id.tv_instruction_previous)
    void gotoPreviousInstruction() {
        Bundle arguments = new Bundle();
        arguments.putInt(RecipeInstructionsListActivity.INSTRUCTION_STEP,
                mInstructionStep);
        fragmentTransaction(arguments);
    }

    @BindView(R.id.tv_instruction_next)
    TextView nextInstruction;

    @OnClick(R.id.tv_instruction_next)
    void gotoNextInstruction() {
        Bundle arguments = new Bundle();
        arguments.putInt(RecipeInstructionsListActivity.INSTRUCTION_STEP,
                mInstructionStep + 2);
        fragmentTransaction(arguments);
    }

    private String mShortDescription, mDescription = "", mVideoUrl;
    private int mInstructionStep;
    private SimpleExoPlayer mExoPlayer;

    public RecipeInstructionDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int recipeId = ApplicationPreferences.getRecipeId(getContext());

        if (getArguments().containsKey(RecipeInstructionsListActivity.INSTRUCTION_STEP)) {
            mInstructionStep =
                    getArguments().getInt(RecipeInstructionsListActivity.INSTRUCTION_STEP) - 1;

            if (mInstructionStep >= 0) {
                Step step
                        = RecipeInstructionDetailActivity.getInstruction(
                        getActivity(), mInstructionStep);
                mShortDescription = step.getShortDescription();
                mDescription = step.getDescription();
                mVideoUrl = step.getVideoURL();
            } else {
                getIngredients(recipeId);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_instruction_detail, container, false);

        ButterKnife.bind(this, rootView);

        if (mInstructionStep == -1) {
            previousInstruction.setVisibility(View.INVISIBLE);
        }

        if (ApplicationPreferences.getTotalInstructions(getContext()) == mInstructionStep) {
            nextInstruction.setVisibility(View.INVISIBLE);
        }

        loadMediaPlayer();

        if (mDescription != null) {
            descriptionText.setText(mDescription);
        }

        if (mShortDescription != null) {
            titleText.setText(mShortDescription);
        }

        return rootView;
    }

    /**
     * Method to fetch all the ingredients for the recipe using the content provider
     *
     * @param recipeId - Used to query the ingredients
     */
    private void getIngredients(int recipeId) {
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

    /**
     * Method to load the media player for playback
     */
    private void loadMediaPlayer() {
        TrackSelector trackSelector = new DefaultTrackSelector();

        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext()
                , trackSelector, loadControl);
        simpleExoPlayer.setPlayer(mExoPlayer);

        String userAgent = Util.getUserAgent(getContext(), getActivity().getString(R.string.app_name));

        if (mVideoUrl != null) {
            if (!mVideoUrl.isEmpty()) {
                Log.d("url", mVideoUrl);
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
    }

    /**
     * Method to change the fragment based on the bottom navigation used only for
     * Phone screens
     *
     * @param arguments - Contains the key to instruct the next step
     */
    private void fragmentTransaction(Bundle arguments) {
        RecipeInstructionDetailFragment fragment = new RecipeInstructionDetailFragment();
        fragment.setArguments(arguments);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipeitem_detail_container, fragment)
                .detach(fragment)
                .attach(fragment)
                .addToBackStack(null)
                .commit();
    }

    /*@Override
    public void onDetach() {
        super.onDetach();
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }*/

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
