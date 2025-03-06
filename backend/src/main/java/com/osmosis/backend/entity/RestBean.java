package com.osmosis.backend.entity;


import lombok.Data;
import lombok.Getter;

public class RestBean<T> {
    private int status;
    private boolean success;
    private T massage;

    private RestBean(int status, boolean success, T massage) {
        this.status = status;
        this.success = success;
        this.massage = massage;
    }
    public static <T>RestBean<T> success() {
        return new RestBean<>(200, true, null);
    }
    public static <T>RestBean<T> success(T data) {
        return new RestBean<>(200, true, data);
    }
    public static <T>RestBean<T> failure(int status) {
        return new RestBean<>(status, false, null);
    }
    public static <T>RestBean<T> failure(int status, T data) {
        return new RestBean<>(status, false, data);
    }

    public int getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getMassage() {
        return massage;
    }
}
