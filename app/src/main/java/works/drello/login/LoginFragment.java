package works.drello.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import works.drello.R;

public class LoginFragment extends Fragment {

    private LoginViewModel mLoginViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);

        final EditText login = view.findViewById(R.id.login);
        final EditText password = view.findViewById(R.id.password);
        final Button loginBtn = view.findViewById(R.id.login_btn);

        mLoginViewModel.getProgress()
                .observe(getViewLifecycleOwner(), new LoginObserver(loginBtn));

        loginBtn.setOnClickListener(v -> {

            // TODO: валидатор, норм верстка
            loginBtn.setEnabled(false);

            mLoginViewModel.login(login.getText().toString(), password.getText().toString());
        });
    }

    private class LoginObserver implements Observer<LoginViewModel.LoginState> {
        private final Button btn;

        public LoginObserver(Button loginBtn) {
            this.btn = loginBtn;
        }

        @Override
        public void onChanged(LoginViewModel.LoginState state) {
            switch (state) {
                case IN_PROGRESS:
                    btn.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    break;
                case SUCCESS:
                    btn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    btn.setEnabled(true);
                    break;
                case RESPONSE_ERROR:
                case INTERNAL_ERROR:
                    btn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    btn.setEnabled(true);
                    break;
                default:
            }
        }
    }
}
