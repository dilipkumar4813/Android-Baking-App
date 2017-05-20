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
import android.widget.Toast;

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

    private static final String TAG = RecipesActivity.class.getSimpleName();
    CompositeDisposable mCompositeDisposable;
    ArrayList<Recipe> recipeItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        mCompositeDisposable = new CompositeDisposable();
        ButterKnife.bind(this);

        BakingApiInterface bakingInterface = NetworkUtils.buildRetrofit().create(BakingApiInterface.class);

        mCompositeDisposable.add(bakingInterface.getRecipes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::apiResponse, this::apiError));
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

        for (Recipe recipe : recipes) {
            recipeItems.add(recipe);

            ContentValues cvRecipe = new ContentValues();
            cvRecipe.put("recipe_id", recipe.getId());
            cvRecipe.put("name", recipe.getName());
            cvRecipe.put("servings", recipe.getServings());
            cvRecipe.put("image", recipe.getImage());
            getContentResolver().insert(BakingProvider.BakingTable.CONTENT_URI, cvRecipe);

            Log.d("recipe details", "" + recipe.getName());

            for (Ingredient ingredient : recipe.getIngredients()) {
                Log.d("recipe ingredients", "" + ingredient.getIngredient());

                ContentValues cvIngredient = new ContentValues();
                cvIngredient.put("recipe_id", recipe.getId());
                cvIngredient.put("ingredient", ingredient.getIngredient());
                cvIngredient.put("quantity", ingredient.getQuantity());
                cvIngredient.put("measure", ingredient.getMeasure());
            }

            for (Step step : recipe.getSteps()) {
                Log.d("recipe steps", step.getShortDescription());

                ContentValues cvSteps = new ContentValues();
                cvSteps.put("recipe_id", recipe.getId());
                cvSteps.put("step", step.getId());
                cvSteps.put("short_description", step.getShortDescription());
                cvSteps.put("long_description", step.getDescription());
                cvSteps.put("video_url", step.getVideoURL());
                cvSteps.put("thumnail_url", step.getThumbnailURL());
            }
        }

        RecipesAdapter adapter = new RecipesAdapter(this, recipeItems);
        gridRecipes.setAdapter(adapter);
        gridRecipes.setOnItemClickListener(this);

        Log.d(TAG, "" + recipes.size());
    }

    /**
     * Method to display the error during network operations
     *
     * @param error -Throwable to access the localized message
     */
    private void apiError(Throwable error) {
        Log.d(TAG, error.getMessage().toString());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, recipeItems.get(position).getName(), Toast.LENGTH_SHORT).show();
        Intent recipesInstructionsIntent = new Intent(this, RecipeInstructionsListActivity.class);
        startActivity(recipesInstructionsIntent);
    }
}
