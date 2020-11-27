package works.drello;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.cliftonlabs.json_simple.JsonObject;

import java.util.List;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import works.drello.network.ApiRepo;
import works.drello.network.SessionApi;

import android.util.Log;


@SuppressWarnings("WeakerAccess")
public class AuthRepo {

    private final ApiRepo mApiRepo;

    public AuthRepo() {
        mApiRepo = new ApiRepo();
    }

    public AuthRepo(ApiRepo apiRepo) {
        mApiRepo = apiRepo;
    }

    private String mCurrentUser;
    private MutableLiveData<AuthProgress> mAuthProgress;

    public LiveData<AuthProgress> login(@NonNull String login, @NonNull String password) {
        if (TextUtils.equals(login, mCurrentUser) && mAuthProgress.getValue() == AuthProgress.IN_PROGRESS) {
            return mAuthProgress;
        } else if (!TextUtils.equals(login, mCurrentUser) && mAuthProgress != null) {
            mAuthProgress.postValue(AuthProgress.FAILED);
        }
        mCurrentUser = login;
        mAuthProgress = new MutableLiveData<>(AuthProgress.IN_PROGRESS);

        JsonObject json = new JsonObject();
        json.put("nickname", login);
        String pass = new String(Base64.getDecoder().decode(password));
        json.put("password", "pass");

        mApiRepo.getSessionApi()
                .create(json.toJson())
                .enqueue(new Callback<List<SessionApi.UserPlain>>() {
            @Override
            public void onResponse(Call<List<SessionApi.UserPlain>> call,
                                   Response<List<SessionApi.UserPlain>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    mAuthProgress.postValue(AuthProgress.SUCCESS);
                    /*List<SessionApi.UserPlain> users = response.body();
                    if (hasUserCredentials(users, login, password)) {
                        mAuthProgress.postValue(AuthProgress.SUCCESS);
                        return;
                    }*/
                }
                mAuthProgress.postValue(AuthProgress.FAILED);
            }

            @Override
            public void onFailure(Call<List<SessionApi.UserPlain>> call, Throwable t) {
                mAuthProgress.postValue(AuthProgress.FAILED);
            }
        });

        return mAuthProgress;
    }

    private static boolean hasUserCredentials(List<SessionApi.UserPlain> users, String login, String pass) {
        for (SessionApi.UserPlain user : users) {
            if (TextUtils.equals(user.name, login) && TextUtils.equals(user.password, pass)) {
                return true;
            }
        }
        return false;
    }

    enum AuthProgress {
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }
}
