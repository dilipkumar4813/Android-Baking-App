package iamdilipkumar.com.udacitybaking.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import iamdilipkumar.com.udacitybaking.R;

/**
 * Created on 22/05/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class ApplicationPreferences {

    private static final String RECIPE_ID = "recipe_id";
    private static final String RECIPE_NAME = "recipe_id";

    private static SharedPreferences getDefaultPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences sharedPreferences = getDefaultPreferences(context);
        return sharedPreferences.edit();
    }

    public static void setRecipeId(Context context, int recipeId) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(RECIPE_ID, recipeId);
        editor.commit();
    }

    public static int getRecipeId(Context context) {
        return getDefaultPreferences(context).getInt(RECIPE_ID, 0);
    }

    public static void setRecipeName(Context context, String recipeName) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(RECIPE_NAME, recipeName);
        editor.commit();
    }

    public static String getRecipeName(Context context) {
        return getDefaultPreferences(context).getString(RECIPE_NAME, context.getString(R.string.app_name));
    }
}
