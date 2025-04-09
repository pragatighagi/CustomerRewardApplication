package com.SpringbootApplication.CustomerRewardApplication.repository;

import com.SpringbootApplication.CustomerRewardApplication.entity.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;
@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction,Long>{
    List<Transaction> findAllByCustomerIdAndTransactionDateBetween(Long customerId, Date startDate, Date endDate);
    boolean existsByCustomerId(Long customerId);
}
