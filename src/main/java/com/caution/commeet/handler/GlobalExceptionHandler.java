package com.caution.commeet.handler;

import com.caution.commeet.dto.ErrorResponse;
import com.caution.commeet.exception.BusinessException;
import org.springframework.dao.DataIntegrityViolationException;
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

    /**
     * DB 제약조건 위반 (Unique Key 등) 발생 시 처리
     * 예: 이미 가입된 아이디(탈퇴 회원 포함)로 가입 시도 시 발생
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        // 1. 에러 로그를 남겨서 나중에 확인 (선택 사항)
        // log.warn("Data Integrity Violation: {}", e.getMessage());

        // 2. 클라이언트에게 409 Conflict 상태 코드와 메시지 반환
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("이미 사용 중인 아이디입니다."));
    }
}