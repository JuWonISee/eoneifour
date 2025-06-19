------------------------------------------------------
-- DB 생성, 유저 생성 및 권한부여
------------------------------------------------------
CREATE DATABASE eone;

CREATE USER 'eone'@'%' identified BY '1234';

GRANT all privileges ON eone.* TO 'eone'@'%';

flush privileges;

--권한 부여 후 혹시 ERROR 1227(42000): Access denied 발생시
GRANT PROCESS ON *.* TO 'eone'@'%';