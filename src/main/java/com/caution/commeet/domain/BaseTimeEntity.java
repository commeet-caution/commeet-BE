package com.caution.commeet.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@MappedSuperclass //클래스를 상속받는 다른 엔티티 필드에 속성을 포함
@EntityListeners(AuditingEntityListener.class) //엔티티가 생성되거나 수정될 때 자동으로 날짜를 기록
@Getter
public abstract class BaseTimeEntity {

    @CreatedDate //생성시 Date 기록
    private LocalDateTime createdAt;

    @LastModifiedDate //마지막 수정시 Date 기록
    private LocalDateTime updatedAt;

}