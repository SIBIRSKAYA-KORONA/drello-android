package works.drello.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import works.drello.R;
import works.drello.common.BaseFragment;
import works.drello.common.Validator;


public class RegisterFragment extends BaseFragment {

    private Validator mValidator;

    private RegisterViewModel mRegisterViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.registration_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mValidator = new Validator(getActivity());
        mRegisterViewModel = new ViewModelProvider(getActivity()).get(RegisterViewModel.class);

        setNavigationUnlocked(false);
        setHeaderTitle(R.string.register_page_title);

        super.onViewCreated(view, savedInstanceState);
        final TextInputLayout firstNameInput = view.findViewById(R.id.register_page_first_name);
        final TextInputLayout secondNameInput = view.findViewById(R.id.register_page_second_name);
        final TextInputLayout nicknameInput = view.findViewById(R.id.register_page_nickname);
        final TextInputLayout passwordInput = view.findViewById(R.id.register_page_password);
        final TextInputLayout passwordRetryInput = view.findViewById(R.id.register_page_password_retry);
        final MaterialButton registerButton = view.findViewById(R.id.register_page_register_button);

        mRegisterViewModel.getProgress()
                .observe(getViewLifecycleOwner(),
                        new RegisterFragment.RegisterObserver(registerButton, nicknameInput));

        registerButton.setOnClickListener(v -> {

            boolean isValid = validateFirstName(firstNameInput);
            isValid &= validateSecondName(secondNameInput);
            isValid &= validateNickname(nicknameInput);
            isValid &= validatePassword(passwordInput);
            isValid &= validatePasswords(passwordInput, passwordRetryInput);

            if (!isValid) {
                return;
            }

            registerButton.setEnabled(false);

            mRegisterViewModel.register(
                    Objects.requireNonNull(firstNameInput.getEditText()).getText().toString(),
                    Objects.requireNonNull(secondNameInput.getEditText()).getText().toString(),
                    Objects.requireNonNull(nicknameInput.getEditText()).getText().toString(),
                    Objects.requireNonNull(passwordInput.getEditText()).getText().toString());
        });
    }


    boolean validateFirstName(TextInputLayout nameInput) {
        String error = mValidator.validateFirstName(Objects.requireNonNull(nameInput.getEditText()).getText().toString());
        nameInput.setError(error);
        return error == null;
    }

    boolean validateSecondName(TextInputLayout nameInput) {
        String error = mValidator.validateSecondName(Objects.requireNonNull(nameInput.getEditText()).getText().toString());
        nameInput.setError(error);
        return error == null;
    }

    boolean validateNickname(TextInputLayout nicknameInput) {
        String error = mValidator.validateNickname(Objects.requireNonNull(nicknameInput.getEditText()).getText().toString());
        nicknameInput.setError(error);
        return error == null;
    }

    boolean validatePassword(TextInputLayout password) {
        String error = mValidator.validatePassword(Objects.requireNonNull(password.getEditText()).getText().toString());
        password.setError(error);
        return error == null;
    }

    boolean validatePasswords(TextInputLayout passwordInput, TextInputLayout passwordRetryInput) {
        String error = mValidator.validatePasswords(
                Objects.requireNonNull(passwordInput.getEditText()).getText().toString(),
                Objects.requireNonNull(passwordRetryInput.getEditText()).getText().toString()
        );
        passwordRetryInput.setError(error);
        return error == null;
    }

    private class RegisterObserver implements Observer<RegisterViewModel.RegisterState> {
        private final Button btn;
        private final TextInputLayout nicknameInput;

        public RegisterObserver(Button btn, TextInputLayout nicknameInput) {
            this.btn = btn;
            this.nicknameInput = nicknameInput;
        }

        @Override
        public void onChanged(RegisterViewModel.RegisterState state) {
            switch (state) {
                case IN_PROGRESS:
                    btn.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    break;
                case SUCCESS:
                    btn.setEnabled(true);
                    btn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    break;
                case EXIST_LOGIN_ERROR:
                    btn.setEnabled(true);
                    nicknameInput.setError(getString(R.string.error_nickname_collision));
                case INTERNAL_ERROR:
                    btn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    break;
                default:
            }
        }

    }
}
