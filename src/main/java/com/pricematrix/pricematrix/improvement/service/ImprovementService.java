package com.pricematrix.pricematrix.improvement.service;

import com.pricematrix.pricematrix.improvement.entity.*;
import com.pricematrix.pricematrix.improvement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ImprovementService {

    private final ImprovementProposalRepository proposalRepository;
    private final ImprovementMemberRepository memberRepository;
    private final ImprovementMilestoneRepository milestoneRepository;
    private final ImprovementReportRepository reportRepository;
    private final ImprovementAttachmentRepository attachmentRepository;

    public ImprovementService(
            ImprovementProposalRepository proposalRepository,
            ImprovementMemberRepository memberRepository,
            ImprovementMilestoneRepository milestoneRepository,
            ImprovementReportRepository reportRepository,
            ImprovementAttachmentRepository attachmentRepository) {
        this.proposalRepository = proposalRepository;
        this.memberRepository = memberRepository;
        this.milestoneRepository = milestoneRepository;
        this.reportRepository = reportRepository;
        this.attachmentRepository = attachmentRepository;
    }

    // ── 提案 ──────────────────────────────────────────

    public List<ImprovementProposal> getAllProposals() {
        return proposalRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<ImprovementProposal> getProposalsByStatus(String status) {
        return proposalRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public ImprovementProposal getProposal(Long id) {
        return proposalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proposal not found: " + id));
    }

    @Transactional
    public ImprovementProposal createProposal(ImprovementProposal proposal, List<String> memberUsernames) {
        proposal.setStatus("DRAFT");
        proposal.setCreatedAt(LocalDateTime.now());
        proposal.setUpdatedAt(LocalDateTime.now());
        ImprovementProposal saved = proposalRepository.save(proposal);

        // 儲存協作成員
        if (memberUsernames != null) {
            for (String username : memberUsernames) {
                ImprovementMember member = new ImprovementMember();
                member.setProposal(saved);
                member.setUsername(username);
                memberRepository.save(member);
            }
        }
        return proposalRepository.findById(saved.getId()).orElseThrow();
    }

    @Transactional
    public ImprovementProposal updateProposal(Long id, ImprovementProposal updated, List<String> memberUsernames) {
        ImprovementProposal existing = getProposal(id);

        // PENDING_REVIEW 以後鎖住編輯
        if (!existing.getStatus().equals("DRAFT")) {
            throw new RuntimeException("Cannot edit proposal in status: " + existing.getStatus());
        }

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setGoal(updated.getGoal());
        existing.setMetricBefore(updated.getMetricBefore());
        existing.setMetricAfterGoal(updated.getMetricAfterGoal());
        existing.setMetricUnit(updated.getMetricUnit());
        existing.setDueDate(updated.getDueDate());
        existing.setUpdatedAt(LocalDateTime.now());

        // 更新協作成員：先刪後存
        if (memberUsernames != null) {
            memberRepository.deleteByProposalId(id);
            for (String username : memberUsernames) {
                ImprovementMember member = new ImprovementMember();
                member.setProposal(existing);
                member.setUsername(username);
                memberRepository.save(member);
            }
        }

        return proposalRepository.save(existing);
    }

    @Transactional
    public ImprovementProposal submitProposal(Long id) {
        ImprovementProposal proposal = getProposal(id);
        if (!proposal.getStatus().equals("DRAFT")) {
            throw new RuntimeException("Only DRAFT proposals can be submitted");
        }
        proposal.setStatus("PENDING_REVIEW");
        proposal.setSubmittedAt(LocalDateTime.now());
        proposal.setUpdatedAt(LocalDateTime.now());
        return proposalRepository.save(proposal);
    }

    @Transactional
    public ImprovementProposal reviewProposal(Long id, boolean approved, String comment, String reviewedBy) {
        ImprovementProposal proposal = getProposal(id);
        if (!proposal.getStatus().equals("PENDING_REVIEW")) {
            throw new RuntimeException("Only PENDING_REVIEW proposals can be reviewed");
        }
        proposal.setStatus(approved ? "ACTIVE" : "REJECTED");
        proposal.setReviewedBy(reviewedBy);
        proposal.setReviewedAt(LocalDateTime.now());
        proposal.setReviewComment(comment);
        proposal.setUpdatedAt(LocalDateTime.now());
        return proposalRepository.save(proposal);
    }

    @Transactional
    public ImprovementProposal cancelProposal(Long id) {
        ImprovementProposal proposal = getProposal(id);
        if (proposal.getStatus().equals("COMPLETED") || proposal.getStatus().equals("REJECTED")) {
            throw new RuntimeException("Cannot cancel proposal in status: " + proposal.getStatus());
        }
        proposal.setStatus("CANCELLED");
        proposal.setUpdatedAt(LocalDateTime.now());
        return proposalRepository.save(proposal);
    }

    // ── Milestone ─────────────────────────────────────

    @Transactional
    public ImprovementMilestone addMilestone(Long proposalId, ImprovementMilestone milestone) {
        ImprovementProposal proposal = getProposal(proposalId);
        milestone.setProposal(proposal);
        milestone.setStatus("PENDING");
        return milestoneRepository.save(milestone);
    }

    @Transactional
    public ImprovementMilestone completeMilestone(Long milestoneId, String completedBy) {
        ImprovementMilestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Milestone not found: " + milestoneId));
        milestone.setStatus("DONE");
        milestone.setCompletedAt(LocalDateTime.now());
        milestone.setCompletedBy(completedBy);

        // 檢查所有 milestone 是否都完成，是的話提案自動變 COMPLETED
        ImprovementProposal proposal = milestone.getProposal();
        boolean allDone = proposal.getMilestones().stream()
                .allMatch(m -> m.getId().equals(milestoneId) || m.getStatus().equals("DONE"));
        if (allDone) {
            proposal.setStatus("COMPLETED");
            proposal.setUpdatedAt(LocalDateTime.now());
            proposalRepository.save(proposal);
        }

        return milestoneRepository.save(milestone);
    }

    // ── Report ────────────────────────────────────────

    @Transactional
    public ImprovementReport addReport(Long milestoneId, ImprovementReport report, List<String> attachmentUrls, List<String> attachmentNames) {
        ImprovementMilestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Milestone not found: " + milestoneId));
        report.setMilestone(milestone);
        report.setReportedAt(LocalDateTime.now());
        ImprovementReport saved = reportRepository.save(report);

        // 儲存附件連結
        if (attachmentUrls != null) {
            for (int i = 0; i < attachmentUrls.size(); i++) {
                ImprovementAttachment att = new ImprovementAttachment();
                att.setReport(saved);
                att.setFileUrl(attachmentUrls.get(i));
                att.setFileName(attachmentNames != null && i < attachmentNames.size() ? attachmentNames.get(i) : "");
                att.setSourceType("LINK");
                att.setUploadedAt(LocalDateTime.now());
                attachmentRepository.save(att);
            }
        }
        return saved;
    }

    // ── 完成提案（手動填實際效果）────────────────────

    @Transactional
    public ImprovementProposal completeProposal(Long id, java.math.BigDecimal metricActual) {
        ImprovementProposal proposal = getProposal(id);
        proposal.setMetricActual(metricActual);
        proposal.setStatus("COMPLETED");
        proposal.setUpdatedAt(LocalDateTime.now());
        return proposalRepository.save(proposal);
    }
}