package com.example.banckingbackend.repository;

import com.example.banckingbackend.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
