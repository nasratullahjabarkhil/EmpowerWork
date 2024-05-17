package com.example.proyecto_final.Models;

import android.text.TextUtils;
import android.util.Patterns;

public class PasswordEmailValidator {

    public static boolean isPasswordValid(String password) {
        // Requerimientos de la contraseña
        int minLength = 8;
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        // Verifica la longitud mínima
        if (password.length() < minLength) {
            return false;
        }

        // Verifica otros requisitos
        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(ch)) {
                hasSpecialChar = true;
            }
        }

        // Verifica si se cumplen todos los criterios
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    public static boolean isEmailValid(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
