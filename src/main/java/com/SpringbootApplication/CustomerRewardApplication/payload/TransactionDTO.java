package com.SpringbootApplication.CustomerRewardApplication.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private Long transactionId;
    @NotNull(message = "Customer ID must not be null.")
    private Long customerId;
    @NotNull(message = "Transaction amount must not be null.")
    @Positive(message = "Transaction amount must be greater than 0.")
    private Double transactionAmount;
    @NotNull(message = "Transaction date must not be null.")
    @PastOrPresent(message = "Transaction date cannot be in the future.")
    private Date transactionDate;
}
