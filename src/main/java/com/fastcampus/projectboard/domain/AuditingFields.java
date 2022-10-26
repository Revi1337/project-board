package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AuditingFields {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // Date 포맷을 파싱해주는 룰 설정.
    @CreatedDate
    @Column(nullable = false, updatable = false) // 업데이트 불가 셋팅
    private LocalDateTime createdAt; // 최초 insert 할 때 생성될 때마다 생성된 시간과 생성한 사람을 실시간으로 추가시켜주겠다는 의미.

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 100)
    private String createdBy;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // Date 포맷을 파싱해주는 룰 설정.
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // update 할때마다 수정한 시간과 사람을 실시간으로 수정해주겠다는 의미

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy;
}
