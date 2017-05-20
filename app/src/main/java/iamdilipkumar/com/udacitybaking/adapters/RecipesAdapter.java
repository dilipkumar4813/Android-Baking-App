package iamdilipkumar.com.udacitybaking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import iamdilipkumar.com.udacitybaking.R;
import iamdilipkumar.com.udacitybaking.models.Recipe;

/**
 * Created on 18/05/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class RecipesAdapter extends BaseAdapter {

    private ArrayList<Recipe> mRecipes = new ArrayList<>();
    private Context mContext;
    @BindView(R.id.tv_recipe_title)
    TextView tvRecipeTitle;

    public RecipesAdapter(Context context, ArrayList<Recipe> recipes) {
        this.mContext = context;
        this.mRecipes = recipes;
    }

    @Override
    public int getCount() {
        return mRecipes.size();
    }

    @Override
    public Object getItem(int position) {
        return mRecipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.item_recipe, parent, false);
            ButterKnife.bind(this, convertView);
        }

        Recipe item = (Recipe) getItem(position);

        tvRecipeTitle.setText(item.getName());

        return convertView;
    }
}
