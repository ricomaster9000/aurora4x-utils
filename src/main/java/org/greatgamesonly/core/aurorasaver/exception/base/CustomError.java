package org.greatgamesonly.core.aurorasaver.exception.base;

public class CustomError {

    public static final String NAMESPACE = "http://co.za.four27.assignment/customerror";

    private final static String errorBaseName = CustomError.class.getSimpleName();

    public final static CustomError INTERNAL_SERVER_ERROR = new CustomError(
            "00500_" + errorBaseName,
            "Internal Server Error",
            500
    );

    public final static CustomError PAYLOAD_VALIDATION_ERROR = new CustomError(
            "00401_" + errorBaseName,
            "Invalid Request",
            400
    );

    private String errorCode;
    private String reason;
    private String appendToReason;
    private Integer httpStatusCode;
    public CustomError() {}

    public CustomError(String errorCode, String reason, int httpStatusCode) {
        this.errorCode = errorCode;
        this.reason = reason;
        this.httpStatusCode = httpStatusCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getReason() {
        return this.appendToReason != null ? this.reason + ", " + this.appendToReason : this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public CustomError withReason(String toAppend) {
        this.reason+=" - "+toAppend;
        return this;
    }

    public void appendToReason(String toAppend) {
        this.appendToReason = toAppend;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    @Override
    public String toString() {
        return this.getReason();
    }
}
