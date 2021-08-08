DROP TABLE IF EXISTS currency_name_mapping;

CREATE TABLE currency_name_mapping (
    currency VARCHAR(3) NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
) DEFAULT CHARSET=UTF8;

--INSERT INTO currency_name_mapping(currency, name) VALUES ('USD', '美元');
--INSERT INTO currency_name_mapping(currency, name) VALUES ('GBP', '英鎊');
--INSERT INTO currency_name_mapping(currency, name) VALUES ('EUR', '歐元');
