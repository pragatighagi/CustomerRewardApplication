package com.SpringbootApplication.CustomerRewardApplication.service;

import com.SpringbootApplication.CustomerRewardApplication.payload.RewardsDTO;
import com.SpringbootApplication.CustomerRewardApplication.payload.TransactionDTO;

import java.util.List;

public interface RewardsService {
    RewardsDTO getRewardsByCustomerId(Long customerId);

    List<RewardsDTO> getRewardsByCustomerId(List<Long> customerIds);
    TransactionDTO saveTransaction(TransactionDTO transactionDTO);
}
