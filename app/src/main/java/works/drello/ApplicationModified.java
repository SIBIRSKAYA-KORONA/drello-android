package works.drello;

import android.app.Application;
import android.content.Context;

import works.drello.network.ApiRepo;

public class ApplicationModified extends Application {

    private AuthRepo mAuthRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        ApiRepo ApiRepo = new ApiRepo();
        mAuthRepo = new AuthRepo(ApiRepo);
    }

    public AuthRepo getAuthRepo() {
        return mAuthRepo;
    }

    public static ApplicationModified from(Context context) {
        return (ApplicationModified) context.getApplicationContext();
    }
}

