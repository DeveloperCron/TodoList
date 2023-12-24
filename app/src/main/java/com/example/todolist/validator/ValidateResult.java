package com.example.todolist.validator;

public class ValidateResult {
    private boolean isSuccess;
    private String err;

    public ValidateResult(boolean isSuccess, String err) {
        this.isSuccess = isSuccess;
        this.err = err;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
