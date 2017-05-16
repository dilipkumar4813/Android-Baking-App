package iamdilipkumar.com.udacitybaking.data;


import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created on 16/05/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public interface BakingColumns {

    @DataType(DataType.Type.INTEGER)
    @AutoIncrement
    @PrimaryKey
    String _ID = "id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String NAME = "name";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String SERVINGS = "servings";

    @DataType(DataType.Type.TEXT)
    String IMAGE = "image";
}
