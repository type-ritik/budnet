package com.network.buddy.model;

import java.util.List;

public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private int errorCode;
    private long timestamp;
    private String path;

    // Getters and Setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean _success) {
        this.success = _success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String _message) {
        this.message = _message;
    }

    public T getData() {
        return data;
    }

    public void setData(T _data) {
        this.data = _data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> _errors) {
        this.errors = _errors;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int _errorCode) {
        this.errorCode = _errorCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long _timestamp) {
        this.timestamp = _timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String _path) {
        this.path = _path;
    }

}
