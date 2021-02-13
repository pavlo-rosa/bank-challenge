package com.prosa.rivertech.rest.bankservices.entity;

import javax.persistence.*;

@Entity
@Table(name = "operation")
public class Operation  extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    public Operation() {
    }

    public Operation(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
