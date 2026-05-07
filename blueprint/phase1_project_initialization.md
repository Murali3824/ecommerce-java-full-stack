# Phase 1: Project Initialization Blueprint

This blueprint covers the full enterprise architecture, package layout, dependency configuration, and infrastructure containerization for the Smart E-Commerce Platform.

---

## 1. High-Level Architecture

The platform follows a modern, stateless, three-tier architecture with dedicated caching, object storage, and payment gateway layers. It is designed to handle high concurrency, ensure secure stateless session management, and support horizontal scaling.

```mermaid
graph TD
    %% Clients
    subgraph Client Layer
        WebClient["React SPA (Web Client)"]
        MobileClient["Mobile Client (Optional)"]
    end

    %% Gateway & Security
    subgraph API Gateway / Security Layer
        SpringSecurity["Spring Security & Filter Chain"]
        RateLimiter["Redis Rate Limiter (Token Bucket)"]
        JWTFilter["JWT Auth Filter (Access Token Validation)"]
    end

    %% Backend Services
    subgraph Application Core (Spring Boot)
        AuthService["Auth & Session Service"]
        ProductService["Product & Catalog Service"]
        OrderService["Order & Checkout Service"]
        PaymentService["Payment & Refund Service"]
        PromoService["Coupon & Loyalty Service"]
        AssetService["Asset Storage Coordinator"]
    end

    %% Caching, Storage & DB
    subgraph Infrastructure Services
        MySQL[("MySQL Primary DB\n(Transactional Data)")]
        RedisCache[("Redis Cluster\n(JWT Blacklist, Rate Limits, Cache)")]
        MinIO[("MinIO Object Store\n(Images, PDF Invoices)")]
    end

    %% External Systems
    subgraph External Gateways
        Razorpay["Razorpay Gateway"]
        Mailtrap["Mailtrap SMTP (Email)"]
    end

    %% Connections
    WebClient -->|HTTPS / REST| SpringSecurity
    MobileClient -->|HTTPS / REST| SpringSecurity
    
    SpringSecurity --> RateLimiter
    SpringSecurity --> JWTFilter
    JWTFilter --> AuthService
    
    AuthService --> ProductService
    ProductService --> OrderService
    OrderService --> PaymentService
    PaymentService --> PromoService
    AssetService --> MinIO

    %% Database connections
    AuthService -.-> MySQL
    ProductService -.-> MySQL
    OrderService -.-> MySQL
    PromoService -.-> MySQL
    
    AuthService -.-> RedisCache
    ProductService -.-> RedisCache
    SpringSecurity -.-> RedisCache

    %% External API connections
    PaymentService --->|SDK / Webhooks| Razorpay
    OrderService --->|SMTP / Mail| Mailtrap
```

---

## 2. Complete Backend Folder Structure

Adhering to traditional layered MVC architecture, the backend organizes files logically by their technical role—housing controllers, services, repositories, entities, DTOs, configurations, and utilities under unified, flat packages.

```text
com.ecommerce
├── EcommerceApplication.java
├── config
│   ├── RedisConfig.java
│   ├── MinioConfig.java
│   ├── SwaggerConfig.java
│   └── WebMvcConfig.java
├── controller
│   ├── AuthController.java
│   ├── UserController.java
│   ├── ProductController.java
│   ├── CategoryController.java
│   ├── CartController.java
│   ├── WishlistController.java
│   ├── OrderController.java
│   ├── PaymentController.java
│   ├── BannerController.java
│   ├── CouponController.java
│   ├── LoyaltyController.java
│   ├── InventoryController.java
│   ├── ReviewController.java
│   └── AnalyticsController.java
├── dto
│   ├── ApiResponse.java
│   ├── PagedResponse.java
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── TokenResponse.java
│   ├── PasswordResetRequest.java
│   └── EmailContext.java
├── entity
│   ├── BaseAuditEntity.java
│   ├── User.java
│   ├── Role.java
│   ├── Address.java
│   ├── Product.java
│   ├── ProductVariant.java
│   ├── Category.java
│   ├── Brand.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── Wishlist.java
│   ├── WishlistItem.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── PaymentTransaction.java
│   ├── Banner.java
│   ├── Coupon.java
│   ├── LoyaltyAccount.java
│   ├── LoyaltyTransaction.java
│   ├── Inventory.java
│   └── Review.java
├── exception
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedException.java
│   └── BadRequestException.java
├── mapper
│   └── BaseMapper.java
├── repository
│   ├── UserRepository.java
│   ├── AddressRepository.java
│   ├── RefreshTokenRepository.java
│   ├── ProductRepository.java
│   ├── CategoryRepository.java
│   ├── BrandRepository.java
│   ├── CartRepository.java
│   ├── WishlistRepository.java
│   ├── OrderRepository.java
│   ├── PaymentTransactionRepository.java
│   ├── BannerRepository.java
│   ├── CouponRepository.java
│   ├── LoyaltyRepository.java
│   ├── InventoryRepository.java
│   └── ReviewRepository.java
├── security
│   ├── SecurityConfig.java
│   ├── JwtAuthenticationFilter.java
│   ├── CustomUserDetailsService.java
│   ├── UserPrincipal.java
│   └── JwtTokenProvider.java
└── service
    ├── AuthService.java
    ├── JwtService.java
    ├── RefreshTokenService.java
    ├── UserService.java
    ├── ProductService.java
    ├── CategoryService.java
    ├── CartService.java
    ├── WishlistService.java
    ├── OrderService.java
    ├── PaymentService.java
    ├── BannerService.java
    ├── CouponService.java
    ├── LoyaltyService.java
    ├── InventoryService.java
    ├── ReviewService.java
    ├── AnalyticsService.java
    └── EmailNotificationService.java
```

---

## 3. Complete Frontend Folder Structure

The React-Vite application utilizes standard feature-based layout, allowing components, custom hooks, and state slices to scale smoothly.

```text
src
├── api
│   └── client.js             # Configured Axios instance with interceptors
├── app
│   └── store.js              # Redux Toolkit Global Store config
├── assets
│   ├── images/               # Local static images
│   └── icons/                # SVG icon sets
├── components
│   ├── common/               # Button, Input, Modal, Loader, Card
│   ├── form/                 # Form-specific reusable components
│   └── navigation/           # Navbar, Footer, Sidebar, Breadcrumbs
├── features
│   ├── auth/                 # slices, hooks, components for Auth
│   ├── products/             # Product list, filters, product details
│   ├── cart/                 # Cart sidebar, checkout items
│   ├── order/                # Order history, track order
│   ├── loyalty/              # Reward point display
│   └── admin/                # Admin sub-modules (Management dashboards)
├── hooks
│   ├── useAuth.js            # Custom auth state wrapper
│   ├── useDebounce.js        # Debouncing utility hook for search
│   └── useLocalStorage.js    # Persistent local storage helper
├── layouts
│   ├── AuthLayout.jsx        # Plain layout for Auth forms
│   ├── CustomerLayout.jsx    # Complete layout with Header/Footer
│   └── AdminLayout.jsx       # Layout with sidebar navigation
├── pages
│   ├── Home.jsx              # Main landing page with Swiper carousel
│   ├── Login.jsx
│   ├── Register.jsx
│   ├── Products.jsx          # Catalog browsing with filters
│   ├── ProductDetails.jsx    # Complete product description & reviews
│   ├── Cart.jsx
│   ├── Checkout.jsx          # Address selection & Razorpay pay integration
│   ├── Profile.jsx
│   └── AdminDashboard.jsx
├── routes
│   ├── AppRoutes.jsx         # Complete React Router setup
│   ├── ProtectedRoute.jsx    # Customer RBAC route guard
│   └── AdminRoute.jsx        # Admin-only route guard
├── services
│   ├── authService.js
│   ├── productService.js
│   ├── cartService.js
│   └── paymentService.js
├── store
│   ├── authSlice.js
│   ├── cartSlice.js
│   └── uiSlice.js
├── styles
│   └── index.css             # Main Tailwind configuration and variables
└── utils
    ├── formatters.js         # Currency and Date formatting utils
    └── validators.js         # Custom Yup/React Hook Form validation schemas
```

---

## 4. Complete Maven Dependencies (`pom.xml`)

We use Spring Boot 3.2.x, Java 21, and the required security, database, and storage libraries.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.ecommerce</groupId>
    <artifactId>smart-ecommerce</artifactId>
    <version>1.0.0</version>
    <name>Smart E-Commerce Platform</name>
    <description>Enterprise-level Smart E-Commerce Platform Backend</description>

    <properties>
        <java.version>21</java.version>
        <jjwt.version>0.12.5</jjwt.version>
        <mapstruct.version>1.5.5.Final</mapstruct.version>
        <lombok-mapstruct-binding.version>0.2.0</lombok-mapstruct-binding.version>
        <minio.version>8.5.9</minio.version>
        <razorpay.version>1.4.3</razorpay.version>
        <springdoc.version>2.5.0</springdoc.version>
    </properties>

    <dependencies>
        <!-- Web & Core -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>

        <!-- Security -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        
        <!-- JWT (JJWT) -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jjwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>

        <!-- Database & JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Redis Cache & Rate Limiting -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!-- MinIO Object Storage -->
        <dependency>
            <groupId>io.minio</groupId>
            <artifactId>minio</artifactId>
            <version>${minio.version}</version>
        </dependency>

        <!-- Razorpay Payment SDK -->
        <dependency>
            <groupId>com.razorpay</groupId>
            <artifactId>razorpay-java</artifactId>
            <version>${razorpay.version}</version>
        </dependency>

        <!-- Mail Service -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>

        <!-- OpenAPI / Swagger Documentation -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>${springdoc.version}</version>
        </dependency>

        <!-- Utilities -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>${lombok.version}</version>
                        </path>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>${mapstruct.version}</version>
                        </path>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok-mapstruct-binding</artifactId>
                            <version>${lombok-mapstruct-binding.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 5. Complete Frontend Dependencies (`package.json`)

Created for a high-performance React application built using Vite, styled with Tailwind CSS, and powered by Redux Toolkit, React Query, and Swiper.js.

```json
{
  "name": "smart-ecommerce-frontend",
  "private": true,
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "lint": "eslint . --ext js,jsx --report-unused-disable-directives --max-warnings 0",
    "preview": "vite preview"
  },
  "dependencies": {
    "@reduxjs/toolkit": "^2.2.3",
    "@tanstack/react-query": "^5.29.2",
    "axios": "^1.6.8",
    "clsx": "^2.1.0",
    "lucide-react": "^0.368.0",
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-hook-form": "^7.51.3",
    "react-redux": "^9.1.1",
    "react-router-dom": "^6.22.3",
    "swiper": "^11.1.1",
    "tailwind-merge": "^2.2.2",
    "zod": "^3.22.4"
  },
  "devDependencies": {
    "@types/react": "^18.2.66",
    "@types/react-dom": "^18.2.22",
    "@vitejs/plugin-react": "^4.2.1",
    "autoprefixer": "^10.4.19",
    "eslint": "^8.57.0",
    "eslint-plugin-react": "^7.34.1",
    "eslint-plugin-react-hooks": "^4.6.0",
    "eslint-plugin-react-refresh": "^0.4.6",
    "postcss": "^8.4.38",
    "tailwindcss": "^3.4.3",
    "vite": "^5.2.8"
  }
}
```

---

## 6. Docker & Containerization Setup (`docker-compose.yml`)

This multi-container Docker Setup provides a reliable local development stack containing MySQL, Redis, and MinIO.

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.2
    container_name: ecommerce-mysql
    restart: always
    environment:
      MYSQL_DATABASE: ecommerce_db
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_USER: app_user
      MYSQL_PASSWORD: app_password
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - ecommerce-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      timeout: 5s
      retries: 5

  redis:
    image: redis:7.2-alpine
    container_name: ecommerce-redis
    restart: always
    command: redis-server --requirepass redis_password
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data
    networks:
      - ecommerce-network
    healthcheck:
      test: ["CMD", "redis-cli", "-a", "redis_password", "ping"]
      timeout: 5s
      retries: 5

  minio:
    image: minio/minio:RELEASE.2024-04-18T19-09-19Z
    container_name: ecommerce-minio
    restart: always
    ports:
      - "9000:9000"
      - "9001:9001"
    environment:
      MINIO_ROOT_USER: minio_admin
      MINIO_ROOT_PASSWORD: minio_password
    command: server /data --console-address ":9001"
    volumes:
      - minio-data:/data
    networks:
      - ecommerce-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:9000/minio/health/live"]
      timeout: 5s
      retries: 5

volumes:
  mysql-data:
    driver: local
  redis-data:
    driver: local
  minio-data:
    driver: local

networks:
  ecommerce-network:
    driver: bridge
```

---

## 7. Environment Variables Configuration

### Backend Core Configuration (`src/main/resources/application.yml`)
```yaml
server:
  port: 8080
  servlet:
    context-path: /api/v1

spring:
  application:
    name: smart-ecommerce
  
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: app_user
    password: app_password
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    database-platform: org.hibernate.dialect.MySQLDialect
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        use_sql_comments: true

  data:
    redis:
      host: localhost
      port: 6379
      password: redis_password
      timeout: 60000

  mail:
    host: sandbox.smtp.mailtrap.io
    port: 2525
    username: mailtrap_user
    password: mailtrap_password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

# Security Configuration
app:
  jwt:
    secret: 9a4f2c8d3b7a1e5f8c6d4e2b0a3f5c7d9a4f2c8d3b7a1e5f8c6d4e2b0a3f5c7d
    accessTokenExpirationMs: 900000     # 15 minutes
    refreshTokenExpirationMs: 604800000 # 7 days
  
  minio:
    endpoint: http://localhost:9000
    accessKey: minio_admin
    secretKey: minio_password
    bucket:
      products: product-images
      banners: banner-images
      profiles: profile-images
      invoices: order-invoices

  razorpay:
    keyId: rzp_test_key_id
    keySecret: rzp_test_key_secret
```

### Frontend Environment File (`.env`)
```env
VITE_API_BASE_URL=http://localhost:8080/api/v1
VITE_RAZORPAY_KEY_ID=rzp_test_key_id
VITE_MINIO_PUBLIC_URL=http://localhost:9000
```

---

## 8. Configuration Strategy

1. **Environment Separation**:
   - For local development, configurations use standard defaults (localhost and direct Docker networking).
   - For production, use environment variable replacements (e.g., `${MYSQL_HOST}`, `${REDIS_PASSWORD}`) in `application.yml` and inject secrets securely through container configuration.

2. **CORS Management**:
   - Strictly control origin access to allow only specific development/production URLs (e.g., `http://localhost:5173` for development).

3. **Exception Strategy**:
   - Provide clean, uniform JSON exception formats with accurate HTTP status codes (400, 401, 403, 404, 429, 500) rather than generic error stack traces.

---

## 9. Development Workflow

1. **Database & Cache Booting**:
   - Simply navigate to the project directory and run:
     ```bash
     docker compose up -d
     ```
   - Verify health checks have passed via `docker compose ps`.

2. **Backend Execution**:
   - Build using Maven wrapper: `./mvnw clean install` (or import directly into Intellij IDEA/Eclipse).
   - Run the main spring boot application: `./mvnw spring-boot:run`.

3. **Frontend Execution**:
   - Install packages: `npm install`.
   - Run development server: `npm run dev` to start the frontend dashboard on `http://localhost:5173`.
