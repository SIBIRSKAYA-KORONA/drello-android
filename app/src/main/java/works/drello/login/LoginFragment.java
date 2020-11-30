package works.drello.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import works.drello.MainActivity;
import works.drello.R;
import works.drello.common.BaseFragment;


public class LoginFragment extends BaseFragment {

    private LoginViewModel mLoginViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: fix initial title, fix close on press back

        setNavigationUnlocked(false);
        setHeaderTitle(R.string.login_page_title);

        final EditText login = view.findViewById(R.id.login);
        final EditText password = view.findViewById(R.id.password);
        final Button loginBtn = view.findViewById(R.id.login_btn);
        final Button registrationBtn = view.findViewById(R.id.to_join_btn);

        mLoginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        mLoginViewModel.getProgress()
                .observe(getViewLifecycleOwner(), new LoginObserver(loginBtn));

        loginBtn.setOnClickListener(v -> mLoginViewModel.login(login.getText().toString(), password.getText().toString()));

        registrationBtn.setOnClickListener(v -> ((MainActivity) getActivity()).getRouter().openRegister());
    }

    private class LoginObserver implements Observer<LoginViewModel.LoginState> {
        private final Button loginBtn;

        public LoginObserver(Button loginBtn) {
            this.loginBtn = loginBtn;
        }

        @Override
        public void onChanged(LoginViewModel.LoginState loginState) {
            switch (loginState) {
                case ERROR:
                    loginBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                    loginBtn.setEnabled(true);
                    break;
                case IN_PROGRESS:
                    loginBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    loginBtn.setEnabled(false);
                    break;
                case SUCCESS:
                    loginBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    break;
                case FAILED:
                    loginBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    loginBtn.setEnabled(true);
                    break;
                default:
            }
        }
    }
}
