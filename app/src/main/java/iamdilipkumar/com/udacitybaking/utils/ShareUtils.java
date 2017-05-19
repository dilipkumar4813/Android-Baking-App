package iamdilipkumar.com.udacitybaking.utils;

import android.app.Activity;
import android.support.v4.app.ShareCompat;

import butterknife.BindString;
import iamdilipkumar.com.udacitybaking.R;

/**
 * Created on 19/05/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class ShareUtils {

    @BindString(R.string.mime_type)
    String mMimeType;

    @BindString(R.string.share_title)
    String mShareTitle;

    @BindString(R.string.share_content)
    String mShareContent;

    /**
     * Method to build the builder for sharing
     *
     * @param activity - The activity in which the share has to be triggered
     */
    public void shareApp(Activity activity){
        ShareCompat.IntentBuilder
                .from(activity)
                .setType(mMimeType)
                .setChooserTitle(mShareTitle)
                .setText(mShareContent)
                .startChooser();
    }
}
