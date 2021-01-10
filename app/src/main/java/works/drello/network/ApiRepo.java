package works.drello.network;

import android.content.Context;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRepo {
    private final SessionApi mSessionApi;
    private final SettingsApi mSettingsApi;
    private final TokenApi mTokenApi;

    public ApiRepo(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(context));

        OkHttpClient clt = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new CsrfInterceptor())
                .cookieJar(cookieJar)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://drello.works/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(clt)
                .build();

        mSessionApi = retrofit.create(SessionApi.class);
        mSettingsApi = retrofit.create(SettingsApi.class);
        mTokenApi = retrofit.create(TokenApi.class);
    }

    private static class CsrfInterceptor implements Interceptor {
        private static final String CSRF_TOKEN_HEADER = "x-csrf-token";
        private static String sCsrfToken;

        @NotNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            if (sCsrfToken != null) {
                request = request.newBuilder()
                        .addHeader(CSRF_TOKEN_HEADER, sCsrfToken)
                        .build();
            }

            Response response = chain.proceed(request);

            if (response.header(CSRF_TOKEN_HEADER) != null) {
                sCsrfToken = response.header(CSRF_TOKEN_HEADER);
                Log.d("CsrfInterceptor", "Set new CSRF token: "
                        + sCsrfToken.substring(0, 7));
            }

            return response;
        }
    }

    public SessionApi getSessionApi() {
        return mSessionApi;
    }

    public SettingsApi getSettingsApi() {
        return mSettingsApi;
    }

    public TokenApi getTokenApi() {
        return mTokenApi;
    }
}
