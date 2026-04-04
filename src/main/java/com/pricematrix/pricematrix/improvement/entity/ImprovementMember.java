package com.pricematrix.pricematrix.improvement.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "improvement_member")
public class ImprovementMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id")
    @JsonIgnoreProperties({"members", "milestones"})
    private ImprovementProposal proposal;

    private String username;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ImprovementProposal getProposal() { return proposal; }
    public void setProposal(ImprovementProposal proposal) { this.proposal = proposal; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}