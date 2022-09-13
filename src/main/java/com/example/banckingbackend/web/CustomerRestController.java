package com.example.banckingbackend.web;

import com.example.banckingbackend.dtos.CustomerDTO;
import com.example.banckingbackend.exceptions.CustomerNotFoundException;
import com.example.banckingbackend.services.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {

    private AccountService accountService;

    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return accountService.listCustomer();
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
        return accountService.getCustomer(customerId);
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return accountService.saveCustomer(customerDTO);
    }

    @PutMapping("/customers/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO){
        customerDTO.setId(id);
        return accountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id){
        accountService.deleteCustomer(id);
    }

    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomer(@RequestParam(name = "keyword", defaultValue = "") String keyword){
        return accountService.searchCustomer(keyword);
    }
}
