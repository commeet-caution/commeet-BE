package com.caution.commeet.controller;

import com.caution.commeet.dto.AvailabilityRequest;
import com.caution.commeet.dto.AvailabilityResponse;
import com.caution.commeet.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    // 교수별 면담 가능 시간 조회
    @GetMapping("/availability/{professorId}")
    public ResponseEntity<List<AvailabilityResponse>> getProfessorAvailability(@PathVariable Long professorId) {
        List<AvailabilityResponse> availability = availabilityService.getProfessorAvailability(professorId);
        return ResponseEntity.ok(availability);
    }

    // 면담 가능 시간 등록
    @PostMapping("/availability")
    public ResponseEntity<AvailabilityResponse> createAvailability(@RequestParam Long professorId,
                                                                   @RequestBody AvailabilityRequest dto) {
        AvailabilityResponse created = availabilityService.createAvailability(professorId, dto);
        return ResponseEntity.ok(created);
    }

    // 면담 가능 시간 수정
    @PatchMapping("/availability/{slotId}")
    public ResponseEntity<AvailabilityResponse> updateAvailability(@PathVariable Long slotId,
                                                                   @RequestBody AvailabilityRequest dto) {
        AvailabilityResponse updated = availabilityService.updateAvailability(slotId, dto);
        return ResponseEntity.ok(updated);
    }

    // 면담 가능 시간 삭제
    @DeleteMapping("/availability/{slotId}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long slotId) {
        availabilityService.deleteAvailability(slotId);
        return ResponseEntity.noContent().build();
    }
}