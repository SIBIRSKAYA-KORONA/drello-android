package works.drello;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import works.drello.common.Router;

public class MainActivity extends AppCompatActivity {
    private Router mRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRouter = new Router(this);

        setContentView(R.layout.activity_main);

        if (!mRouter.isInitialized()) {
            mRouter.openLogin();
        }

    }


    public Router getRouter() {
        return mRouter;
    }
}
