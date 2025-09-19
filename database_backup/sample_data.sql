-- Пример данных для тестирования системы Sauda-DB
-- Выполните этот скрипт после создания основной схемы

-- Вставка тестовых данных для tenant_id = 1

-- Создание магазина
INSERT INTO Shops (tenant_id, name, created_at) VALUES 
(1, 'Магазин "Электроника"', CURRENT_TIMESTAMP);

-- Создание категорий товаров
INSERT INTO ProductCategories (tenant_id, name, parent_id) VALUES 
(1, 'Электроника', NULL),
(1, 'Компьютеры', 1),
(1, 'Смартфоны', 1),
(1, 'Аксессуары', 1);

-- Создание единиц измерения
INSERT INTO Units (tenant_id, name, symbol) VALUES 
(1, 'Штука', 'шт'),
(1, 'Комплект', 'компл'),
(1, 'Метр', 'м'),
(1, 'Килограмм', 'кг');

-- Создание атрибутов товаров
INSERT INTO ProductAttributes (tenant_id, name, data_type) VALUES 
(1, 'Цвет', 'string'),
(1, 'Размер экрана', 'number'),
(1, 'Объем памяти', 'number'),
(1, 'Гарантия', 'number'),
(1, 'Беспроводная зарядка', 'boolean');

-- Создание сотрудников
INSERT INTO Employees (tenant_id, full_name, position, hired_date, is_active) VALUES 
(1, 'Иванов Иван Иванович', 'Продавец-консультант', '2024-01-15', true),
(1, 'Петрова Анна Сергеевна', 'Кассир', '2024-02-01', true),
(1, 'Сидоров Петр Александрович', 'Менеджер', '2023-12-01', true);

-- Создание ролей
INSERT INTO Roles (tenant_id, name, permissions_json) VALUES 
(1, 'Администратор', '{"all": true}'),
(1, 'Менеджер', '{"products": true, "sales": true, "inventory": true}'),
(1, 'Продавец', '{"sales": true, "customers": true}'),
(1, 'Кассир', '{"sales": true}');

-- Создание пользователей (пароли: password123)
INSERT INTO Users (tenant_id, username, password_hash, employee_id, role_id, shop_id) VALUES 
(1, 'admin', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8QZ8K2', 3, 1, 1),
(1, 'manager', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8QZ8K2', 3, 2, 1),
(1, 'seller1', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8QZ8K2', 1, 3, 1),
(1, 'cashier1', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8QZ8K2', 2, 4, 1);

-- Обновление owner_id в таблице Shops
UPDATE Shops SET owner_id = 1 WHERE tenant_id = 1;

-- Создание складов
INSERT INTO Warehouses (tenant_id, name, location) VALUES 
(1, 'Основной склад', 'ул. Промышленная, 15'),
(1, 'Склад аксессуаров', 'ул. Промышленная, 15, корпус 2');

-- Создание поставщиков
INSERT INTO Suppliers (tenant_id, name, contact_person, phone, email, address, is_active) VALUES 
(1, 'ООО "Электро-Снаб"', 'Смирнов А.В.', '+7-495-123-45-67', 'info@electro-supply.ru', 'г. Москва, ул. Торговая, 10', true),
(1, 'ИП "Гаджеты-Мир"', 'Козлов И.П.', '+7-495-987-65-43', 'sales@gadgets-world.ru', 'г. Москва, ул. Техническая, 5', true);

-- Создание товаров
INSERT INTO Products (tenant_id, name, barcode, sku, category_id, unit_id, description, is_active) VALUES 
(1, 'iPhone 15 Pro', '1234567890123', 'IPH15PRO-256', 3, 1, 'Смартфон Apple iPhone 15 Pro 256GB', true),
(1, 'MacBook Air M2', '2345678901234', 'MBA-M2-512', 2, 1, 'Ноутбук Apple MacBook Air 13" M2 512GB', true),
(1, 'Samsung Galaxy S24', '3456789012345', 'SGS24-128', 3, 1, 'Смартфон Samsung Galaxy S24 128GB', true),
(1, 'Чехол для iPhone', '4567890123456', 'CASE-IPH15', 4, 1, 'Силиконовый чехол для iPhone 15', true),
(1, 'Кабель USB-C', '5678901234567', 'CABLE-USBC-2M', 4, 1, 'Кабель USB-C 2 метра', true);

-- Создание значений атрибутов товаров
INSERT INTO ProductAttributeValues (product_id, attribute_id, value_string, value_number, value_boolean) VALUES 
(1, 1, 'Титан', NULL, NULL),
(1, 2, NULL, 6.1, NULL),
(1, 3, NULL, 256, NULL),
(1, 4, NULL, 12, NULL),
(1, 5, NULL, NULL, true),
(2, 1, 'Серебристый', NULL, NULL),
(2, 2, NULL, 13.6, NULL),
(2, 3, NULL, 512, NULL),
(2, 4, NULL, 12, NULL),
(2, 5, NULL, NULL, false),
(3, 1, 'Черный', NULL, NULL),
(3, 2, NULL, 6.2, NULL),
(3, 3, NULL, 128, NULL),
(3, 4, NULL, 24, NULL),
(3, 5, NULL, NULL, true);

-- Создание остатков на складах
INSERT INTO Inventory (tenant_id, product_id, warehouse_id, quantity, last_updated) VALUES 
(1, 1, 1, 5, CURRENT_TIMESTAMP),
(1, 2, 1, 3, CURRENT_TIMESTAMP),
(1, 3, 1, 8, CURRENT_TIMESTAMP),
(1, 4, 2, 20, CURRENT_TIMESTAMP),
(1, 5, 2, 50, CURRENT_TIMESTAMP);

-- Создание клиентов
INSERT INTO Customers (tenant_id, full_name, phone, email, discount_card) VALUES 
(1, 'Клиент Тестовый', '+7-999-123-45-67', 'test@example.com', 'DC001'),
(1, 'Покупатель Обычный', '+7-999-987-65-43', 'buyer@example.com', 'DC002');

-- Создание кассовых терминалов
INSERT INTO POSTerminals (tenant_id, name, location, is_online) VALUES 
(1, 'Касса №1', 'Торговый зал, левая сторона', true),
(1, 'Касса №2', 'Торговый зал, правая сторона', true);

-- Создание прайс-листа
INSERT INTO PriceList (tenant_id, product_id, price, start_date, end_date) VALUES 
(1, 1, 89990.00, '2024-01-01', NULL),
(1, 2, 129990.00, '2024-01-01', NULL),
(1, 3, 69990.00, '2024-01-01', NULL),
(1, 4, 2990.00, '2024-01-01', NULL),
(1, 5, 1990.00, '2024-01-01', NULL);

-- Создание правил скидок
INSERT INTO DiscountRules (tenant_id, description, discount_type, value, start_date, end_date, applies_to) VALUES 
(1, 'Скидка 10% на все аксессуары', 'percentage', 10, '2024-01-01', '2024-12-31', 'category'),
(1, 'Скидка 5000 руб при покупке от 100000 руб', 'fixed', 5000, '2024-01-01', '2024-12-31', 'total');

-- Создание тестовых продаж
INSERT INTO Sales (tenant_id, sale_date, employee_id, customer_id, pos_terminal_id, total_amount, payment_method) VALUES 
(1, '2024-01-15 14:30:00', 1, 1, 1, 92990.00, 'card'),
(1, '2024-01-15 15:45:00', 2, 2, 2, 1990.00, 'cash'),
(1, '2024-01-16 10:20:00', 1, NULL, 1, 129990.00, 'card');

-- Создание позиций продаж
INSERT INTO SalesItems (tenant_id, sale_id, product_id, quantity, price, discount) VALUES 
(1, 1, 1, 1, 89990.00, 0),
(1, 1, 4, 1, 2990.00, 0),
(1, 2, 5, 1, 1990.00, 0),
(1, 3, 2, 1, 129990.00, 0);

-- Создание оплат
INSERT INTO Payments (tenant_id, sale_id, amount, payment_type, payment_date) VALUES 
(1, 1, 92990.00, 'card', '2024-01-15 14:30:00'),
(1, 2, 1990.00, 'cash', '2024-01-15 15:45:00'),
(1, 3, 129990.00, 'card', '2024-01-16 10:20:00');

-- Создание заказов на поставку
INSERT INTO PurchaseOrders (tenant_id, supplier_id, warehouse_id, order_date, total_amount, status) VALUES 
(1, 1, 1, '2024-01-10', 500000.00, 'delivered'),
(1, 2, 2, '2024-01-12', 50000.00, 'pending');

-- Создание позиций заказов на поставку
INSERT INTO PurchaseOrderItems (tenant_id, purchase_order_id, product_id, quantity, price) VALUES 
(1, 1, 1, 10, 80000.00),
(1, 1, 2, 5, 110000.00),
(1, 2, 4, 50, 2000.00),
(1, 2, 5, 100, 1000.00);

-- Вывод информации о созданных данных
SELECT 'Магазины' as table_name, COUNT(*) as records FROM Shops WHERE tenant_id = 1
UNION ALL
SELECT 'Товары', COUNT(*) FROM Products WHERE tenant_id = 1
UNION ALL
SELECT 'Сотрудники', COUNT(*) FROM Employees WHERE tenant_id = 1
UNION ALL
SELECT 'Пользователи', COUNT(*) FROM Users WHERE tenant_id = 1
UNION ALL
SELECT 'Клиенты', COUNT(*) FROM Customers WHERE tenant_id = 1
UNION ALL
SELECT 'Продажи', COUNT(*) FROM Sales WHERE tenant_id = 1
UNION ALL
SELECT 'Остатки', COUNT(*) FROM Inventory WHERE tenant_id = 1;
