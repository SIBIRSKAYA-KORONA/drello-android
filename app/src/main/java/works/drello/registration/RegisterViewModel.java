package works.drello.registration;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;


@SuppressWarnings("WeakerAccess")
public class RegisterViewModel extends AndroidViewModel {

    enum RegisterState {
        NONE,
        IN_PROGRESS,
        SUCCESS,
        EXIST_LOGIN_ERROR,
        INTERNAL_ERROR
    }

    private final MediatorLiveData<RegisterViewModel.RegisterState> mRegisterState = new MediatorLiveData<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        mRegisterState.setValue(RegisterViewModel.RegisterState.NONE);
    }

    public LiveData<RegisterViewModel.RegisterState> getProgress() {
        return mRegisterState;
    }

    public void register(String firstName, String secondName,
                         String nickName, String password) {
        if (mRegisterState.getValue() != RegisterViewModel.RegisterState.IN_PROGRESS) {
            requestRegister(firstName, secondName, nickName, password);
        }
    }

    private void requestRegister(String firstName, String secondName, String nickName, String password) {
        mRegisterState.postValue(RegisterViewModel.RegisterState.IN_PROGRESS);

        final LiveData<RegisterRepo.RegisterProgress> progressLiveData = RegisterRepo.getInstance(getApplication())
                .register(firstName, secondName, nickName, password);

        mRegisterState.addSource(progressLiveData, progress -> {
            if (progress == RegisterRepo.RegisterProgress.SUCCESS) {
                mRegisterState.postValue(RegisterState.SUCCESS);
                mRegisterState.removeSource(progressLiveData);
            } else if (progress == RegisterRepo.RegisterProgress.EXIST_LOGIN_ERROR) {
                mRegisterState.postValue(RegisterState.EXIST_LOGIN_ERROR);
                mRegisterState.removeSource(progressLiveData);
            } else if (progress == RegisterRepo.RegisterProgress.INTERNAL_ERROR) {
                mRegisterState.postValue(RegisterState.INTERNAL_ERROR);
                mRegisterState.removeSource(progressLiveData);
            }
        });
    }

}
