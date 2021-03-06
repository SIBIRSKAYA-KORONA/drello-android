package works.drello.common;

import android.app.Application;
import android.content.Context;

import works.drello.login.LoginRepo;
import works.drello.registration.RegisterRepo;
import works.drello.profile.ProfileRepo;
import works.drello.network.ApiRepo;

public class ApplicationModified extends Application {

    private LoginRepo mLoginRepo;
    private RegisterRepo mRegisterRepo;
    private ProfileRepo mProfileRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        ApiRepo ApiRepo = new ApiRepo(this.getApplicationContext());
        mLoginRepo = new LoginRepo(ApiRepo);
        mRegisterRepo = new RegisterRepo(ApiRepo);
        mProfileRepo = new ProfileRepo(ApiRepo);
    }

    public LoginRepo getLoginRepo() {
        return mLoginRepo;
    }

    public RegisterRepo getRegisterRepo() {
        return mRegisterRepo;
    }

    public ProfileRepo getProfileRepo() {
        return mProfileRepo;
    }

    public static ApplicationModified from(Context context) {
        return (ApplicationModified) context.getApplicationContext();
    }
}

