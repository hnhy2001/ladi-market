package com.example.ladi.repository;

import com.example.ladi.model.CallInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CallInfoRepository extends BaseRepository<CallInfo> {
    List<CallInfo> findAllByIsActive(int isActive);

    @Query(value = "SELECT 1 FROM DUAL", nativeQuery = true)
    int heathCheck();
}
