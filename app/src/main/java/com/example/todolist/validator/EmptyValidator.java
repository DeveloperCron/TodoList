package com.example.todolist.validator;

import android.util.Log;

public class EmptyValidator extends BaseValidator {
    private final String input;

    public EmptyValidator(String input) {
        this.input = input;
    }

    @Override
    public ValidateResult validate() {
        boolean isValid = input != null && !input.isEmpty();
        return new ValidateResult(
                isValid,
                isValid ? "Field isn't empty" : "Field is empty"
        );
    }
}
