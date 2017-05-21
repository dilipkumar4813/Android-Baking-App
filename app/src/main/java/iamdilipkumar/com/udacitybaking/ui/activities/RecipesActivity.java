package iamdilipkumar.com.udacitybaking.ui.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.udacitybaking.R;
import iamdilipkumar.com.udacitybaking.adapters.RecipesAdapter;
import iamdilipkumar.com.udacitybaking.data.BakingProvider;
import iamdilipkumar.com.udacitybaking.models.Ingredient;
import iamdilipkumar.com.udacitybaking.models.Recipe;
import iamdilipkumar.com.udacitybaking.models.Step;
import iamdilipkumar.com.udacitybaking.utils.ShareUtils;
import iamdilipkumar.com.udacitybaking.utils.networking.BakingApiInterface;
import iamdilipkumar.com.udacitybaking.utils.networking.NetworkUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RecipesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.gv_recipes)
    GridView gridRecipes;

    @BindView(R.id.tv_error_message)
    TextView errorText;

    @BindView(R.id.pb_loading_data)
    ProgressBar loadingProgress;

    CompositeDisposable mCompositeDisposable;
    private ArrayList<Recipe> mRecipeItems = new ArrayList<>();
    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_NAME = "recipe_name";
    //private static final String TAG = RecipesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        mCompositeDisposable = new CompositeDisposable();
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            BakingApiInterface bakingInterface = NetworkUtils.buildRetrofit().create(BakingApiInterface.class);

            mCompositeDisposable.add(bakingInterface.getRecipes()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::apiResponse, this::apiError));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_share:
                new ShareUtils().shareApp(this);
                break;
        }

        return true;
    }

    /**
     * Method to read json response and parse the same
     * by passing the objects to the adapter
     *
     * @param recipes - Main GSON Object class
     */
    private void apiResponse(ArrayList<Recipe> recipes) {
        loadingProgress.setVisibility(View.GONE);
        errorText.setVisibility(View.GONE);

        getContentResolver().delete(BakingProvider.BakingTable.CONTENT_URI, null, null);
        getContentResolver().delete(BakingProvider.IngredientsTable.CONTENT_URI, null, null);
        getContentResolver().delete(BakingProvider.StepsTable.CONTENT_URI, null, null);

        for (Recipe recipe : recipes) {
            mRecipeItems.add(recipe);

            ContentValues cvRecipe = new ContentValues();
            cvRecipe.put("recipe_id", recipe.getId());
            cvRecipe.put("name", recipe.getName());
            cvRecipe.put("servings", recipe.getServings());
            cvRecipe.put("image", recipe.getImage());
            getContentResolver().insert(BakingProvider.BakingTable.CONTENT_URI, cvRecipe);

            for (Ingredient ingredient : recipe.getIngredients()) {

                ContentValues cvIngredient = new ContentValues();
                cvIngredient.put("recipe_id", recipe.getId());
                cvIngredient.put("ingredient", ingredient.getIngredient());
                cvIngredient.put("quantity", ingredient.getQuantity());
                cvIngredient.put("measure", ingredient.getMeasure());

                getContentResolver().insert(BakingProvider.IngredientsTable.CONTENT_URI, cvIngredient);
            }

            for (Step step : recipe.getSteps()) {

                ContentValues cvSteps = new ContentValues();
                cvSteps.put("recipe_id", recipe.getId());
                cvSteps.put("step", step.getId());
                cvSteps.put("short_description", step.getShortDescription());
                cvSteps.put("long_description", step.getDescription());
                cvSteps.put("video_url", step.getVideoURL());
                cvSteps.put("thumnail_url", step.getThumbnailURL());

                getContentResolver().insert(BakingProvider.StepsTable.CONTENT_URI, cvSteps);
            }
        }

        RecipesAdapter adapter = new RecipesAdapter(this, mRecipeItems);
        gridRecipes.setAdapter(adapter);
        gridRecipes.setOnItemClickListener(this);
    }

    /**
     * Method to display the error during network operations
     *
     * @param error -Throwable to access the localized message
     */
    private void apiError(Throwable error) {
        loadingProgress.setVisibility(View.GONE);
        errorText.setText(error.getLocalizedMessage());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent recipesInstructionsIntent = new Intent(this, RecipeInstructionsListActivity.class);
        recipesInstructionsIntent.putExtra(RECIPE_ID, mRecipeItems.get(position).getId());
        recipesInstructionsIntent.putExtra(RECIPE_NAME, mRecipeItems.get(position).getName());
        startActivity(recipesInstructionsIntent);
    }
}
