-- Fase 2 - Base de datos MySQL/MariaDB para App Cascos
-- Script idempotente: crea la base de datos y las tablas si no existen.
-- No borra bases de datos, tablas ni registros existentes.

CREATE DATABASE IF NOT EXISTS app_cascos
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE app_cascos;

CREATE TABLE IF NOT EXISTS product_models (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  brand VARCHAR(255) NULL,
  category VARCHAR(100) NULL,
  generation VARCHAR(100) NULL,
  description TEXT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  KEY idx_product_models_name (name),
  KEY idx_product_models_brand (brand),
  KEY idx_product_models_category (category),
  KEY idx_product_models_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS products (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  product_model_id BIGINT UNSIGNED NOT NULL,
  supplier_id BIGINT UNSIGNED NULL,
  name VARCHAR(255) NOT NULL,
  description TEXT NULL,
  product_url VARCHAR(500) NULL,
  serial_number VARCHAR(100) NULL,
  color VARCHAR(100) NOT NULL,
  usual_purchase_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  purchase_shipping_cost DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  other_purchase_costs DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  recommended_sale_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  current_stock INT NOT NULL DEFAULT 0,
  reserved_stock INT NOT NULL DEFAULT 0,
  minimum_stock INT NOT NULL DEFAULT 0,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  notes TEXT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_products_product_model
    FOREIGN KEY (product_model_id) REFERENCES product_models(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT uq_products_serial_number UNIQUE (serial_number),
  CONSTRAINT chk_products_usual_purchase_price CHECK (usual_purchase_price >= 0),
  CONSTRAINT chk_products_recommended_sale_price CHECK (recommended_sale_price >= 0),
  CONSTRAINT chk_products_current_stock CHECK (current_stock >= 0),
  CONSTRAINT chk_products_reserved_stock CHECK (reserved_stock >= 0),
  CONSTRAINT chk_products_minimum_stock CHECK (minimum_stock >= 0),
  CONSTRAINT chk_products_reserved_not_over_current CHECK (reserved_stock <= current_stock),

  KEY idx_products_model_id (product_model_id),
  KEY idx_products_supplier_id (supplier_id),
  KEY idx_products_name (name),
  KEY idx_products_serial_number (serial_number),
  KEY idx_products_active (active),
  KEY idx_products_low_stock (active, current_stock, minimum_stock)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS suppliers (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  website VARCHAR(255) NULL,
  purchase_url VARCHAR(500) NULL,
  contact_name VARCHAR(255) NULL,
  phone VARCHAR(50) NULL,
  email VARCHAR(255) NULL,
  address TEXT NULL,
  notes TEXT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  KEY idx_suppliers_name (name),
  KEY idx_suppliers_email (email),
  KEY idx_suppliers_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS purchases (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  supplier_id BIGINT UNSIGNED NOT NULL,
  order_date DATETIME NOT NULL,
  estimated_arrival_date DATETIME NULL,
  actual_arrival_date DATETIME NULL,
  status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
  shipping_cost DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  other_costs DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  tracking_number VARCHAR(255) NULL,
  external_reference VARCHAR(255) NULL,
  stock_applied BOOLEAN NOT NULL DEFAULT FALSE,
  notes TEXT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_purchases_supplier
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT chk_purchases_shipping_cost CHECK (shipping_cost >= 0),
  CONSTRAINT chk_purchases_other_costs CHECK (other_costs >= 0),

  KEY idx_purchases_supplier_id (supplier_id),
  KEY idx_purchases_order_date (order_date),
  KEY idx_purchases_status (status),
  KEY idx_purchases_stock_applied (stock_applied),
  KEY idx_purchases_external_reference (external_reference)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS purchase_items (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  purchase_id BIGINT UNSIGNED NOT NULL,
  product_id BIGINT UNSIGNED NOT NULL,
  quantity INT NOT NULL,
  unit_purchase_price DECIMAL(10, 2) NOT NULL,

  CONSTRAINT fk_purchase_items_purchase
    FOREIGN KEY (purchase_id) REFERENCES purchases(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_purchase_items_product
    FOREIGN KEY (product_id) REFERENCES products(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT chk_purchase_items_quantity CHECK (quantity > 0),
  CONSTRAINT chk_purchase_items_unit_purchase_price CHECK (unit_purchase_price >= 0),

  KEY idx_purchase_items_purchase_id (purchase_id),
  KEY idx_purchase_items_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sales (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  buyer_reference VARCHAR(255) NULL,
  sale_date DATETIME NOT NULL,
  payment_method VARCHAR(50) NULL,
  platform VARCHAR(50) NULL,
  status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
  commission DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  shipping_cost DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  other_costs DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  stock_applied BOOLEAN NOT NULL DEFAULT FALSE,
  notes TEXT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT chk_sales_commission CHECK (commission >= 0),
  CONSTRAINT chk_sales_shipping_cost CHECK (shipping_cost >= 0),
  CONSTRAINT chk_sales_other_costs CHECK (other_costs >= 0),

  KEY idx_sales_sale_date (sale_date),
  KEY idx_sales_status (status),
  KEY idx_sales_payment_method (payment_method),
  KEY idx_sales_platform (platform),
  KEY idx_sales_buyer_reference (buyer_reference),
  KEY idx_sales_stock_applied (stock_applied)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sale_items (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  sale_id BIGINT UNSIGNED NOT NULL,
  product_id BIGINT UNSIGNED NOT NULL,
  quantity INT NOT NULL,
  unit_sale_price DECIMAL(10, 2) NOT NULL,
  unit_purchase_cost DECIMAL(10, 2) NULL,

  CONSTRAINT fk_sale_items_sale
    FOREIGN KEY (sale_id) REFERENCES sales(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_sale_items_product
    FOREIGN KEY (product_id) REFERENCES products(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT chk_sale_items_quantity CHECK (quantity > 0),
  CONSTRAINT chk_sale_items_unit_sale_price CHECK (unit_sale_price >= 0),
  CONSTRAINT chk_sale_items_unit_purchase_cost CHECK (unit_purchase_cost IS NULL OR unit_purchase_cost >= 0),

  KEY idx_sale_items_sale_id (sale_id),
  KEY idx_sale_items_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS expenses (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  supplier_id BIGINT UNSIGNED NULL,
  purchase_id BIGINT UNSIGNED NULL,
  sale_id BIGINT UNSIGNED NULL,
  concept VARCHAR(255) NOT NULL,
  description TEXT NULL,
  category VARCHAR(50) NULL,
  amount DECIMAL(10, 2) NOT NULL,
  expense_date DATETIME NOT NULL,
  payment_method VARCHAR(50) NULL,
  notes TEXT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_expenses_supplier
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_expenses_purchase
    FOREIGN KEY (purchase_id) REFERENCES purchases(id)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_expenses_sale
    FOREIGN KEY (sale_id) REFERENCES sales(id)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT chk_expenses_amount CHECK (amount >= 0),

  KEY idx_expenses_supplier_id (supplier_id),
  KEY idx_expenses_purchase_id (purchase_id),
  KEY idx_expenses_sale_id (sale_id),
  KEY idx_expenses_concept (concept),
  KEY idx_expenses_category (category),
  KEY idx_expenses_expense_date (expense_date),
  KEY idx_expenses_payment_method (payment_method)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS inventory_movements (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  product_id BIGINT UNSIGNED NOT NULL,
  purchase_id BIGINT UNSIGNED NULL,
  sale_id BIGINT UNSIGNED NULL,
  movement_type VARCHAR(50) NOT NULL,
  quantity INT NOT NULL,
  previous_stock INT NOT NULL,
  resulting_stock INT NOT NULL,
  reason TEXT NULL,
  notes TEXT NULL,
  movement_date DATETIME NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_inventory_movements_product
    FOREIGN KEY (product_id) REFERENCES products(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_inventory_movements_purchase
    FOREIGN KEY (purchase_id) REFERENCES purchases(id)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT fk_inventory_movements_sale
    FOREIGN KEY (sale_id) REFERENCES sales(id)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT chk_inventory_movements_quantity CHECK (quantity > 0),
  CONSTRAINT chk_inventory_movements_previous_stock CHECK (previous_stock >= 0),
  CONSTRAINT chk_inventory_movements_resulting_stock CHECK (resulting_stock >= 0),

  KEY idx_inventory_movements_product_id (product_id),
  KEY idx_inventory_movements_purchase_id (purchase_id),
  KEY idx_inventory_movements_sale_id (sale_id),
  KEY idx_inventory_movements_movement_type (movement_type),
  KEY idx_inventory_movements_movement_date (movement_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS profitability_estimations (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  product_id BIGINT UNSIGNED NULL,
  units INT NOT NULL,
  unit_purchase_price DECIMAL(10, 2) NOT NULL,
  purchase_shipping_cost DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  other_purchase_costs DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  estimated_unit_sale_price DECIMAL(10, 2) NOT NULL,
  fixed_commission DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  percentage_commission DECIMAL(5, 2) NOT NULL DEFAULT 0.00,
  buyer_shipping_cost DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  other_costs DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
  estimated_loss_percentage DECIMAL(5, 2) NOT NULL DEFAULT 0.00,
  total_investment DECIMAL(10, 2) NOT NULL,
  estimated_income DECIMAL(10, 2) NOT NULL,
  estimated_expenses DECIMAL(10, 2) NOT NULL,
  estimated_profit DECIMAL(10, 2) NOT NULL,
  profit_per_unit DECIMAL(10, 2) NULL,
  margin_percentage DECIMAL(5, 2) NULL,
  roi_percentage DECIMAL(5, 2) NULL,
  break_even_price DECIMAL(10, 2) NULL,
  break_even_units INT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_profitability_estimations_product
    FOREIGN KEY (product_id) REFERENCES products(id)
    ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT chk_profitability_estimations_units CHECK (units > 0),
  CONSTRAINT chk_profitability_estimations_unit_purchase_price CHECK (unit_purchase_price >= 0),
  CONSTRAINT chk_profitability_estimations_purchase_shipping_cost CHECK (purchase_shipping_cost >= 0),
  CONSTRAINT chk_profitability_estimations_other_purchase_costs CHECK (other_purchase_costs >= 0),
  CONSTRAINT chk_profitability_estimations_estimated_unit_sale_price CHECK (estimated_unit_sale_price >= 0),
  CONSTRAINT chk_profitability_estimations_fixed_commission CHECK (fixed_commission >= 0),
  CONSTRAINT chk_profitability_estimations_percentage_commission CHECK (percentage_commission >= 0 AND percentage_commission <= 100),
  CONSTRAINT chk_profitability_estimations_buyer_shipping_cost CHECK (buyer_shipping_cost >= 0),
  CONSTRAINT chk_profitability_estimations_other_costs CHECK (other_costs >= 0),
  CONSTRAINT chk_profitability_estimations_loss_percentage CHECK (estimated_loss_percentage >= 0 AND estimated_loss_percentage <= 100),
  CONSTRAINT chk_profitability_estimations_total_investment CHECK (total_investment >= 0),
  CONSTRAINT chk_profitability_estimations_estimated_income CHECK (estimated_income >= 0),
  CONSTRAINT chk_profitability_estimations_estimated_expenses CHECK (estimated_expenses >= 0),
  CONSTRAINT chk_profitability_estimations_break_even_units CHECK (break_even_units IS NULL OR break_even_units >= 0),

  KEY idx_profitability_estimations_product_id (product_id),
  KEY idx_profitability_estimations_name (name),
  KEY idx_profitability_estimations_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS settings (
  id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  setting_key VARCHAR(255) NOT NULL,
  setting_value TEXT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT uq_settings_setting_key UNIQUE (setting_key),

  KEY idx_settings_setting_key (setting_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
