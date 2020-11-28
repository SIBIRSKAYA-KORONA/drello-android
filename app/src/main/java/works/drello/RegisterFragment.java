package works.drello;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import works.drello.utils.Validator;


public class RegisterFragment extends Fragment {

    private Validator mValidator;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registration_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mValidator = new Validator(getActivity());

        super.onViewCreated(view, savedInstanceState);
        final TextInputLayout firstNameInput = view.findViewById(R.id.register_page_first_name);
        final TextInputLayout secondNameInput = view.findViewById(R.id.register_page_second_name);
        final TextInputLayout nicknameInput = view.findViewById(R.id.register_page_nickname);
        final TextInputLayout passwordInput = view.findViewById(R.id.register_page_password);
        final TextInputLayout passwordRetryInput = view.findViewById(R.id.register_page_password_retry);
        final MaterialButton register_button = view.findViewById(R.id.register_page_register_button);


        register_button.setOnClickListener(v -> {
            boolean isValid = validateFirstName(firstNameInput);
            isValid &= validateSecondName(secondNameInput);
            isValid &= validateNickname(nicknameInput);
            isValid &= validatePassword(passwordInput);
            isValid &= validatePasswords(passwordInput, passwordRetryInput);

            if (isValid) {
                // TODO: make api request
            }

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
}
