package works.drello.registration;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import works.drello.R;
import works.drello.common.BaseFragment;
import works.drello.common.Validator;


public class RegisterFragment extends BaseFragment {

    private Validator mValidator;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.registration_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mValidator = new Validator(getActivity());

        setNavigationUnlocked(false);
        setHeaderTitle(R.string.register_page_title);

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

            if (!isValid) {
                return;
            }

            firstNameInput.setEnabled(false);
            secondNameInput.setEnabled(false);
            nicknameInput.setEnabled(false);
            passwordInput.setEnabled(false);
            passwordRetryInput.setEnabled(false);
            register_button.setEnabled(false);

            // TODO(Timofey): call to async task here
        });
    }

    // TODO(Timofey): pass task result here
    void handleRegisterResult() {
        View view = getActivity().findViewById(R.id.register_page_fragment);
        if (view != null) {
            Log.d("RegisterFragment", "Can't handle result, because view not found");
            return;
        }

        final TextInputLayout firstNameInput = view.findViewById(R.id.register_page_first_name);
        final TextInputLayout secondNameInput = view.findViewById(R.id.register_page_second_name);
        final TextInputLayout nicknameInput = view.findViewById(R.id.register_page_nickname);
        final TextInputLayout passwordInput = view.findViewById(R.id.register_page_password);
        final TextInputLayout passwordRetryInput = view.findViewById(R.id.register_page_password_retry);
        final MaterialButton register_button = view.findViewById(R.id.register_page_register_button);

        firstNameInput.setEnabled(true);
        secondNameInput.setEnabled(true);
        nicknameInput.setEnabled(true);
        passwordInput.setEnabled(true);
        passwordRetryInput.setEnabled(true);
        register_button.setEnabled(true);

//
//        if (same nickname was found) {
//            nicknameInput.setError(getString(R.string.error_nickname_collision));
//        }
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
