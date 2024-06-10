CREATE TABLE IF NOT EXISTS currencies (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    full_name VARCHAR NOT NULL,
    sign VARCHAR NOT NULL,
    code VARCHAR NOT NULL,
    CONSTRAINT currencies_code_unique UNIQUE (code)
);

CREATE TABLE IF NOT EXISTS exchange_rates (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id INTEGER NOT NULL,
    target_currency_id INTEGER NOT NULL,
    rate REAL NOT NULL,
    CONSTRAINT exchange_rates_base_currency_id_target_currency_id_unique UNIQUE (base_currency_id, target_currency_id),
    CONSTRAINT exchange_rates_base_currency_id_fk FOREIGN KEY (base_currency_id) REFERENCES currencies(id),
    CONSTRAINT exchange_rates_target_currency_id_fk FOREIGN KEY (target_currency_id) REFERENCES currencies(id)
);

INSERT INTO currencies (full_name, sign, code)
VALUES
    ('Russian Ruble', '₽', 'RUB'),
    ('British Pound', '£', 'GBP'),
    ('Euro', '€', 'EUR'),
    ('US Dollar', '$', 'USD');

INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
SELECT
    (SELECT id FROM currencies WHERE code = 'USD'),
    (SELECT id FROM currencies WHERE code = 'EUR'),
    1.2;

INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
SELECT
    (SELECT id FROM currencies WHERE code = 'USD'),
    (SELECT id FROM currencies WHERE code = 'RUB'),
    70;

INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
SELECT
    (SELECT id FROM currencies WHERE code = 'USD'),
    (SELECT id FROM currencies WHERE code = 'GBP'),
    0.8;

