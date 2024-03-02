-- Create users_app table
CREATE TABLE users_app (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    deposit DECIMAL(19,2) DEFAULT 0.00,
    role VARCHAR(50) NOT NULL
);

-- Create indexes
CREATE UNIQUE INDEX idx_username ON users_app (username);

-- Create product table
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amountAvailable INT NOT NULL,
    cost DECIMAL(19,2) NOT NULL,
    productName VARCHAR(255) NOT NULL,
    sellerId BIGINT NOT NULL
);

-- Add users
INSERT INTO users_app (username, password, deposit, role) VALUES ('buyer1', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 100.00, 'Buyer');
INSERT INTO users_app (username, password, deposit, role) VALUES ('buyer2', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 150.00, 'Buyer');
INSERT INTO users_app (username, password, deposit, role) VALUES ('seller1', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 0.00, 'Seller');
INSERT INTO users_app (username, password, deposit, role) VALUES ('seller2', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 0.00, 'Seller');

-- Add products
INSERT INTO product (amountAvailable, cost, productName, sellerId) VALUES (10, 50.00, 'Product 1', 3); -- Seller1
INSERT INTO product (amountAvailable, cost, productName, sellerId) VALUES (5, 75.00, 'Product 2', 4);  -- Seller2
INSERT INTO product (amountAvailable, cost, productName, sellerId) VALUES (20, 30.00, 'Product 3', 3); -- Seller1
