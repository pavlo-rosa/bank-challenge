-- DELETE
-- FROM beneficiary
-- WHERE (SELECT COUNT(*) FROM beneficiary) > 0;

ALTER SEQUENCE beneficiary_id_seq RESTART;

INSERT INTO beneficiary(name)
VALUES ('Jon');
INSERT INTO beneficiary(name)
VALUES ('Daenerys');
INSERT INTO beneficiary(name)
VALUES ('Sansa');
INSERT INTO beneficiary(name)
VALUES ('Tyrion');

INSERT INTO account(balance, iban, password, owner_id)
VALUES (0, 'ES6621000418401234560000', '1234', 1);
INSERT INTO account(balance, iban, password, owner_id)
VALUES (2000, 'ES6621000418401234560002', '1234', 2);
INSERT INTO account(balance, iban, password, owner_id)
VALUES (10000, 'ES6621000418401234560003', '1234', 3);
INSERT INTO account(balance, iban, password, owner_id)
VALUES (30000, 'ES6621000418401234560004', '1234', 4);
INSERT INTO account(balance, iban, password, owner_id)
VALUES (120000, 'ES6621000418401234560004', '1234', 4);

INSERT INTO operation(name)
VALUES ('deposit');
INSERT INTO operation(name)
VALUES ('withdraw');
INSERT INTO operation(name)
VALUES ('transfer');


INSERT INTO transference (amount, destination_account_id, operation_id,
                          source_account_id)
VALUES (500.00, 1, 3, 4);


INSERT INTO transaction (amount, balance,  destination_account_id, operation_id,
                         transference_id)
VALUES (10.00, 10.00, 1, 1, null);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id,
                         transference_id)
VALUES (1000.00, 1000.00, 2, 1, null);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id,
                         transference_id)
VALUES (-10000.00, 0.00, 3, 2, null);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id,
                         transference_id)
VALUES (-500.00, 29500.00, 4, 3, 1);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id,
                         transference_id)
VALUES (500.00, 510.00, 1, 3, 1);


