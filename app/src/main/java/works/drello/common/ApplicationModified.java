package works.drello.common;

import android.app.Application;
import android.content.Context;

import works.drello.login.LoginRepo;
import works.drello.network.ApiRepo;

public class ApplicationModified extends Application {

    private LoginRepo mAuthRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        ApiRepo ApiRepo = new ApiRepo(this.getApplicationContext());
        mAuthRepo = new LoginRepo(ApiRepo);
    }

    public LoginRepo getAuthRepo() {
        return mAuthRepo;
    }

    public static ApplicationModified from(Context context) {
        return (ApplicationModified) context.getApplicationContext();
    }
}

