package works.drello.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import works.drello.R;
import works.drello.login.LoginFragment;
import works.drello.registration.RegisterFragment;

public class Router {
    private static final String LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG";
    private static final String REGISTRATION_FRAGMENT_TAG = "REGISTRATION_FRAGMENT_TAG";

    private final AppCompatActivity mActivity;


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


    private void clearBackStack() {
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }
}
