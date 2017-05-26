package iamdilipkumar.com.udacitybaking.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import iamdilipkumar.com.udacitybaking.R;
import iamdilipkumar.com.udacitybaking.ui.fragments.RecipeInstructionDetailFragment;

/**
 * An activity representing a single RecipeItem detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeInstructionsListActivity}.
 */
public class RecipeInstructionDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_instruction_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();

            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                if (extras.containsKey(RecipeInstructionsListActivity.INSTRUCTION_STEP)) {
                    arguments.putInt(RecipeInstructionsListActivity.INSTRUCTION_STEP,
                            extras.getInt(RecipeInstructionsListActivity.INSTRUCTION_STEP));
                }
            }

            RecipeInstructionDetailFragment fragment = new RecipeInstructionDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipeitem_detail_container, fragment)
                    .commit();
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
}
