package com.pricematrix.pricematrix.search.controller;

import com.pricematrix.pricematrix.customer.entity.Customer;
import com.pricematrix.pricematrix.customer.repository.CustomerRepository;
import com.pricematrix.pricematrix.sales.entity.SalesQuote;
import com.pricematrix.pricematrix.sales.repository.SalesQuoteRepository;
import com.pricematrix.pricematrix.pricing.entity.Product;
import com.pricematrix.pricematrix.pricing.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final CustomerRepository customerRepository;
    private final SalesQuoteRepository salesQuoteRepository;
    private final ProductRepository productRepository;

    public SearchController(CustomerRepository customerRepository,
                            SalesQuoteRepository salesQuoteRepository,
                            ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.salesQuoteRepository = salesQuoteRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public Map<String, Object> search(@RequestParam String q) {
        Map<String, Object> result = new HashMap<>();

        if (q == null || q.trim().length() < 1) {
            return result;
        }

        String keyword = "%" + q.trim() + "%";

        List<Customer> customers = customerRepository.searchByKeyword(keyword);
        List<SalesQuote> quotes = salesQuoteRepository.searchByKeyword(keyword);
        List<Product> products = productRepository.searchByKeyword(keyword);

        result.put("customers", customers);
        result.put("quotes", quotes);
        result.put("products", products);

        return result;
    }
}