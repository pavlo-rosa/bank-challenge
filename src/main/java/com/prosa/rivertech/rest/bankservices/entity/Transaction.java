package com.prosa.rivertech.rest.bankservices.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transaction")
@JsonFilter("TransactionFilter")
public class Transaction  extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Operation operation;

    @ManyToOne
    private Account destinationAccount;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "balance")
    private BigDecimal balance;

    @ManyToOne
    private Transference transference;

    public Transaction() {
    }

    public Transaction(Operation operation, Account destinationAccount, BigDecimal amount, BigDecimal balance, Transference transference) {
        this.operation = operation;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.balance = balance;
        this.transference = transference;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Transference getTransference() {
        return transference;
    }

    public void setTransference(Transference transference) {
        this.transference = transference;
    }

}
