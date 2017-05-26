package iamdilipkumar.com.udacitybaking.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import iamdilipkumar.com.udacitybaking.R;

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
    private ArrayList<String> mBuzzes = new ArrayList<>();
    private Context mContext;

    StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        Log.d("intent", "" + intent.getExtras());
    }

    public void onCreate() {
    }

    public void onDestroy() {
        mBuzzes.clear();
    }

    public int getCount() {
        return mBuzzes.size();
    }

    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);

        if (position <= getCount()) {
            String buzz = mBuzzes.get(position);

            rv.setTextViewText(R.id.tv_recipe_name, buzz);
            rv.setTextViewText(R.id.tv_servings, "serves 4");

            // store the buzz ID in the extras so the main activity can use it
            Bundle extras = new Bundle();
            extras.putString("id", "testid");
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
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
        mBuzzes.add("test");
        mBuzzes.add("test2");
        mBuzzes.add("test3");
    }
}
