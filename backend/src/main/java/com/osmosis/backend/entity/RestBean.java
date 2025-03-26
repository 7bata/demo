package com.osmosis.backend.entity;


import lombok.Data;
import lombok.Getter;


public class RestBean<T> {
    private int status;
    private boolean success;
    private T message;
    private String errorMsg;

    private RestBean(int status, boolean success, T message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }
    
    private RestBean(int status, boolean success, T message, String errorMsg) {
        this.status = status;
        this.success = success;
        this.message = message;
        this.errorMsg = errorMsg;
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
    
    /**
     * Return error response with message
     * @param status status code
     * @param message error message
     * @return error response
     */
    public static <T> RestBean<T> error(int status, String message) {
        RestBean<T> bean = new RestBean<>(status, false, null);
        bean.setErrorMsg(message);
        return bean;
    }

    public int getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getMessage() {
        return message;
    }
    
    public void setMessage(T message) {
        this.message = message;
    }
    
    public String getErrorMsg() {
        return errorMsg;
    }
    
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
