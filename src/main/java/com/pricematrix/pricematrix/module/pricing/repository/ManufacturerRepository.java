package com.pricematrix.pricematrix.module.pricing.repository;

import com.pricematrix.pricematrix.module.inventory.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
}