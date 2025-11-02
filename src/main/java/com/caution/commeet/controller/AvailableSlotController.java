package com.caution.commeet.controller;

import com.caution.commeet.dto.availableslot.AvailableSlotCreateRequestDto;
import com.caution.commeet.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 상담 가능 시간(Slot) 관련 API 요청을 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class AvailableSlotController {

    private final ReservationService reservationService;

    /**
     * 교수가 상담 가능 시간을 등록하는 API
     * [POST] /api/slots
     *
     * @param requestDto 등록할 교수의 ID와 시간 정보 리스트
     * @return 201 Created 상태 코드
     */
    @PostMapping
    public ResponseEntity<Void> createAvailableSlots(@RequestBody AvailableSlotCreateRequestDto requestDto) {
        reservationService.createAvailableSlots(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
