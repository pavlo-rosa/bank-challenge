package com.prosa.rivertech.rest.bankservices.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable{

    @CreatedDate
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT NOW()", nullable = false, updatable = false)
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT NOW()", nullable = false)
    private Date updatedDate;

    public Auditable() {
    }

    public Auditable(Date createdDate, Date updatedDate) {
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
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
}
