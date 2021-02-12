package com.prosa.rivertech.rest.bankservices.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //TODO: Create some kind of generator and constraint
    @Column(name = "iban")
    private String iban;

    //TODO: Pin 4 digits
    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    private Beneficiary owner;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToMany(mappedBy = "sourceAccount")
    private List<Transference> transferencesEmitted;

    @OneToMany(mappedBy = "destinationAccount")
    private List<Transference> transferencesReceived;

    @CreatedDate
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT NOW() NOT NULL")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT NOW() NOT NULL")
    private Date updatedDate;

    public Account() {
    }

    public Account(String iban, String password, Beneficiary owner, BigDecimal balance) {
        this.iban = iban;
        this.password = password;
        this.owner = owner;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", iban='" + iban + '\'' +
                ", password='" + password + '\'' +
                ", owner=" + owner +
                ", balance=" + balance +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Beneficiary getOwner() {
        return owner;
    }

    public void setOwner(Beneficiary owner) {
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<Transference> getTransferencesReceived() {
        return transferencesReceived;
    }

    public void setTransferencesReceived(List<Transference> transferencesReceived) {
        this.transferencesReceived = transferencesReceived;
    }
}
