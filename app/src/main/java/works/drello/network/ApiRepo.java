package works.drello.network;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRepo {
    private final SessionApi mSessionApi;
    private final SettingsApi mSettingsApi;

    public ApiRepo(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(context));

        OkHttpClient clt = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cookieJar(cookieJar)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://drello.works/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(clt)
                .build();

        mSessionApi = retrofit.create(SessionApi.class);
        mSettingsApi = retrofit.create(SettingsApi.class);

    }

    public SessionApi getSessionApi() {
        return mSessionApi;
    }
    public SettingsApi getSettingsApi() {
        return mSettingsApi;
    }
}
