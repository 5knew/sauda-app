package com.sauda.sauda_app.service.impl;

import com.sauda.sauda_app.entity.Customer;
import com.sauda.sauda_app.repository.CustomerRepository;
import com.sauda.sauda_app.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Реализация сервиса для управления клиентами
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        validateCustomer(customer);
        
        // Проверка уникальности номера телефона
        if (customer.getPhone() != null && customerRepository.existsByPhone(customer.getPhone())) {
            throw new IllegalArgumentException("Клиент с номером телефона " + customer.getPhone() + " уже существует");
        }
        
        // Проверка уникальности email
        if (customer.getEmail() != null && customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Клиент с email " + customer.getEmail() + " уже существует");
        }
        
        // Проверка уникальности дисконтной карты
        if (customer.getDiscountCard() != null && customerRepository.existsByDiscountCard(customer.getDiscountCard())) {
            throw new IllegalArgumentException("Клиент с дисконтной картой " + customer.getDiscountCard() + " уже существует");
        }
        
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        validateCustomer(customer);
        
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Клиент с ID " + id + " не найден"));
        
        // Проверка уникальности номера телефона (если изменился)
        if (customer.getPhone() != null && !customer.getPhone().equals(existingCustomer.getPhone()) &&
            customerRepository.existsByPhone(customer.getPhone())) {
            throw new IllegalArgumentException("Клиент с номером телефона " + customer.getPhone() + " уже существует");
        }
        
        // Проверка уникальности email (если изменился)
        if (customer.getEmail() != null && !customer.getEmail().equals(existingCustomer.getEmail()) &&
            customerRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Клиент с email " + customer.getEmail() + " уже существует");
        }
        
        // Проверка уникальности дисконтной карты (если изменилась)
        if (customer.getDiscountCard() != null && !customer.getDiscountCard().equals(existingCustomer.getDiscountCard()) &&
            customerRepository.existsByDiscountCard(customer.getDiscountCard())) {
            throw new IllegalArgumentException("Клиент с дисконтной картой " + customer.getDiscountCard() + " уже существует");
        }
        
        // Обновление данных клиента
        existingCustomer.setFullName(customer.getFullName());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setDiscountCard(customer.getDiscountCard());
        
        return customerRepository.save(existingCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerByDiscountCard(String discountCard) {
        return customerRepository.findByDiscountCard(discountCard);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> getCustomersByTenant(Long tenantId, Pageable pageable) {
        return customerRepository.findByTenantId(tenantId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> searchCustomersByName(String name, Pageable pageable) {
        return customerRepository.findByFullNameContainingIgnoreCase(name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> searchCustomersByNameInTenant(Long tenantId, String name, Pageable pageable) {
        return customerRepository.findByTenantIdAndFullNameContainingIgnoreCase(tenantId, name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> searchCustomersByPhone(String phone, Pageable pageable) {
        return customerRepository.findByPhoneContaining(phone, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> searchCustomersByPhoneInTenant(Long tenantId, String phone, Pageable pageable) {
        return customerRepository.findByTenantIdAndPhoneContaining(tenantId, phone, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> getCustomersWithDiscountCards(Long tenantId, Pageable pageable) {
        return customerRepository.findByTenantIdAndDiscountCardIsNotNull(tenantId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> getCustomersWithoutDiscountCards(Long tenantId, Pageable pageable) {
        return customerRepository.findByTenantIdAndDiscountCardIsNull(tenantId, pageable);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Клиент с ID " + id + " не найден"));
        
        customerRepository.delete(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCustomerCountByTenant(Long tenantId) {
        return customerRepository.countByTenantId(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCustomerCountWithDiscountCardsByTenant(Long tenantId) {
        return customerRepository.countByTenantIdAndDiscountCardIsNotNull(tenantId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPhone(String phone) {
        return customerRepository.existsByPhone(phone);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDiscountCard(String discountCard) {
        return customerRepository.existsByDiscountCard(discountCard);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPhoneInTenant(Long tenantId, String phone) {
        return customerRepository.existsByTenantIdAndPhone(tenantId, phone);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmailInTenant(Long tenantId, String email) {
        return customerRepository.existsByTenantIdAndEmail(tenantId, email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDiscountCardInTenant(Long tenantId, String discountCard) {
        return customerRepository.existsByTenantIdAndDiscountCard(tenantId, discountCard);
    }

    @Override
    public Customer assignDiscountCard(Long customerId, String discountCard) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Клиент с ID " + customerId + " не найден"));
        
        if (customerRepository.existsByDiscountCard(discountCard)) {
            throw new IllegalArgumentException("Дисконтная карта " + discountCard + " уже используется");
        }
        
        customer.setDiscountCard(discountCard);
        return customerRepository.save(customer);
    }

    @Override
    public Customer removeDiscountCard(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Клиент с ID " + customerId + " не найден"));
        
        customer.setDiscountCard(null);
        return customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerStats getCustomerStatsByTenant(Long tenantId) {
        long totalCustomers = getCustomerCountByTenant(tenantId);
        long customersWithDiscountCards = getCustomerCountWithDiscountCardsByTenant(tenantId);
        long customersWithoutDiscountCards = totalCustomers - customersWithDiscountCards;
        
        return new CustomerStats(totalCustomers, customersWithDiscountCards, customersWithoutDiscountCards);
    }

    /**
     * Валидация клиента
     * @param customer клиент для валидации
     */
    private void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Клиент не может быть null");
        }
        if (customer.getFullName() == null || customer.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя клиента обязательно");
        }
        if (customer.getTenantId() == null) {
            throw new IllegalArgumentException("ID магазина обязателен");
        }
    }
}
