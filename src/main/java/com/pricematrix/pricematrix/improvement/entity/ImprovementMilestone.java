package com.pricematrix.pricematrix.improvement.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "improvement_milestone")
public class ImprovementMilestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id")
    @JsonIgnoreProperties({"members", "milestones"})
    private ImprovementProposal proposal;

    private String title;
    private Integer orderIndex;
    private LocalDate targetDate;
    private String status;
    private LocalDateTime completedAt;
    private String completedBy;

    @OneToMany(mappedBy = "milestone", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ImprovementReport> reports;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ImprovementProposal getProposal() { return proposal; }
    public void setProposal(ImprovementProposal proposal) { this.proposal = proposal; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public String getCompletedBy() { return completedBy; }
    public void setCompletedBy(String completedBy) { this.completedBy = completedBy; }
    public List<ImprovementReport> getReports() { return reports; }
    public void setReports(List<ImprovementReport> reports) { this.reports = reports; }
}