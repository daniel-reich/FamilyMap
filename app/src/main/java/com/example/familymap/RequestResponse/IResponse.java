package com.example.familymap.RequestResponse;

/**
 * Interface allows BaseHandler to implement generic logic
 * @param <T> data type of successful response data
 */
public interface IResponse<T> {
    T getData();
    void setData(T data);
    int getHttpResponse();
    void setHttpResponse(int response);
    ErrorResponse getError();
    void setError(ErrorResponse error);
    boolean isSuccess();
}
