package com.pricematrix.pricematrix.sales.service;

import com.pricematrix.pricematrix.sales.entity.SalesProspect;
import com.pricematrix.pricematrix.sales.repository.SalesProspectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalesProspectService {

    private final SalesProspectRepository repo;

    public SalesProspectService(SalesProspectRepository repo) {
        this.repo = repo;
    }

    public List<SalesProspect> getAll() {
        return repo.findAll();
    }

    public SalesProspect create(SalesProspect prospect) {
        prospect.setCreatedAt(LocalDateTime.now());
        prospect.setUpdatedAt(LocalDateTime.now());
        return repo.save(prospect);
    }

    public SalesProspect update(Long id, SalesProspect data) {
        SalesProspect prospect = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Prospect not found: " + id));
        prospect.setCompanyName(data.getCompanyName());
        prospect.setContactName(data.getContactName());
        prospect.setPhone(data.getPhone());
        prospect.setEmail(data.getEmail());
        prospect.setAddress(data.getAddress());
        prospect.setSource(data.getSource());
        prospect.setAssignedTo(data.getAssignedTo());
        prospect.setStatus(data.getStatus());
        prospect.setUpdatedAt(LocalDateTime.now());
        return repo.save(prospect);
    }
}