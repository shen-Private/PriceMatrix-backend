package com.pricematrix.pricematrix.sales.service;

import com.pricematrix.pricematrix.pricing.entity.Customer;
import com.pricematrix.pricematrix.pricing.entity.Discount;
import com.pricematrix.pricematrix.pricing.entity.Product;
import com.pricematrix.pricematrix.pricing.repository.CustomerRepository;
import com.pricematrix.pricematrix.pricing.repository.DiscountRepository;
import com.pricematrix.pricematrix.pricing.repository.ProductRepository;
import com.pricematrix.pricematrix.sales.dto.CreateQuoteRequest;
import com.pricematrix.pricematrix.sales.entity.SalesQuote;
import com.pricematrix.pricematrix.sales.entity.SalesQuoteItem;
import com.pricematrix.pricematrix.sales.repository.SalesQuoteItemRepository;
import com.pricematrix.pricematrix.sales.repository.SalesQuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesQuoteService {

    private final SalesQuoteRepository quoteRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    // 建立報價單
    public SalesQuote createQuote(CreateQuoteRequest request, String createdBy) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        SalesQuote quote = new SalesQuote();
        quote.setCustomer(customer);
        quote.setNote(request.getNote());
        quote.setCreatedBy(createdBy);

        List<SalesQuoteItem> items = request.getItems().stream().map(itemReq -> {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            SalesQuoteItem item = new SalesQuoteItem();
            item.setQuote(quote);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(itemReq.getUnitPrice());
            return item;
        }).collect(Collectors.toList());

        quote.setItems(items);
        return quoteRepository.save(quote);
    }

    // 查詢全部報價單
    public List<SalesQuote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    // 查詢單一報價單
    public SalesQuote getQuoteById(Long id) {
        return quoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quote not found"));
    }

    // 更新狀態
    public SalesQuote updateStatus(Long id, String status) {
        SalesQuote quote = getQuoteById(id);
        quote.setStatus(status);
        quote.setUpdatedAt(LocalDateTime.now());
        return quoteRepository.save(quote);
    }

    // 建立修正版報價單
    public SalesQuote createRevision(Long parentId, String createdBy) {
        SalesQuote parent = getQuoteById(parentId);
        if (!parent.getStatus().equals("SENT")) {
            throw new RuntimeException("只有 SENT 狀態的報價單才能建立修正版");
        }

        SalesQuote revision = new SalesQuote();
        revision.setCustomer(parent.getCustomer());
        revision.setNote(parent.getNote());
        revision.setCreatedBy(createdBy);
        revision.setParentQuote(parent);

        List<SalesQuoteItem> items = parent.getItems().stream().map(i -> {
            SalesQuoteItem item = new SalesQuoteItem();
            item.setQuote(revision);
            item.setProduct(i.getProduct());
            item.setQuantity(i.getQuantity());
            item.setUnitPrice(i.getUnitPrice());
            return item;
        }).collect(Collectors.toList());

        revision.setItems(items);
        return quoteRepository.save(revision);
    }

    // 更新報價單內容（只有 DRAFT 才能編輯）
    public SalesQuote updateQuote(Long id, CreateQuoteRequest request) {
        SalesQuote quote = getQuoteById(id);
        if (!quote.getStatus().equals("DRAFT")) {
            throw new RuntimeException("只有 DRAFT 狀態的報價單才能編輯");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        quote.setCustomer(customer);
        quote.setNote(request.getNote());
        quote.setUpdatedAt(LocalDateTime.now());

        // 清除舊明細，重新建立
        quote.getItems().clear();

        List<SalesQuoteItem> items = request.getItems().stream().map(itemReq -> {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            SalesQuoteItem item = new SalesQuoteItem();
            item.setQuote(quote);
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());

// 若前端有傳單價則用前端的，否則自動查折扣
            if (itemReq.getUnitPrice() != null) {
                item.setUnitPrice(itemReq.getUnitPrice());
            } else {
                // 查該客戶對該商品的折扣
                Optional<Discount> discount = discountRepository.findByCustomerIdAndProductId(
                        request.getCustomerId(), itemReq.getProductId());
                if (discount.isPresent()) {
                    BigDecimal unitPrice = product.getBasePrice()
                            .multiply(discount.get().getDiscountRatio());
                    item.setUnitPrice(unitPrice);
                } else {
                    // 沒有折扣設定，用定價
                    item.setUnitPrice(product.getBasePrice());
                }
            }
            return item;
        }).collect(Collectors.toList());

        quote.getItems().addAll(items);
        return quoteRepository.save(quote);
    }
}