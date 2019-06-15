package com.example.familymap.RequestResponse;

/**
 * Base Response object
 * @param <T> type for the data attribute of the response object
 */
public class BaseResponse<T> implements IResponse<T> {

    boolean isSuccess;
    T data;
    ErrorResponse error;
    int HttpResponse;

    public int getHttpResponse() {
        return HttpResponse;
    }

    public void setHttpResponse(int httpResponse) {
        HttpResponse = httpResponse;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        isSuccess = true;
        this.data = data;
    }

    public ErrorResponse getError() {
        isSuccess = false;
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }
}
