INSERT INTO "user"(name)
VALUES ('Jon');
INSERT INTO "user"(name)
VALUES ('Daenerys');
INSERT INTO "user"(name)
VALUES ('Sansa');
INSERT INTO "user"(name)
VALUES ('Tyrion');

-- The password in all accounts is: 1234
INSERT INTO account (balance, number, password, owner_id)
VALUES (1050.00, '0000561465857', '$2a$10$yss7nSBgNR4ExQrK8/lIrOm1kObWguXmUHa64ZQEzb89E8QxJg7ou', 1);
INSERT INTO account (balance, number, password, owner_id)
VALUES (10000.00, '0000436885871', '$2a$10$yss7nSBgNR4ExQrK8/lIrOm1kObWguXmUHa64ZQEzb89E8QxJg7ou', 2);
INSERT INTO account (balance, number, password, owner_id)
VALUES (5000.00, '0000576481439', '$2a$10$yss7nSBgNR4ExQrK8/lIrOm1kObWguXmUHa64ZQEzb89E8QxJg7ou', 3);
INSERT INTO account (balance, number, password, owner_id)
VALUES (1000.00, '0000483424269', '$2a$10$yss7nSBgNR4ExQrK8/lIrOm1kObWguXmUHa64ZQEzb89E8QxJg7ou', 4);
INSERT INTO account (balance, number, password, owner_id)
VALUES (998960.00, '0001905133426', '$2a$10$yss7nSBgNR4ExQrK8/lIrOm1kObWguXmUHa64ZQEzb89E8QxJg7ou', 4);


INSERT INTO operation(name)
VALUES ('deposit');
INSERT INTO operation(name)
VALUES ('withdraw');
INSERT INTO operation(name)
VALUES ('transfer');


INSERT INTO transference (amount, destination_account_id, operation_id, source_account_id)
VALUES (1000.00, 4, 3, 1);
INSERT INTO transference (amount, destination_account_id, operation_id, source_account_id)
VALUES (1040.00, 1, 3, 5);


INSERT INTO transaction (amount, balance, destination_account_id, operation_id, transference_id)
VALUES (10.00, 10.00, 1, 1, NULL);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id, transference_id)
VALUES (1000.00, 1010.00, 1, 1, NULL);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id, transference_id)
VALUES (10000.00, 10000.00, 2, 1, NULL);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id, transference_id)
VALUES (5000.00, 5000.00, 3, 1, NULL);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id, transference_id)
VALUES (500.00, 500.00, 4, 1, NULL);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id, transference_id)
VALUES (1000000.00, 1000000.00, 5, 1, NULL);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id, transference_id)
VALUES (-100.00, 400.00, 4, 2, NULL);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id, transference_id)
VALUES (-400.00, 0.00, 4, 2, NULL);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id, transference_id)
VALUES (-1000.00, 10.00, 1, 3, 1);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id, transference_id)
VALUES (1000.00, 1000.00, 4, 3, 1);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id, transference_id)
VALUES (-1040.00, 998960.00, 5, 3, 2);
INSERT INTO transaction (amount, balance, destination_account_id, operation_id, transference_id)
VALUES (1040.00, 1050.00, 1, 3, 2);


CREATE OR REPLACE FUNCTION pseudo_encrypt(value INT) RETURNS INT AS
'
    DECLARE
        l1 INT;
        l2 INT;
        r1 INT;
        r2 INT;
        i  INT := 0;
    BEGIN
        l1 := (value >> 16) & 65535;
        r1 := value & 65535;
        WHILE i < 3
            LOOP
                l2 := r1;
                r2 := l1 # ((((1366 * r1 + 150889) % 714025) / 714025.0) * 32767)::INT;
                l1 := l2;
                r1 := r2;
                i := i + 1;
            END LOOP;
        RETURN ((r1 << 16) + l1);
    END;
' LANGUAGE plpgsql STRICT
                   IMMUTABLE;

CREATE OR REPLACE FUNCTION generate_account_number()
    RETURNS TRIGGER AS
'
    BEGIN NEW.number = (SELECT TO_CHAR(ps, ''fm0000000000000'')
                FROM pseudo_encrypt(CURRVAL(''account_id_seq'')::INT) AS ps); RETURN NEW; END;
' LANGUAGE 'plpgsql';

CREATE TRIGGER generate_account_number_trigger
    BEFORE INSERT
    ON account
    FOR EACH ROW
EXECUTE PROCEDURE generate_account_number();
