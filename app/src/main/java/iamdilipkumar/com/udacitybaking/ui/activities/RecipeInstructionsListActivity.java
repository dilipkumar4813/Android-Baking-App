package iamdilipkumar.com.udacitybaking.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.udacitybaking.R;
import iamdilipkumar.com.udacitybaking.adapters.RecipesInstructionsAdapter;
import iamdilipkumar.com.udacitybaking.data.ApplicationPreferences;
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
    private String mRecipeName;

    public static final String INSTRUCTION_STEP = "instruction_step";
    public static final String INSTRUCTIONS = "instructions";
    // public static final String INSTRUCTIONS_SCROLL = "instructions_scroll_position";

    @BindView(R.id.recipeitem_list)
    RecyclerView instructionsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_instructions_list);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            mRecipeName = ApplicationPreferences.getRecipeName(this);
            mRecipeId = ApplicationPreferences.getRecipeId(this);
            toolbar.setTitle(mRecipeName);

            loadSteps();
        } else {
            mRecipeName = savedInstanceState.getString(RecipesActivity.RECIPE_NAME);
            mRecipeId = savedInstanceState.getInt(RecipesActivity.RECIPE_ID);
            mSteps = savedInstanceState.getParcelableArrayList(INSTRUCTIONS);
        }


        assert instructionsItems != null;
        mAdapter = new RecipesInstructionsAdapter(mSteps, this);
        instructionsItems.setAdapter(mAdapter);

        if (findViewById(R.id.recipeitem_detail_container) != null) {
            mTwoPane = true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, RecipeInstructionsListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(INSTRUCTIONS, mSteps);
        outState.putString(RecipesActivity.RECIPE_NAME, mRecipeName);
        outState.putInt(RecipesActivity.RECIPE_ID, mRecipeId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void instructionClick(int position) {

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putInt(INSTRUCTION_STEP, position);
            RecipeInstructionDetailFragment fragment = new RecipeInstructionDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipeitem_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeInstructionDetailActivity.class);
            intent.putExtra(INSTRUCTION_STEP, position);
            startActivity(intent);
        }
    }

    private void loadSteps() {
        Cursor cursor = getContentResolver().query(BakingProvider.StepsTable.CONTENT_URI,
                null, StepsColumns.RECIPE_ID + "=" + mRecipeId, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String shortDescription = cursor.getString(cursor.getColumnIndex(StepsColumns.SHORT_DESCRIPTION));
                    String step = cursor.getString(cursor.getColumnIndex(StepsColumns.STEPS));

                    Step instruction = new Step();
                    instruction.setShortDescription(shortDescription);
                    instruction.setId(Integer.parseInt(step));

                    mSteps.add(instruction);

                } while (cursor.moveToNext());
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        ApplicationPreferences.setTotalInstructions(this, mSteps.size());

        Step instruction = new Step();
        instruction.setShortDescription(getString(R.string.ingredients));
        instruction.setId(-1);
        mSteps.add(instruction);

        Collections.reverse(mSteps);
    }
}
