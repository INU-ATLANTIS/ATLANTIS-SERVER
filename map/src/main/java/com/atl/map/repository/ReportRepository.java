package com.atl.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atl.map.entity.ReportEntity;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Integer> {

    boolean existsByReportUserIdAndReportedUserId(int reportUserId, int reportedUserId);
    
}
