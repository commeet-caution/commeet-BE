package com.caution.commeet.handler;

import com.caution.commeet.dto.ErrorResponse;
import com.caution.commeet.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 1. 모든 컨트롤러에서 발생하는 예외를 여기서 처리
public class GlobalExceptionHandler {

    // 2. BusinessException을 상속하는 모든 예외는 이 메서드가 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        // 3. 예외 메시지를 ErrorResponse에 담아 400 Bad Request 상태 코드로 응답
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }
}