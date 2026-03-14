package com.pricematrix.pricematrix.pricing.repository;

import com.pricematrix.pricematrix.inventory.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
}