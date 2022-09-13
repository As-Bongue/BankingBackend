package com.example.banckingbackend.mappers;

import com.example.banckingbackend.dtos.CurrentAccountDTO;
import com.example.banckingbackend.dtos.CustomerDTO;
import com.example.banckingbackend.dtos.OperationDTO;
import com.example.banckingbackend.dtos.SavingAccountDTO;
import com.example.banckingbackend.entities.CurrentAccount;
import com.example.banckingbackend.entities.Customer;
import com.example.banckingbackend.entities.Operation;
import com.example.banckingbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountMapperImpl {

    //mapper un customer vers un customerDTO
    public CustomerDTO fromCustumer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    //mapper un customerDTO vers un customer
    public Customer fromCustumerDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    //mapper un SavingAccount vers un SavingAccountDTO
    public SavingAccountDTO fromSavingAccount(SavingAccount savingAccount){
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount, savingAccountDTO);
        savingAccountDTO.setCustomerDTO(fromCustumer(savingAccount.getCustomer()));
        savingAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingAccountDTO;
    }

    //mapper un SavingAccountDTO vers un SavingAccount
    public SavingAccount fromSavingAccountDTO(SavingAccountDTO savingAccountDTO){
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(savingAccountDTO, savingAccount);
        savingAccount.setCustomer(fromCustumerDTO(savingAccountDTO.getCustomerDTO()));
        return savingAccount;

    }

    //mapper un CurrentAccount vers un CurrentAccountDTO
    public CurrentAccountDTO fromCurrentAccount(CurrentAccount currentAccount){
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        BeanUtils.copyProperties(currentAccount, currentAccountDTO);
        currentAccountDTO.setCustomerDTO(fromCustumer(currentAccount.getCustomer()));
        currentAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentAccountDTO;
    }

    //mapper un CurrentAccountDTO vers un CurrentAccount
    public CurrentAccount fromCurrentAccountDTO(CurrentAccountDTO currentAccountDTO){
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDTO, currentAccount);
        currentAccount.setCustomer(fromCustumerDTO(currentAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    public OperationDTO fromOperation(Operation operation){
        OperationDTO operationDTO = new OperationDTO();
        BeanUtils.copyProperties(operation, operationDTO);
        return operationDTO;
    }

}
