package br.com.fiap.fiapautenticacao.validator;

import java.util.regex.Pattern;

public class PasswordValidator {

    public static Boolean validatePassword(String password) {
        return !passwordIsEmpty(password) && validatePasswordPattern(password);
    }

    private static Boolean passwordIsEmpty(String password) {
        return password.isEmpty();
    }

    private static Boolean validatePasswordPattern(String password) {
        /*
         * A senha deve ter:
         * - Letra Maiúscula;
         * - Letra minúcula;
         * - Caracter especial;
         * - Ter 8 e 30 carateres
         */
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,30}$";
        return Pattern.matches(regex, password);
    }

}
