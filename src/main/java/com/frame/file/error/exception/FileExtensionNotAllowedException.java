package com.frame.file.error.exception;

import com.frame.file.error.BusinessException;
import com.frame.file.error.ErrorCode;

public class FileExtensionNotAllowedException extends BusinessException {
    public FileExtensionNotAllowedException() {
        super(ErrorCode.FILE_EXTENSION_NOT_ALLOWED);
    }
}