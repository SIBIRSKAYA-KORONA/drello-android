package works.drello.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import works.drello.network.SettingsApi.User;

@SuppressWarnings("WeakerAccess")
public class ProfileViewModel extends AndroidViewModel {

    private final MediatorLiveData<ProfileData> mState = new MediatorLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        mState.setValue(new ProfileData(ProfileData.Progress.NONE));
    }

    public LiveData<ProfileData> getProgress() {
        return mState;
    }

    public void loadUserData() {
        if (mState.getValue().getProgress() == ProfileData.Progress.IN_PROGRESS) {
            return;
        }

        ProfileData profileData = mState.getValue();
        profileData.setProgress(ProfileData.Progress.IN_PROGRESS);
        mState.postValue(profileData);

        final LiveData<ProfileData> progressLiveData = ProfileRepo.getInstance(getApplication()).get();

        mState.addSource(progressLiveData, data -> {
            if (data.getProgress() == ProfileData.Progress.IN_PROGRESS) {
                return;
            }
            mState.postValue(data);
            mState.removeSource(progressLiveData);
        });
    }

    public void updateUserData(User user) {
        if (mState.getValue().getProgress() == ProfileData.Progress.IN_PROGRESS) {
            return;
        }

        ProfileData profileData = mState.getValue();
        profileData.setProgress(ProfileData.Progress.IN_PROGRESS);
        mState.postValue(profileData);

        final LiveData<ProfileData> progressLiveData = ProfileRepo.getInstance(getApplication()).update(user);

        mState.addSource(progressLiveData, data -> {
            if (data.getProgress() == ProfileData.Progress.IN_PROGRESS) {
                return;
            }
            mState.postValue(data);
            mState.removeSource(progressLiveData);
        });
    }

}
