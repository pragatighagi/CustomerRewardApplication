package com.SpringbootApplication.CustomerRewardApplication.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class RewardsDTO {
    private Long customerId;
    private Map<String, Long> monthlyRewards;
    private long totalRewards;
}
