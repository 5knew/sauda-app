# Sauda-DB Retail Management System

Система управления розничной торговлей с поддержкой мультитенантности.

## Описание

Sauda-DB - это комплексная система управления розничной торговлей, построенная на Spring Boot и PostgreSQL. Система поддерживает:

- Мультитенантность (несколько магазинов)
- Управление товарами и категориями
- Управление складскими остатками
- Продажи и кассовые операции
- Управление клиентами
- Отчетность и аудит
- Интеграция с Keycloak для аутентификации

## Технологический стек

- **Backend**: Spring Boot 3.5.6, Java 21
- **База данных**: PostgreSQL 15
- **ORM**: Spring Data JPA, Hibernate
- **Миграции**: Liquibase
- **Контейнеризация**: Docker Compose
- **Аутентификация**: Keycloak (планируется)

## Структура проекта

```
sauda-app/
├── src/main/java/com/sauda/sauda_app/
│   ├── entity/          # JPA сущности
│   ├── repository/      # Репозитории для работы с БД
│   ├── controller/      # REST контроллеры
│   └── service/         # Бизнес-логика (планируется)
├── src/main/resources/
│   ├── db/changelog/    # Liquibase миграции
│   └── application.properties
├── database/            # SQL скрипты для инициализации
└── compose.yaml         # Docker Compose конфигурация
```

## Быстрый старт

### 1. Запуск базы данных

```bash
# Запуск PostgreSQL через Docker Compose
docker-compose up -d postgres
```

### 2. Запуск приложения

```bash
# Сборка и запуск приложения
./mvnw spring-boot:run
```

Приложение будет доступно по адресу: http://localhost:8080

### 3. Проверка работы

```bash
# Получить список товаров
curl "http://localhost:8080/api/products?tenantId=1"

# Получить информацию о магазине
curl "http://localhost:8080/api/shops/tenant/1"
```

## API Endpoints

### Товары
- `GET /api/products` - Получить все товары
- `GET /api/products/{id}` - Получить товар по ID
- `GET /api/products/search?searchTerm=...` - Поиск товаров
- `GET /api/products/category/{categoryId}` - Товары по категории
- `POST /api/products` - Создать товар
- `PUT /api/products/{id}` - Обновить товар
- `DELETE /api/products/{id}` - Удалить товар

### Магазины
- `GET /api/shops` - Получить все магазины
- `GET /api/shops/{id}` - Получить магазин по ID
- `GET /api/shops/tenant/{tenantId}` - Получить магазин по tenant ID
- `POST /api/shops` - Создать магазин
- `PUT /api/shops/{id}` - Обновить магазин

## База данных

### Структура основных таблиц

- **shops** - Магазины (мультитенантность)
- **products** - Товары
- **product_categories** - Категории товаров
- **inventory** - Складские остатки
- **sales** - Продажи
- **customers** - Клиенты
- **employees** - Сотрудники
- **users** - Пользователи системы

### Тестовые данные

В системе уже загружены тестовые данные для tenant_id = 1:
- Магазин "Электроника"
- Категории: Электроника, Компьютеры, Смартфоны, Аксессуары
- Товары: iPhone 15 Pro, MacBook Air M2, Samsung Galaxy S24, и др.
- Сотрудники и пользователи
- Складские остатки

## Конфигурация

### application.properties

```properties
# База данных
spring.datasource.url=jdbc:postgresql://localhost:5432/sauda_db
spring.datasource.username=postgres
spring.datasource.password=secret

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true

# Liquibase
spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.xml
```

### Docker Compose

```yaml
services:
  postgres:
    image: 'postgres:15'
    environment:
      - 'POSTGRES_DB=sauda_db'
      - 'POSTGRES_PASSWORD=secret'
      - 'POSTGRES_USER=postgres'
    ports:
      - '5432:5432'
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./database:/docker-entrypoint-initdb.d
```

## Разработка

### Добавление новых сущностей

1. Создайте JPA сущность в пакете `entity`
2. Создайте репозиторий в пакете `repository`
3. Создайте контроллер в пакете `controller`
4. Добавьте миграцию Liquibase в `db/changelog`

### Миграции базы данных

Все изменения схемы БД должны выполняться через Liquibase:

1. Создайте новый файл в `src/main/resources/db/changelog/`
2. Добавьте ссылку в `db.changelog-master.xml`
3. Перезапустите приложение

## Планы развития

- [ ] Интеграция с Keycloak
- [ ] Сервисный слой для бизнес-логики
- [ ] Расширенная отчетность
- [ ] WebSocket для real-time уведомлений
- [ ] Frontend интерфейс
- [ ] Мобильное приложение

## Лицензия

MIT License


