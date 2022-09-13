package com.example.banckingbackend.dtos;

import com.example.banckingbackend.enums.AccountOperation;
import lombok.Data;

import java.util.Date;

@Data
public class OperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private String description;
    private AccountOperation accountOperation;
}
