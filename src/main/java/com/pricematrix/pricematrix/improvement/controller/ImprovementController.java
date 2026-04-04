package com.pricematrix.pricematrix.improvement.controller;

import com.pricematrix.pricematrix.improvement.entity.*;
import com.pricematrix.pricematrix.improvement.service.ImprovementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/improvements")
public class ImprovementController {

    private final ImprovementService improvementService;

    public ImprovementController(ImprovementService improvementService) {
        this.improvementService = improvementService;
    }

    // ── 提案 CRUD ─────────────────────────────────────

    @GetMapping
    public List<ImprovementProposal> getAll(
            @RequestParam(required = false) String status) {
        if (status != null) {
            return improvementService.getProposalsByStatus(status);
        }
        return improvementService.getAllProposals();
    }

    @GetMapping("/{id}")
    public ImprovementProposal getOne(@PathVariable Long id) {
        return improvementService.getProposal(id);
    }

    @PostMapping
    public ImprovementProposal create(
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {
        ImprovementProposal proposal = new ImprovementProposal();
        proposal.setTitle((String) body.get("title"));
        proposal.setDescription((String) body.get("description"));
        proposal.setGoal((String) body.get("goal"));
        proposal.setMetricUnit((String) body.get("metricUnit"));

        if (body.get("metricBefore") != null)
            proposal.setMetricBefore(new BigDecimal(body.get("metricBefore").toString()));
        if (body.get("metricAfterGoal") != null)
            proposal.setMetricAfterGoal(new BigDecimal(body.get("metricAfterGoal").toString()));

        String proposer = (String) request.getAttribute("username");
        proposal.setProposer(proposer);

        @SuppressWarnings("unchecked")
        List<String> members = (List<String>) body.get("members");

        return improvementService.createProposal(proposal, members);
    }

    @PutMapping("/{id}")
    public ImprovementProposal update(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        ImprovementProposal updated = new ImprovementProposal();
        updated.setTitle((String) body.get("title"));
        updated.setDescription((String) body.get("description"));
        updated.setGoal((String) body.get("goal"));
        updated.setMetricUnit((String) body.get("metricUnit"));

        if (body.get("metricBefore") != null)
            updated.setMetricBefore(new BigDecimal(body.get("metricBefore").toString()));
        if (body.get("metricAfterGoal") != null)
            updated.setMetricAfterGoal(new BigDecimal(body.get("metricAfterGoal").toString()));

        @SuppressWarnings("unchecked")
        List<String> members = (List<String>) body.get("members");

        return improvementService.updateProposal(id, updated, members);
    }

    // ── 狀態流轉 ──────────────────────────────────────

    @PostMapping("/{id}/submit")
    public ImprovementProposal submit(@PathVariable Long id, HttpServletRequest request) {
        String operatedBy = (String) request.getAttribute("username");
        return improvementService.submitProposal(id, operatedBy);
    }
    @PostMapping("/{id}/review")
    public ImprovementProposal review(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {
        boolean approved = Boolean.TRUE.equals(body.get("approved"));
        String comment = (String) body.get("comment");
        String reviewedBy = (String) request.getAttribute("username");
        return improvementService.reviewProposal(id, approved, comment, reviewedBy);
    }

    @PostMapping("/{id}/cancel")
    public ImprovementProposal cancel(@PathVariable Long id, HttpServletRequest request) {
        String operatedBy = (String) request.getAttribute("username");
        return improvementService.cancelProposal(id, operatedBy);
    }

    @PostMapping("/{id}/complete")
    public ImprovementProposal complete(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {
        String operatedBy = (String) request.getAttribute("username");
        BigDecimal metricActual = body.get("metricActual") != null
                ? new BigDecimal(body.get("metricActual").toString()) : null;
        return improvementService.completeProposal(id, metricActual, operatedBy);
    }

    // ── Milestone ─────────────────────────────────────

    @PostMapping("/{proposalId}/milestones")
    public ImprovementMilestone addMilestone(
            @PathVariable Long proposalId,
            @RequestBody Map<String, Object> body) {
        ImprovementMilestone milestone = new ImprovementMilestone();
        milestone.setTitle((String) body.get("title"));
        milestone.setOrderIndex((Integer) body.get("orderIndex"));
        if (body.get("targetDate") != null)
            milestone.setTargetDate(java.time.LocalDate.parse((String) body.get("targetDate")));
        return improvementService.addMilestone(proposalId, milestone);
    }

    @PostMapping("/milestones/{milestoneId}/complete")
    public ImprovementMilestone completeMilestone(
            @PathVariable Long milestoneId,
            HttpServletRequest request) {
        String completedBy = (String) request.getAttribute("username");
        return improvementService.completeMilestone(milestoneId, completedBy);
    }

    // ── Report ────────────────────────────────────────

    @PostMapping("/milestones/{milestoneId}/reports")
    public ImprovementReport addReport(
            @PathVariable Long milestoneId,
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {
        ImprovementReport report = new ImprovementReport();
        report.setNote((String) body.get("note"));
        report.setReportedBy((String) request.getAttribute("username"));

        @SuppressWarnings("unchecked")
        List<String> urls = (List<String>) body.get("attachmentUrls");
        @SuppressWarnings("unchecked")
        List<String> names = (List<String>) body.get("attachmentNames");

        return improvementService.addReport(milestoneId, report, urls, names);
    }
}