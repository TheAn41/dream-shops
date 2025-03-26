package com.thean.dreamshops.util.excel;

public class BaseResponse {
    public String code;
    public String message;

    public BaseResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}
