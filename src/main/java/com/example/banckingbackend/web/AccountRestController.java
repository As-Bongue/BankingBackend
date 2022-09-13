package com.example.banckingbackend.web;

import com.example.banckingbackend.dtos.AccountDTO;
import com.example.banckingbackend.dtos.AccountHistoryDTO;
import com.example.banckingbackend.dtos.OperationDTO;
import com.example.banckingbackend.exceptions.BankAccountExeption;
import com.example.banckingbackend.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class AccountRestController {

    private AccountService accountService;

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable(name = "id") String accountId) throws BankAccountExeption {
        return accountService.getAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<AccountDTO> getAllAcount(){
        return accountService.listAccount();
    }

    //liste tous les operations d'un compte
    @GetMapping("/accounts/{id}/operations")
    public List<OperationDTO> getHistoryAccount(@PathVariable String id){
        return accountService.accountHistory(id);
    }

    //affiche lhistorique d'un compte avec pagination
    @GetMapping("/accounts/{id}/pagesOperations")
    public AccountHistoryDTO getHistory(
            @PathVariable String id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) throws AccountNotFoundException {
        return accountService.getAccountHistory(id, page, size);
    }

}
