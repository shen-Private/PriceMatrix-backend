package com.pricematrix.pricematrix.module.improvement.repository;

import com.pricematrix.pricematrix.module.improvement.entity.ImprovementReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImprovementReportRepository extends JpaRepository<ImprovementReport, Long> {
    List<ImprovementReport> findByMilestoneIdOrderByReportedAtDesc(Long milestoneId);
}