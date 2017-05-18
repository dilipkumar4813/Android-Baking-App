package iamdilipkumar.com.udacitybaking.utils.networking;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created on 17/05/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public interface BakingApiInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Observable<String> getRecipes();
}
