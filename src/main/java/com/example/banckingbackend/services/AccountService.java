package com.example.banckingbackend.services;

import com.example.banckingbackend.dtos.*;
import com.example.banckingbackend.entities.Account;
import com.example.banckingbackend.entities.CurrentAccount;
import com.example.banckingbackend.entities.Customer;
import com.example.banckingbackend.entities.SavingAccount;
import com.example.banckingbackend.exceptions.BalanceNotSufficientException;
import com.example.banckingbackend.exceptions.BankAccountExeption;
import com.example.banckingbackend.exceptions.CustomerNotFoundException;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

public interface AccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(CustomerDTO customerDTO);
    void deleteCustomer(Long id);
    CustomerDTO getCustomer(long id) throws CustomerNotFoundException;
    CurrentAccountDTO SaveCurrentAccount(double initialBalace, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccountDTO SaveSavingAccount(double initialBalace, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomer();
    List<AccountDTO> listAccount();
    AccountDTO getAccount(String accountId) throws BankAccountExeption;
    void debit(String accountId, double amount, String description) throws BankAccountExeption, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountExeption;
    void transfert(String accountSouceId, String accoundDestinationId, double amount) throws BalanceNotSufficientException, BankAccountExeption;

    List<OperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String id, int page, int size) throws AccountNotFoundException;

    List<CustomerDTO> searchCustomer(String keyword);
}
