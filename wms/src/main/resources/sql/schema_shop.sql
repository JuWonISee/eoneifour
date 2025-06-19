-- SHOP SCHEMA

-- 상위 카테고리 테이블
CREATE TABLE top_category (
    top_category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

-- 하위 카테고리 테이블
CREATE TABLE sub_category (
    sub_category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    top_category_id INT NOT NULL,
    CONSTRAINT fk_top_category_sub_category FOREIGN KEY (top_category_id)
        REFERENCES top_category(top_category_id)
);

-- 상품 테이블
CREATE TABLE product (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    brand_name VARCHAR(50) NOT NULL,
    price INT NOT NULL DEFAULT 0,
    detail VARCHAR(255) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sub_category_id INT NOT NULL,
    CONSTRAINT fk_sub_category_product FOREIGN KEY (sub_category_id)
        REFERENCES sub_category(sub_category_id)
);

-- 상품 이미지 경로 테이블
CREATE TABLE product_img (
    product_img_id INT PRIMARY KEY AUTO_INCREMENT,
    filename VARCHAR(255) NOT NULL,
    product_id INT NOT NULL,
    CONSTRAINT fk_product_product_img FOREIGN KEY (product_id)
        REFERENCES product(product_id)
);

-- 회원 테이블
CREATE TABLE user (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    address VARCHAR(200) NOT NULL,
    address_detail VARCHAR(200) NOT NULL,
    role INT NOT NULL DEFAULT 0 COMMENT '권한( user : 0 / admin : 1)',  -- 권한 (user: 0 / admin: 1)
    status INT NOT NULL DEFAULT 0  COMMENT '회원 탈퇴 유무 상태(active : 0 / deleted : 1)',  -- 상태 (active: 0 / deleted: 1)
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 발주 테이블
CREATE TABLE purchase_order (
    purchase_order_id INT PRIMARY KEY AUTO_INCREMENT,
    quantity INT NOT NULL DEFAULT 0,
    status INT NOT NULL DEFAULT 0 COMMENT '발주 상태 (발주완료 : 0 / 창고도착 : 1 / 입고완료 : 2)', -- 발주 상태 (0: 발주완료 / 1: 창고도착 / 2: 입고완료)
    request_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    complete_date DATETIME COMMENT '입고 완료 시 기록',
    requested_by INT NOT NULL,
    product_id INT NOT NULL,
    CONSTRAINT fk_user_purchase_order FOREIGN KEY (requested_by)
        REFERENCES user(user_id),
    CONSTRAINT fk_product_purchase_order FOREIGN KEY (product_id)
        REFERENCES product(product_id)
);

-- 주문 테이블
CREATE TABLE orders (
    orders_id INT PRIMARY KEY AUTO_INCREMENT,
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_price INT NOT NULL DEFAULT 0,
    status INT NOT NULL DEFAULT 0  COMMENT '주문상태 (배송준비 : 0 / 배송 중 : 1 / 배송완료 : 2 / 주문 취소: 3)',   -- 주문상태 (0: 배송준비 / 1: 배송중 / 2: 배송완료 / 3: 주문취소)
    user_id INT NOT NULL,
    CONSTRAINT fk_user_order FOREIGN KEY (user_id)
        REFERENCES user(user_id)
);

-- 주문 상품 테이블
CREATE TABLE order_item (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    quantity INT NOT NULL DEFAULT 0,
    price INT NOT NULL DEFAULT 0,
    orders_id INT NOT NULL,
    product_id INT NOT NULL,
    CONSTRAINT fk_orders_order_item FOREIGN KEY (orders_id)
        REFERENCES orders(orders_id),
    CONSTRAINT fk_product_order_item FOREIGN KEY (product_id)
        REFERENCES product(product_id)
);