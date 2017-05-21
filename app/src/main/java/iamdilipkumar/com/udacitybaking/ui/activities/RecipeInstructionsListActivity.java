package iamdilipkumar.com.udacitybaking.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.udacitybaking.R;
import iamdilipkumar.com.udacitybaking.adapters.RecipesInstructionsAdapter;
import iamdilipkumar.com.udacitybaking.data.BakingProvider;
import iamdilipkumar.com.udacitybaking.data.StepsColumns;
import iamdilipkumar.com.udacitybaking.models.Step;
import iamdilipkumar.com.udacitybaking.ui.fragments.RecipeInstructionDetailFragment;

import java.util.ArrayList;
import java.util.Collections;

/**
 * An activity representing a list of RecipeItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeInstructionDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeInstructionsListActivity extends AppCompatActivity implements RecipesInstructionsAdapter.OnInstructionsClick {

    private boolean mTwoPane;
    private ArrayList<Step> mSteps = new ArrayList<>();
    private int mRecipeId;
    RecipesInstructionsAdapter mAdapter;

    public static final String INSTRUCTION_STEP = "instruction_step";

    @BindView(R.id.recipeitem_list)
    RecyclerView instructionsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_instructions_list);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(RecipesActivity.RECIPE_NAME)) {
                toolbar.setTitle(extras.getString(RecipesActivity.RECIPE_NAME));
                Log.d("Recipe", extras.getString(RecipesActivity.RECIPE_NAME));
            }

            if (extras.containsKey(RecipesActivity.RECIPE_ID)) {
                mRecipeId = extras.getInt(RecipesActivity.RECIPE_ID);
                Cursor cursor = getContentResolver().query(BakingProvider.StepsTable.CONTENT_URI,
                        null, StepsColumns.RECIPE_ID + "=" + mRecipeId, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            //int stepId = cursor.getInt(cursor.getColumnIndex(StepsColumns._ID));
                            //int recipeId = cursor.getInt(cursor.getColumnIndex(StepsColumns.RECIPE_ID));
                            /*String longDescription = cursor.getString(cursor.getColumnIndex(StepsColumns.LONG_DESCRIPTION));
                            String step = cursor.getString(cursor.getColumnIndex(StepsColumns.STEPS));
                            String thumbnailUrl = cursor.getString(cursor.getColumnIndex(StepsColumns.THUMBNAIL_URL));
                            String videoUrl = cursor.getString(cursor.getColumnIndex(StepsColumns.VIDEO_URL));*/
                            String shortDescription = cursor.getString(cursor.getColumnIndex(StepsColumns.SHORT_DESCRIPTION));
                            String step = cursor.getString(cursor.getColumnIndex(StepsColumns.STEPS));

                            Step instruction = new Step();
                            instruction.setShortDescription(shortDescription);
                            instruction.setId(Integer.parseInt(step));
                            /*instruction.setDescription(longDescription);
                            instruction.setThumbnailURL(thumbnailUrl);
                            instruction.setVideoURL(videoUrl);*/

                            mSteps.add(instruction);

                        } while (cursor.moveToNext());
                    }
                }

                if (cursor != null) {
                    cursor.close();
                }
            }
        } else {
            toolbar.setTitle(getTitle());
        }

        Step instruction = new Step();
        instruction.setShortDescription("Ingredients");
        instruction.setId(-1);
        mSteps.add(instruction);

        Collections.reverse(mSteps);

        assert instructionsItems != null;
        mAdapter = new RecipesInstructionsAdapter(mSteps, this);
        instructionsItems.setAdapter(mAdapter);

        if (findViewById(R.id.recipeitem_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public void instructionClick(int position) {

        Step item = mSteps.get(position);

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(RecipeInstructionDetailFragment.ARG_ITEM_ID, String.valueOf(item.getId()));
            RecipeInstructionDetailFragment fragment = new RecipeInstructionDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipeitem_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeInstructionDetailActivity.class);
            intent.putExtra(RecipesActivity.RECIPE_ID, mRecipeId);
            intent.putExtra(INSTRUCTION_STEP, position);
            intent.putExtra(RecipeInstructionDetailFragment.ARG_ITEM_ID, String.valueOf(item.getId()));
            startActivity(intent);
        }
    }
}
