package org.greatgamesonly.core.aurorasaver.exception;

import org.greatgamesonly.core.aurorasaver.exception.base.CustomError;
import org.greatgamesonly.core.aurorasaver.exception.base.CustomException;

public class RepositoryException extends CustomException {
    public RepositoryException(RepositoryError type) {
        super(type);
    }

    public RepositoryException(RepositoryError type, Exception e) {
        super(type, e);
    }

    public RepositoryException(RepositoryError type, String message) {
        super(type, message);
    }

    public RepositoryException(RepositoryError type, String message, Exception e) {
        super(type, message, e);
    }

    public static class RepositoryError extends CustomError {

        private final static String errorBaseName = RepositoryError.class.getSimpleName();

        public static final RepositoryError REPOSITORY_GENERAL_SQL__ERROR = new RepositoryError(
                errorBaseName+"_00099",
                "General SQL related error",
                500
        );

        RepositoryError(String errorCode, String reason, int httpStatusCode) {
            super(errorCode,reason,httpStatusCode);
        }
    }
}
