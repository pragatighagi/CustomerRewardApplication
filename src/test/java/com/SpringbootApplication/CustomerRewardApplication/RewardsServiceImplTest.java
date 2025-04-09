package com.SpringbootApplication.CustomerRewardApplication;


import com.SpringbootApplication.CustomerRewardApplication.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import com.SpringbootApplication.CustomerRewardApplication.payload.RewardsDTO;
import com.SpringbootApplication.CustomerRewardApplication.repository.TransactionRepository;
import com.SpringbootApplication.CustomerRewardApplication.service.RewardsServiceImpl;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RewardsServiceImpl.
 */
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
        Timestamp now = new Timestamp(System.currentTimeMillis());
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1L, customerId, now, 120.0),
                new Transaction(2L, customerId, now, 75.0),
                new Transaction(3L, customerId, now, 30.0)
        );

        when(transactionRepository.existsByCustomerId(customerId)).thenReturn(true);
        when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(eq(customerId), any(), any()))
                .thenReturn(transactions);

        RewardsDTO rewards = rewardsService.getRewardsByCustomerId(customerId);

        assertEquals(customerId, rewards.getCustomerId());
        assertTrue(rewards.getTotalRewards() > 0);
    }

    @Test
    public void testGetRewardsByCustomerId_customerNotFound() {
        Long customerId = 2L;
        when(transactionRepository.existsByCustomerId(customerId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            rewardsService.getRewardsByCustomerId(customerId);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }
}
