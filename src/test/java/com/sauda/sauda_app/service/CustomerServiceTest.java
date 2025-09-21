package com.sauda.sauda_app.service;

import com.sauda.sauda_app.entity.Customer;
import com.sauda.sauda_app.repository.CustomerRepository;
import com.sauda.sauda_app.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setTenantId(1L);
        customer.setFullName("John Doe");
        customer.setPhone("+1234567890");
        customer.setEmail("john.doe@example.com");
        customer.setDiscountCard("DC123456");
    }

    @Test
    void createCustomer_ShouldCreateCustomer_WhenValidData() {
        // Given
        when(customerRepository.existsByPhone(anyString())).thenReturn(false);
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.existsByDiscountCard(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // When
        Customer result = customerService.createCustomer(customer);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("+1234567890", result.getPhone());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("DC123456", result.getDiscountCard());
        
        verify(customerRepository).existsByPhone("+1234567890");
        verify(customerRepository).existsByEmail("john.doe@example.com");
        verify(customerRepository).existsByDiscountCard("DC123456");
        verify(customerRepository).save(customer);
    }

    @Test
    void createCustomer_ShouldThrowException_WhenPhoneExists() {
        // Given
        when(customerRepository.existsByPhone(anyString())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(customer));
        
        assertEquals("Клиент с номером телефона +1234567890 уже существует", exception.getMessage());
        verify(customerRepository).existsByPhone("+1234567890");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void createCustomer_ShouldThrowException_WhenEmailExists() {
        // Given
        when(customerRepository.existsByPhone(anyString())).thenReturn(false);
        when(customerRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(customer));
        
        assertEquals("Клиент с email john.doe@example.com уже существует", exception.getMessage());
        verify(customerRepository).existsByPhone("+1234567890");
        verify(customerRepository).existsByEmail("john.doe@example.com");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void createCustomer_ShouldThrowException_WhenDiscountCardExists() {
        // Given
        when(customerRepository.existsByPhone(anyString())).thenReturn(false);
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.existsByDiscountCard(anyString())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(customer));
        
        assertEquals("Клиент с дисконтной картой DC123456 уже существует", exception.getMessage());
        verify(customerRepository).existsByPhone("+1234567890");
        verify(customerRepository).existsByEmail("john.doe@example.com");
        verify(customerRepository).existsByDiscountCard("DC123456");
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void createCustomer_ShouldThrowException_WhenFullNameIsEmpty() {
        // Given
        customer.setFullName("");

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(customer));
        
        assertEquals("Имя клиента обязательно", exception.getMessage());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void createCustomer_ShouldThrowException_WhenTenantIdIsNull() {
        // Given
        customer.setTenantId(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(customer));
        
        assertEquals("ID магазина обязателен", exception.getMessage());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void updateCustomer_ShouldUpdateCustomer_WhenValidData() {
        // Given
        Long customerId = 1L;
        Customer updatedCustomer = new Customer();
        updatedCustomer.setFullName("Jane Doe");
        updatedCustomer.setPhone("+9876543210");
        updatedCustomer.setEmail("jane.doe@example.com");
        updatedCustomer.setDiscountCard("DC654321");
        updatedCustomer.setTenantId(1L);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByPhone(anyString())).thenReturn(false);
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.existsByDiscountCard(anyString())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // When
        Customer result = customerService.updateCustomer(customerId, updatedCustomer);

        // Then
        assertNotNull(result);
        verify(customerRepository).findById(customerId);
        verify(customerRepository).existsByPhone("+9876543210");
        verify(customerRepository).existsByEmail("jane.doe@example.com");
        verify(customerRepository).existsByDiscountCard("DC654321");
        verify(customerRepository).save(customer);
    }

    @Test
    void updateCustomer_ShouldThrowException_WhenCustomerNotExists() {
        // Given
        Long customerId = 999L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.updateCustomer(customerId, customer));
        
        assertEquals("Клиент с ID 999 не найден", exception.getMessage());
        verify(customerRepository).findById(customerId);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void getCustomerById_ShouldReturnCustomer_WhenExists() {
        // Given
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // When
        Optional<Customer> result = customerService.getCustomerById(customerId);

        // Then
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getFullName());
        verify(customerRepository).findById(customerId);
    }

    @Test
    void getCustomerById_ShouldReturnEmpty_WhenNotExists() {
        // Given
        Long customerId = 999L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When
        Optional<Customer> result = customerService.getCustomerById(customerId);

        // Then
        assertFalse(result.isPresent());
        verify(customerRepository).findById(customerId);
    }

    @Test
    void getCustomerByPhone_ShouldReturnCustomer_WhenExists() {
        // Given
        String phone = "+1234567890";
        when(customerRepository.findByPhone(phone)).thenReturn(Optional.of(customer));

        // When
        Optional<Customer> result = customerService.getCustomerByPhone(phone);

        // Then
        assertTrue(result.isPresent());
        assertEquals(phone, result.get().getPhone());
        verify(customerRepository).findByPhone(phone);
    }

    @Test
    void getCustomerByEmail_ShouldReturnCustomer_WhenExists() {
        // Given
        String email = "john.doe@example.com";
        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));

        // When
        Optional<Customer> result = customerService.getCustomerByEmail(email);

        // Then
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(customerRepository).findByEmail(email);
    }

    @Test
    void getCustomerByDiscountCard_ShouldReturnCustomer_WhenExists() {
        // Given
        String discountCard = "DC123456";
        when(customerRepository.findByDiscountCard(discountCard)).thenReturn(Optional.of(customer));

        // When
        Optional<Customer> result = customerService.getCustomerByDiscountCard(discountCard);

        // Then
        assertTrue(result.isPresent());
        assertEquals(discountCard, result.get().getDiscountCard());
        verify(customerRepository).findByDiscountCard(discountCard);
    }

    @Test
    void deleteCustomer_ShouldDeleteCustomer_WhenExists() {
        // Given
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // When
        customerService.deleteCustomer(customerId);

        // Then
        verify(customerRepository).findById(customerId);
        verify(customerRepository).delete(customer);
    }

    @Test
    void deleteCustomer_ShouldThrowException_WhenNotExists() {
        // Given
        Long customerId = 999L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.deleteCustomer(customerId));
        
        assertEquals("Клиент с ID 999 не найден", exception.getMessage());
        verify(customerRepository).findById(customerId);
        verify(customerRepository, never()).delete(any(Customer.class));
    }

    @Test
    void assignDiscountCard_ShouldAssignCard_WhenValidData() {
        // Given
        Long customerId = 1L;
        String discountCard = "DC789012";
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByDiscountCard(discountCard)).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // When
        Customer result = customerService.assignDiscountCard(customerId, discountCard);

        // Then
        assertNotNull(result);
        verify(customerRepository).findById(customerId);
        verify(customerRepository).existsByDiscountCard(discountCard);
        verify(customerRepository).save(customer);
    }

    @Test
    void assignDiscountCard_ShouldThrowException_WhenCardExists() {
        // Given
        Long customerId = 1L;
        String discountCard = "DC789012";
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.existsByDiscountCard(discountCard)).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerService.assignDiscountCard(customerId, discountCard));
        
        assertEquals("Дисконтная карта DC789012 уже используется", exception.getMessage());
        verify(customerRepository).findById(customerId);
        verify(customerRepository).existsByDiscountCard(discountCard);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void removeDiscountCard_ShouldRemoveCard_WhenCustomerExists() {
        // Given
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // When
        Customer result = customerService.removeDiscountCard(customerId);

        // Then
        assertNotNull(result);
        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(customer);
    }

    @Test
    void getCustomerStatsByTenant_ShouldReturnStats_WhenValidTenant() {
        // Given
        Long tenantId = 1L;
        when(customerRepository.countByTenantId(tenantId.longValue())).thenReturn(100L);
        when(customerRepository.countByTenantIdAndDiscountCardIsNotNull(tenantId.longValue())).thenReturn(75L);

        // When
        CustomerService.CustomerStats result = customerService.getCustomerStatsByTenant(tenantId);

        // Then
        assertNotNull(result);
        assertEquals(100L, result.getTotalCustomers());
        assertEquals(75L, result.getCustomersWithDiscountCards());
        assertEquals(25L, result.getCustomersWithoutDiscountCards());
        assertEquals(75.0, result.getDiscountCardPercentage(), 0.01);
        
        verify(customerRepository).countByTenantId(tenantId.longValue());
        verify(customerRepository).countByTenantIdAndDiscountCardIsNotNull(tenantId.longValue());
    }

    @Test
    void existsByPhone_ShouldReturnTrue_WhenPhoneExists() {
        // Given
        String phone = "+1234567890";
        when(customerRepository.existsByPhone(phone)).thenReturn(true);

        // When
        boolean result = customerService.existsByPhone(phone);

        // Then
        assertTrue(result);
        verify(customerRepository).existsByPhone(phone);
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        // Given
        String email = "john.doe@example.com";
        when(customerRepository.existsByEmail(email)).thenReturn(true);

        // When
        boolean result = customerService.existsByEmail(email);

        // Then
        assertTrue(result);
        verify(customerRepository).existsByEmail(email);
    }

    @Test
    void existsByDiscountCard_ShouldReturnTrue_WhenCardExists() {
        // Given
        String discountCard = "DC123456";
        when(customerRepository.existsByDiscountCard(discountCard)).thenReturn(true);

        // When
        boolean result = customerService.existsByDiscountCard(discountCard);

        // Then
        assertTrue(result);
        verify(customerRepository).existsByDiscountCard(discountCard);
    }
}
