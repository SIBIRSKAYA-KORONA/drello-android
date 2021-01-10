package works.drello.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import works.drello.R;
import works.drello.login.LoginFragment;
import works.drello.profile.ProfileFragment;
import works.drello.registration.RegisterFragment;

public class Router {
    private static final String LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG";
    private static final String REGISTRATION_FRAGMENT_TAG = "REGISTRATION_FRAGMENT_TAG";
    private static final String PROFILE_SETTINGS_PAGE = "PROFILE_SETTINGS_PAGE";

    private final AppCompatActivity mActivity;

    /****************************************************
     * PUBLIC
     ****************************************************
     */

    public Router(@NonNull AppCompatActivity activity) {
        mActivity = activity;
    }

    public boolean isInitialized() {
        return !mActivity.getSupportFragmentManager().getFragments().isEmpty();
    }

    public void openLogin() {
        clearBackStack();
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment(), LOGIN_FRAGMENT_TAG)
                .commitAllowingStateLoss();
    }

    public void openRegister() {
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RegisterFragment(), REGISTRATION_FRAGMENT_TAG)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void openProfileSettings() {
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment(), PROFILE_SETTINGS_PAGE)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    /****************************************************
     * PRIVATE
     ****************************************************
     */


    private void clearBackStack() {
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }
}
