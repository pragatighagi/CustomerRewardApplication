package com.SpringbootApplication.CustomerRewardApplication.service;

import com.SpringbootApplication.CustomerRewardApplication.payload.RewardsDTO;
import com.SpringbootApplication.CustomerRewardApplication.payload.TransactionDTO;

public interface RewardsService {
    public RewardsDTO getRewardsByCustomerId(Long customerId);

    TransactionDTO saveTransaction(TransactionDTO transactionDTO);
}
