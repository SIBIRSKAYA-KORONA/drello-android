package works.drello.profile;

import android.util.Log;
import android.content.Context;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import works.drello.common.ApplicationModified;
import works.drello.network.ApiRepo;
import works.drello.network.SettingsApi;

@SuppressWarnings("WeakerAccess")
public class ProfileRepo {

    private final MutableLiveData<ProfileData> mProfileData = new MutableLiveData<>(new ProfileData(ProfileData.Progress.NONE));
    private final ApiRepo mApiRepo;

    public ProfileRepo(ApiRepo apiRepo) {
        mApiRepo = apiRepo;
    }

    @NonNull
    public static ProfileRepo getInstance(Context context) {
        return ApplicationModified.from(context).getProfileRepo();
    }

    public LiveData<ProfileData> get() {
        ProfileData profileData = mProfileData.getValue();
        if (profileData.getProgress() == ProfileData.Progress.IN_PROGRESS) {
            return mProfileData;
        }
        profileData.setProgress(ProfileData.Progress.IN_PROGRESS);
        profileData.setUserData(null);
        mProfileData.setValue(profileData);

        mApiRepo.getSettingsApi()
                .get()
                .enqueue(new Callback<SettingsApi.User>() {
                    @Override
                    public void onResponse(@NotNull Call<SettingsApi.User> call,
                                           @NotNull Response<SettingsApi.User> response) {
                        if (response.isSuccessful()) {
                            SettingsApi.User user = response.body();
                            profileData.setProgress(ProfileData.Progress.SUCCESS);
                            profileData.setUserData(user);
                        } else {
                            Log.w("profile onResponse", response.toString());
                            if (response.code() == 401) {
                                profileData.setProgress(ProfileData.Progress.INVALID_SESSION_ERROR);

                            } else {
                                profileData.setProgress(ProfileData.Progress.INTERNAL_ERROR);
                            }
                        }
                        mProfileData.postValue(profileData);
                    }

                    @Override
                    public void onFailure(@NotNull Call<SettingsApi.User> call, @NotNull Throwable t) {
                        Log.w("profile onFailure", t.getMessage());
                        profileData.setProgress(ProfileData.Progress.INTERNAL_ERROR);
                        mProfileData.postValue(profileData);
                    }
                });

        return mProfileData;
    }

    public LiveData<ProfileData> update(SettingsApi.User user) {
        ProfileData profileData = mProfileData.getValue();
        if (profileData.getProgress() == ProfileData.Progress.IN_PROGRESS) {
            return mProfileData;
        }
        profileData.setProgress(ProfileData.Progress.IN_PROGRESS);
        profileData.setUserData(null);
        mProfileData.setValue(profileData);

        // TODO: заправить все поля, если это работает
        LinkedHashMap<String, RequestBody> body = new LinkedHashMap<>();
        // body.put("newName", RequestBody.create(MediaType.parse("text/plain"), user.name));
        body.put("newName", RequestBody.create(user.name.getBytes()));

        mApiRepo.getSettingsApi()
                .update(body)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call,
                                           @NotNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            profileData.setProgress(ProfileData.Progress.SUCCESS);
                        } else {
                            Log.w("profile onResponse", response.toString());
                            if (response.code() == 401) {
                                profileData.setProgress(ProfileData.Progress.INVALID_SESSION_ERROR);
                            } else if (response.code() == 409) {
                                profileData.setProgress(ProfileData.Progress.EXIST_LOGIN_ERROR);
                            } else if (response.code() == 412) {
                                profileData.setProgress(ProfileData.Progress.WRONG_PASSWORD_ERROR);
                            } else {
                                profileData.setProgress(ProfileData.Progress.INTERNAL_ERROR);
                            }
                        }
                        mProfileData.postValue(profileData);
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.w("profile onFailure", t.getMessage());
                        profileData.setProgress(ProfileData.Progress.INTERNAL_ERROR);
                        mProfileData.postValue(profileData);
                    }
                });

        return mProfileData;
    }

}
