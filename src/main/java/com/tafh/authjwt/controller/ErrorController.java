package com.tafh.authjwt.controller;

import com.tafh.authjwt.model.WebResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constraintViolationException(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder().errors(exception.getMessage()).build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> apiException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode())
                .body(WebResponse.<String>builder().errors(exception.getReason()).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<String>> handleSecurityException(Exception exception) {

        log.error(String.valueOf(exception));

        if (exception instanceof BadCredentialsException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(WebResponse.<String>builder().errors("The username or password is incorrect").build());
        }

        if (exception instanceof AccountStatusException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(WebResponse.<String>builder().errors("The account is locked").build());
        }

        if (exception instanceof AccessDeniedException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(WebResponse.<String>builder().errors("You are not authorized to access this resource").build());
        }

        if (exception instanceof SignatureException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(WebResponse.<String>builder().errors("The JWT signature is invalid").build());
        }

        if (exception instanceof ExpiredJwtException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(WebResponse.<String>builder().errors("The JWT token has expired").build());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<String>builder().errors("Unknown internal server error.").build());

    }
}
