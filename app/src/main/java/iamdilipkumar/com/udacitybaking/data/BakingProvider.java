package iamdilipkumar.com.udacitybaking.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created on 16/05/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

@ContentProvider(authority = BakingProvider.AUTHORITY, database = BakingDatabase.class)
public class BakingProvider {

    public static final String AUTHORITY = "iamdilipkumar.com.udacitybaking.data.provider";

    @TableEndpoint(table = BakingDatabase.BAKING_TABLE)
    public static class BakingTable {

        @ContentUri(path = BakingDatabase.BAKING_TABLE,
                type = "vnd.android.cursor/baking_table",
                defaultSort = BakingColumns._ID + " DESC")
        public static final Uri CONTENT_URI
                = Uri.parse("content://" + AUTHORITY + "/baking_table");

        @InexactContentUri(
                path = "baking_table/#",
                name = "LIST_ID",
                type = "vnd.android.cursor.item/baking_table",
                whereColumn = BakingColumns._ID,
                pathSegment = 1
        )
        public static Uri withId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/baking_table/" + id);
        }
    }
}
