package com.pricematrix.pricematrix.improvement.repository;

import com.pricematrix.pricematrix.improvement.entity.ImprovementMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImprovementMilestoneRepository extends JpaRepository<ImprovementMilestone, Long> {
    List<ImprovementMilestone> findByProposalIdOrderByOrderIndex(Long proposalId);
}