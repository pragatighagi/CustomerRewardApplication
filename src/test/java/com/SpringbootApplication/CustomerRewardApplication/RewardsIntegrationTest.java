package com.SpringbootApplication.CustomerRewardApplication;

import com.SpringbootApplication.CustomerRewardApplication.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import com.SpringbootApplication.CustomerRewardApplication.payload.RewardsDTO;
import com.SpringbootApplication.CustomerRewardApplication.repository.TransactionRepository;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for reward endpoint.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RewardsIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() {
        transactionRepository.deleteAll();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        transactionRepository.save(new Transaction(null, 1L, now, 120.0));
        transactionRepository.save(new Transaction(null, 1L, now, 80.0));
    }

    @Test
    public void testGetRewardsAPI() {
        ResponseEntity<RewardsDTO> response = restTemplate.getForEntity("/api/rewards/1", RewardsDTO.class);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getTotalRewards() > 0);
    }
}