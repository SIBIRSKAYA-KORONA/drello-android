package works.drello.common;

import androidx.annotation.StringRes;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;

import works.drello.R;

public class BaseFragment extends Fragment {

    private MaterialToolbar getHeader() {
        return (MaterialToolbar) getActivity().findViewById(R.id.header);
    }

    private DrawerLayout getDrawer() {
        return (DrawerLayout) getActivity().findViewById(R.id.drawer);
    }

    protected void setNavigationUnlocked(boolean toUnlock) {
        if (toUnlock) {
            getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            getHeader().setNavigationIcon(R.drawable.ic_menu_24dp);

        } else {
            getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getHeader().setNavigationIcon(null);
        }
    }

    protected void setHeaderTitle(@StringRes int stringId) {
        getHeader().setTitle(getString(stringId));
    }

}
