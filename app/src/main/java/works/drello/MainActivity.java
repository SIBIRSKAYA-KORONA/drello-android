package works.drello;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

        DrawerLayout drawer = findViewById(R.id.navigation_drawer);
        Toolbar header = findViewById(R.id.header);

        // if title is null, app name will be set on initialization
        header.setTitle("");
        setSupportActionBar(header);
        header.setNavigationOnClickListener(v -> drawer.openDrawer(GravityCompat.START, true));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.navigation_drawer);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public Router getRouter() {
        return mRouter;
    }
}
