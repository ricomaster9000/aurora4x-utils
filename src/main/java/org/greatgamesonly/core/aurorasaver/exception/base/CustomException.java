package org.greatgamesonly.core.aurorasaver.exception.base;


public class CustomException extends RuntimeException {
    private final CustomError customError;
    private Exception innerException;

    public CustomException(CustomError customError) {
        super(customError.getReason());
        this.customError = customError;
    }

    public CustomException(CustomError customError, Exception e) {
        super(customError.getReason()+","+e.getMessage());
        this.customError = customError;
        this.innerException = e;
    }

    public CustomException(CustomError customError, String message) {
        super(customError.getReason()+","+message);
        this.customError = customError;
    }

    public CustomException(CustomError customError, String message, Exception e) {
        super(customError.getReason()+","+message,e);
        this.customError = customError;
        this.innerException = e;
    }

    public CustomError getCustomError() {
        return customError;
    }

    public CustomError getErrorType() {
        return customError;
    }
}
