package com.hehspace_host.network;

public class ApiResponse<Object> {
    private final Status status;
    private final Object data;
    private final Throwable error;

    public ApiResponse(Status status, Object data, Throwable error) {
        super();
        this.status = status;
        this.data = data;
        this.error = error;
    }


    public final ApiResponse<Object> loading() {
        return new ApiResponse(Status.LOADING,  null, null);
    }

    public final ApiResponse<Object> success(Object data) {
        return new ApiResponse(Status.SUCCESS, data, null);
    }

    public final ApiResponse<Object> error(Throwable error) {
        return new ApiResponse(Status.ERROR, null, error);
    }

    public final Status getStatus() {
        return this.status;
    }

    public final Object getData() {
        return this.data;
    }

    public final Throwable getError() {
        return this.error;
    }

    public enum Status { LOADING, SUCCESS, ERROR }

}
