package com.pricematrix.pricematrix.improvement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "improvement_proposal")
public class ImprovementProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String goal;
    private BigDecimal metricBefore;
    private BigDecimal metricAfterGoal;
    private String metricUnit;
    private BigDecimal metricActual;
    private String proposer;
    private String status;
    private String reviewedBy;
    private LocalDateTime reviewedAt;
    private String reviewComment;
    private LocalDateTime submittedAt;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ImprovementMember> members;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ImprovementMilestone> milestones;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }
    public BigDecimal getMetricBefore() { return metricBefore; }
    public void setMetricBefore(BigDecimal metricBefore) { this.metricBefore = metricBefore; }
    public BigDecimal getMetricAfterGoal() { return metricAfterGoal; }
    public void setMetricAfterGoal(BigDecimal metricAfterGoal) { this.metricAfterGoal = metricAfterGoal; }
    public String getMetricUnit() { return metricUnit; }
    public void setMetricUnit(String metricUnit) { this.metricUnit = metricUnit; }
    public BigDecimal getMetricActual() { return metricActual; }
    public void setMetricActual(BigDecimal metricActual) { this.metricActual = metricActual; }
    public String getProposer() { return proposer; }
    public void setProposer(String proposer) { this.proposer = proposer; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    public String getReviewComment() { return reviewComment; }
    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<ImprovementMember> getMembers() { return members; }
    public void setMembers(List<ImprovementMember> members) { this.members = members; }
    public List<ImprovementMilestone> getMilestones() { return milestones; }
    public void setMilestones(List<ImprovementMilestone> milestones) { this.milestones = milestones; }
}