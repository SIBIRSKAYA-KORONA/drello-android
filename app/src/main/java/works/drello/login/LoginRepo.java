package works.drello.login;

import java.util.Base64;

import android.util.Log;
import android.content.Context;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.github.cliftonlabs.json_simple.JsonObject;

import works.drello.common.ApplicationModified;
import works.drello.network.ApiRepo;

@SuppressWarnings("WeakerAccess")
public class LoginRepo {

    enum LoginProgress {
        NONE,
        IN_PROGRESS,
        SUCCESS,
        RESPONSE_ERROR,
        INTERNAL_ERROR
    }

    private final MutableLiveData<LoginProgress> mProgress = new MutableLiveData<>(LoginProgress.NONE);
    private final ApiRepo mApiRepo;

    public LoginRepo(ApiRepo apiRepo) {
        mApiRepo = apiRepo;
    }

    @NonNull
    public static LoginRepo getInstance(Context context) {
        return ApplicationModified.from(context).getLoginRepo();
    }

    public LiveData<LoginProgress> login(@NonNull String login, @NonNull String password) {
        if (mProgress.getValue() == LoginProgress.IN_PROGRESS) {
            return mProgress;
        }
        mProgress.postValue(LoginProgress.IN_PROGRESS);

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
                            mProgress.postValue(LoginProgress.SUCCESS);

                            String cookieHeader = response.headers().get("Set-Cookie");
                            Log.d("Set-Cookie header", cookieHeader);
                            return;
                        }
                        Log.w("login onResponse", response.toString());

                        if (response.code() == 404 || response.code() == 412) {
                            // not found login or invalid password
                            mProgress.postValue(LoginProgress.RESPONSE_ERROR);
                        } else {
                            mProgress.postValue(LoginProgress.INTERNAL_ERROR);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.w("login onFailure", t.getMessage());
                        mProgress.postValue(LoginProgress.INTERNAL_ERROR);
                    }
                });

        return mProgress;
    }

    public LiveData<LoginProgress> logout(@NonNull String login, @NonNull String password) {
        if (mProgress.getValue() == LoginProgress.IN_PROGRESS) {
            return mProgress;
        }
        mProgress.postValue(LoginProgress.IN_PROGRESS);

        mApiRepo.getSessionApi()
                .delete()
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call,
                                           @NotNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            mProgress.postValue(LoginProgress.SUCCESS);
                            return;
                        }
                        Log.w("logout onResponse", response.toString());
                        mProgress.postValue(LoginProgress.INTERNAL_ERROR);
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.w("logout onFailure", t.getMessage());
                        mProgress.postValue(LoginProgress.INTERNAL_ERROR);
                    }
                });

        return mProgress;
    }

    public void initCsrfToken() {
        mApiRepo.getTokenApi().get().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call,
                                   @NotNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Log.w("login onFailure", t.getMessage());
            }
        });
    }
}
