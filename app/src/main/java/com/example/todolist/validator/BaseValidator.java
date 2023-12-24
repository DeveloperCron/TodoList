package com.example.todolist.validator;

public abstract class BaseValidator implements IValidator {

    public static ValidateResult validate(IValidator... validators) {
        for (IValidator validator : validators) {
            ValidateResult result = validator.validate();
            if (!result.isSuccess()) {
                return result;
            }
        }
        return new ValidateResult(true, "Field isn't empty");
    }
}