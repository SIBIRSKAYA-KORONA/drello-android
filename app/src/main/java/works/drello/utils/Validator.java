package works.drello.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import works.drello.R;

public class Validator {
    private final Context mCtx;

    public Validator(@NonNull Context ctx) {
        mCtx = ctx;
    }


    private String getString(@StringRes int id) {
        return mCtx.getString(id);
    }

    @Nullable
    public String validateFirstName(String firstName) {
        if (firstName.length() < 1) {
            return getString(R.string.register_page_error_first_name_too_short);
        }

        String regex = "^[а-яёА-ЯЁa-zA-z]+$";
        if (!firstName.matches(regex)) {
            return getString(R.string.register_page_error_first_name_bad_symbols);
        }

        return null;
    }

    @Nullable
    public String validateSecondName(String secondName) {
        if (secondName.length() < 1) {
            return getString(R.string.register_page_error_second_name_too_short);
        }

        String regex = "^[а-яёА-ЯЁa-zA-z]+$";
        if (!secondName.matches(regex)) {
            return getString(R.string.register_page_error_second_name_bad_symbols);
        }

        return null;
    }

    @Nullable
    public String validateNickname(String nickname) {
        if (nickname.length() < 4) {
            return getString(R.string.register_page_error_nickname_too_short);
        }

        String regex = "^[a-zA-Z0-9_.]+$";
        if (!nickname.matches(regex)) {
            return getString(R.string.register_page_error_nickname_bad_symbols);
        }

        return null;
    }

    @Nullable
    public String validatePassword(String password) {
        if (password.length() < 6) {
            return getString(R.string.register_page_error_password_too_short);
        }

        String regex = "^[a-zA-Z0-9]+$";
        if (!password.matches(regex)) {
            return getString(R.string.register_page_error_password_bad_symbols);
        }

        return null;
    }

    @Nullable
    public String validatePasswords(String password, String passwordRetry) {
        if (!password.equals(passwordRetry)) {
            return getString(R.string.register_page_error_password_mismatch);
        }

        return null;
    }

}
