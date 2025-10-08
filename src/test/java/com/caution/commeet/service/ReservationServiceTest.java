package com.caution.commeet.service;


import com.caution.commeet.domain.*;
import com.caution.commeet.dto.appointment.AppointmentRequestDto;
import com.caution.commeet.dto.availableslot.AvailableSlotCreateRequestDto;
import com.caution.commeet.exception.SlotAlreadyBookedException;
import com.caution.commeet.repository.AppointmentRepository;
import com.caution.commeet.repository.AvailableSlotRepository;
import com.caution.commeet.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // 1. Mockito 기능을 JUnit 5와 연결
class ReservationServiceTest {

    @InjectMocks // 2. 테스트 대상: 이 클래스에 @Mock으로 만든 가짜 객체들을 주입
    private ReservationService reservationService;

    @Mock // 3. 가짜 객체(Mock): 실제 DB 대신 사용할 가짜 Repository
    private UserRepository userRepository;

    @Mock
    private AvailableSlotRepository availableSlotRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private EntityManager em; // EntityManager도 가짜


    /**
     * 학생이 면담 예약을 정상적으로 신청하는 시나리오를 테스트
     *
     * 테스트 흐름:
     * 1. 학생과 상담 슬롯 정보를 가짜(mock)로 설정
     * 2. 예약 요청 DTO를 생성하여 서비스 메서드 호출
     * 3. 예약 객체가 정상적으로 생성되고 상태가 PENDING인지 검증
     * 4. 슬롯 예약 처리 및 저장 로직이 호출되었는지 확인
     *
     * 기대 결과:
     * - Appointment 객체가 null이 아니고
     * - 상태가 PENDING이며
     * - slot.book()과 appointmentRepository.save()가 호출됨
     */


    @Test // 4. 이 메서드가 테스트 메서드임을 선언
    @DisplayName("학생이 면담 예약을 성공적으로 신청한다")
    void requestAppointment_Success() {
        // given - 테스트 준비 (가짜 객체들이 어떻게 행동할지 정의)
        long studentId = 1L;
        long slotId = 1L;
        AppointmentRequestDto requestDto = createRequestDto(studentId, slotId);

        User student = mock(User.class); // 가짜 학생, 교수, 슬롯 객체 생성
        AvailableSlot slot = mock(AvailableSlot.class);

        // "userRepository.findById(1L)가 호출되면, 가짜 student 객체를 반환해줘" 라고 설정
        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));
        // "em.find(...)가 호출되면, 가짜 slot 객체를 반환해줘" 라고 설정
        when(em.find(AvailableSlot.class, slotId, LockModeType.PESSIMISTIC_WRITE)).thenReturn(slot);


        // when - 실제 테스트 실행
        Appointment newAppointment = reservationService.requestAppointment(requestDto);


        // then - 결과 검증
        assertNotNull(newAppointment); // 1. 생성된 예약 객체가 null이 아닌지 확인
        assertEquals(AppointmentStatus.PENDING, newAppointment.getStatus()); // 2. 예약 상태가 PENDING이 맞는지 확인

        verify(slot).book(); // 3. slot 객체의 book() 메서드가 '반드시 한 번' 호출되었는지 확인
        verify(appointmentRepository).save(any(Appointment.class)); // 4. repository의 save가 '어떤 Appointment 객체로든' 호출되었는지 확인
    }


    /**
     * 테스트용 AppointmentRequestDto 객체를 생성
     *
     * 입력값:
     * - studentId: 예약을 신청하는 학생의 ID
     * - slotId: 예약할 상담 슬롯의 ID
     *
     * 설정값:
     * - topic: CAREER로 고정
     *
     * 반환값:
     * - 예약 요청에 필요한 필드가 채워진 DTO 객체
     */

    private AppointmentRequestDto createRequestDto(long studentId, long slotId) {
        AppointmentRequestDto dto = new AppointmentRequestDto();
        dto.setStudentId(studentId);
        dto.setSlotId(slotId);
        dto.setTopic(Topic.CAREER);
        return dto;
    }


    /**
     * 이미 예약된 상담 슬롯에 대해 면담을 신청할 경우 예외가 발생하는 시나리오를 테스트
     *
     * 테스트 흐름:
     * 1. 학생과 상담 슬롯 정보를 가짜(mock)로 설정
     * 2. 예약 요청 DTO를 생성하여 서비스 메서드 호출
     * 3. slot.book() 호출 시 SlotAlreadyBookedException을 던지도록 설정
     * 4. 예약 요청 시 해당 예외가 발생하는지 검증
     * 5. 예외 발생 시 appointmentRepository.save()가 호출되지 않았는지 확인
     *
     * 기대 결과:
     * - SlotAlreadyBookedException 예외가 발생함
     * - 예약 저장 로직은 실행되지 않음
     */


    @Test
    @DisplayName("이미 예약된 슬롯에 면담을 신청하면 예외가 발생한다")
    void requestAppointment_Fail_AlreadyBooked() {
        // given - 테스트 준비
        long studentId = 1L;
        long slotId = 1L;
        AppointmentRequestDto requestDto = createRequestDto(studentId, slotId);

        User student = mock(User.class);
        AvailableSlot slot = mock(AvailableSlot.class);

        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(em.find(AvailableSlot.class, slotId, LockModeType.PESSIMISTIC_WRITE)).thenReturn(slot);

        // [핵심] slot.book() 메서드가 호출되면, SlotAlreadyBookedException을 던지도록 설정
        doThrow(new SlotAlreadyBookedException()).when(slot).book();

        // when & then - 실행 및 결과 검증
        // reservationService.requestAppointment(...)를 실행했을 때,
        // SlotAlreadyBookedException 예외가 발생하는 것을 기대하고 검증합니다.
        assertThrows(SlotAlreadyBookedException.class, () -> {
            reservationService.requestAppointment(requestDto);
        });

        // 추가 검증: 예외가 발생했으므로, appointmentRepository.save()는 절대 호출되면 안 됨
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }


    /**
     * 교수가 자신에게 배정된 면담을 정상적으로 승인하는 시나리오를 테스트
     *
     * 테스트 흐름:
     * 1. 교수와 예약 정보를 가짜(mock)로 설정
     * 2. 예약 조회 후 교수 권한 확인
     * 3. 예약 상태 변경(confirm())이 호출되는지 검증
     *
     * 기대 결과:
     * - 예약 객체의 confirm() 메서드가 호출됨
     */


    @Test
    @DisplayName("교수가 면담 신청을 성공적으로 승인한다")
    void confirmAppointment_Success() {
        // given
        long professorId = 1L;
        long appointmentId = 1L;

        // [수정] Builder 대신 Mockito로 가짜 User 생성
        User professor = mock(User.class);
        when(professor.getId()).thenReturn(professorId);

        Appointment appointment = mock(Appointment.class);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointment.getProfessor()).thenReturn(professor);

        // when
        reservationService.confirmAppointment(professorId, appointmentId);

        // then
        verify(appointment).confirm();
    }


    /**
     * 권한이 없는 교수가 다른 교수의 면담을 승인하려 할 때 예외가 발생하는 시나리오를 테스트
     *
     * 테스트 흐름:
     * 1. 예약에 배정된 교수와 요청한 교수 ID가 다름
     * 2. 예약 조회 후 권한 확인
     * 3. SecurityException 예외 발생 여부 검증
     *
     * 기대 결과:
     * - SecurityException 예외가 발생함
     */

    @Test
    @DisplayName("권한 없는 교수가 면담을 승인하면 예외가 발생한다")
    void confirmAppointment_Fail_Unauthorized() {
        // given
        long realProfessorId = 1L;
        long fakeProfessorId = 2L;
        long appointmentId = 1L;

        // [수정] Builder 대신 Mockito로 가짜 User 생성
        User realProfessor = mock(User.class);
        when(realProfessor.getId()).thenReturn(realProfessorId);

        Appointment appointment = mock(Appointment.class);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointment.getProfessor()).thenReturn(realProfessor);

        // when & then
        assertThrows(SecurityException.class, () -> {
            reservationService.confirmAppointment(fakeProfessorId, appointmentId);
        });
    }


    /**
     * 학생이 자신에게 배정된 면담을 정상적으로 취소하는 시나리오를 테스트
     *
     * 테스트 흐름:
     * 1. 예약 정보와 관련된 학생, 교수, 슬롯을 mock으로 설정
     * 2. 예약 조회 후 권한 확인
     * 3. 예약 취소(cancel()) 및 슬롯 상태 변경(makeAvailable()) 검증
     *
     * 기대 결과:
     * - 예약 객체의 cancel(reason) 메서드가 호출됨
     * - 슬롯 객체의 makeAvailable() 메서드가 호출됨
     */


    @Test
    @DisplayName("학생이 자신의 면담을 성공적으로 취소한다")
    void cancelAppointment_Success() {
        // given
        long studentId = 1L;
        long professorId = 2L;
        long appointmentId = 1L;
        String reason = "개인 사정";

        // [수정] 모든 User 객체를 Mockito로 생성
        User student = mock(User.class);
        when(student.getId()).thenReturn(studentId);

        User professor = mock(User.class);
        when(professor.getId()).thenReturn(professorId);

        AvailableSlot slot = mock(AvailableSlot.class);
        Appointment appointment = mock(Appointment.class);

        when(appointmentRepository.findByIdWithSlot(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointment.getStudent()).thenReturn(student);
        when(appointment.getProfessor()).thenReturn(professor);
        when(appointment.getAvailableSlot()).thenReturn(slot);

        // when
        reservationService.cancelAppointment(studentId, appointmentId, reason);

        // then
        verify(appointment).cancel(reason);
        verify(slot).makeAvailable();
    }


    /**
     * 교수가 자신에게 배정된 면담을 정상적으로 완료 처리하는 시나리오를 테스트
     *
     * 테스트 흐름:
     * 1. 예약 정보와 교수 정보를 mock으로 설정
     * 2. 예약 조회 후 권한 확인
     * 3. 예약 완료 처리(complete())가 호출되는지 검증
     *
     * 기대 결과:
     * - 예약 객체의 complete() 메서드가 호출됨
     */


    @Test
    @DisplayName("교수가 면담을 성공적으로 완료 처리한다")
    void completeAppointment_Success() {
        // given
        long professorId = 1L;
        long appointmentId = 1L;

        // [수정] Builder 대신 Mockito로 가짜 User 생성
        User professor = mock(User.class);
        when(professor.getId()).thenReturn(professorId);

        Appointment appointment = mock(Appointment.class);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointment.getProfessor()).thenReturn(professor);

        // when
        reservationService.completeAppointment(professorId, appointmentId);

        // then
        verify(appointment).complete();
    }


    /**
     * 교수가 상담 가능 시간을 성공적으로 등록하는 시나리오를 테스트
     *
     * 테스트 흐름:
     * 1. 교수 정보와 상담 시간 정보를 포함한 DTO 생성
     * 2. 교수 조회 후 슬롯 생성 및 저장
     *
     * 기대 결과:
     * - availableSlotRepository.saveAll(...) 메서드가 호출됨
     */


    @Test
    @DisplayName("교수가 상담 가능 시간을 성공적으로 등록한다")
    void createAvailableSlots_Success() {
        // given
        long professorId = 1L;
        AvailableSlotCreateRequestDto requestDto = createSlotRequestDto(professorId);
        User professor = mock(User.class);

        when(userRepository.findById(professorId)).thenReturn(Optional.of(professor));

        // when
        reservationService.createAvailableSlots(requestDto);

        // then
        verify(availableSlotRepository).saveAll(any(List.class));
    }

    /**
     * 테스트용 AvailableSlotCreateRequestDto 객체를 생성한다.
     *
     * 입력값:
     * - professorId: 상담 가능 시간을 등록할 교수의 ID
     *
     * 설정값:
     * - 1개의 SlotInfo 객체를 생성하여 DTO에 설정
     *
     * 반환값:
     * - 상담 가능 시간 정보가 포함된 DTO 객체
     */
    private AvailableSlotCreateRequestDto createSlotRequestDto(long professorId) {
        AvailableSlotCreateRequestDto dto = new AvailableSlotCreateRequestDto();
        dto.setProfessorId(professorId);

        AvailableSlotCreateRequestDto.SlotInfo slot1 = new AvailableSlotCreateRequestDto.SlotInfo();
        slot1.setStartTime(LocalDateTime.now().plusDays(1));
        slot1.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        dto.setSlots(List.of(slot1));
        return dto;
    }

}