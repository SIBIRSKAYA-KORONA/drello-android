package works.drello.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

@SuppressWarnings("WeakerAccess")
public class LoginViewModel extends AndroidViewModel {

    enum LoginState {
        NONE,
        IN_PROGRESS,
        SUCCESS,
        RESPONSE_ERROR,
        INTERNAL_ERROR
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
        if (mLoginState.getValue() != LoginState.IN_PROGRESS) {
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
            } else if (loginProgress == LoginRepo.LoginProgress.RESPONSE_ERROR) {
                mLoginState.postValue(LoginState.RESPONSE_ERROR);
                mLoginState.removeSource(progressLiveData);
            } else if (loginProgress == LoginRepo.LoginProgress.INTERNAL_ERROR) {
                mLoginState.postValue(LoginState.INTERNAL_ERROR);
                mLoginState.removeSource(progressLiveData);
            }
        });
    }
}
