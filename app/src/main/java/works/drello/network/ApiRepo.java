package works.drello.network;

import android.util.Log;
import java.util.Arrays;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.ConnectionSpec;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRepo {
    private final SessionApi mSessionApi;

    public ApiRepo() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

      /*
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .header("User-Agent", "Your-App-Name")
                    .header("Accept", "application/vnd.yourapi.v1.full+json")
                    .method(original.method(), original.body())
                    .build();

            Response resp = chain.proceed(request);
            Log.w("!!!!!!!!!!!!!!!!! req: ", request.toString());
            Log.w("!!!!!!!!!!!!!!!! resp: ", resp.toString());

            return resp;
        }); */

        OkHttpClient clt = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://drello.works/api/")
                .addConverterFactory(MoshiConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(clt)
                .build();

        mSessionApi = retrofit.create(SessionApi.class);
    }

    public SessionApi getSessionApi() {
        return mSessionApi;
    }
}
