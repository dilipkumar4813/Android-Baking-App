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
 * Created on 25/05/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class RecipesWidgetService extends RemoteViewsService {

    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.e("RecipesWidgetService", "Widget service");
        return new ListViewRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
