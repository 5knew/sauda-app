package com.sauda.sauda_app.service;

import com.sauda.sauda_app.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Сервис для управления клиентами
 * Предоставляет бизнес-логику для работы с клиентами
 */
public interface CustomerService {

    /**
     * Создать нового клиента
     * @param customer клиент для создания
     * @return созданный клиент
     */
    Customer createCustomer(Customer customer);

    /**
     * Обновить существующего клиента
     * @param id идентификатор клиента
     * @param customer клиент с обновленными данными
     * @return обновленный клиент
     */
    Customer updateCustomer(Long id, Customer customer);

    /**
     * Получить клиента по идентификатору
     * @param id идентификатор клиента
     * @return клиент или пустой Optional
     */
    Optional<Customer> getCustomerById(Long id);

    /**
     * Получить клиента по номеру телефона
     * @param phone номер телефона
     * @return клиент или пустой Optional
     */
    Optional<Customer> getCustomerByPhone(String phone);

    /**
     * Получить клиента по email
     * @param email email клиента
     * @return клиент или пустой Optional
     */
    Optional<Customer> getCustomerByEmail(String email);

    /**
     * Получить клиента по дисконтной карте
     * @param discountCard номер дисконтной карты
     * @return клиент или пустой Optional
     */
    Optional<Customer> getCustomerByDiscountCard(String discountCard);

    /**
     * Получить всех клиентов с пагинацией
     * @param pageable параметры пагинации
     * @return страница клиентов
     */
    Page<Customer> getAllCustomers(Pageable pageable);

    /**
     * Получить всех клиентов по магазину
     * @param tenantId идентификатор магазина
     * @param pageable параметры пагинации
     * @return страница клиентов магазина
     */
    Page<Customer> getCustomersByTenant(Long tenantId, Pageable pageable);

    /**
     * Поиск клиентов по имени
     * @param name имя клиента (частичное совпадение)
     * @param pageable параметры пагинации
     * @return страница найденных клиентов
     */
    Page<Customer> searchCustomersByName(String name, Pageable pageable);

    /**
     * Поиск клиентов по имени в рамках магазина
     * @param tenantId идентификатор магазина
     * @param name имя клиента (частичное совпадение)
     * @param pageable параметры пагинации
     * @return страница найденных клиентов
     */
    Page<Customer> searchCustomersByNameInTenant(Long tenantId, String name, Pageable pageable);

    /**
     * Поиск клиентов по номеру телефона
     * @param phone номер телефона (частичное совпадение)
     * @param pageable параметры пагинации
     * @return страница найденных клиентов
     */
    Page<Customer> searchCustomersByPhone(String phone, Pageable pageable);

    /**
     * Поиск клиентов по номеру телефона в рамках магазина
     * @param tenantId идентификатор магазина
     * @param phone номер телефона (частичное совпадение)
     * @param pageable параметры пагинации
     * @return страница найденных клиентов
     */
    Page<Customer> searchCustomersByPhoneInTenant(Long tenantId, String phone, Pageable pageable);

    /**
     * Получить клиентов с дисконтными картами
     * @param tenantId идентификатор магазина
     * @param pageable параметры пагинации
     * @return страница клиентов с дисконтными картами
     */
    Page<Customer> getCustomersWithDiscountCards(Long tenantId, Pageable pageable);

    /**
     * Получить клиентов без дисконтных карт
     * @param tenantId идентификатор магазина
     * @param pageable параметры пагинации
     * @return страница клиентов без дисконтных карт
     */
    Page<Customer> getCustomersWithoutDiscountCards(Long tenantId, Pageable pageable);

    /**
     * Удалить клиента
     * @param id идентификатор клиента
     */
    void deleteCustomer(Long id);

    /**
     * Получить количество клиентов по магазину
     * @param tenantId идентификатор магазина
     * @return количество клиентов
     */
    long getCustomerCountByTenant(Long tenantId);

    /**
     * Получить количество клиентов с дисконтными картами по магазину
     * @param tenantId идентификатор магазина
     * @return количество клиентов с дисконтными картами
     */
    long getCustomerCountWithDiscountCardsByTenant(Long tenantId);

    /**
     * Проверить существование клиента по номеру телефона
     * @param phone номер телефона
     * @return true если клиент существует
     */
    boolean existsByPhone(String phone);

    /**
     * Проверить существование клиента по email
     * @param email email клиента
     * @return true если клиент существует
     */
    boolean existsByEmail(String email);

    /**
     * Проверить существование клиента по дисконтной карте
     * @param discountCard номер дисконтной карты
     * @return true если клиент существует
     */
    boolean existsByDiscountCard(String discountCard);

    /**
     * Проверить существование клиента по номеру телефона в рамках магазина
     * @param tenantId идентификатор магазина
     * @param phone номер телефона
     * @return true если клиент существует
     */
    boolean existsByPhoneInTenant(Long tenantId, String phone);

    /**
     * Проверить существование клиента по email в рамках магазина
     * @param tenantId идентификатор магазина
     * @param email email клиента
     * @return true если клиент существует
     */
    boolean existsByEmailInTenant(Long tenantId, String email);

    /**
     * Проверить существование клиента по дисконтной карте в рамках магазина
     * @param tenantId идентификатор магазина
     * @param discountCard номер дисконтной карты
     * @return true если клиент существует
     */
    boolean existsByDiscountCardInTenant(Long tenantId, String discountCard);

    /**
     * Назначить дисконтную карту клиенту
     * @param customerId идентификатор клиента
     * @param discountCard номер дисконтной карты
     * @return обновленный клиент
     */
    Customer assignDiscountCard(Long customerId, String discountCard);

    /**
     * Удалить дисконтную карту у клиента
     * @param customerId идентификатор клиента
     * @return обновленный клиент
     */
    Customer removeDiscountCard(Long customerId);

    /**
     * Получить статистику клиентов по магазину
     * @param tenantId идентификатор магазина
     * @return статистика клиентов
     */
    CustomerStats getCustomerStatsByTenant(Long tenantId);

    /**
     * Класс для статистики клиентов
     */
    class CustomerStats {
        private long totalCustomers;
        private long customersWithDiscountCards;
        private long customersWithoutDiscountCards;
        private double discountCardPercentage;

        // Конструкторы, геттеры и сеттеры
        public CustomerStats() {}

        public CustomerStats(long totalCustomers, long customersWithDiscountCards, long customersWithoutDiscountCards) {
            this.totalCustomers = totalCustomers;
            this.customersWithDiscountCards = customersWithDiscountCards;
            this.customersWithoutDiscountCards = customersWithoutDiscountCards;
            this.discountCardPercentage = totalCustomers > 0 ? (double) customersWithDiscountCards / totalCustomers * 100 : 0;
        }

        public long getTotalCustomers() { return totalCustomers; }
        public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }

        public long getCustomersWithDiscountCards() { return customersWithDiscountCards; }
        public void setCustomersWithDiscountCards(long customersWithDiscountCards) { this.customersWithDiscountCards = customersWithDiscountCards; }

        public long getCustomersWithoutDiscountCards() { return customersWithoutDiscountCards; }
        public void setCustomersWithoutDiscountCards(long customersWithoutDiscountCards) { this.customersWithoutDiscountCards = customersWithoutDiscountCards; }

        public double getDiscountCardPercentage() { return discountCardPercentage; }
        public void setDiscountCardPercentage(double discountCardPercentage) { this.discountCardPercentage = discountCardPercentage; }
    }
}
