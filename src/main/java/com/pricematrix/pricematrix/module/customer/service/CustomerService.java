package com.pricematrix.pricematrix.module.customer.service;

import com.pricematrix.pricematrix.module.customer.entity.Customer;
import com.pricematrix.pricematrix.module.customer.repository.CustomerRepository;
import com.pricematrix.pricematrix.module.sales.entity.SalesProspect;
import com.pricematrix.pricematrix.module.sales.repository.SalesProspectRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SalesProspectRepository prospectRepository;

    public CustomerService(CustomerRepository customerRepository,
                           SalesProspectRepository prospectRepository) {
        this.customerRepository = customerRepository;
        this.prospectRepository = prospectRepository;
    }
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();  // 改這行，移除 findByActiveTrue()
    }

    // 新增停用方法
    public Customer deactivateCustomer(Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
        c.setActive(false);
        return customerRepository.save(c);
    }

    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByNameContaining(name);
    }

    // 新增客戶
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // 更新客戶
    public Customer updateCustomer(Long id, Customer updated) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + id));

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        existing.setContactPerson(updated.getContactPerson());
        existing.setNote(updated.getNote());
        existing.setParent(updated.getParent());
        existing.setAssignedTo(updated.getAssignedTo());
        return customerRepository.save(existing);

    }

    public Customer updateStatus(Long id, boolean isActive) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setActive(isActive);
        return customerRepository.save(customer);
    }

    public Customer convertProspect(Long prospectId) {
        SalesProspect prospect = prospectRepository.findById(prospectId)
                .orElseThrow(() -> new RuntimeException("Prospect not found: " + prospectId));

        Customer customer = new Customer();
        customer.setName(prospect.getCompanyName());
        customer.setPhone(prospect.getPhone());
        customer.setEmail(prospect.getEmail());
        customer.setAddress(prospect.getAddress());
        customer.setContactPerson(prospect.getContactName());
        customer.setProspectId(prospectId);
        customer.setActive(true);

        prospect.setStatus("CONVERTED");
        prospectRepository.save(prospect);

        return customerRepository.save(customer);
    }

}