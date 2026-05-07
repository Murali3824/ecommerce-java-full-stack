# Phase 2: Database Design Blueprint

This blueprint covers the complete database schemas, relationships, indexing and performance optimizations, Redis caching strategy, and MinIO object storage mapping for the Smart E-Commerce Platform.

---

## 1. Relational Database Schema & Entities

The relational database is built on **MySQL 8.x** with **InnoDB** as the transactional engine. Refer to the complete SQL script [schema.sql](file:///c:/Users/mural/.gemini/antigravity/scratch/Ecommerce/blueprint/schema.sql) for implementation.

### Comprehensive Table Index & Mapping

| # | Table Name | Key Columns / Constraints | Purpose / Domain Role |
| :--- | :--- | :--- | :--- |
| 1 | `users` | `id` (PK), `email` (Unique) | Houses credentials, profile basics, and activation status. |
| 2 | `roles` | `id` (PK), `name` (Unique) | System roles (`ROLE_CUSTOMER`, `ROLE_SELLER`, `ROLE_ADMIN`). |
| 3 | `user_roles` | `(user_id, role_id)` (Composite PK) | Direct many-to-many link between Users and Roles. |
| 4 | `refresh_tokens` | `id` (PK), `token` (Unique), `user_id` (FK) | Tracks active refresh tokens, rotational metadata, and expiries. |
| 5 | `categories` | `id` (PK), `name` (Unique), `parent_id` (FK) | Category tree (supports parent/child hierarchies). |
| 6 | `brands` | `id` (PK), `name` (Unique) | Manufactures / Brand definitions. |
| 7 | `products` | `id` (PK), `category_id` (FK), `brand_id` (FK) | Core product details. Managed at product level. |
| 8 | `product_variants`| `id` (PK), `product_id` (FK), `sku` (Unique) | Handles variations of products (color, size, unique SKU, price). |
| 9 | `product_images` | `id` (PK), `product_id` (FK) | Paths to product images stored in MinIO. |
| 10| `addresses` | `id` (PK), `user_id` (FK) | Recipient profiles, street addresses, and default billing/shipping flag. |
| 11| `carts` | `id` (PK), `user_id` (FK, Unique) | Single active cart per registered user. |
| 12| `cart_items` | `id` (PK), `cart_id` (FK), `variant_id` (FK) | Quantified link of chosen variants inside a cart. |
| 13| `wishlists` | `id` (PK), `user_id` (FK, Unique) | Individual customer's wishlist container. |
| 14| `wishlist_items` | `id` (PK), `wishlist_id` (FK), `product_id` (FK) | Tracks specific liked products (Many-to-Many). |
| 15| `orders` | `id` (PK), `user_id` (FK), `order_number` (Unique) | Customer order transactional header (totals, status, audit). |
| 16| `order_items` | `id` (PK), `order_id` (FK), `variant_id` (FK) | Locked quantity and frozen purchasing price for ordered variants. |
| 17| `payments` | `id` (PK), `order_id` (FK), `transaction_id` | Payment details, signature verifications, status (Razorpay). |
| 18| `reviews` | `id` (PK), `user_id`, `product_id` (UQ Composite) | Customer ratings (1-5 star bounds) and comment details. |
| 19| `coupons` | `id` (PK), `code` (Unique) | Percentage or Flat discount rules, usage quotas, and validity windows. |
| 20| `offers` | `id` (PK), `name` | Seasonal promotions, banner triggers, and flash sale structures. |
| 21| `banners` | `id` (PK), `banner_type` (Enum) | Hero sliders, offer banners, and dynamic frontend scheduling. |
| 22| `loyalty_points` | `id` (PK), `user_id` (FK, Unique) | Current running point balance for each customer. |
| 23| `loyalty_transactions`| `id` (PK), `user_id` (FK) | Complete points logging (EARNED / REDEEMED audit). |
| 24| `inventory` | `id` (PK), `variant_id` (FK, Unique) | Real-time stock counts and active reservations. |
| 25| `audit_logs` | `id` (PK), `timestamp` | Tracking critical administrative changes (inserts, updates). |

---

## 2. Table Relationships and ER Architecture

```mermaid
erDiagram
    USERS ||--o{ USER_ROLES : has
    ROLES ||--o{ USER_ROLES : maps
    USERS ||--o{ REFRESH_TOKENS : generates
    USERS ||--o{ ADDRESSES : owns
    USERS ||--o1 CARTS : owns
    USERS ||--o1 WISHLISTS : owns
    USERS ||--o{ ORDERS : places
    USERS ||--o1 LOYALTY_POINTS : earns
    USERS ||--o{ LOYALTY_TRANSACTIONS : logs
    
    CATEGORIES ||--o{ PRODUCTS : categorizes
    CATEGORIES ||--o{ CATEGORIES : nests
    BRANDS ||--o{ PRODUCTS : brands
    
    PRODUCTS ||--o{ PRODUCT_VARIANTS : contains
    PRODUCTS ||--o{ PRODUCT_IMAGES : displays
    PRODUCTS ||--o{ WISHLIST_ITEMS : liked
    PRODUCTS ||--o{ REVIEWS : reviews
    
    PRODUCT_VARIANTS ||--o{ CART_ITEMS : added
    PRODUCT_VARIANTS ||--o{ ORDER_ITEMS : purchased
    PRODUCT_VARIANTS ||--o1 INVENTORY : stocks
    
    CARTS ||--o{ CART_ITEMS : details
    WISHLISTS ||--o{ WISHLIST_ITEMS : contains
    ORDERS ||--o{ ORDER_ITEMS : details
    ORDERS ||--o1 PAYMENTS : completes
```

### Critical Relationship Constraints

1. **One-to-One Cart and Wishlist**:
   - `carts` and `wishlists` have a `user_id` with a `UNIQUE` constraint, enforcing a strict 1-to-1 relationship. This optimizes Redis syncs and speeds up cart retrieval operations.
2. **Product Variants and Inventory**:
   - High-performance variant setup: Products hold common metadata (description, category), while `product_variants` represents distinct purchasable items (size/color).
   - `inventory` links directly 1-to-1 with `product_variants` for instant SKU tracking.
3. **Cascade Deletes vs Set Null**:
   - Deleting a `User` cascades down to delete their `carts`, `wishlists`, `addresses`, `refresh_tokens`, and `loyalty_points` to respect privacy.
   - However, deleting a `User` **does not** delete their `orders`. The `orders.user_id` is maintained (or soft deleted) to preserve transactional history.
   - Deleting a `Category` sets `categories.parent_category_id` or `products.category_id` to `NULL` (or a default 'Uncategorized' fallback) to prevent breaking existing products.

---

## 3. High-Performance Indexing Strategy

To handle high-traffic e-commerce operations, indexes are placed strategically on frequently queried columns:

- **E-Commerce Search optimization**:
  - `products(name)`: Single index on name to facilitate auto-complete and search.
- **Foreign Key Query joins**:
  - `products(category_id)`, `products(brand_id)`: Speeds up faceted filtering.
- **Rotational Token queries**:
  - `refresh_tokens(token)`: Speeds up authentication refreshes.
- **Checkout & Tracking**:
  - `orders(order_number)`: Instant lookups for invoice generation and order details.
- **Active Schedulers**:
  - `banners(active, start_date, end_date)`: Compound index to instantly fetch active, scheduled banners without table scans.

---

## 4. Redis Key Namespace Strategy

Redis serves as an in-memory cache, rate limiter, and fast persistence layer for tokens and carts:

```text
ecommerce:[namespace]:[identifier]
```

| Namespace | Key Format | Expiry Strategy | Purpose |
| :--- | :--- | :--- | :--- |
| **Token Blacklist**| `ecommerce:blacklist:token:<jwt_hash>` | Equal to remaining JWT expiry | Denies blacklisted tokens on logout. |
| **Active Refresh** | `ecommerce:refresh:user:<user_id>` | 7 Days (Sliding) | Verifies refresh token legitimacy. |
| **Rate Limiter** | `ecommerce:rate:ip:<ip_address>` | 1 Minute (Windowed) | Rate limiting API endpoints (Token Bucket). |
| **Product Detail** | `ecommerce:product:id:<product_id>` | 24 Hours | Caches product metadata. |
| **Banner Slider** | `ecommerce:banners:active` | 1 Hour | Caches active promotional hero banners. |
| **Cart Temp** | `ecommerce:cart:user:<user_id>` | 14 Days | Fast read/write cache for guest/active carts. |

---

## 5. MinIO S3-Compatible Bucket Structure

MinIO is configured as an S3 object store. Storage is categorized into distinct buckets with public/private ACLs:

```text
minio-dashboard
├── product-images (Public - Read Only)
│   ├── prod-101/
│   │   ├── primary.webp
│   │   ├── gallery-1.webp
│   │   └── gallery-2.webp
├── banner-images (Public - Read Only)
│   ├── home-hero-summer.webp
│   └── category-electronics.webp
├── profile-images (Private - Auth Access)
│   ├── user-502/
│   │   └── avatar.png
└── order-invoices (Private - Secured Signed Access)
    └── inv-2026-05-0012.pdf
```

- **Read Optimization**: Public buckets (`product-images`, `banner-images`) allow the React frontend to load assets directly without generating short-lived presigned URLs.
- **Security Control**: Private buckets (`order-invoices`) require the backend to generate temporary secure presigned URLs (expires in 15 minutes) for verified users, preventing invoice leaks.
