package works.drello.login;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

@SuppressWarnings("WeakerAccess")
public class LoginViewModel extends AndroidViewModel {

    enum LoginState {
        NONE,
        ERROR,
        IN_PROGRESS,
        SUCCESS,
        FAILED
    }

    private final MediatorLiveData<LoginState> mLoginState = new MediatorLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mLoginState.setValue(LoginState.NONE);
    }

    public LiveData<LoginState> getProgress() {
        return mLoginState;
    }

    public void login(String login, String password) {
        if (!isValid(login, password)) {
            mLoginState.postValue(LoginState.ERROR);
        } else if (mLoginState.getValue() != LoginState.IN_PROGRESS) {
            requestLogin(login, password);
        }
    }

    private void requestLogin(String login, String password) {
        mLoginState.postValue(LoginState.IN_PROGRESS);

        final LiveData<LoginRepo.LoginProgress> progressLiveData = LoginRepo.getInstance(getApplication())
                .login(login, password);

        mLoginState.addSource(progressLiveData, loginProgress -> {
            if (loginProgress == LoginRepo.LoginProgress.SUCCESS) {
                mLoginState.postValue(LoginState.SUCCESS);
                mLoginState.removeSource(progressLiveData);
            } else if (loginProgress == LoginRepo.LoginProgress.FAILED) {
                mLoginState.postValue(LoginState.FAILED);
                mLoginState.removeSource(progressLiveData);
            }
        });
    }

    private boolean isValid(String login, String password) {
        return !TextUtils.isEmpty(login) && !TextUtils.isEmpty(password);
    }
}
