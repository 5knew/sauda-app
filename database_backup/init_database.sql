-- Скрипт инициализации базы данных Sauda-DB
-- Выполните этот скрипт для создания базы данных и всех необходимых таблиц

-- Создание базы данных
CREATE DATABASE sauda_db
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'ru_RU.UTF-8'
    LC_CTYPE = 'ru_RU.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Подключение к созданной базе данных
\c sauda_db;

-- Включение расширений PostgreSQL
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Выполнение основной схемы базы данных
\i database_schema.sql

-- Создание пользователя для приложения (опционально)
-- CREATE USER sauda_user WITH PASSWORD 'your_secure_password';
-- GRANT ALL PRIVILEGES ON DATABASE sauda_db TO sauda_user;
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO sauda_user;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO sauda_user;

-- Вывод информации о созданных таблицах
SELECT 
    schemaname,
    tablename,
    tableowner
FROM pg_tables 
WHERE schemaname = 'public'
ORDER BY tablename;
