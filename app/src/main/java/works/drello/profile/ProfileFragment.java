package works.drello.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import works.drello.MainActivity;
import works.drello.R;
import works.drello.common.BaseFragment;
import works.drello.common.Validator;
import works.drello.network.SettingsApi;


public class ProfileFragment extends BaseFragment {

    private Validator mValidator;

    private ProfileViewModel mProfileViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.profile_settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mValidator = new Validator(getActivity());
        mProfileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

        setNavigationUnlocked(true);
        setHeaderTitle(R.string.profile_page_title);

        super.onViewCreated(view, savedInstanceState);
        final TextInputLayout firstNameInput = view.findViewById(R.id.first_name);
        final TextInputLayout secondNameInput = view.findViewById(R.id.second_name);
        final TextInputLayout oldPasswordInput = view.findViewById(R.id.old_password);
        final TextInputLayout passwordInput = view.findViewById(R.id.password);
        final TextInputLayout passwordRetryInput = view.findViewById(R.id.password_retry);
        final MaterialButton saveButton = view.findViewById(R.id.save_button);

        mProfileViewModel.getProgress()
                .observe(getViewLifecycleOwner(),
                        new ProfileFragment.ProfileObserver(saveButton, firstNameInput, oldPasswordInput));

        saveButton.setOnClickListener(v -> {

//            boolean isValid = validateFirstName(firstNameInput);
//            isValid &= validateSecondName(secondNameInput);
//
//            if (!oldPasswordInput.getEditText().getText().toString().isEmpty()
//                    || !passwordInput.getEditText().getText().toString().isEmpty()
//                    || !passwordRetryInput.getEditText().getText().toString().isEmpty()) {
//                isValid &= validatePassword(oldPasswordInput);
//                isValid &= validatePassword(passwordInput);
//                isValid &= validatePasswords(passwordInput, passwordRetryInput);
//            }
//
//            if (!isValid) {
//                return;
//            }

            SettingsApi.User userData = new SettingsApi.User();
            userData.name = firstNameInput.getEditText().getText().toString();
//            userData.surname = secondNameInput.getEditText().getText().toString();

            mProfileViewModel.updateUserData(userData);
        });

        mProfileViewModel.loadUserData();
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

    private class ProfileObserver implements Observer<ProfileData> {
        private final Button mSaveButton;
        private final TextInputLayout mFirstNameInput;
        private final TextInputLayout mOldPasswordInput;

        public ProfileObserver(Button saveButton, TextInputLayout firstNameInput, TextInputLayout oldPasswordInput) {
            this.mSaveButton = saveButton;
            this.mFirstNameInput = firstNameInput;
            this.mOldPasswordInput = oldPasswordInput;
        }

        @Override
        public void onChanged(ProfileData result) {
            switch (result.getProgress()) {
                case IN_PROGRESS:
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    break;
                case SUCCESS:
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    mFirstNameInput.getEditText().setText(result.getUserData().name);
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            R.string.profile_settings_page_save_success,
                            Toast.LENGTH_SHORT).show();
                    break;
                case INVALID_SESSION_ERROR:
                    ((MainActivity)getActivity()).getRouter().openLogin();
                    return;
                case WRONG_PASSWORD_ERROR:
                    mSaveButton.setEnabled(true);
                    mOldPasswordInput.setError(getActivity().getString(R.string.error_old_password_wrong));
                    break;
                case INTERNAL_ERROR:
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    break;
                default:
                    mSaveButton.setEnabled(true);
            }
        }

    }
}
