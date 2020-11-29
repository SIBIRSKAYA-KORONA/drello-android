package works.drello;

import android.util.Log;
import android.content.Context;
import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.cliftonlabs.json_simple.JsonObject;

import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import works.drello.network.ApiRepo;

@SuppressWarnings("WeakerAccess")
public class AuthRepo {

    private MutableLiveData<AuthProgress> mAuthProgress = new MutableLiveData<>(AuthProgress.NONE);
    private final ApiRepo mApiRepo;

    public AuthRepo(ApiRepo apiRepo) {
        mApiRepo = apiRepo;
    }

    @NonNull
    public static AuthRepo getInstance(Context context) {
        return ApplicationModified.from(context).getAuthRepo();
    }

    public LiveData<AuthProgress> login(@NonNull String login, @NonNull String password) {
        if (mAuthProgress.getValue() == AuthProgress.IN_PROGRESS) {
            return mAuthProgress;
        }
        mAuthProgress = new MutableLiveData<>(AuthProgress.IN_PROGRESS);

        JsonObject json = new JsonObject();
        json.put("nickname", login);
        String pass = Base64.getEncoder().encodeToString(password.getBytes());
        json.put("password", pass);

        mApiRepo.getSessionApi()
                .create(json)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call,
                                           @NotNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            mAuthProgress.postValue(AuthProgress.SUCCESS);

                            String cookieHeader = response.headers().get("Set-Cookie");
                            Log.i("Set-Cookie header", cookieHeader);
                            Log.i("session_id", cookieHeader.split(";")[0].split("=")[1]);
                            return;
                        }
                        Log.w("login onResponse", response.toString());
                        mAuthProgress.postValue(AuthProgress.FAILED);
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.w("login onFailure", t.getMessage());
                        mAuthProgress.postValue(AuthProgress.FAILED);
                    }
                });

        return mAuthProgress;
    }

    public LiveData<AuthProgress> logout(@NonNull String login, @NonNull String password) {
        if (mAuthProgress.getValue() == AuthProgress.IN_PROGRESS) {
            return mAuthProgress;
        }
        mAuthProgress = new MutableLiveData<>(AuthProgress.IN_PROGRESS);

        mApiRepo.getSessionApi()
                .delete()
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call,
                                           @NotNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            mAuthProgress.postValue(AuthProgress.SUCCESS);
                            return;
                        }
                        Log.w("logout onResponse", response.toString());
                        mAuthProgress.postValue(AuthProgress.FAILED);
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.w("logout onFailure", t.getMessage());
                        mAuthProgress.postValue(AuthProgress.FAILED);
                    }
                });

        return mAuthProgress;
    }

    enum AuthProgress {
        NONE,
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }
}
