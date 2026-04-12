package com.pricematrix.pricematrix.module.sales.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales_contact_log")
public class SalesContactLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = true)
    private Long customerId;
    private Long prospectId;

    // VISIT / PHONE / EMAIL / QUOTE / OTHER
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDateTime contactedAt;

    // PENDING / OK / NO
    @Column(nullable = false)
    private String result;

    private String note;

    private String nextAction;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public SalesContactLog() {}

    // Getters & Setters
    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getProspectId() { return prospectId; }
    public void setProspectId(Long prospectId) { this.prospectId = prospectId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDateTime getContactedAt() { return contactedAt; }
    public void setContactedAt(LocalDateTime contactedAt) { this.contactedAt = contactedAt; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getNextAction() { return nextAction; }
    public void setNextAction(String nextAction) { this.nextAction = nextAction; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}