package iamdilipkumar.com.udacitybaking.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

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
    TextView recipeTitle;

    @BindView(R.id.tv_recipe_serves)
    TextView recipeServesText;

    @BindView(R.id.rl_recipe)
    RelativeLayout recipeContainer;

    @BindView(R.id.iv_recipe_image)
    ImageView recipeImage;

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
        String servesText =
                mContext.getString(R.string.recipe_serves_text) + " : " + item.getServings();

        recipeTitle.setText(item.getName());
        recipeServesText.setText(servesText);

        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        recipeContainer.setBackgroundColor(Color.rgb(r, g, b));

        String url = item.getImage();

        if (url != null) {
            if (!url.isEmpty()) {
                Glide.with(mContext).load(url).centerCrop().into(recipeImage);
            }
        }

        return convertView;
    }
}
