package br.com.financasz.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.financasz.models.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByCategoryId(Long categoryId);   
}
