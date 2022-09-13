package com.example.banckingbackend.repository;

import com.example.banckingbackend.entities.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findByAccountId(String accountId);
    //retourne les operations d'un compte avec pagination
    Page<Operation> findByAccountId(String accountId, Pageable pageable);
}
