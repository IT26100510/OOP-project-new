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