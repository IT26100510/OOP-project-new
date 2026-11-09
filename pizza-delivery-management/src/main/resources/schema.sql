CREATE TABLE IF NOT EXISTS users (
  user_id INT PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(180) NOT NULL UNIQUE,
  password_hash VARCHAR(256) NOT NULL,
  salt VARCHAR(128) NOT NULL,
  role VARCHAR(20) NOT NULL,
  staff_code VARCHAR(40),
  phone VARCHAR(30),
  address VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS customers (
  id VARCHAR(20) PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(180) NOT NULL UNIQUE,
  phone VARCHAR(30) NOT NULL,
  address VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
  id VARCHAR(20) PRIMARY KEY,
  customer_id VARCHAR(20) NOT NULL,
  customer_name VARCHAR(120) NOT NULL,
  pizza_name VARCHAR(160) NOT NULL,
  quantity INT NOT NULL,
  total DECIMAL(12,2) NOT NULL,
  status VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS deliveries (
  id VARCHAR(20) PRIMARY KEY,
  order_id VARCHAR(20) NOT NULL,
  customer_name VARCHAR(120) NOT NULL,
  rider_name VARCHAR(120) NOT NULL,
  rider_phone VARCHAR(30) NOT NULL,
  status VARCHAR(30) NOT NULL,
  eta VARCHAR(40) NOT NULL,
  notes VARCHAR(1000),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_delivery_order (order_id)
);

CREATE TABLE IF NOT EXISTS feedback (
  id INT PRIMARY KEY,
  review_type VARCHAR(20) NOT NULL,
  customer_id INT NOT NULL,
  rating INT NOT NULL,
  review_text VARCHAR(1000) NOT NULL,
  review_date DATE NOT NULL,
  admin_reply VARCHAR(1000),
  pizza_id INT,
  pizza_name VARCHAR(160),
  order_id INT,
  delivery_person_name VARCHAR(120)
);

