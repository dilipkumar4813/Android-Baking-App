package iamdilipkumar.com.udacitybaking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.udacitybaking.models.Recipe;
import iamdilipkumar.com.udacitybaking.utils.networking.BakingApiInterface;
import iamdilipkumar.com.udacitybaking.utils.networking.NetworkUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RecipesActivity extends AppCompatActivity {

    @BindView(R.id.gv_recipes)
    GridView gridRecipes;

    private static final String TAG = RecipesActivity.class.getSimpleName();
    CompositeDisposable mCompositeDisposable;

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

    /**
     * Method to read json response and parse the same
     * by passing the objects to the adapter
     *
     * @param recipes - Main GSON Object class
     */
    private void apiResponse(Recipe recipes) {
        Log.d(TAG, "" + recipes);
    }

    /**
     * Method to display the error during network operations
     *
     * @param error -Throwable to access the localized message
     */
    private void apiError(Throwable error) {
        Log.d(TAG, error.getLocalizedMessage().toString());
    }
}
