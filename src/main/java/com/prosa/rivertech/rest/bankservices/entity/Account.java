package com.prosa.rivertech.rest.bankservices.entity;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "account")
@JsonFilter("AccountFilter")
public class Account  extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", unique = true)
    private String number;

    @Column(name = "password")
    private String password;

    @ManyToOne()
    private User owner;

    @Column(name = "balance", columnDefinition = "NUMERIC(19,2) DEFAULT 0 NOT NULL")
    private BigDecimal balance = new BigDecimal(0);

    @OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transference> transferencesEmitted;

    @OneToMany(mappedBy = "destinationAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transference> transferencesReceived;

    @OneToMany(mappedBy = "destinationAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    public Account() {
    }

    public Account(User owner, String password, String number) {
        this.password = password;
        this.owner = owner;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", password='" + password + '\'' +
                ", owner=" + owner +
                ", balance=" + balance +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Transference> getTransferencesEmitted() {
        return transferencesEmitted;
    }

    public void setTransferencesEmitted(List<Transference> transferencesEmitted) {
        this.transferencesEmitted = transferencesEmitted;
    }

    public List<Transference> getTransferencesReceived() {
        return transferencesReceived;
    }

    public void setTransferencesReceived(List<Transference> transferencesReceived) {
        this.transferencesReceived = transferencesReceived;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
