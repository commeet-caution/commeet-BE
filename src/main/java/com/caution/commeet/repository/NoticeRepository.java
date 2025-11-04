package com.caution.commeet.repository;

import com.caution.commeet.domain.Notice;

import java.util.List;

public interface NoticeRepository {

    /**
     * 모든 공지사항을 최신순으로 조회
     * @return List<Notice> 조회된 공지사항 리스트
     */
    List<Notice> findAllByOrderByCreatedAtDesc();
}
