package works.drello.profile;

import works.drello.network.SettingsApi.User;

public class ProfileData {

    enum Progress {
        NONE,
        IN_PROGRESS,
        SUCCESS,
        INVALID_SESSION_ERROR,
        EXIST_LOGIN_ERROR,
        WRONG_PASSWORD_ERROR,
        INTERNAL_ERROR
    }

    private Progress mProgress;
    private User mUserData = null;

    public ProfileData(Progress progress) {
        this.mProgress = progress;
    }

    public Progress getProgress() {
        return mProgress;
    }

    public User getUserData() {
        return mUserData;
    }

    public void setProgress(Progress progress) {
        this.mProgress = progress;
    }

    public void setUserData(User userData) {
        this.mUserData = userData;
    }
}
