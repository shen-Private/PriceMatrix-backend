package com.pricematrix.pricematrix.improvement.repository;

import com.pricematrix.pricematrix.improvement.entity.ImprovementProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImprovementProposalRepository extends JpaRepository<ImprovementProposal, Long> {
    List<ImprovementProposal> findByStatusOrderByCreatedAtDesc(String status);
    List<ImprovementProposal> findByProposerOrderByCreatedAtDesc(String proposer);
    List<ImprovementProposal> findAllByOrderByCreatedAtDesc();
}