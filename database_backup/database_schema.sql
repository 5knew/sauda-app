-- PostgreSQL Database Schema for Sauda-DB
-- Multi-tenant retail management system

-- Создание базы данных (если нужно)
-- CREATE DATABASE sauda_db;

-- Подключение к базе данных
-- \c sauda_db;

-- Таблица поставщиков (добавлена для корректной работы схемы)
CREATE TABLE Suppliers (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    name VARCHAR(255) NOT NULL,
    contact_person VARCHAR(255),
    phone VARCHAR(50),
    email VARCHAR(255),
    address TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    UNIQUE (tenant_id, name)  -- Уникальность поставщиков в рамках магазина
);

-- Таблица магазинов (шопов)
CREATE TABLE Shops (
    id SERIAL PRIMARY KEY,
    tenant_id INT UNIQUE NOT NULL,  -- tenant_id для мультитенантности
    name VARCHAR(255) NOT NULL,
    owner_id INT,  -- Будет добавлен после создания таблицы Users
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица категорий товаров
CREATE TABLE ProductCategories (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    name VARCHAR(255) NOT NULL,
    parent_id INT REFERENCES ProductCategories(id) ON DELETE SET NULL,
    UNIQUE (tenant_id, name)  -- Уникальность категории в рамках магазина
);

-- Таблица единиц измерения
CREATE TABLE Units (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    name VARCHAR(255) NOT NULL,
    symbol VARCHAR(50) NOT NULL,
    UNIQUE (tenant_id, name)  -- Уникальность единицы измерения в рамках магазина
);

-- Таблица товаров
CREATE TABLE Products (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    name VARCHAR(255) NOT NULL,
    barcode VARCHAR(50) UNIQUE NOT NULL,
    sku VARCHAR(50) UNIQUE NOT NULL,
    category_id INT REFERENCES ProductCategories(id) ON DELETE SET NULL,
    unit_id INT REFERENCES Units(id) ON DELETE SET NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    is_deleted BOOLEAN DEFAULT FALSE,
    UNIQUE (tenant_id, sku)  -- Уникальность артикула в рамках магазина
);

-- Таблица атрибутов товаров (гибкие характеристики)
CREATE TABLE ProductAttributes (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    name VARCHAR(255) NOT NULL,
    data_type VARCHAR(50) NOT NULL
);

-- Таблица значений атрибутов товаров
CREATE TABLE ProductAttributeValues (
    id SERIAL PRIMARY KEY,
    product_id INT REFERENCES Products(id) ON DELETE CASCADE,
    attribute_id INT REFERENCES ProductAttributes(id) ON DELETE CASCADE,
    value_string TEXT,
    value_number NUMERIC,
    value_boolean BOOLEAN
);

-- Таблица сотрудников
CREATE TABLE Employees (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    full_name VARCHAR(255) NOT NULL,
    position VARCHAR(255) NOT NULL,
    hired_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    UNIQUE (tenant_id, full_name)  -- Уникальность сотрудников в рамках магазина
);

-- Таблица ролей пользователей
CREATE TABLE Roles (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    name VARCHAR(50) NOT NULL,
    permissions_json JSONB,
    UNIQUE (tenant_id, name)  -- Уникальность ролей в рамках магазина
);

-- Таблица пользователей
CREATE TABLE Users (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    employee_id INT REFERENCES Employees(id) ON DELETE SET NULL,
    role_id INT REFERENCES Roles(id) ON DELETE SET NULL,
    shop_id INT REFERENCES Shops(id) ON DELETE CASCADE,  -- Магазин, к которому привязан пользователь
    UNIQUE (tenant_id, username)  -- Уникальность пользователей в рамках магазина
);

-- Добавление внешнего ключа для owner_id в таблице Shops
ALTER TABLE Shops ADD CONSTRAINT fk_shops_owner 
    FOREIGN KEY (owner_id) REFERENCES Users(id) ON DELETE SET NULL;

-- Таблица прав пользователей на доступ к магазинам
CREATE TABLE UserShopRoles (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES Users(id) ON DELETE CASCADE,
    shop_id INT REFERENCES Shops(id) ON DELETE CASCADE,
    role_id INT REFERENCES Roles(id) ON DELETE SET NULL,  -- Роль в рамках магазина
    UNIQUE (user_id, shop_id)  -- Один пользователь может быть связан только с одним магазином
);

-- Таблица складов
CREATE TABLE Warehouses (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255)
);

-- Таблица остатков товаров на складах
CREATE TABLE Inventory (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    product_id INT REFERENCES Products(id) ON DELETE CASCADE,
    warehouse_id INT REFERENCES Warehouses(id) ON DELETE CASCADE,
    quantity NUMERIC DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (tenant_id, product_id, warehouse_id)  -- Уникальность остатков в рамках магазина
);

-- Таблица заказов на поступление товаров
CREATE TABLE PurchaseOrders (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    supplier_id INT REFERENCES Suppliers(id) ON DELETE SET NULL,
    warehouse_id INT REFERENCES Warehouses(id) ON DELETE SET NULL,
    order_date DATE NOT NULL,
    total_amount NUMERIC NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- Таблица позиций в заказах на поступление
CREATE TABLE PurchaseOrderItems (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    purchase_order_id INT REFERENCES PurchaseOrders(id) ON DELETE CASCADE,
    product_id INT REFERENCES Products(id) ON DELETE CASCADE,
    quantity NUMERIC NOT NULL,
    price NUMERIC NOT NULL
);

-- Таблица терминалов для касс
CREATE TABLE POSTerminals (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    is_online BOOLEAN DEFAULT TRUE,
    UNIQUE (tenant_id, name)  -- Уникальность терминалов в рамках магазина
);

-- Таблица клиентов
CREATE TABLE Customers (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    email VARCHAR(255),
    discount_card VARCHAR(50) UNIQUE,
    UNIQUE (tenant_id, discount_card)  -- Уникальность дисконтных карт в рамках магазина
);

-- Таблица продаж
CREATE TABLE Sales (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    employee_id INT REFERENCES Employees(id) ON DELETE SET NULL,
    customer_id INT REFERENCES Customers(id) ON DELETE SET NULL,
    pos_terminal_id INT REFERENCES POSTerminals(id) ON DELETE SET NULL,
    total_amount NUMERIC NOT NULL,
    payment_method VARCHAR(50)
);

-- Таблица позиций в продажах
CREATE TABLE SalesItems (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    sale_id INT REFERENCES Sales(id) ON DELETE CASCADE,
    product_id INT REFERENCES Products(id) ON DELETE CASCADE,
    quantity NUMERIC NOT NULL,
    price NUMERIC NOT NULL,
    discount NUMERIC,
    UNIQUE (tenant_id, sale_id, product_id)  -- Уникальность позиций в рамках магазина
);

-- Таблица оплат
CREATE TABLE Payments (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    sale_id INT REFERENCES Sales(id) ON DELETE CASCADE,
    amount NUMERIC NOT NULL,
    payment_type VARCHAR(50),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица цен на товары
CREATE TABLE PriceList (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    product_id INT REFERENCES Products(id) ON DELETE CASCADE,
    price NUMERIC NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    UNIQUE (tenant_id, product_id, start_date)  -- Уникальность ценового листа в рамках магазина
);

-- Таблица правил скидок
CREATE TABLE DiscountRules (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    description TEXT,
    discount_type VARCHAR(50) NOT NULL,
    value NUMERIC NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    applies_to VARCHAR(50) -- Применяется к товарам, категориям, группам клиентов
);

-- Таблица аудита операций
CREATE TABLE AuditLog (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    user_id INT REFERENCES Users(id) ON DELETE CASCADE,
    action TEXT NOT NULL,
    object_type VARCHAR(255) NOT NULL,
    object_id INT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    details_json JSONB
);

-- Таблица истории товаров
CREATE TABLE ProductHistory (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    product_id INT REFERENCES Products(id) ON DELETE CASCADE,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    old_value JSONB,
    new_value JSONB,
    changed_by INT REFERENCES Users(id) ON DELETE SET NULL
);

-- Таблица ошибок
CREATE TABLE ErrorLog (
    id SERIAL PRIMARY KEY,
    tenant_id INT NOT NULL,  -- Идентификатор магазина
    error_message TEXT NOT NULL,
    error_type VARCHAR(50) NOT NULL,
    occurred_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Создание индексов для улучшения производительности
CREATE INDEX idx_products_tenant_id ON Products(tenant_id);
CREATE INDEX idx_products_barcode ON Products(barcode);
CREATE INDEX idx_products_sku ON Products(sku);
CREATE INDEX idx_products_category_id ON Products(category_id);
CREATE INDEX idx_products_is_active ON Products(is_active);

CREATE INDEX idx_sales_tenant_id ON Sales(tenant_id);
CREATE INDEX idx_sales_sale_date ON Sales(sale_date);
CREATE INDEX idx_sales_employee_id ON Sales(employee_id);
CREATE INDEX idx_sales_customer_id ON Sales(customer_id);

CREATE INDEX idx_inventory_tenant_id ON Inventory(tenant_id);
CREATE INDEX idx_inventory_product_id ON Inventory(product_id);
CREATE INDEX idx_inventory_warehouse_id ON Inventory(warehouse_id);

CREATE INDEX idx_audit_log_tenant_id ON AuditLog(tenant_id);
CREATE INDEX idx_audit_log_user_id ON AuditLog(user_id);
CREATE INDEX idx_audit_log_timestamp ON AuditLog(timestamp);

CREATE INDEX idx_product_history_tenant_id ON ProductHistory(tenant_id);
CREATE INDEX idx_product_history_product_id ON ProductHistory(product_id);
CREATE INDEX idx_product_history_changed_at ON ProductHistory(changed_at);

-- Комментарии к таблицам
COMMENT ON TABLE Shops IS 'Таблица магазинов с поддержкой мультитенантности';
COMMENT ON TABLE Products IS 'Основная таблица товаров';
COMMENT ON TABLE Sales IS 'Таблица продаж';
COMMENT ON TABLE Inventory IS 'Остатки товаров на складах';
COMMENT ON TABLE AuditLog IS 'Журнал аудита всех операций';
COMMENT ON TABLE ProductHistory IS 'История изменений товаров';
