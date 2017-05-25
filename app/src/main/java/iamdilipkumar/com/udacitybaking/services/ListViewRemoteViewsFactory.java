package iamdilipkumar.com.udacitybaking.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import iamdilipkumar.com.udacitybaking.R;
import iamdilipkumar.com.udacitybaking.widget.RecipesWidget;

/**
 * Created on 25/05/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList<String> records;

    ListViewRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    public void onCreate() {
        records = new ArrayList<>();
        records.add("test");
        records.add("checking");
        records.add("somethign else");
        records.add("hello");
    }

    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.recipes_widget);

        String data = records.get(position);
        rv.setTextViewText(R.id.tv_recipe_title, data);

        Bundle extras = new Bundle();
        extras.putInt(RecipesWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("homescreen_meeting", data);
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.ll_widget_item, fillInIntent);

        return rv;
    }

    public int getCount() {
        Log.e("size=", records.size() + "");
        return records.size();
    }

    public void onDataSetChanged() {
        // Fetching JSON data from server and add them to records arraylist
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public void onDestroy() {
        records.clear();
    }

    public boolean hasStableIds() {
        return true;
    }

    public RemoteViews getLoadingView() {
        return null;
    }
}