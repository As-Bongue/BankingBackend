package com.example.banckingbackend;

import com.example.banckingbackend.dtos.AccountDTO;
import com.example.banckingbackend.dtos.CurrentAccountDTO;
import com.example.banckingbackend.dtos.CustomerDTO;
import com.example.banckingbackend.dtos.SavingAccountDTO;
import com.example.banckingbackend.entities.*;
import com.example.banckingbackend.enums.AccountOperation;
import com.example.banckingbackend.enums.AccountStatus;
import com.example.banckingbackend.exceptions.BalanceNotSufficientException;
import com.example.banckingbackend.exceptions.BankAccountExeption;
import com.example.banckingbackend.exceptions.CustomerNotFoundException;
import com.example.banckingbackend.repository.AccountRepository;
import com.example.banckingbackend.repository.CustomerRepository;
import com.example.banckingbackend.repository.OperationRepository;
import com.example.banckingbackend.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BanckingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BanckingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner start(AccountService accountService){
        return args -> {
            Stream.of("Hassan", "AsB", "Mohamed").forEach(name ->{
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                accountService.saveCustomer(customer);
            });

            accountService.listCustomer().forEach(customer -> {
                try {
                    accountService.SaveCurrentAccount(Math.random() * 90000, 5000, customer.getId());
                    accountService.SaveSavingAccount(Math.random() * 120000, 5.5, customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
            //recuperation des comptes et creations de 10 operations debit et credit pour chaque comptes
            List<AccountDTO> accountList = accountService.listAccount();
            for (AccountDTO account:accountList){
                for (int i = 0; i < 10; i++) {
                    String accounId;
                    if (account instanceof SavingAccountDTO) {
                        accounId = ((SavingAccountDTO) account).getId();
                    }
                    else {
                        accounId = ((CurrentAccountDTO) account).getId();
                    }
                    accountService.credit(accounId, 10000 + Math.random() * 120000, "Credit");
                    accountService.debit(accounId, 10000 + Math.random() * 9000, "Debit");
                }
            }
        };
    }

    //permet l'insertion des donnees au demarage
    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository, AccountRepository accountRepository,
                            OperationRepository operationRepository){
        return args -> {
            Stream.of("Hassan", "Yasmine", "Aicha").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setOverDraft(9000);
                currentAccount.setAccountStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                accountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setInterest(5.5);
                savingAccount.setAccountStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                accountRepository.save(savingAccount);
            });

            accountRepository.findAll().forEach(acc->{
                for (int i = 0; i < 5 ; i++) {
                    Operation operation = new Operation();
                    operation.setOperationDate(new Date());
                    operation.setAccountOperation(Math.random() > 0.5 ? AccountOperation.DEBIT : AccountOperation.CREDIT);
                    operation.setAmount(Math.random() * 12000);
                    operation.setAccount(acc);
                    operationRepository.save(operation);
                }
            });
        };
    }

}
