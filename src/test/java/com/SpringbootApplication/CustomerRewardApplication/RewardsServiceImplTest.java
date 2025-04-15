package com.SpringbootApplication.CustomerRewardApplication;

import com.SpringbootApplication.CustomerRewardApplication.entity.Transaction;
import com.SpringbootApplication.CustomerRewardApplication.exception.CustomerNotFoundException;
import com.SpringbootApplication.CustomerRewardApplication.payload.RewardsDTO;
import com.SpringbootApplication.CustomerRewardApplication.repository.TransactionRepository;
import com.SpringbootApplication.CustomerRewardApplication.service.RewardsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RewardsServiceImplTest {

    private TransactionRepository transactionRepository;
    private RewardsServiceImpl rewardsService;

    @BeforeEach
    public void setup() {
        transactionRepository = mock(TransactionRepository.class);
        rewardsService = new RewardsServiceImpl(transactionRepository, new ModelMapper());
    }

    @Test
    public void testGetRewardsByCustomerId_validCustomer() {
        Long customerId = 1L;

        // Use current time as sample transaction date
        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Timestamp timestamp = new Timestamp(date.getTime());

        // Create sample transactions
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1L, customerId, timestamp, 120.0),  // Reward: (120 - 100) * 2 + 50 = 90
                new Transaction(2L, customerId, timestamp, 75.0),   // Reward: (75 - 50) = 25
                new Transaction(3L, customerId, timestamp, 30.0)    // Reward: 0
        );

        when(transactionRepository.existsByCustomerId(customerId)).thenReturn(true);
        when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(eq(customerId), any(), any()))
                .thenReturn(transactions);

        List<RewardsDTO> rewards = rewardsService.getRewardsByCustomerId(Arrays.asList(customerId));

        // Ensure there is a reward DTO for the customer
        assertFalse(rewards.isEmpty());
        RewardsDTO rewardsDTO = rewards.get(0);  // Get the first element since it's a list

        assertEquals(customerId, rewardsDTO.getCustomerId());
        assertEquals(115, rewardsDTO.getTotalRewards()); // (120 - 100) * 2 + 50 + (75 - 50) = 90 + 25 = 115
        assertTrue(rewardsDTO.getMonthlyRewards().size() >= 1); // Check that monthly rewards are present
    }

    @Test
    public void testGetRewardsByCustomerId_customerNotFound() {
        Long customerId = 2L;
        when(transactionRepository.existsByCustomerId(customerId)).thenReturn(false);

        RuntimeException exception = assertThrows(CustomerNotFoundException.class, () -> {
            rewardsService.getRewardsByCustomerId(Arrays.asList(customerId));
        });

        assertTrue(exception.getMessage().contains("Customer with ID 2 not found"));
    }

    @Test
    public void testGetRewardsByCustomerId_noTransactions() {
        Long customerId = 3L;

        // Simulate no transactions for the customer
        when(transactionRepository.existsByCustomerId(customerId)).thenReturn(true);
        when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(eq(customerId), any(), any()))
                .thenReturn(Arrays.asList()); // Empty list

        List<RewardsDTO> rewards = rewardsService.getRewardsByCustomerId(Arrays.asList(customerId));

        assertFalse(rewards.isEmpty());
        RewardsDTO rewardsDTO = rewards.get(0);
        assertEquals(customerId, rewardsDTO.getCustomerId());
        assertEquals(0, rewardsDTO.getTotalRewards()); // No transactions, no rewards
    }
}
