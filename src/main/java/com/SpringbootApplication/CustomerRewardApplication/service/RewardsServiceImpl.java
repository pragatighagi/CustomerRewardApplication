package com.SpringbootApplication.CustomerRewardApplication.service;
import com.SpringbootApplication.CustomerRewardApplication.entity.Transaction;
import com.SpringbootApplication.CustomerRewardApplication.exception.CustomerNotFoundException;
import com.SpringbootApplication.CustomerRewardApplication.payload.DateUtil;
import com.SpringbootApplication.CustomerRewardApplication.payload.RewardsDTO;
import com.SpringbootApplication.CustomerRewardApplication.payload.TransactionDTO;
import com.SpringbootApplication.CustomerRewardApplication.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Implementation of RewardsService for calculating and saving customer rewards.
 */
@Service
public class RewardsServiceImpl implements RewardsService {

    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RewardsServiceImpl(TransactionRepository transactionRepository, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Retrieves reward points earned by a customer over the past three months.
     *
     * @param customerId ID of the customer
     * @return DTO containing month-wise and total reward points
     * @throws IllegalArgumentException if customerId is null
     * @throws CustomerNotFoundException if customer does not exist
     */
    @Override
    public RewardsDTO getRewardsByCustomerId(Long customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID must not be null.");
        }

        if (!transactionRepository.existsByCustomerId(customerId)) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
        }

        long lastMonthPoints = calculateMonthlyRewards(fetchTransactions(customerId, 1));
        long secondMonthPoints = calculateMonthlyRewards(fetchTransactions(customerId, 2));
        long thirdMonthPoints = calculateMonthlyRewards(fetchTransactions(customerId, 3));

        return RewardsDTO.builder()
                .customerId(customerId)
                .lastMonthRewardPoints(lastMonthPoints)
                .lastSecondMonthRewardPoints(secondMonthPoints)
                .lastThirdMonthRewardPoints(thirdMonthPoints)
                .totalRewards(lastMonthPoints + secondMonthPoints + thirdMonthPoints)
                .build();
    }
    /**
     * Saves a transaction after converting the DTO to entity.
     *
     * @param transactionDTO validated DTO object
     * @return saved transaction as DTO
     */
    @Override
    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {

        transactionDTO.setTransactionId(null);

        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);

        Date dtoDate = transactionDTO.getTransactionDate();
        if (dtoDate != null) {
            transaction.setTransactionDate(new Timestamp(dtoDate.getTime()));
        }

        Transaction saved = transactionRepository.save(transaction);

        // Map back to DTO
        TransactionDTO savedDTO = modelMapper.map(saved, TransactionDTO.class);
        if (saved.getTransactionDate() != null) {
            savedDTO.setTransactionDate(new Date(saved.getTransactionDate().getTime()));
        }

        return savedDTO;
    }

    /**
     * Fetches all transactions for a customer in a given month offset.
     *
     * @param customerId customer ID
     * @param monthOffset how many months back (1 = last month, etc.)
     * @return list of transactions
     */
    private List<Transaction> fetchTransactions(Long customerId, int monthOffset) {
        Date start = DateUtil.getStartOfMonthOffset(monthOffset);
        Date end = DateUtil.getEndOfMonthOffset(monthOffset);
        return transactionRepository.findAllByCustomerIdAndTransactionDateBetween(customerId, start, end);
    }

    /**
     * Calculates total rewards from a list of transactions.
     *
     * @param transactions list of transactions
     * @return reward points
     */
    private long calculateMonthlyRewards(List<Transaction> transactions) {
        return transactions.stream()
                .mapToLong(this::calculateRewardPoints)
                .sum();
    }

    /**
     * Calculates reward points from a single transaction.
     *
     * @param transaction the transaction object
     * @return reward points
     */
    private long calculateRewardPoints(Transaction transaction) {
        double amount = transaction.getTransactionAmount();
        if (amount > 100) {
            return (long) ((amount - 100) * 2 + 50);
        } else if (amount > 50) {
            return (long) (amount - 50);
        } else {
            return 0;
        }
    }
}
