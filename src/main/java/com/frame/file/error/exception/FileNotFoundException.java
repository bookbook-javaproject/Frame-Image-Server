package com.frame.file.error.exception;

import com.frame.file.error.BusinessException;
import com.frame.file.error.ErrorCode;

public class FileNotFoundException extends BusinessException {
    public FileNotFoundException() {
        super(ErrorCode.FILE_NOT_FOUND);
    }
}
