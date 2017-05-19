package iamdilipkumar.com.udacitybaking.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import iamdilipkumar.com.udacitybaking.models.Recipe;

/**
 * Created on 18/05/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class RecipesAdapter extends BaseAdapter {

    ArrayList<Recipe> recipes = new ArrayList<>();

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
