package com.example.banckingbackend.services;

import com.example.banckingbackend.dtos.*;
import com.example.banckingbackend.entities.*;
import com.example.banckingbackend.enums.AccountOperation;
import com.example.banckingbackend.enums.AccountStatus;
import com.example.banckingbackend.exceptions.BalanceNotSufficientException;
import com.example.banckingbackend.exceptions.BankAccountExeption;
import com.example.banckingbackend.exceptions.CustomerNotFoundException;
import com.example.banckingbackend.mappers.AccountMapperImpl;
import com.example.banckingbackend.repository.AccountRepository;
import com.example.banckingbackend.repository.CustomerRepository;
import com.example.banckingbackend.repository.OperationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private CustomerRepository customerRepository;
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;
    private AccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer");
        Customer customer = dtoMapper.fromCustumerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustumer(savedCustomer);
    }

    @Override
    public CustomerDTO getCustomer(long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow( () -> new CustomerNotFoundException("Customer not found"));
        return dtoMapper.fromCustumer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating customer");
        Customer customer = dtoMapper.fromCustumerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustumer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public CurrentAccountDTO SaveCurrentAccount(double initialBalace, double overDraft, Long customerId) throws CustomerNotFoundException {
        //verifier si le client existe avant de lui creer son compte
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow( () -> new CustomerNotFoundException("Customer not found"));

        CurrentAccount account = new CurrentAccount();
        account.setId(UUID.randomUUID().toString());
        account.setCreatedAt(new Date());
        account.setBalance(initialBalace);
        account.setAccountStatus(AccountStatus.CREATED);
        account.setCustomer(customer);
        account.setOverDraft(overDraft);
        CurrentAccount savedCurrentAccount = accountRepository.save(account);
        return dtoMapper.fromCurrentAccount(savedCurrentAccount);
    }

    @Override
    public SavingAccountDTO SaveSavingAccount(double initialBalace, double interestRate, Long customerId) throws CustomerNotFoundException {
        //verifier si le client existe avant de lui creer son compte
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow( () -> new CustomerNotFoundException("Customer not found"));

        SavingAccount account = new SavingAccount();
        account.setId(UUID.randomUUID().toString());
        account.setCreatedAt(new Date());
        account.setBalance(initialBalace);
        account.setAccountStatus(AccountStatus.CREATED);
        account.setCustomer(customer);
        account.setInterest(interestRate);
        SavingAccount savedSavingAccount = accountRepository.save(account);
        return dtoMapper.fromSavingAccount(savedSavingAccount);
    }

    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customerList = customerRepository.findAll();
        //on tranforme chaque customer d'un objet customer vers un objet customerDTO en utilisan stream().map()
        //et enfin on transforme ce stream en liste coe au depart en utilisant collect
        List<CustomerDTO> customerDTOS = customerList.stream()
                .map(customer -> dtoMapper.fromCustumer(customer))
                .collect(Collectors.toList());

        /*
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        for (Customer customer:customerList){
            CustomerDTO customerDTO = dtoMapper.fromCustumer(customer);
            customerDTOList.add(customerDTO);
        }
        *
         */

        return customerDTOS;
    }

    @Override
    public List<AccountDTO> listAccount() {
        List<Account> accountList = accountRepository.findAll();
        return accountList.stream().map(account -> {
            if (account instanceof SavingAccount){
                SavingAccount savingAccount = (SavingAccount) account;
                return dtoMapper.fromSavingAccount(savingAccount);
            }
            else {
                CurrentAccount currentAccount = (CurrentAccount) account;
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccount(String accountId) throws BankAccountExeption {
        Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new BankAccountExeption("Account not foun"));

        if (account instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) account;
            return dtoMapper.fromSavingAccount(savingAccount);
        }
        else {
            CurrentAccount currentAccount = (CurrentAccount) account;
            return dtoMapper.fromCurrentAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountExeption, BalanceNotSufficientException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountExeption("Account not foun"));
        //verifier le solde du compte
        if(account.getBalance() < amount)
            throw new BalanceNotSufficientException("Balance not sufficient");
        Operation operation = new Operation();
        operation.setAccountOperation(AccountOperation.DEBIT);
        operation.setAmount(amount);
        operation.setOperationDate(new Date());
        operation.setDescription(description);
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountExeption {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountExeption("Account not foun"));

        Operation operation = new Operation();
        operation.setAccountOperation(AccountOperation.CREDIT);
        operation.setAmount(amount);
        operation.setOperationDate(new Date());
        operation.setDescription(description);
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    @Override
    public void transfert(String accountSouceId, String accoundDestinationId, double amount) throws BalanceNotSufficientException, BankAccountExeption {
        debit(accountSouceId, amount, "Tranfer to " + accountSouceId);
        credit(accoundDestinationId, amount, "Transfer from " + accoundDestinationId);
    }

    @Override
    public List<OperationDTO> accountHistory(String accountId){
        List<Operation> operationList = operationRepository.findByAccountId(accountId);
        return operationList.stream().map(operation -> dtoMapper.fromOperation(operation)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String id, int page, int size) throws AccountNotFoundException {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) throw new AccountNotFoundException("Account not found");

        //retourne les pages d'operations d'un compte
        Page<Operation> operationPage = operationRepository.findByAccountId(id, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        //getContent nous permet de recuperer le contenu de la page
        List<OperationDTO> operationDTOS = operationPage.getContent().stream().map(operation ->
                dtoMapper.fromOperation(operation)).collect(Collectors.toList());
        accountHistoryDTO.setOperationDTOList(operationDTOS);
        accountHistoryDTO.setAccountId(account.getId());
        accountHistoryDTO.setBalance(account.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(operationPage.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomer(String keyword) {
        List<Customer> custumers = customerRepository.findByNameContains(keyword);
        return custumers.stream().map(custumer->dtoMapper.fromCustumer(custumer)).collect(Collectors.toList());
    }
}
