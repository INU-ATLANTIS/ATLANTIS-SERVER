package com.atl.map.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="report")
@Table(name="report")
public class ReportEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private int reportUserId;
    private int reportedUserId;
    
    public ReportEntity(int reportUserId, int reportedUserId) {
        this.reportUserId = reportUserId;
        this.reportedUserId = reportedUserId;
    }
}
