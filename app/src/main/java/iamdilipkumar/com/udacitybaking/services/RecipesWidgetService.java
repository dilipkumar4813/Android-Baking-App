package iamdilipkumar.com.udacitybaking.services;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import iamdilipkumar.com.udacitybaking.R;
import iamdilipkumar.com.udacitybaking.data.ApplicationPreferences;
import iamdilipkumar.com.udacitybaking.data.BakingColumns;
import iamdilipkumar.com.udacitybaking.data.BakingProvider;
import iamdilipkumar.com.udacitybaking.models.Recipe;
import iamdilipkumar.com.udacitybaking.ui.activities.RecipeInstructionsListActivity;

/**
 * Created on 26/05/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class RecipesWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(getApplicationContext(), intent);
    }
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;

    StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {
    }

    public void onDestroy() {
        recipes.clear();
    }

    public int getCount() {
        return recipes.size();
    }

    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);

        if (position <= getCount()) {
            Recipe recipe = recipes.get(position);

            rv.setTextViewText(R.id.tv_recipe_name, recipe.getName());
            String serves = mContext.getString(R.string.recipe_serves_text) + " : ";
            rv.setTextViewText(R.id.tv_servings, serves + recipe.getServings());

            if (position % 2 == 0) {
                rv.setImageViewResource(R.id.iv_recipe, R.drawable.background_stack_view);
            } else {
                rv.setImageViewResource(R.id.iv_recipe, R.drawable.food_banner);
            }

            Intent fillInIntent = new Intent();
            rv.setOnClickFillInIntent(R.id.stack_item, fillInIntent);
        }

        return rv;
    }

    public RemoteViews getLoadingView() {
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        getRecipes();
    }

    private void getRecipes() {

        Cursor cursor = mContext.getContentResolver().query(BakingProvider.BakingTable.CONTENT_URI,
                null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndex(BakingColumns.NAME));
                    int servings = cursor.getInt(cursor.getColumnIndex(BakingColumns.SERVINGS));

                    Recipe recipe = new Recipe();
                    recipe.setName(name);
                    recipe.setServings(servings);

                    recipes.add(recipe);

                } while (cursor.moveToNext());
            }
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}
