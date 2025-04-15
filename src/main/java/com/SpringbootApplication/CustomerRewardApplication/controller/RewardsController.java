package com.SpringbootApplication.CustomerRewardApplication.controller;

import com.SpringbootApplication.CustomerRewardApplication.payload.TransactionDTO;
import com.SpringbootApplication.CustomerRewardApplication.payload.RewardsDTO;
import com.SpringbootApplication.CustomerRewardApplication.service.RewardsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for handling reward calculation and transaction operations.
 *
 * Provides endpoints to:
 * - Retrieve reward points for a single customer.
 * - Save customer transactions.
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    private final RewardsServiceImpl rewardsService;

    /**
     * Constructor-based injection of RewardsServiceImpl.
     *
     * @param rewardsService service for reward calculation and transaction persistence
     */
    @Autowired
    public RewardsController(RewardsServiceImpl rewardsService) {
        this.rewardsService = rewardsService;
    }

    /**
     * Retrieves reward points for a single customer.
     *
     * This endpoint accepts a single customer ID as a path variable and returns the calculated rewards.
     *
     * @param customerId the customer ID whose rewards are to be fetched
     * @return a ResponseEntity containing the calculated rewards as {@link RewardsDTO}
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<RewardsDTO> getRewards(@PathVariable Long customerId) {
        RewardsDTO rewards = rewardsService.getRewardsByCustomerId(Collections.singletonList(customerId)).get(0);
        return ResponseEntity.ok(rewards);
    }

    /**
     * Saves a list of customer transactions after validation.
     *
     * This endpoint accepts a list of transaction data, validates and saves them in the repository.
     *
     * @param transactionDTOList the list of transaction data to be saved
     * @return a ResponseEntity containing the list of saved transaction data
     */
    @PostMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> saveTransactions(@Valid @RequestBody List<TransactionDTO> transactionDTOList) {
        List<TransactionDTO> savedTransactions = transactionDTOList.stream()
                .map(rewardsService::saveTransaction)
                .collect(Collectors.toList());

        return ResponseEntity.ok(savedTransactions);
    }
}