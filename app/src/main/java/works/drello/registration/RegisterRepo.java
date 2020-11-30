package works.drello.registration;

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
public class RegisterRepo {

    enum RegisterProgress {
        NONE,
        IN_PROGRESS,
        SUCCESS,
        EXIST_LOGIN_ERROR,
        INTERNAL_ERROR
    }

    private final MutableLiveData<RegisterProgress> mProgress = new MutableLiveData<>(RegisterProgress.NONE);
    private final ApiRepo mApiRepo;

    public RegisterRepo(ApiRepo apiRepo) {
        mApiRepo = apiRepo;
    }

    @NonNull
    public static RegisterRepo getInstance(Context context) {
        return ApplicationModified.from(context).getRegisterRepo();
    }

    public LiveData<RegisterProgress> register(@NonNull String firstName, @NonNull String secondName,
                                               @NonNull String nickName, @NonNull String password) {
        if (mProgress.getValue() == RegisterProgress.IN_PROGRESS) {
            return mProgress;
        }
        mProgress.setValue(RegisterProgress.IN_PROGRESS);

        JsonObject json = new JsonObject();
        json.put("name", firstName);
        json.put("surname", secondName);
        json.put("nickname", nickName);
        String pass = Base64.getEncoder().encodeToString(password.getBytes());
        json.put("password", pass);

        mApiRepo.getSettingsApi()
                .create(json)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call,
                                           @NotNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            mProgress.postValue(RegisterProgress.SUCCESS);

                            String cookieHeader = response.headers().get("Set-Cookie");
                            Log.d("Set-Cookie header", cookieHeader);
                            return;
                        }
                        Log.w("register onResponse", response.toString());
                        if (response.code() == 409) {
                            mProgress.postValue(RegisterProgress.EXIST_LOGIN_ERROR);
                        } else {
                            mProgress.postValue(RegisterProgress.INTERNAL_ERROR);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.w("register onFailure", t.getMessage());
                        mProgress.postValue(RegisterProgress.INTERNAL_ERROR);
                    }
                });

        return mProgress;
    }
}
