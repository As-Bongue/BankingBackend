package com.example.banckingbackend.dtos;

import com.example.banckingbackend.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class CurrentAccountDTO extends AccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus accountStatus;
    private CustomerDTO customerDTO;
    private double overDraft;
}
