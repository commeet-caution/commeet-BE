package com.caution.commeet.service;

import com.caution.commeet.domain.AvailableSlot;
import com.caution.commeet.domain.User;
import com.caution.commeet.dto.AvailabilityRequest;
import com.caution.commeet.dto.AvailabilityResponse;
import com.caution.commeet.repository.AvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final UserService userService;

    // 교수별 면담 가능 시간 조회
    public List<AvailabilityResponse> getProfessorAvailability(Long professorId) {
        User professor = userService.findById(professorId);
        List<AvailableSlot> slots = availabilityRepository.findByProfessorAndIsBookedFalse(professor);
        return slots.stream()
                .map(AvailabilityResponse::fromEntity)
                .toList();
    }

    // 면담 가능 시간 등록
    public AvailabilityResponse createAvailability(Long professorId, AvailabilityRequest request) {
        User professor = userService.findById(professorId);

        AvailableSlot slot = AvailableSlot.builder()
                .professor(professor)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        AvailableSlot savedSlot = availabilityRepository.save(slot);
        return AvailabilityResponse.fromEntity(savedSlot);
    }

    // 면담 가능 시간 수정
    public AvailabilityResponse updateAvailability(Long slotId, AvailabilityRequest request) {
        AvailableSlot slot = availabilityRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("해당 슬롯이 존재하지 않습니다."));

        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());

        AvailableSlot updatedSlot = availabilityRepository.save(slot);
        return AvailabilityResponse.fromEntity(updatedSlot);
    }

    // 면담 가능 시간 삭제
    public void deleteAvailability(Long slotId) {
        if (!availabilityRepository.existsById(slotId)) {
            throw new IllegalArgumentException("삭제할 슬롯이 존재하지 않습니다.");
        }
        availabilityRepository.deleteById(slotId);
    }


}
