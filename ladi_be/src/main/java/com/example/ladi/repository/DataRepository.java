package com.example.ladi.repository;

import com.example.ladi.model.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DataRepository extends BaseRepository<Data> {
    List<Data> findAll();
    Data findAllById(int id);

    @Query("SELECT d FROM Data d WHERE (:status is null or d.status = :status)")
    List<Data> findAllByStatus(@Param("status") int status);
}
