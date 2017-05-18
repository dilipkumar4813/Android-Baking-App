package iamdilipkumar.com.udacitybaking.utils.networking;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import iamdilipkumar.com.udacitybaking.BuildConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 18/05/17.
 *
 * @author dilipkumar4813
 * @version 1.0
 */

public class NetworkUtils {

    /**
     * Method to create a new Retrofit instance
     * Add the API key {@link BakingApiInterceptor#intercept(Interceptor.Chain)}
     * Show debug information only in DEBUG build
     *
     * @return Retrofit - used for building API calls
     */
    public static Retrofit buildRetrofit() {
        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(new BakingApiInterceptor());

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(loggingInterceptor);
        }

        return new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                .client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
