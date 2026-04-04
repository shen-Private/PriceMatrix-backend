package com.pricematrix.pricematrix.improvement.repository;

import com.pricematrix.pricematrix.improvement.entity.ImprovementMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImprovementMemberRepository extends JpaRepository<ImprovementMember, Long> {
    List<ImprovementMember> findByProposalId(Long proposalId);
    void deleteByProposalId(Long proposalId);
}