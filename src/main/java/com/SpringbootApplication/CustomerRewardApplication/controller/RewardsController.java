package com.SpringbootApplication.CustomerRewardApplication.controller;

import com.SpringbootApplication.CustomerRewardApplication.payload.TransactionDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.SpringbootApplication.CustomerRewardApplication.payload.RewardsDTO;
import com.SpringbootApplication.CustomerRewardApplication.service.RewardsServiceImpl;

/**
 * REST controller for handling reward calculation and transaction operations.
 *
 * Provides endpoints to:
 * - Retrieve reward points for a specific customer.
 * - Save a new customer transaction.
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
     * Retrieves reward points for a given customer ID.
     *
     * @param customerId the ID of the customer whose rewards are to be fetched
     * @return a ResponseEntity containing the calculated rewards as {@link RewardsDTO}
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<RewardsDTO> getRewards(@PathVariable Long customerId) {
        RewardsDTO rewards = rewardsService.getRewardsByCustomerId(customerId);
        return ResponseEntity.ok(rewards);
    }

    /**
     * Saves a customer transaction after validation.
     *
     * @param transactionDTO the transaction data received from the client
     * @return a ResponseEntity containing the saved transaction data
     */
    @PostMapping
    public ResponseEntity<TransactionDTO> saveTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO saved = rewardsService.saveTransaction(transactionDTO);
        return ResponseEntity.ok(saved);
    }
}