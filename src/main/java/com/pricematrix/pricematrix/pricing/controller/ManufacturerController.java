package com.pricematrix.pricematrix.pricing.controller;

import com.pricematrix.pricematrix.inventory.entity.Manufacturer;
import com.pricematrix.pricematrix.pricing.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerRepository manufacturerRepository;

    @GetMapping
    public List<Manufacturer> getAll() {
        return manufacturerRepository.findAll();
    }
}