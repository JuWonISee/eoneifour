----------------------------------------------------------
--입고 테이블 생성
----------------------------------------------------------
CREATE TABLE stock_product (
    stock_product_id INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_number   INT		  NOT NULL,
    product_name     VARCHAR(100) NOT NULL,
    product_brand    VARCHAR(50)  NOT NULL,
    s                INT 		  NOT NULL,
    z                INT 		  NOT NULL,
    x                INT 		  NOT NULL,
    y                INT 		  NOT NULL,
    stock_status     INT 		  NOT NULL,     -- 1 : 입고중   9 : 입고완료
    detail           VARCHAR(500)
);


----------------------------------------------------------
--출고 테이블 생성
----------------------------------------------------------
CREATE TABLE release_product (
    release_product_id INT  	    NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_number     INT		    NOT NULL,
    product_name       VARCHAR(100) NOT NULL,
    product_brand      VARCHAR(50)  NOT NULL,
    s                  INT 		    NOT NULL,
    z                  INT 		    NOT NULL,
    x                  INT 		    NOT NULL,
    y                  INT 		    NOT NULL,
    release_status     INT 		    NOT NULL, 	-- 1 : 출고대기,   2 : 출고중,   9 : 출고완료
    detail             VARCHAR(500)
);


----------------------------------------------------------
--입고기록 테이블 생성
----------------------------------------------------------
CREATE TABLE stock_log (
    stock_log_id 	 INT 		  NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_number   INT		  NOT NULL,
    product_name     VARCHAR(100) NOT NULL,
    product_brand    VARCHAR(50)  NOT NULL,
    s                INT 		  NOT NULL,
    z                INT 		  NOT NULL,
    x                INT 		  NOT NULL,
    y                INT 		  NOT NULL,
    stock_date       DATETIME     NOT NULL, 	-- 1 : 입고중,  9 : 입고완료
    detail           VARCHAR(500)
);


----------------------------------------------------------
--출고기록 테이블 생성
----------------------------------------------------------
CREATE TABLE release_log (
    release_log_id 	 INT 		 NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_number   INT		 NOT NULL,
    product_name     VARCHAR(45) NOT NULL,
    product_brand    VARCHAR(45) NOT NULL,
    s                INT 		 NOT NULL,
    z                INT 		 NOT NULL,
    x                INT 		 NOT NULL,
    y                INT 		 NOT NULL,
    release_date     DATETIME    NOT NULL, 	-- 1 : 입고중,  9 : 입고완료
    detail           VARCHAR(500)
);


----------------------------------------------------------
--스태커 테이블 생성
----------------------------------------------------------
CREATE TABLE stacker (
    stacker_id     INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    z              INT NOT NULL,
    x              INT NOT NULL,
    y              INT NOT NULL,
    stacker_status INT NOT NULL, -- 0 : 대기중   	1 : 이동중
    direction      INT NOT NULL, -- 0 : 왼쪽    	1 : 오른쪽
    on_product     INT NOT NULL  -- 0 : 물건 없음	1 : 물건 있음
);


----------------------------------------------------------
--conveyor 테이블 생성
----------------------------------------------------------
CREATE TABLE conveyor (
    conveyor_id INT 		NOT NULL PRIMARY KEY AUTO_INCREMENT,
    on_product  VARCHAR(45) NOT NULL -- 0 : 물건 없음	1 : 물건 있음
);


----------------------------------------------------------
--랙 테이블 생성 (한꺼번에 말고 순서대로 진행)
----------------------------------------------------------
-- 1 테이블 생성
CREATE TABLE rack (
    rack_id     INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    s           INT NOT NULL,
    z           INT NOT NULL,
    x           INT NOT NULL,
    y           INT NOT NULL,
    rack_status INT NOT NULL -- 0 : 물건 없음	1 : 입고중	
    						 -- 2 : 입고 완료	3 : 출고 대기 
);

-- 2 프로시저 생성
CREATE PROCEDURE insert_rack_data()
BEGIN
    DECLARE s_val INT DEFAULT 1;
    DECLARE z_val INT;
    DECLARE x_val INT;
    DECLARE y_val INT;

    WHILE s_val <= 2 DO
        SET z_val = 1;
        WHILE z_val <= 2 DO
            SET x_val = 1;
            WHILE x_val <= 7 DO
                SET y_val = 1;
                WHILE y_val <= 7 DO
                    INSERT INTO rack (s, z, x, y, rack_status)
                    VALUES (s_val, z_val, x_val, y_val, 0);
                    SET y_val = y_val + 1;
                END WHILE;
                SET x_val = x_val + 1;
            END WHILE;
            SET z_val = z_val + 1;
        END WHILE;
        SET s_val = s_val + 1;
    END WHILE;
END;

-- 3 프로시저 실행
CALL insert_rack_data();
