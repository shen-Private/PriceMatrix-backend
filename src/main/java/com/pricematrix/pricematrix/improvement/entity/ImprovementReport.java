package com.pricematrix.pricematrix.improvement.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "improvement_report")
public class ImprovementReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id")
    @JsonIgnoreProperties({"reports", "proposal"})
    private ImprovementMilestone milestone;

    private String note;
    private String reportedBy;
    private LocalDateTime reportedAt;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ImprovementAttachment> attachments;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ImprovementMilestone getMilestone() { return milestone; }
    public void setMilestone(ImprovementMilestone milestone) { this.milestone = milestone; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
    public List<ImprovementAttachment> getAttachments() { return attachments; }
    public void setAttachments(List<ImprovementAttachment> attachments) { this.attachments = attachments; }
}