# Sauda-DB Retail Management System

Полнофункциональная система управления розничной торговлей с интеграцией Keycloak для аутентификации и авторизации.

## 🚀 Технологический стек

- **Backend**: Spring Boot 3.5.6
- **Database**: PostgreSQL 15
- **Authentication**: Keycloak 24.0
- **ORM**: Hibernate/JPA
- **Migration**: Liquibase
- **Containerization**: Docker Compose
- **Build Tool**: Maven

## 📋 Функциональность

### 🔐 Система ролей и прав доступа

| Роль | Описание | Права доступа |
|------|----------|---------------|
| **USER** | Обычный пользователь | Просмотр товаров |
| **CASHIER** | Кассир | Просмотр товаров + управление продажами |
| **INVENTORY_MANAGER** | Менеджер склада | Управление инвентарем + просмотр товаров |
| **SALES_MANAGER** | Менеджер продаж | Управление клиентами + просмотр товаров |
| **MANAGER** | Менеджер | Управление магазинами + просмотр отчетов |
| **ADMIN** | Администратор | Полный доступ ко всем операциям |

### 🛠️ API Endpoints

#### Публичные endpoints (без авторизации)
- `GET /api/public/health` - Проверка состояния системы
- `GET /api/public/info` - Информация о системе

#### Товары (`/api/products`)
- `GET /api/products` - Получить список товаров (USER+)
- `POST /api/products` - Создать товар (INVENTORY_MANAGER+)
- `PUT /api/products/{id}` - Обновить товар (INVENTORY_MANAGER+)
- `DELETE /api/products/{id}` - Удалить товар (MANAGER+)

#### Продажи (`/api/sales`)
- `GET /api/sales` - Получить данные о продажах (CASHIER+)
- `POST /api/sales` - Записать продажу (CASHIER+)

#### Инвентарь (`/api/inventory`)
- `GET /api/inventory` - Получить данные об инвентаре (INVENTORY_MANAGER+)
- `POST /api/inventory/stock-update` - Обновить остатки (INVENTORY_MANAGER+)

#### Клиенты (`/api/customers`)
- `GET /api/customers` - Получить список клиентов (SALES_MANAGER+)
- `POST /api/customers` - Добавить клиента (SALES_MANAGER+)

#### Отчеты (`/api/reports`)
- `GET /api/reports/sales-summary` - Отчет по продажам (MANAGER+)
- `GET /api/reports/inventory-value` - Отчет по стоимости инвентаря (MANAGER+)

#### Магазины (`/api/shops`)
- `GET /api/shops` - Получить список магазинов (MANAGER+)
- `POST /api/shops` - Создать магазин (ADMIN)
- `PUT /api/shops/{id}` - Обновить магазин (ADMIN)
- `DELETE /api/shops/{id}` - Удалить магазин (ADMIN)

#### Отладка (`/api/debug`)
- `GET /api/debug/user` - Информация о текущем пользователе (без авторизации)

## 🚀 Быстрый старт

### 1. Запуск инфраструктуры

```bash
# Запуск PostgreSQL и Keycloak
docker-compose up -d

# Ожидание запуска сервисов
Start-Sleep -Seconds 30
```

### 2. Настройка Keycloak

```powershell
# Настройка realm, клиента и пользователей
.\setup-keycloak.ps1

# Или обновление ролей (если realm уже существует)
.\update-keycloak-roles.ps1
```

### 3. Запуск приложения

```bash
# Сборка и запуск Spring Boot приложения
./mvnw spring-boot:run
```

### 4. Проверка работы

```bash
# Проверка состояния системы
curl http://localhost:8080/api/public/health

# Получение токена для тестирования
$tokenBody = "username=cashier&password=cashier123&grant_type=password&client_id=sauda-app&client_secret=sauda-secret&scope=openid profile email"
$tokenResponse = Invoke-RestMethod -Uri "http://localhost:8081/realms/sauda-realm/protocol/openid-connect/token" -Method Post -Body $tokenBody -ContentType "application/x-www-form-urlencoded"
$token = $tokenResponse.access_token

# Тестирование API с авторизацией
$headers = @{"Authorization" = "Bearer $token"}
Invoke-RestMethod -Uri "http://localhost:8080/api/products?tenantId=1" -Headers $headers
```

## 👥 Тестовые пользователи

| Пользователь | Пароль | Роли |
|-------------|--------|------|
| admin | admin123 | ADMIN, USER |
| manager | manager123 | MANAGER, USER |
| cashier | cashier123 | CASHIER, USER |
| inventory | inventory123 | INVENTORY_MANAGER, USER |
| sales | sales123 | SALES_MANAGER, USER |
| user | user123 | USER |

## 🗄️ База данных

### Схема базы данных
- **Multi-tenant архитектура** с поддержкой `tenant_id`
- **Полная схема** для розничной торговли:
  - Товары и категории
  - Поставщики и единицы измерения
  - Магазины и сотрудники
  - Клиенты и продажи
  - Инвентарь и аудит

### Миграции
- **Liquibase** для управления схемой БД
- **Автоматическое создание** таблиц при запуске
- **Тестовые данные** для демонстрации

## 🔧 Конфигурация

### application.properties
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/sauda_db
spring.datasource.username=postgres
spring.datasource.password=secret

# Keycloak
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8081/realms/sauda-realm
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8081/realms/sauda-realm/protocol/openid-connect/certs
```

### Docker Compose
- **PostgreSQL**: порт 5432
- **Keycloak**: порт 8081
- **Spring Boot**: порт 8080

## 🛡️ Безопасность

- **JWT токены** для аутентификации
- **Role-based access control (RBAC)** для авторизации
- **Кастомный JWT конвертер** для извлечения ролей из Keycloak
- **CORS** настроен для всех origins
- **CSRF** отключен для API

## 📝 Логирование

- **Hibernate SQL** логи включены для отладки
- **Spring Security** логи для мониторинга авторизации
- **Liquibase** логи для отслеживания миграций

## 🐛 Отладка

### Проверка авторизации
```bash
# Получение информации о текущем пользователе
curl http://localhost:8080/api/debug/user
```

### Проверка ролей в JWT
```bash
# Декодирование JWT токена (требует jq)
echo "YOUR_JWT_TOKEN" | jq -R 'split(".") | .[1] | @base64d | fromjson'
```

## 📚 Дополнительные ресурсы

- [Spring Boot Security](https://spring.io/guides/gs/securing-web/)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Liquibase Documentation](https://www.liquibase.org/documentation)

## 🤝 Вклад в проект

1. Fork репозитория
2. Создайте feature branch
3. Внесите изменения
4. Создайте Pull Request

## 📄 Лицензия

Этот проект распространяется под лицензией MIT.