package com.SpringbootApplication.CustomerRewardApplication.service;

import com.SpringbootApplication.CustomerRewardApplication.entity.Transaction;
import com.SpringbootApplication.CustomerRewardApplication.exception.CustomerNotFoundException;
import com.SpringbootApplication.CustomerRewardApplication.payload.RewardsDTO;
import com.SpringbootApplication.CustomerRewardApplication.payload.TransactionDTO;
import com.SpringbootApplication.CustomerRewardApplication.repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RewardsServiceImpl implements RewardsService {

    private static final Logger logger = LoggerFactory.getLogger(RewardsServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RewardsServiceImpl(TransactionRepository transactionRepository, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RewardsDTO getRewardsByCustomerId(Long customerId) {
        // Validate if the customer exists
        if (!transactionRepository.existsByCustomerId(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
        }

        try {
            LocalDate now = LocalDate.now();
            String lastMonth = capitalizeMonth(now.minusMonths(1).getMonth().name());
            String secondLastMonth = capitalizeMonth(now.minusMonths(2).getMonth().name());
            String thirdLastMonth = capitalizeMonth(now.minusMonths(3).getMonth().name());

            // Calculate rewards for each month
            long lastMonthPoints = calculateMonthlyRewards(fetchTransactions(customerId, 1));
            long secondMonthPoints = calculateMonthlyRewards(fetchTransactions(customerId, 2));
            long thirdMonthPoints = calculateMonthlyRewards(fetchTransactions(customerId, 3));

            Map<String, Long> monthlyRewards = new LinkedHashMap<>();
            monthlyRewards.put(lastMonth, lastMonthPoints);
            monthlyRewards.put(secondLastMonth, secondMonthPoints);
            monthlyRewards.put(thirdLastMonth, thirdMonthPoints);

            // Return rewards details
            return RewardsDTO.builder()
                    .customerId(customerId)
                    .monthlyRewards(monthlyRewards)
                    .totalRewards(lastMonthPoints + secondMonthPoints + thirdMonthPoints)
                    .build();
        } catch (Exception e) {
            logger.error("Error calculating rewards for customer with ID " + customerId, e);
            throw new RuntimeException("Error calculating rewards for customer with ID " + customerId, e);
        }
    }

    @Override
    public List<RewardsDTO> getRewardsByCustomerId(List<Long> customerIds) {
        return customerIds.stream()
                .map(this::getRewardsByCustomerId)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        try {
            transactionDTO.setTransactionId(null); // ensure ID is autogenerated
            Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);

            if (transactionDTO.getTransactionDate() != null) {
                transaction.setTransactionDate(convertToTimestamp(transactionDTO.getTransactionDate()));
            }

            Transaction savedTransaction = transactionRepository.save(transaction);
            TransactionDTO savedDTO = modelMapper.map(savedTransaction, TransactionDTO.class);
            savedDTO.setTransactionDate(new Date(savedTransaction.getTransactionDate().getTime()));

            return savedDTO;
        } catch (Exception e) {
            logger.error("Error saving transaction", e);
            throw new RuntimeException("Error saving transaction", e);
        }
    }

    private Timestamp convertToTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    private List<Transaction> fetchTransactions(Long customerId, int monthOffset) {
        try {
            Date start = getStartOfMonthOffset(monthOffset);
            Date end = getEndOfMonthOffset(monthOffset);
            return transactionRepository.findAllByCustomerIdAndTransactionDateBetween(customerId, start, end);
        } catch (Exception e) {
            logger.error("Error fetching transactions for customer " + customerId, e);
            throw new RuntimeException("Error fetching transactions for customer " + customerId, e);
        }
    }

    private long calculateMonthlyRewards(List<Transaction> transactions) {
        return transactions.stream()
                .mapToLong(this::calculateRewardPoints)
                .sum();
    }

    private long calculateRewardPoints(Transaction transaction) {
        double amount = transaction.getTransactionAmount();
        if (amount > 100) {
            return (long) ((amount - 100) * 2 + 50);
        } else if (amount > 50) {
            return (long) (amount - 50);
        }
        return 0;
    }

    private Date getStartOfMonthOffset(int monthOffset) {
        LocalDate date = LocalDate.now().minusMonths(monthOffset);
        return Date.from(date.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date getEndOfMonthOffset(int monthOffset) {
        LocalDate date = LocalDate.now().minusMonths(monthOffset);
        return Date.from(date.withDayOfMonth(date.lengthOfMonth()).atTime(23, 59, 59)
                .atZone(ZoneId.systemDefault()).toInstant());
    }

    private String capitalizeMonth(String month) {
        return month.charAt(0) + month.substring(1).toLowerCase();
    }
}