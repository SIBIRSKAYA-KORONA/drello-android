package works.drello.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRepo {
    private final SessionApi mSessionApi;

    public ApiRepo() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient clt = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://drello.works/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(clt)
                .build();

        mSessionApi = retrofit.create(SessionApi.class);
    }

    public SessionApi getSessionApi() {
        return mSessionApi;
    }
}
