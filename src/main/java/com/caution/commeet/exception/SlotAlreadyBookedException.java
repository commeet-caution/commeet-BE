package com.caution.commeet.exception;

public class SlotAlreadyBookedException extends BusinessException {
    public SlotAlreadyBookedException() {
        super("이미 예약된 시간입니다.");
    }
}