package com.hyeongsh.feb;

import com.hyeongsh.feb.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyBlockedException.class)
    public ResponseEntity<?> handleAlreadyBlocked(AlreadyBlockedException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(ExtensionAlreadyInFixedException.class)
    public ResponseEntity<?> handleExtensionAlreadyInFixed(ExtensionAlreadyInFixedException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidExtensionException.class)
    public ResponseEntity<?> handleInvalidExtension(InvalidExtensionException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(NotFixedException.class)
    public ResponseEntity<?> handleNotFixed(NotFixedException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
