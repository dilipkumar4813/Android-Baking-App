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

public interface IngredientsColumns {

    @DataType(DataType.Type.INTEGER)
    @AutoIncrement
    @PrimaryKey
    String _ID = "id";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String RECIPE_ID = "recipe_id";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String QUANTITY = "quantity";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String MEASURE = "measure";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String INGREDIENT = "ingredient";

}
