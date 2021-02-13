package com.prosa.rivertech.rest.bankservices.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "account")
public class Account  extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transference> transferencesEmitted;

    @OneToMany(mappedBy = "destinationAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transference> transferencesReceived;

    @OneToMany(mappedBy = "destinationAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
//    @JsonBackReference
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

    public List<Transference> getTransferencesReceived() {
        return transferencesReceived;
    }

    public void setTransferencesReceived(List<Transference> transferencesReceived) {
        this.transferencesReceived = transferencesReceived;
    }
}
