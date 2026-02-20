-- 插入分類
INSERT INTO pricing_categories (name) VALUES ('電子產品') ON CONFLICT DO NOTHING;
INSERT INTO pricing_categories (name) VALUES ('食品') ON CONFLICT DO NOTHING;
INSERT INTO pricing_categories (name) VALUES ('服飾') ON CONFLICT DO NOTHING;
INSERT INTO pricing_categories (name) VALUES ('辦公用品') ON CONFLICT DO NOTHING;
INSERT INTO pricing_categories (name) VALUES ('家電') ON CONFLICT DO NOTHING;

-- 插入客戶（10個）
INSERT INTO pricing_customers (name, email) VALUES ('張三', 'zhang@example.com') ON CONFLICT DO NOTHING;
INSERT INTO pricing_customers (name, email) VALUES ('李四', 'li@example.com') ON CONFLICT DO NOTHING;
INSERT INTO pricing_customers (name, email) VALUES ('王五', 'wang@example.com') ON CONFLICT DO NOTHING;
INSERT INTO pricing_customers (name, email) VALUES ('陳六', 'chen@example.com') ON CONFLICT DO NOTHING;
INSERT INTO pricing_customers (name, email) VALUES ('林七', 'lin@example.com') ON CONFLICT DO NOTHING;
INSERT INTO pricing_customers (name, email) VALUES ('黃八', 'huang@example.com') ON CONFLICT DO NOTHING;
INSERT INTO pricing_customers (name, email) VALUES ('趙九', 'zhao@example.com') ON CONFLICT DO NOTHING;
INSERT INTO pricing_customers (name, email) VALUES ('周十', 'zhou@example.com') ON CONFLICT DO NOTHING;
INSERT INTO pricing_customers (name, email) VALUES ('吳一一', 'wu@example.com') ON CONFLICT DO NOTHING;
INSERT INTO pricing_customers (name, email) VALUES ('鄭一二', 'zheng@example.com') ON CONFLICT DO NOTHING;

-- 插入商品（15個）
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('iPhone 15', 30000.00, 1) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('MacBook Pro', 60000.00, 1) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('AirPods', 5000.00, 1) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('iPad Air', 18000.00, 1) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('Apple Watch', 12000.00, 1) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('有機米', 500.00, 2) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('橄欖油', 800.00, 2) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('進口咖啡豆', 1200.00, 2) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('蜂蜜禮盒', 600.00, 2) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('T恤', 1200.00, 3) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('牛仔褲', 2500.00, 3) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('羽絨外套', 8000.00, 3) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('A4影印紙', 300.00, 4) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('電動掃地機', 15000.00, 5) ON CONFLICT DO NOTHING;
INSERT INTO pricing_products (name, base_price, category_id) VALUES ('空氣清淨機', 12000.00, 5) ON CONFLICT DO NOTHING;

-- 插入折扣（張三：5筆）
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (1, 1, 0.85, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (1, 2, 0.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (1, 3, 0.80, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (1, 10, 0.75, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (1, 14, 0.95, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;

-- 插入折扣（李四：4筆）
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (2, 4, 0.95, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (2, 6, 0.88, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (2, 11, 0.70, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (2, 15, 0.85, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;

-- 插入折扣（王五：3筆）
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (3, 5, 0.80, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (3, 7, 0.90, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (3, 12, 0.65, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;

-- 插入折扣（陳六：4筆）
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (4, 1, 0.78, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (4, 8, 0.85, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (4, 9, 0.92, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (4, 13, 0.88, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;

-- 插入折扣（林七：3筆）
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (5, 2, 0.82, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (5, 6, 0.95, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;
INSERT INTO pricing_discounts (customer_id, product_id, discount_ratio, created_at, updated_at) VALUES (5, 14, 0.80, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) ON CONFLICT DO NOTHING;