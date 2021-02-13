package com.prosa.rivertech.rest.bankservices.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "transference")
public class Transference  extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account sourceAccount;

    @ManyToOne
    private Account destinationAccount;

    @Column(name = "amount")
    private BigDecimal amount;

    @ManyToOne
    private Operation operation;

    @OneToMany(mappedBy = "transference", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public Transference() {
    }

    @Override
    public String toString() {
        return "Transference{" +
                "id=" + id +
                ", sourceAccount=" + sourceAccount +
                ", destinationAccount=" + destinationAccount +
                ", amount=" + amount +
                '}';
    }

    public Transference(Account sourceAccount, Account destinationAccount, BigDecimal amount, Operation operation) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.operation = operation;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
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

}
