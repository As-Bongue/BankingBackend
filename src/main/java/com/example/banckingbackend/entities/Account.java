package com.example.banckingbackend.entities;

import com.example.banckingbackend.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //preciser le type d'heritage
@DiscriminatorColumn(name = "TYPE", length = 4) // colone pour preciser le type d'entité
@Data @NoArgsConstructor @AllArgsConstructor
public abstract class Account {
    @Id
    private String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "account")
    private List<Operation> operationList;
}
