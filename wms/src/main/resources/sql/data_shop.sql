-- USER 테이블 dummy data

-- 관리자 계정 (role = 1)
INSERT INTO user (email, password, name, address, address_detail, role, status)
VALUES ('admin@eone.com', 'admin1234', '관리자', '서울특별시 중구', '본사 1층', 1, 0);

-- 일반 사용자 1
INSERT INTO user (email, password, name, address, address_detail, role, status)
VALUES ('user1@eone.com', 'user1234', '홍길동', '서울특별시 강남구', '101동 202호', 0, 0);

-- 일반 사용자 2
INSERT INTO user (email, password, name, address, address_detail, role, status)
VALUES ('user2@eone.com', 'user5678', '김영희', '부산광역시 해운대구', '303동 101호', 0, 0);

-- 일반 사용자 3
INSERT INTO user (email, password, name, address, address_detail, role, status)
VALUES ('user3@eone.com', 'pass3344', '이철수', '대전광역시 유성구', 'B동 5층', 0, 0);

-- 일반 사용자 4
INSERT INTO user (email, password, name, address, address_detail, role, status)
VALUES ('user4@eone.com', 'pw112233', '박민지', '인천광역시 남동구', '아파트 302호', 0, 0);

select * from user;