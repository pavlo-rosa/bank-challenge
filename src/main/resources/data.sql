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
BEGIN
    NEW.number = (SELECT TO_CHAR(ps, ''fm0000000000000'')
                FROM pseudo_encrypt(CURRVAL(''account_id_seq'')::INT) AS ps);
    RETURN NEW;
END;
' LANGUAGE 'plpgsql';

CREATE TRIGGER generate_account_number_trigger
    BEFORE INSERT
    ON account
    FOR EACH ROW
EXECUTE PROCEDURE generate_account_number();

INSERT INTO beneficiary(name)
VALUES ('Jon');
INSERT INTO beneficiary(name)
VALUES ('Daenerys');
INSERT INTO beneficiary(name)
VALUES ('Sansa');
INSERT INTO beneficiary(name)
VALUES ('Tyrion');
INSERT INTO beneficiary(name)
VALUES ('Hodor');

INSERT INTO account(balance, password, owner_id)
VALUES (0, '1234', 1);
INSERT INTO account(balance, password, owner_id)
VALUES (2000, '1234', 2);
INSERT INTO account(balance, password, owner_id)
VALUES (10000, '1234', 3);
INSERT INTO account(balance, password, owner_id)
VALUES (30000, '1234', 4);
INSERT INTO account(balance, password, owner_id)
VALUES (120000, '1234', 4);

INSERT INTO operation(name)
VALUES ('deposit');
INSERT INTO operation(name)
VALUES ('withdraw');
INSERT INTO operation(name)
VALUES ('transfer');


INSERT INTO transference (amount, destination_account_id, operation_id,
                          source_account_id)
VALUES (500.00, 1, 3, 4);


INSERT INTO transaction (amount, balance, destination_account_id, operation_id,
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


